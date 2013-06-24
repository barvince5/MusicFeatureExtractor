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

package feature.lowLevel;

/**
 * This class extract information related to the audio content.
 */ 
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import log.SongLogger;

import songArtifacts.lowLevel.RhythmHistogramType;
import songArtifacts.lowLevel.RhythmPatternType;
import songArtifacts.lowLevel.RowType;
import songArtifacts.lowLevel.SongType;
import songArtifacts.lowLevel.ObjectFactory;
import songArtifacts.lowLevel.SsdType;
import songArtifacts.lowLevel.StatisticalSpectrumDescriptorType;
import utils.DateConverter;

import customException.DateConverterException;
import customException.LogException;
import customException.SongFeatureException;
import entagged.audioformats.AudioFile;
import feature.MP3Info;
import feature.lowLevel.audio.LowLevelExtractor;
import feature.lowLevel.audio.data.FeatureExtractionOptions;


public final class LowLevelSongFeature {

	/**
	 * Extract Low Level Features on a given file.
	 * @param mp3 MP3Info to use for obtaining file information from the system
	 * @param file file to extract the features from
	 * @return true if successful, false otherwise
	 * @throws SongFeatureException
	 */
	public final  static Boolean start(MP3Info mp3, File file) 
			throws SongFeatureException {
		
		FeatureExtractionOptions opt= null;
		File output= null;
		LowLevelExtractor lle= null;
		
		Logger log;
		try {
			log= SongLogger.getInstance().getLog();
		} catch (LogException e) {
			throw new SongFeatureException("LogException "+e.getMessage(), e);
		}
		
		try {
			
			opt= new FeatureExtractionOptions();
	        opt.enableAll();
	        lle= new LowLevelExtractor(opt, file);
			
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
			
			// SSD
			StatisticalSpectrumDescriptorType ssd= obf.createStatisticalSpectrumDescriptorType();

			BigInteger rows= BigInteger.valueOf(lle.getDimensionSSD());
			
			SsdType mean= obf.createSsdType();
			mean.setSize(rows);
			mean.getValue().addAll(lle.getMeanList());
			ssd.setMean(mean);
			
			SsdType median= obf.createSsdType();
			median.setSize(rows);
			median.getValue().addAll(lle.getMedianList());
			ssd.setMedian(median);
			
			SsdType variance= obf.createSsdType();
			variance.setSize(rows);
			variance.getValue().addAll(lle.getVarianceList());
			ssd.setVariance(variance);
			
			SsdType skewness= obf.createSsdType();
			skewness.setSize(rows);
			skewness.getValue().addAll(lle.getSkewnessList());
			ssd.setSkewness(skewness);
			
			SsdType kurtosis= obf.createSsdType();
			kurtosis.setSize(rows);
			kurtosis.getValue().addAll(lle.getKurtosisList());
			ssd.setKurtosis(kurtosis);
			
			SsdType minValue= obf.createSsdType();
			minValue.setSize(rows);
			minValue.getValue().addAll(lle.getMinValueList());
			ssd.setMinValue(minValue);
			
			SsdType maxValue= obf.createSsdType();
			maxValue.setSize(rows);
			maxValue.getValue().addAll(lle.getMaxValueList());
			ssd.setMaxValue(maxValue);
			
			song.setStatisticalSpectrumDescriptor(ssd);
			
			// Rhythm Pattern
			RhythmPatternType rp= obf.createRhythmPatternType();
			int rpRows= lle.getRowsRP();
			BigInteger columns= BigInteger.valueOf(lle.getColumnsRP());
			
			rp.setRows(BigInteger.valueOf(rpRows));
			rp.setColumns(columns);
			String des= "The rhythm pattern describes modulation amplitude for a series of critical bands (rows) on various frequencies (columns)";
			rp.setDescription(des);
			
			for(int i= 0; i< rpRows; ++i) {
				RowType rt= obf.createRowType();
				rt.getColumn().addAll(lle.getRPRowList(i));
				rp.getRow().add(rt);
			}
			
			song.setRhythmPattern(rp);
			
			// Rhythm Histogram
			RhythmHistogramType rh= obf.createRhythmHistogramType();
			columns= BigInteger.valueOf(lle.getDimensionRH());
			rh.setSize(columns);
			des= "Rhythm histogram contains general descriptors for rhythmics for a number of modulation frequencies";
			rh.setDescription(des);
			
			rh.getValue().addAll(lle.getRHList());
			
			song.setRhythmHistogram(rh);
			
			//marshall this JaxbElement
			JAXBContext jc= JAXBContext.newInstance("songArtifacts.lowLevel");
			JAXBElement<SongType> je= obf.createSongMetadata(song);
			Marshaller m= jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			SchemaFactory sf= SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
			InputStream is= LowLevelSongFeature.class.getClassLoader().getResourceAsStream("MetadataSchema/songLowLevel.xsd");
			Schema schema= sf.newSchema(new StreamSource(is));
			m.setSchema(schema);
			m.setEventHandler(new ValidationEventHandler() {
				
				@Override
				public boolean handleEvent(ValidationEvent event) {
					return false;
				}
			});
			
			output= new File("SONG_LL_"+mp3.getAudioFile().getName()+".xml");
			m.marshal(je, output);
			
		} catch (JAXBException e) {
			if(output != null)
				output.delete();
			log.warning("Marshalling validation error for file: "+mp3.getAudioFile().getName());
			throw new SongFeatureException(e.getMessage(), e);
		} catch (DateConverterException e) {
			log.warning("Error converting date for file: "+mp3.getAudioFile().getName());
			throw new SongFeatureException(e.getMessage(), e);
		} catch(Exception e) {
			log.warning("Error "+e.getMessage()+" for file: "+mp3.getAudioFile().getName());
			throw new SongFeatureException(e.getMessage(), e);
		}
		
		return true;
	}
	
	
}
