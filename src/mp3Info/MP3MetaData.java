package mp3Info;

import java.io.File;
import java.io.IOException;

import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.share.sampled.file.TAudioFileFormat;

import customException.MP3Exception;

/**
 * This class as a method that gets ID3v informations given a mp3 file as input.
 * @author Antonio Collarino
 *
 */
public final class MP3MetaData {
	
	/**
	 * This method gets ID3v tags info from a mp3 file.
	 * @param audioFile .mp3 file
	 * @return
	 * @throws MP3Exception in case of errors
	 */
	public final static Map<String, Object> getMetaDateMP3(File audioFile) 
			throws MP3Exception {
		
		Map<String, Object> properties= null;	
		AudioFileFormat baseFileFormat = null;
		
		try {
			baseFileFormat = AudioSystem.getAudioFileFormat(audioFile);
			if (baseFileFormat instanceof TAudioFileFormat)
				properties = baseFileFormat.properties();
			
		} catch (UnsupportedAudioFileException e) {
			throw new MP3Exception("UnsupportedAudioFileException "+e.getMessage(), e);
		} catch (IOException e) {
			throw new MP3Exception("IOException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new MP3Exception("Exception "+e.getMessage(), e);
		}
		
		return properties;
	}
}

