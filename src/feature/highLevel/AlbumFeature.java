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

package feature.highLevel;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import feature.MP3Info;
import feature.lowLevel.LowLevelSongFeature;
import httpGET.GetHttpPage;

import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import log.AlbumLogger;
import musicbrainz.MusicbrainzUrl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.CreateDoc;
import utils.DateConverter;
import utils.FindAlbumArtist;

import albumArtifacts.AlbumType;
import albumArtifacts.ArtistType;
import albumArtifacts.MediumListType;
import albumArtifacts.ObjectFactory;
import albumArtifacts.SongListType;
import albumArtifacts.SongType;

import customException.AlbumFeatureException;
import customException.CreateDocException;
import customException.DateConverterException;
import customException.FindAlbumArtistException;
import customException.GetHttpException;
import customException.LogException;
import customException.MP3Exception;
import customException.MusicbrainzUrlException;

/**
 * This class is able to get some information about the album of and artist and it stores them in an xml file.
 */
public class AlbumFeature extends MP3Info implements Callable<Boolean> {

	private ObjectFactory obf= null;
	private AlbumType album= null;
	private Document albumDoc= null;
	private Document songsDoc= null;
	private String albumID= null;
	private GetHttpPage getHttp= null;
	private Logger log;
	
	/**
	 * This is the constructor.
	 * @param song is the .mp3 file
	 * @throws MP3Exception because this class extends MP3Info and in case of error this exception is thrown.
	 */
	public AlbumFeature(File song) 
			throws MP3Exception {
		
		super(song);
		this.obf= new ObjectFactory();
		this.album= this.obf.createAlbumType();
		this.getHttp= GetHttpPage.getInstance();
		try {
			this.log= AlbumLogger.getInstance().getLog();
		} catch (LogException e) {
			throw new MP3Exception("LogException "+e.getMessage(), e);
		}
	}
	
	/**
	 * This method as the all procedure for extract the features.
	 */
	@Override
	public final Boolean call() 
			throws AlbumFeatureException {
		
		String content= "";
		File output= null;
		NodeList nodeList= null;
		
		try {
			
			String title= super.getTitle();
			if(title == null || title.equals("")) {
				this.log.info("The title is null for file: "+super.getAudioFile().getName());
				return false;
			}
			
			String albumName= super.getAlbum();
			String artistName= super.getArtist();
			if(albumName.equals("") || artistName.equals("")) {
				
				FindAlbumArtist finder= new FindAlbumArtist(artistName, albumName, title);
				artistName= finder.getArtistName();
				albumName= finder.getAlbumName();
				if(albumName.equals("") || artistName.equals("")) {
					this.log.info("Could not get Artist and Album from musicbrainz for file: "+super.getAudioFile().getName());
					return false;
				}
			}
			
			Element albumListNode= this.getAlbumListNode(artistName, albumName);
			if(albumListNode == null) {
				
				FindAlbumArtist finder= new FindAlbumArtist(artistName, albumName, title);
				artistName= finder.getArtistName();
				albumName= finder.getAlbumName();
				
				if(albumName.equals("") || artistName.equals("")) {
					this.log.info("Could not get Artist and Album from musicbrainz for file: "+super.getAudioFile().getName());
					return false;
				}
				
				albumListNode= this.getAlbumListNode(artistName, albumName);
				if(albumListNode == null) {
					this.log.info("Could not get Artist and Album from musicbrainz for file: "+super.getAudioFile().getName());
					return false;
				}
			}
			
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
				
				content= this.getHttp.getWebPageAsString(MusicbrainzUrl.getMbRecordingsOfRelease(this.albumID));
				if(content.equals("") == false) {
					
					this.songsDoc= CreateDoc.create(content);
					SongListType slt= this.obf.createSongListType();

					//if the count is zero, no song was found.
					Element songListNode= (Element) this.songsDoc.getElementsByTagName("recording-list").item(0);
					Integer count= Integer.valueOf(songListNode.getAttributes().getNamedItem("count").getNodeValue());
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
			
			//set the creation date
			this.album.setXMLFileCreation(DateConverter.CurrentDateToXMLGregorianCalendar());
			
			//marshall this JaxbElement
			JAXBContext jc= JAXBContext.newInstance("albumArtifacts");
			JAXBElement<AlbumType> je= this.obf.createAlbumMetadata(this.album);
			Marshaller m= jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			SchemaFactory sf= SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
			InputStream is= LowLevelSongFeature.class.getClassLoader().getResourceAsStream("MetadataSchema/album.xsd");
			Schema schema= sf.newSchema(new StreamSource(is));
			m.setSchema(schema);
			m.setEventHandler(new ValidationEventHandler() {
				
				@Override
				public boolean handleEvent(ValidationEvent event) {
					return false;
				}
			});
			
			output= new File("ALBUM_"+this.album.getTitle()+".xml");
			m.marshal(je, output);

		} catch (JAXBException e) {
			if(output != null)
				output.delete();
			this.log.warning("Marshalling validation error for file: "+super.getAudioFile().getName());
			throw new AlbumFeatureException("JAXBException "+e.getMessage(), e);
		} catch (FindAlbumArtistException e) {
			this.log.warning("Error finding album for file: "+super.getAudioFile().getName());
			throw new AlbumFeatureException("FindAlbumArtistException "+e.getMessage(), e);
		} catch (NullPointerException e) {
			this.log.warning("NullPointerException for file: "+super.getAudioFile().getName());
			throw new AlbumFeatureException("NullPointerException "+e.getMessage(), e);
		} catch (DateConverterException e) {
			this.log.warning("Error converting date for file: "+super.getAudioFile().getName());
			throw new AlbumFeatureException("DateConverterException "+e.getMessage(), e);
		} catch (MusicbrainzUrlException e) {
			this.log.warning("Error musicbrainz url generation for file: "+super.getAudioFile().getName());
			throw new AlbumFeatureException("MusicbrainzUrlException "+e.getMessage(), e);
		} catch (GetHttpException e) {
			this.log.warning("Error getting html page for file: "+super.getAudioFile().getName());
			throw new AlbumFeatureException("GetHttpException "+e.getMessage(), e);
		} catch (CreateDocException e) {
			this.log.warning("Error creating document DOM for file: "+super.getAudioFile().getName());
			throw new AlbumFeatureException("MusicbrainzDocException doc creation problem "+e.getMessage(), e);
		} catch (Exception e) {
			this.log.warning("Error "+e.getMessage()+" for file: "+super.getAudioFile().getName());
			throw new AlbumFeatureException("Exception "+e.getMessage(), e);
		}
		
		return true;
	}
	
	/**
	 * This method gets the corresponding doc. Element given the name of the album name as input. A query to
	 * musicbrainz website is done.
	 * @param artistName
	 * @return Element
	 * @throws GetHttpException in case of http error.
	 * @throws MusicbrainzUrlException if the url is not correct
	 * @throws CreateDocException is it is not possible to create the DOM document.
	 */
	private final Element getAlbumListNode(String artistName, String albumName) 
			throws GetHttpException, MusicbrainzUrlException, CreateDocException {
		
		String content= "";
		
		content= this.getHttp.getWebPageAsString(MusicbrainzUrl.getMbRelease(artistName, albumName));
		if(content.equals(""))
			return null;
		this.albumDoc= CreateDoc.create(content);
		
		//if the count is zero, no album was found.
		Element albumListNode= (Element) this.albumDoc.getElementsByTagName("release-list").item(0);
		Integer count= Integer.valueOf(albumListNode.getAttributes().getNamedItem("count").getNodeValue());
		if(count.intValue() == 0)
			return null;
		else
			return albumListNode;
	}
}
