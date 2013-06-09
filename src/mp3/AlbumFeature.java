package mp3;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import httpGET.GetHttpPage;

import java.io.File;
import java.math.BigInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import musicbrainz.MusicbrainzDoc;
import musicbrainz.MusicbrainzUrl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import albumArtifacts.AlbumType;
import albumArtifacts.ArtistType;
import albumArtifacts.MediumListType;
import albumArtifacts.ObjectFactory;
import albumArtifacts.SongListType;
import albumArtifacts.SongType;

import customException.GetHttpException;
import customException.MP3Exception;
import customException.MusicbrainzDocException;
import customException.MusicbrainzUrlException;

public class AlbumFeature extends MP3Info {

	private ObjectFactory obf= null;
	private AlbumType album= null;
	private Document albumDoc= null;
	private Document songsDoc= null;
	private String albumID= null;
	
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
			if(title == null || title.equals(""))
				return false;
			
			String albumName= super.getAlbum();
			String artistName= super.getArtist();
			if(albumName.equals("") || artistName.equals("")) {
				
				content= getHttp.getWebPageAsString(MusicbrainzUrl.getMbRecordingUrl(artistName, title, albumName));
				if(content.equals(""))
					return false;
				Document tempDoc= MusicbrainzDoc.createDoc(content);
				nodeList= tempDoc.getElementsByTagName("artist");
				if (nodeList.getLength() == 0)
					return false;
				Element e= (Element)nodeList.item(0);
				nodeList= e.getElementsByTagName("name");
				if (nodeList.getLength() == 0)
					return false;				
				artistName = nodeList.item(0).getTextContent();
				if (artistName.equals(""))
					return false;
				nodeList= tempDoc.getElementsByTagName("release");
				if (nodeList.getLength() == 0)
					return false;
				e= (Element)nodeList.item(0);
				nodeList= e.getElementsByTagName("title");
				if (nodeList.getLength() == 0)
					return false;				
				albumName = nodeList.item(0).getTextContent();
				if (albumName.equals(""))
					return false;
			}
			
			content= getHttp.getWebPageAsString(MusicbrainzUrl.getMbAlbum(artistName, albumName));
			if(content.equals(""))
				return false;
			this.albumDoc= MusicbrainzDoc.createDoc(content);
			
			//if the count is zero, no album was found.
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
			
			//set album id
			Element albumElement= (Element) albumListNode.getElementsByTagName("release").item(0);
			this.albumID= albumElement.getAttribute("id");
			this.album.setMbAlbumID(this.albumID);
			
			//set title
			this.album.setTitle(albumElement.getElementsByTagName("title").item(0).getTextContent());
				
			//set date
			nodeList= albumElement.getElementsByTagName("date");
			if(nodeList.getLength() != 0)
				this.album.setDate(nodeList.item(0).getTextContent());
			
			//set country
			nodeList= albumElement.getElementsByTagName("country");
			if(nodeList.getLength() != 0)
				this.album.setCountry(nodeList.item(0).getTextContent());
			
			//set track count
			nodeList= albumElement.getElementsByTagName("track-count");
			if(nodeList.getLength() != 0)
				this.album.setTrackCount(new BigInteger(nodeList.item(0).getTextContent()));
			
			//medium list
			nodeList= albumListNode.getElementsByTagName("format");
			MediumListType mlt= this.obf.createMediumListType();
			
			for(int i= 0; i< nodeList.getLength(); ++i) {
				
				String format= nodeList.item(i).getTextContent();
				boolean present= false;
				for(int j= 0; j< mlt.getMedium().size() && !present; ++j) {
					if(mlt.getMedium().get(j).equalsIgnoreCase(format)) {
						present= true;
					}
				}
				
				if(present == false)
					mlt.getMedium().add(format);
				
			}
			
			if(mlt.getMedium().isEmpty() == false)
				this.album.setMediumList(mlt);
			
			//fill song list
			nodeList= albumElement.getElementsByTagName("release-group");
			if(nodeList.getLength() != 0) {
				
				content= getHttp.getWebPageAsString(MusicbrainzUrl.getMbSongsOfAlbum(this.albumID));
				if(content.equals("") == false) {
					
					this.songsDoc= MusicbrainzDoc.createDoc(content);
					SongListType slt= this.obf.createSongListType();

					//if the count is zero, no song was found.
					Element songListNode= (Element) this.songsDoc.getElementsByTagName("recording-list").item(0);
					count= Integer.valueOf(songListNode.getAttributes().getNamedItem("count").getNodeValue());
					if(count.intValue() != 0) {

						nodeList= songListNode.getElementsByTagName("recording");
						for (int i=0; i< nodeList.getLength(); ++i) {
							
							SongType st= this.obf.createSongType();
							Element songElement= (Element) nodeList.item(i);
							st.setMbSongID(songElement.getAttribute("id"));
							st.setTitle(songElement.getElementsByTagName("title").item(0).getTextContent());
							NodeList numberList= songElement.getElementsByTagName("number");
							if(numberList.getLength() != 0)
								st.setPosition(numberList.item(0).getTextContent());
							slt.getSong().add(st);
							
							}
						this.album.setSongList(slt);							
					}
					
				}
			}
			
			//marshall this JaxbElement
			JAXBContext jc= JAXBContext.newInstance("albumArtifacts");
			JAXBElement<AlbumType> je= this.obf.createAlbumMetadata(this.album);
			Marshaller m= jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			SchemaFactory sf= SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
			Schema schema= sf.newSchema(new File("MetadataSchema/album.xsd"));
			m.setSchema(schema);
			m.setEventHandler(new ValidationEventHandler() {
				
				@Override
				public boolean handleEvent(ValidationEvent event) {
					return false;
				}
			});
			
			//TODO correct path it's not present yet.
			
			output= new File("mfe_"+this.album.getTitle()+".xml");
			m.marshal(je, output);

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
