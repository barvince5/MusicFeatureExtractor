package feature;

import java.io.File;

import customException.MP3Exception;
import customException.UnsupportedAudioException;
import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.Tag;
import entagged.audioformats.exceptions.CannotReadException;

public class MP3Info {
	
	private Tag tag= null;
	private AudioFile audioFile= null;
	private String songTitle= "";
	private String albumName= "";
	private String artistName= "";
	
	public MP3Info(File song) 
			throws MP3Exception {
		
		if(song == null)
			throw new MP3Exception("The song is null");
		
		try {
			this.getMP3Tag(song);
		} catch (UnsupportedAudioException e) {
			throw new MP3Exception("UnsupportedAudioFileException "+e.getMessage(), e);
		}
	}
	
	private final void getMP3Tag(File song) 
			throws MP3Exception, UnsupportedAudioException {
		
		try {
			this.audioFile = AudioFileIO.read(song);
			this.tag= audioFile.getTag();
			// all of these values are just stored in the attributes of this class.
			this.songTitle= this.tag.getFirstTitle();
			this.albumName= this.tag.getFirstAlbum();
			this.artistName= this.tag.getFirstArtist();
		} catch (CannotReadException e) {
			throw new UnsupportedAudioException("CannotReadException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new MP3Exception("Exception during setMp3Tag");
		}
	}
	
	public final String getTitle() {
		return this.songTitle;
	}
	
	public final String getAlbum() {
		return this.albumName;
	}
	
	public final String getArtist() {
		return this.artistName;
	}

	public final Tag getTags() {
		return this.tag;
	}
	
	public final AudioFile getAudioFile() {
		return this.audioFile;
	}
}
