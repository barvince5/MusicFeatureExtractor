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

public final class FindAlbumArtist {

	private String artistName= "";
	private String albumName= "";
	private String title= "";
	
	public FindAlbumArtist(String artist, String album, String title) 
			throws FindAlbumArtistException {
		
		this.artistName= artist;
		this.albumName= album;
		if (title.equals("") == false)
			this.find();
	}
	
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

	public final String getArtistName() {
		return this.artistName;
	}

	public final String getAlbumName() {
		return this.albumName;
	}

	public String getTitle() {
		return this.title;
	}

}
