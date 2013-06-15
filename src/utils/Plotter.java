package utils;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import customException.LogException;
import customException.PlotterException;

import share.log.SongLogger;
import songArtifacts.lowLevel.RhythmHistogramType;
import songArtifacts.lowLevel.SongType;

public final class Plotter {

	public final static void plot(String[] files) 
			throws PlotterException {
		
		Logger log= null; 
		try {
			log= SongLogger.getInstance().getLog();
		} catch(LogException e) {
			throw new PlotterException(e.getMessage(), e);
		}
		
		for(int i= 1; i< files.length; ++i) {
			try {
				File file= new File(files[i]);
				double[] values= Plotter.getRhythmHistogram(file);
				HistogramPlotter hp= new HistogramPlotter(values, file.getName());
				hp.drawFrame();
			} catch(Exception e) {
				log.warning(files[i]+" PLOT FAILED");
			}
		}
	}
	
	private final static double[] getRhythmHistogram(File file) 
			throws PlotterException {
		
		double[] rhValues= null;
		try {
			
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
				
			
		} catch(JAXBException e) {
			throw new PlotterException(e.getMessage(), e);
		} catch(Exception e) {
			throw new PlotterException(e.getMessage(), e);
		}
		
		return rhValues;
	}
}
