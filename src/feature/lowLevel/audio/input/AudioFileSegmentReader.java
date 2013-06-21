/*
 * Copyright 2004-2010 Information & Software Engineering Group (188/1)
 *                     Institute of Software Technology and Interactive Systems
 *                     Vienna University of Technology, Austria
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.ifs.tuwien.ac.at/mir/index.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package feature.lowLevel.audio.input;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.share.sampled.file.TAudioFileFormat;


/**
 * Copyright Vienna University of Technology
 * 
 * @author Thomas Lidy
 * @author Jakob Frank
 * @version $Id: AudioFileSegmentReader.java 173 2010-06-17 16:28:05Z mayer $
 */
public class AudioFileSegmentReader {

    private InputStream inStream = null;
    private AudioInputStream audioInputStream = null;
    private AudioInputStream mpegInputStream = null; // needed if reading mp3s
    private AudioFileFormat audioFileFormat = null;
    private AudioFormat audioFormat = null;
    private boolean open = false;
    private boolean isMpeg = false;
    private int sampleRate;
    private int sampleSizeInBits;
    private int nChannels;

    private long durationMicrosec = 0;
    private long streamLength;

    public AudioFileSegmentReader(File file) throws UnsupportedAudioFileException, IOException {
        this(AudioSystem.getAudioInputStream(file));
    }

    public AudioFileSegmentReader(AudioInputStream inStream) 
    		throws UnsupportedAudioFileException, IOException {
    	
        this.inStream = inStream;
        this.streamLength = inStream.available();
        this.audioFileFormat = AudioSystem.getAudioFileFormat(inStream);
        this.audioFormat = this.audioFileFormat.getFormat();
        this.isMpeg = this.audioFormat.getEncoding().toString().startsWith("MPEG");

        openStream();

        this.audioFormat = this.audioInputStream.getFormat(); // needs to be read again in
        // case of mp3s (for wav layer values)
        this.sampleRate = (int) this.audioFormat.getSampleRate();
        this.sampleSizeInBits = this.audioFormat.getSampleSizeInBits();
        this.nChannels = this.audioFormat.getChannels();

        if ((this.isMpeg) && (this.audioFileFormat instanceof TAudioFileFormat)) {
            Map<?, ?> properties = ((TAudioFileFormat) this.audioFileFormat).properties();
            long bitrate = ((Integer) properties.get("mp3.bitrate.nominal.bps")).longValue();
            this.durationMicrosec = ((this.streamLength * 8L) / bitrate) * 1000000L;

        } else {
        	this.durationMicrosec = this.audioInputStream.getFrameLength() * 1000 * 1000 / this.sampleRate;
        }

    }

    public int getSampleRate() {
        return this.sampleRate;
    }

    public int getSampleSizeInBits() {
        return this.sampleSizeInBits;
    }

    public long getNumberOfSamples() {
        if (this.isMpeg) {
            // audioInputStream.getFrameLength() returns -1 for MP3s
            // thus another method is needed:
            // the following WORKAROUND will approximate the number of samples only
            // - usually it is SMALLER!

            return this.durationMicrosec / 1000 * (long) this.sampleRate / 1000;
        } else
            return this.audioInputStream.getFrameLength();
    }

    public int getDurationInSeconds() {
        return (int) (this.durationMicrosec / 1000 / 1000);
    }
    
    public void openStream() throws UnsupportedAudioFileException, IOException {
        if (this.isMpeg) {
        	this.mpegInputStream = AudioSystem.getAudioInputStream(this.inStream);
            AudioFormat baseFormat = this.mpegInputStream.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
                    baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            this.audioInputStream = AudioSystem.getAudioInputStream(decodedFormat, this.mpegInputStream);
        } else {
        	this.audioInputStream = AudioSystem.getAudioInputStream(this.inStream);
        }

        this.open = true;
    }

    public void resetStream() throws IOException {
        if (this.audioInputStream != null) {
        	this.audioInputStream.reset();
        }
    }

    public void closeStream() throws IOException {
        if (this.audioInputStream != null) {
        	this.audioInputStream.close();
        }
        if (this.mpegInputStream != null) {
        	this.mpegInputStream.close();
        }

        this.open = false;
    }

    public int readMono(short[] outputArray) throws IOException {
        if (this.nChannels == 1) {
            return read(outputArray);
        } else {
            int len = outputArray.length;
            short[] stereoArray = new short[len * 2];
            int bytesRead = read(stereoArray);
            for (int i = 0; i < len; i++) {
                outputArray[i] = (short) ((short) (stereoArray[i * 2] + stereoArray[i * 2 + 1]) / 2);
            }

            if (bytesRead == -1)
                return -1;
            return bytesRead / 2;
        }
    }

    public int read(short[] outputArray) throws IOException {
        int len = outputArray.length;
        int bytesRead;

        /* 16 bit or 8 bit files */
        if (this.sampleSizeInBits == 16) {
            byte[] byteArray = new byte[len * 2];
            bytesRead = read(byteArray);

            if (this.audioFormat.isBigEndian()) { 
            	// e.g. AU files
                for (int i = 0; i < len; i++) {
                    outputArray[i] = (short) ((byteArray[i * 2 + 1] & 0xff) | ((byteArray[i * 2] & 0xff) << 8));
                }
            } else {
            	// WAV files
            	for (int i = 0; i < len; i++) {
                    outputArray[i] = (short) (((byteArray[i * 2 + 1] & 0xff) << 8) | (byteArray[i * 2] & 0xff));
                }
            }

            if (bytesRead == -1)
                return -1;
            return bytesRead / 2;
        } else {
            byte[] byteArray = new byte[len];
            bytesRead = read(byteArray);
            
            for (int i = 0; i < len; i++) {
                outputArray[i] = (short) byteArray[i];
            }
            return bytesRead;
        }
    }

    /**
     * @param outputArray array into which input is read (# of bytes to read equals size of array)
     * @return number of bytes actually read (can be smaller at the end of file, or -1 in case of
     *         errors)
     * @throws IOException
     */
    public int read(byte[] outputArray) throws IOException {
        if (!this.open) {
            try {
                openStream();
            } catch (UnsupportedAudioFileException e) {
                // this should not happen, because openStream is called in constructor already
                // and an exception should be noticed already there
                e.printStackTrace();
            }
        }

        int len = outputArray.length;
        int bytesDone = 0;
        int bytesToRead = len;
        int bytesRead = 0;

        // read bytes as long as you can (in mp3s, maximum #bytes read at once = framesize, thus we read multiple times)

        while (bytesToRead > 0) {
            // Syntax: read(intoArray, fromOffset, numberOfBytes)
            bytesRead = this.audioInputStream.read(outputArray, bytesDone, bytesToRead);

            if (bytesRead == -1)
                break;

            bytesDone += bytesRead;
            bytesToRead -= bytesRead;
        }

        if (bytesDone > 0)
            return bytesDone;
        else
            return -1;
    }
}
