package mp3;

import java.io.File;

import customException.MP3Exception;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.Tag;
import entagged.audioformats.exceptions.CannotReadException;

public final class MP3Tags {
	
	private static Tag tag= null;
	private static AudioFile audioFile= null;
	
	private final static Tag getTags(File song) 
			throws MP3Exception {
		
		if(song == null)
			throw new MP3Exception("The song is null");
		
		try {
			audioFile = AudioFileIO.read(song);
			MP3Tags.tag= audioFile.getTag();
			
		} catch (CannotReadException e) {
			throw new MP3Exception("CannotReadException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new MP3Exception("Exception "+e.getMessage(), e);
		}
		
		return MP3Tags.tag;
		
		
//		System.err.println("Ar "+audioFile.getTag().getFirstArtist());
//		System.err.println("til "+audioFile.getTag().getFirstTitle());
//		System.err.println("al "+audioFile.getTag().getFirstAlbum());
		
	}
}
