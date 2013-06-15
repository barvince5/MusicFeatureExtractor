//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.06.15 at 12:19:05 PM CEST 
//


package songArtifacts.lowLevel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StatisticalSpectrumDescriptorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StatisticalSpectrumDescriptorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mean" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}ssdType"/>
 *         &lt;element name="median" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}ssdType"/>
 *         &lt;element name="variance" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}ssdType"/>
 *         &lt;element name="skewness" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}ssdType"/>
 *         &lt;element name="kurtosis" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}ssdType"/>
 *         &lt;element name="minValue" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}ssdType"/>
 *         &lt;element name="maxValue" type="{https://github.com/AntonioCollarino/MusicFeatureExtractor.git}ssdType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatisticalSpectrumDescriptorType", propOrder = {
    "mean",
    "median",
    "variance",
    "skewness",
    "kurtosis",
    "minValue",
    "maxValue"
})
public class StatisticalSpectrumDescriptorType {

    @XmlElement(required = true)
    protected SsdType mean;
    @XmlElement(required = true)
    protected SsdType median;
    @XmlElement(required = true)
    protected SsdType variance;
    @XmlElement(required = true)
    protected SsdType skewness;
    @XmlElement(required = true)
    protected SsdType kurtosis;
    @XmlElement(required = true)
    protected SsdType minValue;
    @XmlElement(required = true)
    protected SsdType maxValue;

    /**
     * Gets the value of the mean property.
     * 
     * @return
     *     possible object is
     *     {@link SsdType }
     *     
     */
    public SsdType getMean() {
        return mean;
    }

    /**
     * Sets the value of the mean property.
     * 
     * @param value
     *     allowed object is
     *     {@link SsdType }
     *     
     */
    public void setMean(SsdType value) {
        this.mean = value;
    }

    /**
     * Gets the value of the median property.
     * 
     * @return
     *     possible object is
     *     {@link SsdType }
     *     
     */
    public SsdType getMedian() {
        return median;
    }

    /**
     * Sets the value of the median property.
     * 
     * @param value
     *     allowed object is
     *     {@link SsdType }
     *     
     */
    public void setMedian(SsdType value) {
        this.median = value;
    }

    /**
     * Gets the value of the variance property.
     * 
     * @return
     *     possible object is
     *     {@link SsdType }
     *     
     */
    public SsdType getVariance() {
        return variance;
    }

    /**
     * Sets the value of the variance property.
     * 
     * @param value
     *     allowed object is
     *     {@link SsdType }
     *     
     */
    public void setVariance(SsdType value) {
        this.variance = value;
    }

    /**
     * Gets the value of the skewness property.
     * 
     * @return
     *     possible object is
     *     {@link SsdType }
     *     
     */
    public SsdType getSkewness() {
        return skewness;
    }

    /**
     * Sets the value of the skewness property.
     * 
     * @param value
     *     allowed object is
     *     {@link SsdType }
     *     
     */
    public void setSkewness(SsdType value) {
        this.skewness = value;
    }

    /**
     * Gets the value of the kurtosis property.
     * 
     * @return
     *     possible object is
     *     {@link SsdType }
     *     
     */
    public SsdType getKurtosis() {
        return kurtosis;
    }

    /**
     * Sets the value of the kurtosis property.
     * 
     * @param value
     *     allowed object is
     *     {@link SsdType }
     *     
     */
    public void setKurtosis(SsdType value) {
        this.kurtosis = value;
    }

    /**
     * Gets the value of the minValue property.
     * 
     * @return
     *     possible object is
     *     {@link SsdType }
     *     
     */
    public SsdType getMinValue() {
        return minValue;
    }

    /**
     * Sets the value of the minValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link SsdType }
     *     
     */
    public void setMinValue(SsdType value) {
        this.minValue = value;
    }

    /**
     * Gets the value of the maxValue property.
     * 
     * @return
     *     possible object is
     *     {@link SsdType }
     *     
     */
    public SsdType getMaxValue() {
        return maxValue;
    }

    /**
     * Sets the value of the maxValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link SsdType }
     *     
     */
    public void setMaxValue(SsdType value) {
        this.maxValue = value;
    }

}
