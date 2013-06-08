//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.06.08 at 04:07:52 PM CEST 
//


package albumArtifacts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the albumArtifacts package. 
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

    private final static QName _AlbumMetadata_QNAME = new QName("https://github.com/AntonioCollarino/MusicFeatureExtractor.git", "albumMetadata");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: albumArtifacts
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AlbumMetadataType }
     * 
     */
    public AlbumMetadataType createAlbumMetadataType() {
        return new AlbumMetadataType();
    }

    /**
     * Create an instance of {@link ArtistType }
     * 
     */
    public ArtistType createArtistType() {
        return new ArtistType();
    }

    /**
     * Create an instance of {@link SongListType }
     * 
     */
    public SongListType createSongListType() {
        return new SongListType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AlbumMetadataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://github.com/AntonioCollarino/MusicFeatureExtractor.git", name = "albumMetadata")
    public JAXBElement<AlbumMetadataType> createAlbumMetadata(AlbumMetadataType value) {
        return new JAXBElement<AlbumMetadataType>(_AlbumMetadata_QNAME, AlbumMetadataType.class, null, value);
    }

}
