//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.06.09 at 06:18:55 PM CEST 
//


package songArtifacts.highLevel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for artistType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="artistType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="disambiguation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "artistType", propOrder = {
    "disambiguation",
    "name"
})
public class ArtistType {

    protected String disambiguation;
    @XmlElement(required = true)
    protected String name;
    @XmlAttribute(name = "mbArtistID")
    protected String mbArtistID;

    /**
     * Gets the value of the disambiguation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisambiguation() {
        return disambiguation;
    }

    /**
     * Sets the value of the disambiguation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisambiguation(String value) {
        this.disambiguation = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
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
