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
		this.song= this.obf.createSongType();
		
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
			if(count.intValue() == 0) {
				// some tags were not correct, performing a more generic query to find the right one
				content= getHttp.getWebPageAsString(MusicbrainzUrl.getMbAllRecordingsUrl(title));
				if(content.equals(""))
					return false;
				
				this.songDoc= MusicbrainzDoc.createDoc(content);
				
				recordingListNode= (Element) this.songDoc.getElementsByTagName("recording-list").item(0);
				count= Integer.valueOf(recordingListNode.getAttributes().getNamedItem("count").getNodeValue());
				// if count is still 0, the title is not correct, and will be skipped
				if(count.intValue() == 0) 
					return false;
				
				// get all recording nodes in the xml and search for the correct one (has one correct tag between release and artist)
				// when one is found, the loop will stop
				NodeList recordingList= this.songDoc.getElementsByTagName("recording");
				boolean found= false;
				// these two strings are for comparison with the ones in the tag. They are initialized to empty string
				// at the beginning of processing each song in the recording list
				String currArtistName;
				String currAlbumName;
				
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
					// first try exact equality, then without spaces
					if (currAlbumName.equalsIgnoreCase(albumName) || currArtistName.equalsIgnoreCase(artistName)
						|| currAlbumName.replace(" ", "").equalsIgnoreCase(albumName.replace(" ", ""))
						|| currArtistName.replace(" ", "").equalsIgnoreCase(artistName.replace(" ", "")) ) {
						artistName= currArtistName;
						albumName= currAlbumName;
						found= true;
					}
				}
				
				// last attempt, try for partially correct artist names (useful for artist collaborations, containing "featuring")
				if (!found) {
					String[] artistNameSplit = artistName.split(" ");
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
						for (int a=0; a< artistNameSplit.length && !found; ++a) {
							if (currArtistName.equalsIgnoreCase(artistNameSplit[a])
								|| currArtistName.replace(" ", "").equalsIgnoreCase(artistNameSplit[a].replace(" ", "")) ) {
									artistName= currArtistName;
									albumName= currAlbumName;
									found= true;
								}	
						}
					}
					
				}
					
				// try the query with the new parameters
				content= getHttp.getWebPageAsString(MusicbrainzUrl.getMbRecordingUrl(artistName, title, albumName));
				if(content.equals(""))
					return false;
					
				this.songDoc= MusicbrainzDoc.createDoc(content);
				
				recordingListNode= (Element) this.songDoc.getElementsByTagName("recording-list").item(0);
				count= Integer.valueOf(recordingListNode.getAttributes().getNamedItem("count").getNodeValue());
				
				if(count.intValue() == 0) 
					return false;		
				
			}
			
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
					this.song.setPosition(nodeList.item(0).getTextContent());	
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
