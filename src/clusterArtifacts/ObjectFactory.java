//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.06.16 at 04:15:14 PM CEST 
//


package clusterArtifacts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the clusterArtifacts package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ClusterData_QNAME = new QName("https://github.com/AntonioCollarino/MusicFeatureExtractor.git", "ClusterData");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: clusterArtifacts
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ClusterDataType }
     * 
     */
    public ClusterDataType createClusterDataType() {
        return new ClusterDataType();
    }

    /**
     * Create an instance of {@link ClusterType }
     * 
     */
    public ClusterType createClusterType() {
        return new ClusterType();
    }

    /**
     * Create an instance of {@link ClusterListType }
     * 
     */
    public ClusterListType createClusterListType() {
        return new ClusterListType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClusterDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://github.com/AntonioCollarino/MusicFeatureExtractor.git", name = "ClusterData")
    public JAXBElement<ClusterDataType> createClusterData(ClusterDataType value) {
        return new JAXBElement<ClusterDataType>(_ClusterData_QNAME, ClusterDataType.class, null, value);
    }

}
