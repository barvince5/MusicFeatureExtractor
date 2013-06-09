package mp3;

import httpGET.GetHttpPage;

import java.io.File;

import javax.xml.bind.JAXBException;

import musicbrainz.MusicbrainzDoc;
import musicbrainz.MusicbrainzUrl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import albumArtifacts.AlbumType;
import albumArtifacts.ArtistType;
import albumArtifacts.ObjectFactory;

import customException.GetHttpException;
import customException.MP3Exception;
import customException.MusicbrainzDocException;
import customException.MusicbrainzUrlException;

public class AlbumFeature extends MP3Info {

	private ObjectFactory obf= null;
	private AlbumType album= null;
	private Document albumDoc= null;
	
	public AlbumFeature(File song) 
			throws MP3Exception {
		
		super(song);
		this.obf= new ObjectFactory();
		this.album= this.obf.createAlbumType();
	}

	public final void stop() {
		
	}
	
	public final boolean start() 
			throws MP3Exception {
		
		GetHttpPage getHttp= GetHttpPage.getInstance();
		String content= "";
		File output= null;
		NodeList nodeList= null;
		
		try {
			
			String title= super.getTitle();
			if(title.equals(""))
				return false;
			
			String albumName= super.getAlbum();
			String artistName= super.getArtist();
			if(albumName.equals("") || artistName.equals("")) {
				//TODO
			}
			
			content= getHttp.getWebPageAsString(MusicbrainzUrl.getMbAlbum(artistName, albumName));
			if(content.equals(""))
				return false;
			this.albumDoc= MusicbrainzDoc.createDoc(content);
			
			//if the count is zero, no artist was found.
			Element albumListNode= (Element) this.albumDoc.getElementsByTagName("release-list").item(0);
			Integer count= Integer.valueOf(albumListNode.getAttributes().getNamedItem("count").getNodeValue());
			if(count.intValue() == 0)
				return false;
			
			//artist id and artist info (i.e. name, disambiguation)
			Element artistNode= (Element) albumListNode.getElementsByTagName("artist").item(0);
			this.album.setMbArtistID(artistNode.getAttribute("id")); //global variable useful for next queries.
			ArtistType artist= this.obf.createArtistType();
			artist.setName(artistNode.getElementsByTagName("name").item(0).getTextContent());
			nodeList= artistNode.getElementsByTagName("disambiguation");
			if(nodeList.getLength() != 0)
				artist.setDisambiguation(nodeList.item(0).getTextContent());
			this.album.setArtist(artist);
			
			//
			
			
		} catch (JAXBException e) {
			if(output != null)
				output.delete();
			throw new MP3Exception("JAXBException "+e.getMessage(), e);
		} catch (NullPointerException e) {
			throw new MP3Exception("NullPointerException "+e.getMessage(), e);
		} catch (MusicbrainzUrlException e) {
			throw new MP3Exception("MusicbrainzUrlException "+e.getMessage(), e);
		} catch (GetHttpException e) {
			throw new MP3Exception("GetHttpException "+e.getMessage(), e);
		} catch (MusicbrainzDocException e) {
			throw new MP3Exception("MusicbrainzDocException doc creation problem "+e.getMessage(), e);
		} catch (Exception e) {
			throw new MP3Exception("Exception "+e.getMessage(), e);
		}
		
		return true;
	}
}
