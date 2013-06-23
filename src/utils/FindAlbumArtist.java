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

package utils;

import httpGET.GetHttpPage;
import musicbrainz.MusicbrainzUrl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import customException.CreateDocException;
import customException.FindAlbumArtistException;
import customException.GetHttpException;
import customException.MusicbrainzUrlException;

/**
 * The aim of this class is to try to get data from musicbrainz website even is some tags are not good.
 */
public final class FindAlbumArtist {

	private String artistName= "";
	private String albumName= "";
	private String title= "";
	
	/**
	 * This is the constructor.
	 * @param artist
	 * @param album
	 * @param title of the song is mandatory
	 * @throws FindAlbumArtistException
	 */
	public FindAlbumArtist(String artist, String album, String title) 
			throws FindAlbumArtistException {
		
		this.artistName= artist;
		this.albumName= album;
		this.title= title;
		if (title.equals("") == false)
			this.find();
	}
	
	/**
	 * This method is able to find information about artist name and album name. It try to create different
	 * url query for musicbrainz website to try to get data, even if some tags of the .mp3 file are not correct.
	 * @throws FindAlbumArtistException in case of error
	 */
	private final void find() 
			throws FindAlbumArtistException {
		
		GetHttpPage getHttp= GetHttpPage.getInstance();
		try {
			
			String content= getHttp.getWebPageAsString(MusicbrainzUrl.getMbAllRecordingsUrl(this.title));
			if(content.equals(""))
				return;
			
			Document songDoc= CreateDoc.create(content);
			
			Element recordingListNode= (Element) songDoc.getElementsByTagName("recording-list").item(0);
			Integer count= Integer.valueOf(recordingListNode.getAttributes().getNamedItem("count").getNodeValue());
			// if count is still 0, the title is not correct, and will be skipped
			if(count.intValue() == 0) 
				return;
			
			// get all recording nodes in the xml and search for the correct one (has one correct tag between release and artist)
			// when one is found, the loop will stop
			NodeList recordingList= songDoc.getElementsByTagName("recording");
			boolean found= false;
			
			// these two strings are for comparison with the ones in the tag. They are initialized to empty string
			// at the beginning of processing each song in the recording list
			String currArtistName;
			String currAlbumName;
			
			for (int i=0; i<recordingList.getLength() && !found; ++i) {
				
				currArtistName= "";
				currAlbumName= "";
				Element currentRecording= (Element)recordingList.item(i);
				Element currentEl= null;
				
				NodeList valueList= currentRecording.getElementsByTagName("artist");
				if (valueList.getLength() != 0) {
					currentEl= (Element)valueList.item(0);
					currArtistName= currentEl.getElementsByTagName("name").item(0).getTextContent();
				} 
				
				valueList= currentRecording.getElementsByTagName("release");
				if (valueList.getLength() != 0) {
					currentEl= (Element)valueList.item(0);
					currAlbumName= currentEl.getElementsByTagName("title").item(0).getTextContent();
				} 
				
				valueList= currentRecording.getElementsByTagName("release-group");
				if (valueList.getLength() != 0) {
					currentEl= (Element) valueList.item(0);
					String type = currentEl.getAttribute("type");
					if(type.equalsIgnoreCase("Soundtrack") || type.equalsIgnoreCase("Compilation"))
						continue;	
					}
				
				// first try exact equality, then without spaces
				if (currAlbumName.equalsIgnoreCase(this.albumName) || currArtistName.equalsIgnoreCase(this.artistName)
					|| currAlbumName.replace(" ", "").equalsIgnoreCase(this.albumName.replace(" ", ""))
					|| currArtistName.replace(" ", "").equalsIgnoreCase(this.artistName.replace(" ", "")) ) {
					this.artistName= currArtistName;
					this.albumName= currAlbumName;
					found= true;
				}
			}
			
			// last attempt, try for partially correct artist names (useful for artist collaborations, containing "featuring")
			if (!found) {
				for (int i=0; i<recordingList.getLength() && !found; ++i) {
					currArtistName= "";
					currAlbumName= "";
					Element currentRecording= (Element)recordingList.item(i);
					NodeList valueList= currentRecording.getElementsByTagName("artist");
					Element currentEl= null;
					if (valueList.getLength() != 0) {
						currentEl= (Element)valueList.item(0);
						currArtistName= currentEl.getElementsByTagName("name").item(0).getTextContent();
					} 
					valueList= currentRecording.getElementsByTagName("release");
					if (valueList.getLength() != 0) {
						currentEl= (Element)valueList.item(0);
						currAlbumName= currentEl.getElementsByTagName("title").item(0).getTextContent();
					}
					if (this.artistName.toLowerCase().contains(currArtistName.toLowerCase())
						|| this.artistName.replace(" ", "").toLowerCase().contains(currArtistName.replace(" ", "").toLowerCase()) ) {
							this.artistName= currArtistName;
							this.albumName= currAlbumName;
							found= true;
						}	
				}	
			}
			
		} catch (CreateDocException e) {
			throw new FindAlbumArtistException("CreateDocException "+e.getMessage(), e);
		} catch (GetHttpException e) {
			throw new FindAlbumArtistException("GetHttpException "+e.getMessage(), e);
		} catch (MusicbrainzUrlException e) {
			throw new FindAlbumArtistException("MusicbrainzUrlException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new FindAlbumArtistException("Exception "+e.getMessage(), e);
		}
	}

	/**
	 * This methods returns the name of the artist.
	 * @return a string
	 */
	public final String getArtistName() {
		return this.artistName;
	}

	/**
	 * This methods returns the name of the album.
	 * @return a string
	 */
	public final String getAlbumName() {
		return this.albumName;
	}

	/**
	 * This methods returns the title of the song.
	 * @return a string
	 */
	public String getTitle() {
		return this.title;
	}

}
