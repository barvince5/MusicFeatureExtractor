package feature.lowLevel;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import customException.FeatureProcessorException;
import customException.DataModelException;
import feature.lowLevel.audioFeatures.*;
import feature.lowLevel.extractionTools.FeatureProcessor;
import feature.lowLevel.extractionTools.StringMethods;



/**
 * Main method for low level feature extraction on a single file
 *
 * @author Daniel McEnnis
 * @author modified by Vincenzo Barone
 */
public class DataModel {

	/**
	 * list of which features are enabled by default
	 */
	public boolean[] defaults;

	/**
	 * list of all features available
	 */
	public FeatureExtractor[] features;


	/**
	 * whether or a feature is a derived feature or not
	 */
	public boolean[] is_primary;

	/**
	 * cached FeatureDefinitions for all available features
	 */
	public FeatureDefinition[] featureDefinitions;

	/**
	 * info on all recordings that are made available for feature extraction
	 */
	private int windowSize; 
	private double windowOverlap;
	private double samplingRate; 
	private boolean normalize;

	private File recordingFile;
	private HashMap<String, String[]> extractedFeatures= null;
	
	/**
	 * Initializes each of the lists with all available features. 
	 *
	 * @throws DataModelException 
	 */
	public DataModel() throws DataModelException {

		this.extractedFeatures= new HashMap<String, String[]>();
		this.setDefaults();
		
		//TODO read from file
		
		LinkedList<MetaFeatureFactory> metaExtractors= this.setMetaExtractors();
		LinkedList<FeatureExtractor> extractors= this.setExtractors(); 
		this.populateMetaFeatures(metaExtractors, extractors);
	}

	/**
	 * Loads the five MetaExtractors into the list. 
	 *
	 * @return metaExtractor LinkedList with the MetaExtractors
	 */
	private LinkedList<MetaFeatureFactory> setMetaExtractors() {
		
		LinkedList<MetaFeatureFactory> metaExtractors= new LinkedList<MetaFeatureFactory>();
		
		metaExtractors.add(new Derivative());
		metaExtractors.add(new feature.lowLevel.audioFeatures.Mean());
		metaExtractors.add(new feature.lowLevel.audioFeatures.StandardDeviation());
		metaExtractors.add(new Derivative(new feature.lowLevel.audioFeatures.Mean()));
		metaExtractors.add(new Derivative(new feature.lowLevel.audioFeatures.StandardDeviation()));
		
		return metaExtractors;
	}

	/**
	 * Loads the selected FeatureExtractors into the list. 
	 * 
	 * @return extractors LinkedList with the FeatureExtractors
	 */
	private LinkedList<FeatureExtractor> setExtractors() {

		LinkedList<FeatureExtractor> extractors= new LinkedList<FeatureExtractor>();

		extractors.add(new feature.lowLevel.audioFeatures.AreaMoments());
		extractors.add(new BeatHistogram());
		extractors.add(new BeatHistogramLabels());
		extractors.add(new BeatSum());
		extractors.add(new Compactness());
		extractors.add(new FFTBinFrequencies());
		extractors.add(new FractionOfLowEnergyWindows());
		extractors.add(new HarmonicSpectralCentroid());
		extractors.add(new HarmonicSpectralFlux());
		extractors.add(new HarmonicSpectralSmoothness());
		extractors.add(new LPC());
		extractors.add(new MagnitudeSpectrum());
		extractors.add(new feature.lowLevel.audioFeatures.MFCC());
		extractors.add(new Moments());
		extractors.add(new PeakFinder());
		extractors.add(new PowerSpectrum());
		extractors.add(new RelativeDifferenceFunction());
		extractors.add(new RMS());
		extractors.add(new SpectralCentroid());
		extractors.add(new SpectralFlux());
		extractors.add(new SpectralRolloffPoint());
		extractors.add(new SpectralVariability());
		extractors.add(new StrengthOfStrongestBeat());
		extractors.add(new StrongestBeat());
		extractors.add(new StrongestFrequencyVariability());
		extractors.add(new StrongestFrequencyViaFFTMax());
		extractors.add(new StrongestFrequencyViaSpectralCentroid());
		extractors.add(new StrongestFrequencyViaZeroCrossings());
		extractors.add(new ZeroCrossings());
		 
		return extractors;
	}

	/**
	 * Uses the Feature and MetaFeature lists to fill the arrays that will be
	 * used by the FeatureProcessor. 
	 *
	 * @param listMFF list with the MetaFeatures
	 * @param listFE list with the Feature Extractors
	 */
	private void populateMetaFeatures(LinkedList<MetaFeatureFactory> listMFF,
			LinkedList<FeatureExtractor> listFE){
		
		LinkedList<Boolean> tmpDefaults = new LinkedList<Boolean>();
		LinkedList<FeatureExtractor> tmpFeatures = new LinkedList<FeatureExtractor>();
		LinkedList<Boolean> isPrimaryList = new LinkedList<Boolean>();
		
		Iterator<FeatureExtractor> lFE = listFE.iterator();
		while (lFE.hasNext()) {
			
			FeatureExtractor tmpF = lFE.next();
			tmpFeatures.add(tmpF);
			tmpDefaults.add(true);
			isPrimaryList.add(new Boolean(true));
			tmpF.setParent(this);
			
			if (tmpF.getFeatureDefinition().dimensions != 0) {
				
				Iterator<MetaFeatureFactory> lM = listMFF.iterator();
				while (lM.hasNext()) {
					
					MetaFeatureFactory tmpMFF = lM.next();
					FeatureExtractor tmp = tmpMFF.defineFeature((FeatureExtractor) tmpF.clone());
					tmp.setParent(this);
					tmpFeatures.add(tmp);
					tmpDefaults.add(new Boolean(false));
					isPrimaryList.add(new Boolean(false));
				}
			}
		}
		
		this.features = tmpFeatures.toArray(new FeatureExtractor[1]);
		Boolean[] defaults_temp = tmpDefaults.toArray(new Boolean[1]);
		Boolean[] is_primary_temp = isPrimaryList.toArray(new Boolean[] {});
		this.defaults = new boolean[defaults_temp.length];
		is_primary = new boolean[defaults_temp.length];
		
		for (int i = 0; i < this.defaults.length; i++) {
			this.defaults[i] = defaults_temp[i].booleanValue();
			is_primary[i] = is_primary_temp[i].booleanValue();
		}
		
		this.featureDefinitions = new FeatureDefinition[this.defaults.length];
		for (int i = 0; i < this.featureDefinitions.length; ++i) {
			this.featureDefinitions[i] = features[i].getFeatureDefinition();
		}
	}

	/**
	 * Function for executing the feature extraction process against a set of
	 * files.
	 * 
	 * @throws DataModelException
	 */
	public void extract()
			throws DataModelException {
		
		// Get the audio recordings to extract features from and throw an exception
		// if there are none
		if (this.recordingFile == null)
			throw new DataModelException("LowLevelExtractionException: No recordings available to extract features from.");

		try {
			
			// Prepare to extract features
			FeatureProcessor processor = new FeatureProcessor (this.windowSize, this.windowOverlap, 
					this.samplingRate, this.normalize, this.features, this.defaults);
			
			// Extract features from recordings one by one
			processor.extractFeatures(this.recordingFile);

			double[][] results = processor.getFeatures();
			FeatureDefinition[] definitions = processor.getFeatureDefinitions();
			
			this.buildFeatureMap(results, definitions);
			
		} catch (FeatureProcessorException e) {
			throw new DataModelException("LowLevelExtractionException "+e.getMessage(), e);
		}
	}

	/**
	 * Fills the Feature Map containing the values of the extracted features, using
	 * the Feature name as key. The values are saved as Scientific Notation-formatted Strings.
	 * 
	 * @param results matrix containing the extracted feature values
	 * @param definitions array with the Feature Definitions
	 * 
	 */	
	private void buildFeatureMap(double[][] results, FeatureDefinition[] definitions) {
		
		// the definitions contain information about the Features that were applied.
		// since the extraction process fills the results matrix in the same order
		// the features were defined, definitions[i] contains information about the array results[i].

		// significant digits for the scientific notation
		int maxDigits= 4;
		
		for (int i=0; i<results.length; ++i) {
			
			int totValues = results[i].length;
			String[] values= new String[totValues];
			
			for (int v=0; v<totValues; ++v)
				values[v] = StringMethods.getDoubleInScientificNotation(results[i][v], maxDigits);
			
			this.extractedFeatures.put(definitions[i].name, values);
		}
	}
	
	/**
	 * Sets the Extraction parameters to the default values. This is automatically performed 
	 * to avoid extraction attempts with incomplete information, to override one of the 
	 * parameters call the setter functions.
	 * 
	 */	
	private void setDefaults() {
		
		this.windowSize= 512; 
		this.windowOverlap= 0.0;
		this.samplingRate= 16000.0; 
		this.normalize= false;	
	
	}

	/**
	 * Explicitly sets the Extraction Window Size.
	 * 
	 * @param size window size, must be > 2 (default value is 512).
	 *             if is is incorrect, it will be ignored.
	 */	
	public void setWindowSize(int size) {
		
		if (size > 2)
			this.windowSize= size;	
	}

	/**
	 * Explicitly sets the Extraction Window Overlap.
	 * 
	 * @param overlap window overlap, must be >= 0.0 and < 1.0 (default value is 0.0).
	 * 			      if is is incorrect, it will be ignored.
	 */	
	public void setWindowOverlap(float overlap) throws DataModelException {
		
		if (overlap >= 0.0 || overlap < 1.0)
			this.windowOverlap= overlap;	
	}	

	/**
	 * Explicitly sets the Extraction Sampling Rate.
	 * 
	 * @param rate sampling rate (default value is 16000.0)
	 */	
	public void setSamplingRate(float rate) {
		
		this.samplingRate= rate;	
	}

	/**
	 * Explicitly sets the Extraction Normalization.
	 * 
	 * @param norm normalization (default value is false)
	 */	
	public void setNormalize(boolean norm) {
		
		this.normalize= norm;	
	}
	
	/**
	 * Sets the File to extracts features from. 
	 * 
	 * @param fileName the path of the audio file to analyze
	 * @throws DataModelException 
	 */	
	public void setFile(String fileName) throws DataModelException {
		try {
			this.recordingFile= new File(fileName);
		} catch (NullPointerException e) {
			throw new DataModelException("NullPointerException "+e.getMessage(), e);
		}
	}

	/**
	 * Sets the File to extracts features from. 
	 * 
	 * @param file the audio file to analyze
	 */	
	public void setFile(File file) {

		this.recordingFile= file;
	}	
	
	// TODO for testing, to delete
	public void foo() throws DataModelException {
		try {
		FileWriter fw= new FileWriter(new File("out.txt"));
		for (String k: this.extractedFeatures.keySet()) {
			fw.write(k+"\n");
			String[] curr= this.extractedFeatures.get(k);
			for (String c: curr)
				fw.write(c+"\n");
			fw.write("-----------------------\n");
		}
		fw.close();
		} catch (IOException e) {
			throw new DataModelException();
		}
		

	}
}
