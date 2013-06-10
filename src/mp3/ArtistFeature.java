package mp3;

import httpGET.GetHttpPage;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import musicbrainz.MusicbrainzUrl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.CreateDoc;
import utils.FindAlbumArtist;
import wikipedia.ArtistBiography;

import artistArtifacts.AlbumListType;
import artistArtifacts.AlbumType;
import artistArtifacts.AliasListType;
import artistArtifacts.ArtistType;
import artistArtifacts.LinkListType;
import artistArtifacts.ObjectFactory;

import customException.FindAlbumArtistException;
import customException.GetHttpException;
import customException.MP3Exception;
import customException.CreateDocException;
import customException.MusicbrainzUrlException;

public class ArtistFeature extends MP3Info {

	private ObjectFactory obf= null;
	private ArtistType artist= null;
	private Document artistDoc= null;
	private Document albumsDoc= null;
	private Document linksDoc= null;
	private String artistID= null;
	private GetHttpPage getHttp= null;
	
	public ArtistFeature(File song) 
			throws MP3Exception {
		
		super(song);
		this.obf= new ObjectFactory();
		this.artist= this.obf.createArtistType();
		this.getHttp= GetHttpPage.getInstance();
	}
		
	public final void stop() {
		
	}
	
	private final Element getArtistListNode(String artistName) 
			throws GetHttpException, MusicbrainzUrlException, CreateDocException {
		
		String content= "";
		
		content= this.getHttp.getWebPageAsString(MusicbrainzUrl.getMbArtistUrl(artistName));
		if(content.equals(""))
			return null;
		this.artistDoc= CreateDoc.create(content);
		
		//if the count is zero, no artist was found.
		Element artistListNode= (Element) this.artistDoc.getElementsByTagName("artist-list").item(0);
		Integer count= Integer.valueOf(artistListNode.getAttributes().getNamedItem("count").getNodeValue());
		if(count.intValue() == 0)
			return null;
		else
			return artistListNode;
	}

	public final boolean start() 
			throws MP3Exception {
				
		String content= "";
		File output= null;
		NodeList nodeList= null;
		
		try {
			
			String title= super.getTitle();
			if(title.equals(""))
				return false;
			
			String artistName= super.getArtist();
			if(artistName.equals("")) {
				
				FindAlbumArtist finder= new FindAlbumArtist(artistName, super.getAlbum(), title);
				artistName= finder.getArtistName();
				if(artistName.equals(""))
					return false;
			}
		
			Element artistListNode= this.getArtistListNode(artistName);
			if(artistListNode == null) {
				
				FindAlbumArtist finder= new FindAlbumArtist(artistName, super.getAlbum(), title);
				artistName= finder.getArtistName();
				if(artistName.equals(""))
					return false;
				
				artistListNode= this.getArtistListNode(artistName);
				if(artistListNode == null)
					return false;
			}
			
			Element artistNode= (Element) artistListNode.getElementsByTagName("artist").item(0);
			this.artistID= artistNode.getAttribute("id"); //global variable useful for next queries.
			
			//set artist id
			this.artist.setMbArtistID(this.artistID);
			
			//set artist type (e.g person or group)
			this.artist.setType(artistNode.getAttribute("type"));
			
			//set artist/group name
			nodeList= artistNode.getElementsByTagName("name");
			if(nodeList.getLength() != 0) {
				this.artist.setName(nodeList.item(0).getTextContent());
			}
			
			//set gender (only for person)
			nodeList= artistNode.getElementsByTagName("gender");
			if(nodeList.getLength() != 0) {
				this.artist.setGender(nodeList.item(0).getTextContent());
			}
			
			//set country
			nodeList= artistNode.getElementsByTagName("country");
			if(nodeList.getLength() != 0) {
				this.artist.setCountry(nodeList.item(0).getTextContent());
			}
			
			//set begin
			nodeList= artistNode.getElementsByTagName("begin");
			if(nodeList.getLength() != 0) {
				this.artist.setBegin(nodeList.item(0).getTextContent());
			}
			
			//set ended/end
			nodeList= artistNode.getElementsByTagName("ended");
			if(nodeList.getLength() != 0) {
				this.artist.setEnded(Boolean.valueOf(nodeList.item(0).getTextContent()));
				if(this.artist.isEnded())
					this.artist.setEnd(artistNode.getElementsByTagName("end").item(0).getTextContent());
			}
			
			//set disambiguation
			nodeList= artistNode.getElementsByTagName("disambiguation");
			if(nodeList.getLength() != 0) {
				this.artist.setDisambiguation(nodeList.item(0).getTextContent());
			}
			
			//set alias list
			nodeList= artistNode.getElementsByTagName("alias");
			if(nodeList.getLength() != 0) {
				AliasListType aliasList= this.obf.createAliasListType();
				for(int i= 0; i< nodeList.getLength(); ++i)
					aliasList.getAlias().add((nodeList.item(i).getTextContent()));
				this.artist.setAliasList(aliasList);
			}
			
			//get all links for this artist, new this.getHttp is required
			content= "";
			content= this.getHttp.getWebPageAsString(MusicbrainzUrl.getMbLinksUrl(this.artistID));
			if(content.equals("") == false) {
				
				this.linksDoc= CreateDoc.create(content);
				nodeList= this.linksDoc.getElementsByTagName("target");
				
				if(nodeList.getLength() != 0) {
					
					String wikipediaLink= null;
					String link= null;
					LinkListType linkList= this.obf.createLinkListType();
					
					for(int i= 0; i< nodeList.getLength(); ++i) {
						link= nodeList.item(i).getTextContent();
						if(link.contains("en.wikipedia"))
							wikipediaLink= link;
						linkList.getLink().add(link);
					}
					
					this.artist.setLinkList(linkList);
					
					//get wikipedia biography
					if(wikipediaLink != null)
						this.artist.setBiography(ArtistBiography.getInstance().getArtistBiography(wikipediaLink));
				}
			}
			
			//get all albums of this artist
			content= "";
			content= this.getHttp.getWebPageAsString(MusicbrainzUrl.getMbReleasesOfArtist(this.artistID));
			if(content.equals("") == false) {
				
				this.albumsDoc= CreateDoc.create(content);
				nodeList= this.albumsDoc.getElementsByTagName("release-group");
				
				AlbumListType albumList= this.obf.createAlbumListType();
				for(int i= 0; i< nodeList.getLength(); ++i) {
					
					AlbumType album= this.obf.createAlbumType();
					Element element= (Element) nodeList.item(i);
					album.setMbAlbumID(element.getAttribute("id"));
					album.setType(element.getAttribute("type"));
					album.setTitle(element.getElementsByTagName("title").item(0).getTextContent());
					album.setDate(element.getElementsByTagName("first-release-date").item(0).getTextContent());
					albumList.getAlbum().add(album);
					
				}
				this.artist.setAlbumList(albumList);
			}
			
			//marshall this JaxbElement
			JAXBContext jc= JAXBContext.newInstance("artistArtifacts");
			JAXBElement<ArtistType> je= this.obf.createArtistMetadata(this.artist);
			Marshaller m= jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			SchemaFactory sf= SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
			Schema schema= sf.newSchema(new File("MetadataSchema/artist.xsd"));
			m.setSchema(schema);
			m.setEventHandler(new ValidationEventHandler() {
				
				@Override
				public boolean handleEvent(ValidationEvent event) {
					return false;
				}
			});
			
			//TODO correct path it's not present yet.
			
			output= new File("mfe_"+this.artist.getName()+".xml");
			m.marshal(je, output);
			
		} catch (JAXBException e) {
			if(output != null)
				output.delete();
			throw new MP3Exception("JAXBException "+e.getMessage(), e);
		} catch (FindAlbumArtistException e) {
			throw new MP3Exception("FindAlbumArtistException "+e.getMessage(), e);
		} catch (NullPointerException e) {
			throw new MP3Exception("NullPointerException "+e.getMessage(), e);
		} catch (MusicbrainzUrlException e) {
			throw new MP3Exception("MusicbrainzUrlException "+e.getMessage(), e);
		} catch (GetHttpException e) {
			throw new MP3Exception("GetHttpException "+e.getMessage(), e);
		} catch (CreateDocException e) {
			throw new MP3Exception("MusicbrainzDocException doc creation problem "+e.getMessage(), e);
		} catch (Exception e) {
			throw new MP3Exception("Exception "+e.getMessage(), e);
		}
		
		return true;
	}
}
