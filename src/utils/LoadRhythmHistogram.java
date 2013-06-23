package utils;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import feature.lowLevel.LowLevelSongFeature;

import songArtifacts.lowLevel.RhythmHistogramType;
import songArtifacts.lowLevel.SongType;

/**
 * This class gets data from the xml file containing the low level feature of a song.
 */
public final class LoadRhythmHistogram {
	
	/**
	 * To avoid instantiation of this class.
	 */
	private LoadRhythmHistogram() {
		
	}
	
	/**
	 * This method gets the rhythm histogram array of 60 values.
	 * @param file
	 * @return rhythm histogram values
	 * @throws JAXBException
	 * @throws SAXException
	 */
	public final static double[] getValues(File file) 
			throws JAXBException, SAXException {
		
		double[] rhValues= null;

			
		JAXBContext jc= JAXBContext.newInstance("songArtifacts.lowLevel");
		Unmarshaller u= jc.createUnmarshaller();
		SchemaFactory sf= SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
		InputStream is= LowLevelSongFeature.class.getClassLoader().getResourceAsStream("MetadataSchema/songLowLevel.xsd");
		Schema schema= sf.newSchema(new StreamSource(is));
		u.setSchema(schema);
		u.setEventHandler(new ValidationEventHandler() {
				
			@Override
			public boolean handleEvent(ValidationEvent event) {
				return false;
			}
		});

		@SuppressWarnings("unchecked")
		JAXBElement<SongType> je= (JAXBElement<SongType>) u.unmarshal(file);
		SongType song= je.getValue();
			
		RhythmHistogramType rh= song.getRhythmHistogram();
		List<Double> values= rh.getValue();
		rhValues= new double[values.size()];
			
		Iterator<Double> iter= values.iterator();
		for(int i= 0; iter.hasNext(); ++i)
			rhValues[i]= iter.next().doubleValue();

		return rhValues;
	}
}
