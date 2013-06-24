/* Copyright 2013 Antonio Collarino, Vincenzo Barone

This file is part of Music Feature Extractor (MFE).

Music Feature Extractor (MFE) is free software; you can redistribute it 
and/or modify it under the terms of the GNU Lesser General Public License 
as published by the Free Software Foundation; either version 3 of the 
License, or (at your option) any later version.

Music Feature Extractor (MFE) is distributed in the hope that it will be 
useful, but WITHOUT ANY WARRANTY; without even the implied warranty of 
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser 
General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with Music Feature Extractor (MFE).  If not, see 
http://www.gnu.org/licenses/.  */

package feature;

import java.io.File;

import customException.MP3Exception;
import customException.UnsupportedAudioException;
import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.Tag;
import entagged.audioformats.exceptions.CannotReadException;

/**
 * This class extracts all data from ID3 and file. This class is also extended by all classes that extracts
 * features (i.e. SonFeature, AlbumFeature, ArtistFeature)
 */
public class MP3Info {
	
	private Tag tag= null;
	private AudioFile audioFile= null;
	private String songTitle= "";
	private String albumName= "";
	private String artistName= "";
	
	/**
	 * This is the constructor which needs an .mp3 file as input.
	 * @param song
	 * @throws MP3Exception
	 */
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
	
	/**
	 * This method extract data from ID3 and audio file.
	 * @param song
	 * @throws MP3Exception
	 * @throws UnsupportedAudioException
	 */
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
	
	/**
	 * Gets the corresponding song title.
	 * @return title
	 */
	public final String getTitle() {
		return this.songTitle;
	}
	
	/**
	 * Gets the corresponding album title.
	 * @return album
	 */
	public final String getAlbum() {
		return this.albumName;
	}
	
	/**
	 * Gets the corresponding artist name.
	 * @return artist
	 */
	public final String getArtist() {
		return this.artistName;
	}

	/**
	 * Gets informations inside the ID3 tags
	 * @return tags
	 */
	public final Tag getTags() {
		return this.tag;
	}
	
	/**
	 * Gets informations about the file.
	 * @return audio file
	 */
	public final AudioFile getAudioFile() {
		return this.audioFile;
	}
}
