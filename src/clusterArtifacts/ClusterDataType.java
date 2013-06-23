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
// Generated on: 2013.06.23 at 12:03:48 PM CEST 
//


package clusterArtifacts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ClusterDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ClusterDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="XMLFileCreation" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="clusterList" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}clusterListType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClusterDataType", propOrder = {
    "xmlFileCreation",
    "clusterList"
})
public class ClusterDataType {

    @XmlElement(name = "XMLFileCreation", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar xmlFileCreation;
    @XmlElement(required = true)
    protected ClusterListType clusterList;

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
     * Gets the value of the clusterList property.
     * 
     * @return
     *     possible object is
     *     {@link ClusterListType }
     *     
     */
    public ClusterListType getClusterList() {
        return clusterList;
    }

    /**
     * Sets the value of the clusterList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClusterListType }
     *     
     */
    public void setClusterList(ClusterListType value) {
        this.clusterList = value;
    }

}
