package mp3;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import songArtifacts.highLevel.AlbumListType;
import songArtifacts.highLevel.AlbumType;
import songArtifacts.highLevel.ObjectFactory;
import songArtifacts.highLevel.SongType;
import songArtifacts.highLevel.ArtistType;

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


import customException.GetHttpException;
import customException.MP3Exception;
import customException.MusicbrainzDocException;
import customException.MusicbrainzUrlException;
import entagged.audioformats.AudioFile;

public final class HighLevelSongFeature extends MP3Info {

	private SongType song;
	private ObjectFactory obf;
	private Document songDoc= null;
	
	public HighLevelSongFeature(File song) 
			throws MP3Exception {
		
		super(song);
		this.obf= new ObjectFactory();
		
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
			
			String artistName= super.getArtist();
			String albumName = super.getAlbum();
			
			// try all parameters, the function will use the most it can
			content= getHttp.getWebPageAsString(MusicbrainzUrl.getMbRecordingUrl(artistName, title, albumName));
			if(content.equals(""))
				return false;
			
			this.songDoc= MusicbrainzDoc.createDoc(content);
			
			//if the count is zero, no song was found.
			Element recordingListNode= (Element) this.songDoc.getElementsByTagName("recording-list").item(0);
			Integer count= Integer.valueOf(recordingListNode.getAttributes().getNamedItem("count").getNodeValue());
			if(count.intValue() == 0)
				return false;
			
			Element recordingNode= (Element) recordingListNode.getElementsByTagName("recording").item(0);
			//set song id
			this.song.setMbSongID(recordingNode.getAttribute("id"));
			
			//set song title
			this.song.setTitle(recordingNode.getElementsByTagName("title").item(0).getTextContent());
			
			//set song position
			nodeList= recordingNode.getElementsByTagName("track");
			if (nodeList.getLength() != 0) {
				Element tracknode=(Element) nodeList.item(0);
				nodeList= tracknode.getElementsByTagName("number");
				if (nodeList.getLength() != 0)
					this.song.setPosition(new BigInteger(nodeList.item(0).getTextContent()));	
			}
			
			
			//set artist
			ArtistType artist= this.obf.createArtistType();
			Element artistNode= (Element) recordingNode.getElementsByTagName("artist").item(0);
			artist.setMbArtistID(artistNode.getAttribute("id"));
			artist.setName(artistNode.getElementsByTagName("name").item(0).getTextContent());
			nodeList= artistNode.getElementsByTagName("disambiguation");			
			if(nodeList.getLength() != 0) 
				artist.setDisambiguation(nodeList.item(0).getTextContent());
			this.song.setArtist(artist);
			
			//set albums for this song
			nodeList= recordingNode.getElementsByTagName("release");
			AlbumListType albumList = this.obf.createAlbumListType();
			for (int i=0; i<nodeList.getLength(); ++i) {
				Element releaseNode = (Element) nodeList.item(i);
				AlbumType album = this.obf.createAlbumType();
				
				//set album ID
				album.setMbAlbumID(releaseNode.getAttribute("id"));
				
				//set album title
				album.setTitle(releaseNode.getElementsByTagName("title").item(0).getTextContent());
				
				//set album status
				nodeList = releaseNode.getElementsByTagName("status");
				if (nodeList.getLength() != 0) 
					album.setStatus(nodeList.item(0).getTextContent());
				
				//set album date
				nodeList = releaseNode.getElementsByTagName("date");
				if (nodeList.getLength() != 0) 
					album.setDate(nodeList.item(0).getTextContent());
				
				//set album country
				nodeList = releaseNode.getElementsByTagName("country");
				if (nodeList.getLength() != 0) 
					album.setCountry(nodeList.item(0).getTextContent());
				
				//set album track count
				nodeList = releaseNode.getElementsByTagName("track-count");
				if (nodeList.getLength() != 0) 
					album.setTrackCount(new BigInteger(nodeList.item(0).getTextContent()));
				
				albumList.getAlbum().add(album);
			}
			
			this.song.setAlbumList(albumList);
			
			AudioFile afile = super.getAudioFile();
			
			//set file name
			this.song.setFileName(afile.getName());
			
			//set bitrate
			this.song.setBitrate(BigInteger.valueOf(afile.getBitrate()));
			
			//set channels
			this.song.setChannelsNum(BigInteger.valueOf(afile.getChannelNumber()));
			
			//set frequency
			this.song.setFrequency(BigInteger.valueOf(afile.getSamplingRate()));
			
			//set encoding
			String encoding = afile.getEncodingType();
			if (encoding != null && !encoding.equals(""))
				this.song.setEncoding(encoding);
			
			//marshall this JaxbElement
			JAXBContext jc= JAXBContext.newInstance("songArtifacts.highLevel");
			JAXBElement<SongType> je= this.obf.createSongMetadata(this.song);
			Marshaller m= jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			SchemaFactory sf= SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
			Schema schema= sf.newSchema(new File("MetadataSchema/songHighLevel.xsd"));
			m.setSchema(schema);
			m.setEventHandler(new ValidationEventHandler() {
				
				@Override
				public boolean handleEvent(ValidationEvent event) {
					return false;
				}
			});
			
			//TODO correct path it's not present yet.
			
			output= new File("mfe_"+this.song.getTitle()+".xml");
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
