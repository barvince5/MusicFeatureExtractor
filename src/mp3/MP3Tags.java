package mp3;

import java.io.File;
import java.math.BigInteger;

import songArtifacts.ObjectFactory;
import songArtifacts.SongMetadataType;

import customException.MP3Exception;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.Tag;
import entagged.audioformats.exceptions.CannotReadException;

public final class MP3Tags {
	
	private static Tag tag= null;
	private static AudioFile audioFile= null;
	private static SongMetadataType smt;
	
	private final static Tag getTags(File song) 
			throws MP3Exception {
		
		if(song == null)
			throw new MP3Exception("The song is null");
		
		try {
			MP3Tags.audioFile = AudioFileIO.read(song);
			MP3Tags.tag= audioFile.getTag();
			
		} catch (CannotReadException e) {
			throw new MP3Exception("CannotReadException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new MP3Exception("Exception "+e.getMessage(), e);
		}
		
		return MP3Tags.tag;		
	}
	
	private final static void setAudioFile() 
			throws MP3Exception {
		
		try {
			MP3Tags.smt.setName(MP3Tags.audioFile.getName()); //file name.
			MP3Tags.smt.setBitrate(BigInteger.valueOf(MP3Tags.audioFile.getBitrate())); //KB/s
			MP3Tags.smt.setChannelsNum(BigInteger.valueOf(MP3Tags.audioFile.getChannelNumber()));
			MP3Tags.smt.setEncoding(MP3Tags.audioFile.getEncodingType());
			MP3Tags.smt.setFrequency(BigInteger.valueOf(MP3Tags.audioFile.getSamplingRate())); //freq. in hz
			MP3Tags.smt.setLength(BigInteger.valueOf(MP3Tags.audioFile.getLength())); // seconds
		} catch (Exception e) {
			throw new MP3Exception("Exception during setAudioFile");
		}
	}
	
	private final static void setMp3Tag() 
			throws MP3Exception {
		
		try {
			String songTitle= MP3Tags.audioFile.getTag().getFirstTitle();
			String albumName= MP3Tags.audioFile.getTag().getFirstAlbum();
			String artistName= MP3Tags.audioFile.getTag().getFirstArtist();
			MP3Tags.smt.getArtist().setName(artistName);
			
			//TODO
			//take xml from musicbrainz
			
		} catch (Exception e) {
			throw new MP3Exception("Exception during setMp3Tag");
		}
	}
	
	public final static boolean storeMP3Tags(File song, ObjectFactory obf) 
			throws MP3Exception {
		
		if(song == null || obf == null)
			throw new MP3Exception("The song and/or objectfactory is null");

		try {
			MP3Tags.smt= obf.createSongMetadataType();
			MP3Tags.smt.setArtist(obf.createArtistType());
			MP3Tags.getTags(song);
			MP3Tags.setAudioFile();
			MP3Tags.setMp3Tag();
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
}
