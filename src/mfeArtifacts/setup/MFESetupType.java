//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.06.16 at 09:14:10 PM CEST 
//


package mfeArtifacts.setup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for MFESetupType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MFESetupType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="XMLCreationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="authors" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}authorType"/>
 *         &lt;element name="possibleCommands" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}possibleCommandType"/>
 *         &lt;element name="flags" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}flagType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="MFENumberVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="1.0.0" />
 *       &lt;attribute name="MFENameVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="Music Feature Extractor (MFE)" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MFESetupType", propOrder = {
    "xmlCreationDate",
    "authors",
    "possibleCommands",
    "flags"
})
public class MFESetupType {

    @XmlElement(name = "XMLCreationDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar xmlCreationDate;
    @XmlElement(required = true)
    protected AuthorType authors;
    @XmlElement(required = true)
    protected PossibleCommandType possibleCommands;
    @XmlElement(required = true)
    protected FlagType flags;
    @XmlAttribute(name = "MFENumberVersion", required = true)
    protected String mfeNumberVersion;
    @XmlAttribute(name = "MFENameVersion", required = true)
    protected String mfeNameVersion;

    /**
     * Gets the value of the xmlCreationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getXMLCreationDate() {
        return xmlCreationDate;
    }

    /**
     * Sets the value of the xmlCreationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setXMLCreationDate(XMLGregorianCalendar value) {
        this.xmlCreationDate = value;
    }

    /**
     * Gets the value of the authors property.
     * 
     * @return
     *     possible object is
     *     {@link AuthorType }
     *     
     */
    public AuthorType getAuthors() {
        return authors;
    }

    /**
     * Sets the value of the authors property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthorType }
     *     
     */
    public void setAuthors(AuthorType value) {
        this.authors = value;
    }

    /**
     * Gets the value of the possibleCommands property.
     * 
     * @return
     *     possible object is
     *     {@link PossibleCommandType }
     *     
     */
    public PossibleCommandType getPossibleCommands() {
        return possibleCommands;
    }

    /**
     * Sets the value of the possibleCommands property.
     * 
     * @param value
     *     allowed object is
     *     {@link PossibleCommandType }
     *     
     */
    public void setPossibleCommands(PossibleCommandType value) {
        this.possibleCommands = value;
    }

    /**
     * Gets the value of the flags property.
     * 
     * @return
     *     possible object is
     *     {@link FlagType }
     *     
     */
    public FlagType getFlags() {
        return flags;
    }

    /**
     * Sets the value of the flags property.
     * 
     * @param value
     *     allowed object is
     *     {@link FlagType }
     *     
     */
    public void setFlags(FlagType value) {
        this.flags = value;
    }

    /**
     * Gets the value of the mfeNumberVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMFENumberVersion() {
        if (mfeNumberVersion == null) {
            return "1.0.0";
        } else {
            return mfeNumberVersion;
        }
    }

    /**
     * Sets the value of the mfeNumberVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMFENumberVersion(String value) {
        this.mfeNumberVersion = value;
    }

    /**
     * Gets the value of the mfeNameVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMFENameVersion() {
        if (mfeNameVersion == null) {
            return "Music Feature Extractor (MFE)";
        } else {
            return mfeNameVersion;
        }
    }

    /**
     * Sets the value of the mfeNameVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMFENameVersion(String value) {
        this.mfeNameVersion = value;
    }

}
