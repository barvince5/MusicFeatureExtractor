//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.06.26 at 06:06:37 PM CEST 
//


package clusterArtifacts;

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
 * <p>Java class for clusterListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="clusterListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cluster" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}clusterType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="k" use="required" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *       &lt;attribute name="iter" use="required" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clusterListType", propOrder = {
    "cluster"
})
public class ClusterListType {

    @XmlElement(required = true)
    protected List<ClusterType> cluster;
    @XmlAttribute(name = "k", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger k;
    @XmlAttribute(name = "iter", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger iter;

    /**
     * Gets the value of the cluster property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cluster property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCluster().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClusterType }
     * 
     * 
     */
    public List<ClusterType> getCluster() {
        if (cluster == null) {
            cluster = new ArrayList<ClusterType>();
        }
        return this.cluster;
    }

    /**
     * Gets the value of the k property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getK() {
        return k;
    }

    /**
     * Sets the value of the k property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setK(BigInteger value) {
        this.k = value;
    }

    /**
     * Gets the value of the maxIter property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIter() {
        return iter;
    }

    /**
     * Sets the value of the maxIter property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIter(BigInteger value) {
        this.iter = value;
    }

}
