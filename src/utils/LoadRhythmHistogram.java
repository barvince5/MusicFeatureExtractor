package utils;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import songArtifacts.lowLevel.RhythmHistogramType;
import songArtifacts.lowLevel.SongType;

public final class LoadRhythmHistogram {
	
	private LoadRhythmHistogram() {
		
	}
	
	public final static double[] getValues(File file) 
			throws JAXBException, SAXException {
		
		double[] rhValues= null;

			
			JAXBContext jc= JAXBContext.newInstance("songArtifacts.lowLevel");
			Unmarshaller u= jc.createUnmarshaller();
			SchemaFactory sf= SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
			Schema schema= sf.newSchema(new File("MetadataSchema/songLowLevel.xsd"));
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
