//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.06.07 at 08:07:30 PM CEST 
//


package albumArtifatcs;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for albumMetadataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="albumMetadataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="songList" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}songListType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="artist" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}artistType" minOccurs="0"/>
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="country" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}countryType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="mbArtistID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "albumMetadataType", propOrder = {
    "title",
    "songList",
    "artist",
    "date",
    "country"
})
public class AlbumMetadataType {

    @XmlElement(required = true)
    protected String title;
    protected List<SongListType> songList;
    protected ArtistType artist;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar date;
    protected String country;
    @XmlAttribute(name = "mbArtistID")
    protected String mbArtistID;

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
     * Gets the value of the songList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the songList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSongList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SongListType }
     * 
     * 
     */
    public List<SongListType> getSongList() {
        if (songList == null) {
            songList = new ArrayList<SongListType>();
        }
        return this.songList;
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
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * Gets the value of the mbArtistID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMbArtistID() {
        return mbArtistID;
    }

    /**
     * Sets the value of the mbArtistID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMbArtistID(String value) {
        this.mbArtistID = value;
    }

}
