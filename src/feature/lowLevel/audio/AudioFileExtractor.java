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
package feature.lowLevel.audio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import customException.AudioFileExtractorException;

import feature.lowLevel.audio.data.FeatureExtractionOptions;
import feature.lowLevel.audio.data.RealMatrixExt;
import feature.lowLevel.audio.input.AudioFileSegmentReader;


/**
 * Copyright Vienna University of Technology
 * 
 * @author Thomas Lidy
 * @version $Id: AudioFileExtractor.java 195 2010-08-17 16:52:18Z frank $
 */
public class AudioFileExtractor {

    public RealMatrixExt[] extractAudioFile(InputStream inStream, FeatureExtractionOptions opt)
            throws AudioFileExtractorException {
        try {
			return extractAudioFile(AudioSystem.getAudioInputStream(inStream), opt);
		} catch (UnsupportedAudioFileException e) {
			throw new AudioFileExtractorException(e.getMessage(), e);
		} catch (IOException e) {
			throw new AudioFileExtractorException(e.getMessage(), e);
		}

    }

    public RealMatrixExt[] extractAudioFile(File fileIn, FeatureExtractionOptions opt) 
    		throws AudioFileExtractorException {

        try {
			return extractAudioFile(AudioSystem.getAudioInputStream(fileIn), opt);
		} catch (UnsupportedAudioFileException e) {
			throw new AudioFileExtractorException(e.getMessage(), e);
		} catch (IOException e) {
			throw new AudioFileExtractorException(e.getMessage(), e);
		}
    }

    public RealMatrixExt[] extractAudioFile(String filename, FeatureExtractionOptions opt)
            throws AudioFileExtractorException {
        File fileIn = new File(filename);
        return extractAudioFile(fileIn, opt);
    }

    public RealMatrixExt[] extractAudioFile(AudioInputStream inStream, FeatureExtractionOptions opt)
            throws AudioFileExtractorException {
        
        AudioFileSegmentReader audioFile;

        try {
			audioFile = new AudioFileSegmentReader(inStream);
		} catch (UnsupportedAudioFileException e) {
			throw new AudioFileExtractorException(e.getMessage(), e);
		} catch (IOException e) {
			throw new AudioFileExtractorException(e.getMessage(), e);
		}
        
        long fileLength = audioFile.getNumberOfSamples();
        int sampleRate = audioFile.getSampleRate();
        int sampleBits = audioFile.getSampleSizeInBits();
        int segmentSize;
        int fftWindowSize;

        // segment_size should always be ~6 sec, fft_window_size should always be ~ 23ms
        if (sampleRate <= 11025) {
            segmentSize = 65536 /* 2^16 */;
            fftWindowSize = 256;
        } else if (sampleRate <= 22050) {
            segmentSize = 131072 /* 2^17 */;
            fftWindowSize = 512;
        } else if (sampleRate <= 44100) {
            segmentSize = 262144 /* 2^18 */;
            fftWindowSize = 1024;
        } else
            throw new AudioFileExtractorException("Sample rate of " + sampleRate + " Hz not supported!");

        int numSegmentsInFile = (int) (fileLength / segmentSize);

        if (numSegmentsInFile == 0)
            throw new AudioFileExtractorException("Number of segments will be 0! (File length: " + fileLength + "  Segment Size: " + segmentSize + ")");

        int stepWidth = opt.getStepWidth();
        int skipSegments = opt.getSkipLIFO();

        // adjust stepWidth so that a preferred number of segments is read and the rest are skipped
        if ((skipSegments > 0) | (stepWidth > 1)) {
            if (audioFile.getDurationInSeconds() < 45) {
                stepWidth = 1;
                skipSegments = 0;
            }
        }

        int numFSets = opt.getNumberOfFeatureSets();
        int numSegmentsToExtract = ((numSegmentsInFile - (skipSegments * 2) - 1) / stepWidth) + 1;

        RealMatrixExt[][] featureSets = new RealMatrixExt[numSegmentsToExtract][];

        FeatureExtractor featExtract = new FeatureExtractor(sampleRate, sampleBits);

        int seg; // segment# read in
        int extseg = 0; // segment# extracted
        int framesRead; // #frames read from file in one iteration
      
        short[] waveSegment = new short[segmentSize];

        for (seg = 0; (seg < numSegmentsInFile) && (extseg < numSegmentsToExtract); seg++) {
            try {
                framesRead = audioFile.readMono(waveSegment);
            } catch (IOException e) {
                throw new AudioFileExtractorException("Problem reading segment " + seg + ". " + e.getMessage());
            }

            if ((framesRead == -1) || (framesRead < segmentSize)) {
                break; // end of file
            }

            // start with x-th segment (according to skipSegments),
            // take only every 2nd, 3rd, 4th, ... segment according to stepWidth
            if (((seg - skipSegments) >= 0) && ((seg - skipSegments) % stepWidth == 0)) {
                featureSets[extseg] = featExtract.extractFeatureSets(waveSegment, opt, fftWindowSize);
                extseg++;
            }
        }

        try {
			audioFile.closeStream();
		} catch (IOException e) {
			throw new AudioFileExtractorException(e.getMessage(), e);
		}

        int segmentsRead = extseg;

        if (segmentsRead == 0)
            throw new AudioFileExtractorException(
                    "Number of segments extracted was 0! (File length: " + fileLength + "  Segment Size: " + segmentSize + "  Step Width: " + stepWidth);
        else if (segmentsRead != numSegmentsToExtract) {
        	throw new AudioFileExtractorException("Number of segments read is different from the number to extract!");
        }

        /* calculate Median */

        RealMatrixExt[] medianFeatureSets = new RealMatrixExt[numFSets];

        for (int fi = 0; fi < numFSets; fi++) {
            // "transpose"
            RealMatrixExt[] segmFeatureSets = new RealMatrixExt[segmentsRead];
            for (int s = 0; s < segmentsRead; s++) {
                segmFeatureSets[s] = featureSets[s][fi];
            }

            medianFeatureSets[fi] = RealMatrixExt.median(segmFeatureSets);
        }

        return medianFeatureSets;
    }

}
