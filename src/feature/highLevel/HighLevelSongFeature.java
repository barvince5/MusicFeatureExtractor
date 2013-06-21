package feature.highLevel;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import songArtifacts.highLevel.AlbumListType;
import songArtifacts.highLevel.AlbumType;
import songArtifacts.highLevel.ObjectFactory;
import songArtifacts.highLevel.SongType;
import songArtifacts.highLevel.ArtistType;
import utils.CreateDoc;
import utils.DateConverter;
import utils.FindAlbumArtist;

import httpGET.GetHttpPage;

import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import musicbrainz.MusicbrainzUrl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import customException.CreateDocException;
import customException.DateConverterException;
import customException.FindAlbumArtistException;
import customException.GetHttpException;
import customException.MusicbrainzUrlException;
import customException.SongFeatureException;
import entagged.audioformats.AudioFile;
import feature.MP3Info;
import feature.lowLevel.LowLevelSongFeature;

/**
 * This class is able to get some high level information about the song and it stores them in an xml file.
 */
public final class HighLevelSongFeature {
	
	/**
	 * This method as the all procedure for extract the features.
	 */
	public final static Boolean start(MP3Info mp3)
			throws SongFeatureException {
		
		GetHttpPage getHttp= GetHttpPage.getInstance();
		String content= "";
		File output= null;
		NodeList nodeList= null;
		SongType song= null;
		ObjectFactory obf= null;
		Document songDoc= null;
		
		try {
			
			obf= new ObjectFactory();
			song= obf.createSongType();
			
			String title= mp3.getTitle();
			if(title == null || title.equals(""))
				return false;
			
			String artistName= mp3.getArtist();
			String albumName = mp3.getAlbum();
			
			// try all parameters, the function will use the most it can
			content= getHttp.getWebPageAsString(MusicbrainzUrl.getMbRecordingUrl(artistName, title, albumName));
			if(content.equals(""))
				return false;
			
			songDoc= CreateDoc.create(content);
			
			//if the count is zero, no song was found.
			Element recordingListNode= (Element) songDoc.getElementsByTagName("recording-list").item(0);
			Integer count= Integer.valueOf(recordingListNode.getAttributes().getNamedItem("count").getNodeValue());
			if(count.intValue() == 0) {
			
				// some tags were not correct, performing a more generic query to find the right one
				FindAlbumArtist finder= new FindAlbumArtist(artistName, albumName, title);
				artistName= finder.getArtistName();
				albumName= finder.getAlbumName();
			}
					
			// try the query with the new parameters
			content= getHttp.getWebPageAsString(MusicbrainzUrl.getMbRecordingUrl(artistName, title, albumName));
			if(content.equals(""))
					return false;
					
			songDoc= CreateDoc.create(content);
				
			recordingListNode= (Element) songDoc.getElementsByTagName("recording-list").item(0);
			count= Integer.valueOf(recordingListNode.getAttributes().getNamedItem("count").getNodeValue());
				
			if(count.intValue() == 0) 
				return false;		
				
			
			Element recordingNode= (Element) recordingListNode.getElementsByTagName("recording").item(0);
	
			//set song id
			song.setMbSongID(recordingNode.getAttribute("id"));
			//set song title

			song.setTitle(recordingNode.getElementsByTagName("title").item(0).getTextContent());
			
			//set artist
			ArtistType artist= obf.createArtistType();
			Element artistNode= (Element) recordingNode.getElementsByTagName("artist").item(0);
			artist.setMbArtistID(artistNode.getAttribute("id"));
			artist.setName(artistNode.getElementsByTagName("name").item(0).getTextContent());
			nodeList= artistNode.getElementsByTagName("disambiguation");			
			if(nodeList.getLength() != 0) 
				artist.setDisambiguation(nodeList.item(0).getTextContent());
			song.setArtist(artist);
			
			//set albums for this song
			nodeList= recordingNode.getElementsByTagName("release");
			AlbumListType albumList = obf.createAlbumListType();
			for (int i=0; i<nodeList.getLength(); ++i) {
				Element releaseNode = (Element) nodeList.item(i);
				AlbumType album = obf.createAlbumType();
				
				//set album ID
				album.setMbAlbumID(releaseNode.getAttribute("id"));
				
				//set album title
				album.setTitle(releaseNode.getElementsByTagName("title").item(0).getTextContent());
				
				//set album status
				NodeList valueList = releaseNode.getElementsByTagName("status");
				if (valueList.getLength() != 0) 
					album.setStatus(valueList.item(0).getTextContent());
				
				//set album date
				valueList = releaseNode.getElementsByTagName("date");
				if (valueList.getLength() != 0) 
					album.setDate(valueList.item(0).getTextContent());
				
				//set album country
				valueList = releaseNode.getElementsByTagName("country");
				if (valueList.getLength() != 0) 
					album.setCountry(valueList.item(0).getTextContent());
				
				//set album track count
				valueList = releaseNode.getElementsByTagName("track-count");
				if (valueList.getLength() != 0) 
					album.setTrackCount(new BigInteger(valueList.item(0).getTextContent()));
				
				albumList.getAlbum().add(album);
			}
			
			if (albumList.getAlbum().isEmpty() == false)
				song.setAlbumList(albumList);
			
			AudioFile afile = mp3.getAudioFile();
			
			//set file length (seconds)
			song.setLength(BigInteger.valueOf(afile.getLength()));
			
			//set file name
			song.setFileName(afile.getName());
			
			//set bitrate
			song.setBitrate(BigInteger.valueOf(afile.getBitrate()));
			
			//set channels
			song.setChannelsNum(BigInteger.valueOf(afile.getChannelNumber()));
			
			//set frequency
			song.setFrequency(BigInteger.valueOf(afile.getSamplingRate()));
			
			//set encoding
			String encoding = afile.getEncodingType();
			if (encoding != null && !encoding.equals(""))
				song.setEncoding(encoding);
			
			//set the creation date
			song.setXMLFileCreation(DateConverter.CurrentDateToXMLGregorianCalendar());
			
			//marshall this JaxbElement
			JAXBContext jc= JAXBContext.newInstance("songArtifacts.highLevel");
			JAXBElement<SongType> je= obf.createSongMetadata(song);
			Marshaller m= jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			SchemaFactory sf= SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
			InputStream is= LowLevelSongFeature.class.getClassLoader().getResourceAsStream("MetadataSchema/songHighLevel.xsd");
			Schema schema= sf.newSchema(new StreamSource(is));
			m.setSchema(schema);
			m.setEventHandler(new ValidationEventHandler() {
				
				@Override
				public boolean handleEvent(ValidationEvent event) {
					return false;
				}
			});
			
			
			output= new File("SONG_HL_"+song.getTitle()+".xml");
			m.marshal(je, output);
			
		} catch (JAXBException e) {
			if(output != null)
				output.delete();
			throw new SongFeatureException("JAXBException "+e.getMessage(), e);	
		} catch (FindAlbumArtistException e) {
			throw new SongFeatureException("FindAlbumArtistException "+e.getMessage(), e);
		} catch (DateConverterException e) {
			throw new SongFeatureException("DateConverterException "+e.getMessage(), e);
		} catch (NullPointerException e) {
			throw new SongFeatureException("NullPointerException "+e.getMessage(), e);
		} catch (MusicbrainzUrlException e) {
			throw new SongFeatureException("MusicbrainzUrlException "+e.getMessage(), e);
		} catch (GetHttpException e) {
			throw new SongFeatureException("GetHttpException "+e.getMessage(), e);
		} catch (CreateDocException e) {
			throw new SongFeatureException("MusicbrainzDocException doc creation problem "+e.getMessage(), e);
		} catch (Exception e) {
			throw new SongFeatureException("Exception "+e.getMessage(), e);
		}
		
		return true;
	}
	
}
