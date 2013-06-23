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

// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.06.15 at 11:38:35 AM CEST 
//


package songArtifacts.highLevel;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for SongType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SongType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="XMLFileCreation" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="artist" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}artistType" minOccurs="0"/>
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fileName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="length" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *         &lt;element name="albumList" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}albumListType" minOccurs="0"/>
 *         &lt;element name="bitrate" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *         &lt;element name="encoding" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="channelsNum" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *         &lt;element name="frequency" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *       &lt;/sequence>
 *       &lt;attribute name="mbSongID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SongType", propOrder = {
    "xmlFileCreation",
    "artist",
    "title",
    "fileName",
    "length",
    "albumList",
    "bitrate",
    "encoding",
    "channelsNum",
    "frequency"
})
public class SongType {

    @XmlElement(name = "XMLFileCreation", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar xmlFileCreation;
    protected ArtistType artist;
    @XmlElement(required = true)
    protected String title;
    @XmlElement(required = true)
    protected String fileName;
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger length;
    protected AlbumListType albumList;
    @XmlElement(required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger bitrate;
    protected String encoding;
    @XmlElement(required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger channelsNum;
    @XmlElement(required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger frequency;
    @XmlAttribute(name = "mbSongID")
    protected String mbSongID;

    /**
     * Gets the value of the xmlFileCreation property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getXMLFileCreation() {
        return xmlFileCreation;
    }

    /**
     * Sets the value of the xmlFileCreation property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setXMLFileCreation(XMLGregorianCalendar value) {
        this.xmlFileCreation = value;
    }

    /**
     * Gets the value of the artist property.
     * 
     * @return
     *     possible object is
     *     {@link ArtistType }
     *     
     */
    public ArtistType getArtist() {
        return artist;
    }

    /**
     * Sets the value of the artist property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArtistType }
     *     
     */
    public void setArtist(ArtistType value) {
        this.artist = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the fileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the value of the fileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    /**
     * Gets the value of the length property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLength() {
        return length;
    }

    /**
     * Sets the value of the length property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLength(BigInteger value) {
        this.length = value;
    }

    /**
     * Gets the value of the albumList property.
     * 
     * @return
     *     possible object is
     *     {@link AlbumListType }
     *     
     */
    public AlbumListType getAlbumList() {
        return albumList;
    }

    /**
     * Sets the value of the albumList property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlbumListType }
     *     
     */
    public void setAlbumList(AlbumListType value) {
        this.albumList = value;
    }

    /**
     * Gets the value of the bitrate property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBitrate() {
        return bitrate;
    }

    /**
     * Sets the value of the bitrate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBitrate(BigInteger value) {
        this.bitrate = value;
    }

    /**
     * Gets the value of the encoding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Sets the value of the encoding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEncoding(String value) {
        this.encoding = value;
    }

    /**
     * Gets the value of the channelsNum property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getChannelsNum() {
        return channelsNum;
    }

    /**
     * Sets the value of the channelsNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setChannelsNum(BigInteger value) {
        this.channelsNum = value;
    }

    /**
     * Gets the value of the frequency property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFrequency() {
        return frequency;
    }

    /**
     * Sets the value of the frequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFrequency(BigInteger value) {
        this.frequency = value;
    }

    /**
     * Gets the value of the mbSongID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMbSongID() {
        return mbSongID;
    }

    /**
     * Sets the value of the mbSongID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMbSongID(String value) {
        this.mbSongID = value;
    }

}
