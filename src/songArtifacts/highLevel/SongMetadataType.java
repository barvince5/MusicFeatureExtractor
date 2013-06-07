//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.06.07 at 08:06:42 PM CEST 
//


package songArtifacts.highLevel;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SongMetadataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SongMetadataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="artist" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}artistType" minOccurs="0"/>
 *         &lt;element name="albumList" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}albumListType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="length" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *         &lt;element name="fileName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="bitrate" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *         &lt;element name="encoding" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="channelsNum" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *         &lt;element name="frequency" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
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
@XmlType(name = "SongMetadataType", propOrder = {
    "artist",
    "albumList",
    "title",
    "length",
    "fileName",
    "bitrate",
    "encoding",
    "channelsNum",
    "frequency"
})
public class SongMetadataType {

    protected ArtistType artist;
    protected List<AlbumListType> albumList;
    @XmlElement(required = true)
    protected String title;
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger length;
    @XmlElement(required = true)
    protected String fileName;
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger bitrate;
    protected String encoding;
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger channelsNum;
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger frequency;
    @XmlAttribute(name = "mbSongID")
    protected String mbSongID;

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
     * Gets the value of the albumList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the albumList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAlbumList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AlbumListType }
     * 
     * 
     */
    public List<AlbumListType> getAlbumList() {
        if (albumList == null) {
            albumList = new ArrayList<AlbumListType>();
        }
        return this.albumList;
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
