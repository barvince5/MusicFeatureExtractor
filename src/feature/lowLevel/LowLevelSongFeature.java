package feature.lowLevel;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import songArtifacts.lowLevel.RhythmHistogramType;
import songArtifacts.lowLevel.RhythmPatternType;
import songArtifacts.lowLevel.RowType;
import songArtifacts.lowLevel.SongType;
import songArtifacts.lowLevel.ObjectFactory;
import songArtifacts.lowLevel.SsdType;
import songArtifacts.lowLevel.StatisticalSpectrumDescriptorType;
import utils.DateConverter;

import at.tuwien.ifs.feature.extraction.audio.AudioFileExtractor;
import at.tuwien.ifs.feature.extraction.audio.FeatureExtractorException;
import at.tuwien.ifs.feature.extraction.audio.data.FeatureExtractionOptions;
import at.tuwien.ifs.feature.extraction.audio.data.RealMatrixExt;

import customException.DateConverterException;
import customException.SongFeatureException;
import entagged.audioformats.AudioFile;
import feature.MP3Info;

public final class LowLevelSongFeature {

	public final  static Boolean start(MP3Info mp3, File file) 
			throws SongFeatureException {
		
		FeatureExtractionOptions opt= null;
		AudioFileExtractor afe= null;
		RealMatrixExt[] rm= null;
		File output= null;
		
		try {
			
			opt= new FeatureExtractionOptions();
	        opt.extractRH = true;
	        opt.extractRP = true;
	        opt.extractSSD = true;
	        afe= new AudioFileExtractor();
	       
			//extracts low level features from the mp3 file
			rm = afe.extractAudioFile(file, opt);
			
			//puts values inside artifacts
			ObjectFactory obf= new ObjectFactory();
			SongType song= obf.createSongType();
			
			AudioFile af= mp3.getAudioFile();
			
			//set filename
			song.setFileName(af.getName());
			
			//length
			song.setLength(BigInteger.valueOf(af.getLength()));
			
			//set the bitrate value
			song.setBitrate(BigInteger.valueOf(af.getBitrate()));
			
			//encoding
			song.setEncoding(af.getEncodingType());
			
			//channel
			song.setChannelsNum(BigInteger.valueOf(af.getChannelNumber()));
			
			//frequency
			song.setFrequency(BigInteger.valueOf(af.getSamplingRate()));
			
			//set file creation date
			song.setXMLFileCreation(DateConverter.CurrentDateToXMLGregorianCalendar());
			
			//set ssd
			StatisticalSpectrumDescriptorType ssd= obf.createStatisticalSpectrumDescriptorType();
			SsdType mean= obf.createSsdType();
			SsdType median= obf.createSsdType();
			SsdType variance= obf.createSsdType();
			SsdType skewness= obf.createSsdType();
			SsdType kurtosis= obf.createSsdType();
			SsdType minValue= obf.createSsdType();
			SsdType maxValue= obf.createSsdType();
			
			double[][] ssdMatrix= rm[0].getData();
			int rows= rm[0].getRowDimension();
			
			//there are 23 rows with 7 measures
			mean.setSize(BigInteger.valueOf(rows));
			for(int i= 0; i< rows; ++i) {
				mean.getValue().add(new Double(ssdMatrix[i][0]));
				median.getValue().add(new Double(ssdMatrix[i][1]));
				variance.getValue().add(new Double(ssdMatrix[i][2]));
				skewness.getValue().add(new Double(ssdMatrix[i][3]));
				kurtosis.getValue().add(new Double(ssdMatrix[i][4]));
				minValue.getValue().add(new Double(ssdMatrix[i][5]));
				maxValue.getValue().add(new Double(ssdMatrix[i][6]));
			}
			
			median.setSize(BigInteger.valueOf(rows));
			variance.setSize(BigInteger.valueOf(rows));
			skewness.setSize(BigInteger.valueOf(rows));
			kurtosis.setSize(BigInteger.valueOf(rows));
			minValue.setSize(BigInteger.valueOf(rows));
			maxValue.setSize(BigInteger.valueOf(rows));
			
			ssd.setMean(mean);
			ssd.setMedian(median);
			ssd.setVariance(variance);
			ssd.setSkewness(skewness);
			ssd.setKurtosis(kurtosis);
			ssd.setMinValue(minValue);
			ssd.setMaxValue(maxValue);
			song.setStatisticalSpectrumDescriptor(ssd);
			
			//Rhythm pattern
			RhythmPatternType rp= obf.createRhythmPatternType();
			rows= rm[1].getRowDimension();
			int columns= rm[1].getColumnDimension();
			
			rp.setRows(BigInteger.valueOf(rows));
			rp.setColumns(BigInteger.valueOf(columns));
			String des= "The rhythm pattern describes modulation amplitude for a series of critical bands (rows) on various frequencies (columns)";
			rp.setDescription(des);
			
			double[][] rpMatrix= rm[1].getData();
			for(int i= 0; i< rows; ++i) {
				RowType rt= obf.createRowType();
				for(int j= 0; j< columns; ++j)
					rt.getColumn().add(new Double(rpMatrix[i][j]));
				rp.getRow().add(rt);
			}
			
			song.setRhythmPattern(rp);
			
			//rhythm histogram
			RhythmHistogramType rh= obf.createRhythmHistogramType();
			columns= rm[2].getColumnDimension();
			rh.setSize(BigInteger.valueOf(columns));
			des= "Rhythm histogram contains general descriptors for rhythmics for a number of modulation frequencies";
			rh.setDescription(des);
			
			double[] rhArray= rm[2].getData()[0]; //there is only one row
			for(int i= 0; i< columns; ++i)
				rh.getValue().add(new Double(rhArray[i]));
			
			song.setRhythmHistogram(rh);
			
			//marshall this JaxbElement
			JAXBContext jc= JAXBContext.newInstance("songArtifacts.lowLevel");
			JAXBElement<SongType> je= obf.createSongMetadata(song);
			Marshaller m= jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			SchemaFactory sf= SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
			Schema schema= sf.newSchema(new File("MetadataSchema/songLowLevel.xsd"));
			m.setSchema(schema);
			m.setEventHandler(new ValidationEventHandler() {
				
				@Override
				public boolean handleEvent(ValidationEvent event) {
					return false;
				}
			});
			
			//TODO correct path it's not present yet.
			
			output= new File("LL_"+mp3.getAudioFile().getName()+".xml");
			m.marshal(je, output);
			
		} catch (JAXBException e) {
			if(output != null)
				output.delete();
			throw new SongFeatureException(e.getMessage(), e);
		} catch(UnsupportedAudioFileException e) {
			throw new SongFeatureException(e.getMessage(), e);
		} catch(IOException e) {
			throw new SongFeatureException(e.getMessage(), e);
		} catch(FeatureExtractorException e) {
			throw new SongFeatureException(e.getMessage(), e);
		} catch (DateConverterException e) {
			throw new SongFeatureException(e.getMessage(), e);
		} catch(Exception e) {
			throw new SongFeatureException(e.getMessage(), e);
		}
		
		return true;
	}
	
	
}
