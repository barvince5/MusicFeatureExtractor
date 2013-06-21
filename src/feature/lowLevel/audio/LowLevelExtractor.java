package feature.lowLevel.audio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import customException.AudioFileExtractorException;

import feature.lowLevel.audio.data.FeatureExtractionOptions;
import feature.lowLevel.audio.data.RealMatrixExt;

/**
 * This class is a wrapper for the Low Level features extracted with AFE.
 * It allows to directly get the needed values without having to deal with
 * matrices inside the main code.
 * @author Vincenzo Barone
 *
 */

public class LowLevelExtractor {

	private RealMatrixExt[] rm= null;
	
	/**
	 * Constructor for LowLevelExtractor, initializes the results matrix and fills it
	 * with the features specified in the options passed as argument.
	 * @param opt feature extraction options. To extract all features with default params, use opt's enableAll() method before calling this constructor.
	 * @param file the audio file to extract features from 
	 * @throws AudioFileExtractorException
	 * @see {@link FeatureExtractionOptions}
	 */
	public LowLevelExtractor (FeatureExtractionOptions opt, File file) 
			throws AudioFileExtractorException {
        AudioFileExtractor afe= new AudioFileExtractor();
        this.rm = afe.extractAudioFile(file, opt);
	}
	
	// RHYTHM HISTOGRAM METHODS
	
	/**
	 * Get the Rhythm Histogram (column) dimension as an Integer.
	 * @return column dimension
	 */
	public Integer getDimensionRH() {
		return this.rm[2].getColumnDimension();
	}
	
	/**
	 * Get the Rhythm Histogram as an array of doubles.
	 * @return rhythm histogram array
	 */
	public double[] getRHArray() {
		return this.rm[2].getRow(0);
	}
	

	/**
	 * Get the Rhythm Histogram as a list of Doubles.
	 * @return rhythm histogram list
	 */
	public List<Double> getRHList() {
		List<Double> result= new ArrayList<Double>();
		double[] values= this.getRHArray();
		for (int i= 0; i< values.length; ++i) {
			result.add(Double.valueOf(values[i]));
		}
		return result;
	}
	
	// RHYTHM PATTERN METHODS
	
	/**
	 * Get the Rhythm Pattern row dimension as an Integer.
	 * @return row dimension
	 */
	public Integer getRowsRP() {
		return this.rm[1].getRowDimension();
	}
	
	/**
	 * Get the Rhythm Pattern column dimension as an Integer.
	 * @return column dimension
	 */
	public Integer getColumnsRP() {
		return this.rm[1].getColumnDimension();
	}
	
	
	/**
	 * Get a row from the Rhythm Pattern given its index, as an array of doubles. 
	 * @param index row index
	 * @return rhythm pattern row
	 */
	public double[] getRPRowArray(int index) {
		return this.rm[1].getRow(index);
	}

	/**
	 * Get a row from the Rhythm Pattern given its index, as a list of Doubles. 
	 * @param index row index
	 * @return rhythm pattern row
	 */
	public List<Double> getRPRowList(int index) {
		List<Double> result= new ArrayList<Double>();
		double[] values= this.getRPRowArray(index);
		for (int i= 0; i< values.length; ++i) {
			result.add(Double.valueOf(values[i]));
		}
		return result;
	}
	
	// SPECTRUM STATISTICAL DESCRIPTORS METHODS
	
	/**
	 * Get the Spectrum Statistical Descriptors dimension as an Integer.
	 * @return dimension
	 */
	public Integer getDimensionSSD() {
		return this.rm[0].getRowDimension();
	}
	
	// MEAN
	
	/**
	 * Gets the Mean as an array of doubles.
	 * @return mean array
	 */
	public double[] getMeanArray() {
		return this.rm[0].getColumn(0);
	}
	
	/**
	 * Gets the Mean as a list of Doubles.
	 * @return mean list
	 */
	public List<Double> getMeanList() {
		List<Double> result= new ArrayList<Double>();
		double[] values= this.getMeanArray();
		for (int i= 0; i< values.length; ++i) {
			result.add(Double.valueOf(values[i]));
		}
		return result;
	}

	// MEDIAN
	
	/**
	 * Gets the Median as an array of doubles.
	 * @return median array
	 */
	public double[] getMedianArray() {
		return this.rm[0].getColumn(1);
	}

	/**
	 * Gets the Median as a list of Doubles.
	 * @return median list
	 */
	
	public List<Double> getMedianList() {
		List<Double> result= new ArrayList<Double>();
		double[] values= this.getMedianArray();
		for (int i= 0; i< values.length; ++i) {
			result.add(Double.valueOf(values[i]));
		}
		return result;
	}
	
	/**
	 * Gets the Variance as an array of doubles.
	 * @return variance array
	 */
	public double[] getVarianceArray() {
		return this.rm[0].getColumn(2);
	}
	
	/**
	 * Gets the Variance as a list of Doubles.
	 * @return variance list
	 */
	public List<Double> getVarianceList() {
		List<Double> result= new ArrayList<Double>();
		double[] values= this.getVarianceArray();
		for (int i= 0; i< values.length; ++i) {
			result.add(Double.valueOf(values[i]));
		}
		return result;
	}

	/**
	 * Gets the Skewness as an array of doubles.
	 * @return skewness array
	 */
	public double[] getSkewnessArray() {
		return this.rm[0].getColumn(3);
	}
	
	/**
	 * Gets the Skewness as a list of Doubles.
	 * @return skewness list
	 */
	public List<Double> getSkewnessList() {
		List<Double> result= new ArrayList<Double>();
		double[] values= this.getSkewnessArray();
		for (int i= 0; i< values.length; ++i) {
			result.add(Double.valueOf(values[i]));
		}
		return result;
	}
	
	/**
	 * Gets the Kurtosis as an array of doubles.
	 * @return kurtosis array
	 */
	public double[] getKurtosisArray() {
		return this.rm[0].getColumn(4);
	}

	/**
	 * Gets the Kurtosis as a list of Doubles.
	 * @return kurtosis list
	 */
	public List<Double> getKurtosisList() {
		List<Double> result= new ArrayList<Double>();
		double[] values= this.getKurtosisArray();
		for (int i= 0; i< values.length; ++i) {
			result.add(Double.valueOf(values[i]));
		}
		return result;
	}
	
	/**
	 * Gets the MinValue as an array of doubles.
	 * @return minvalue array
	 */
	public double[] getMinValueArray() {
		return this.rm[0].getColumn(5);
	}

	/**
	 * Gets the MinValue as a list of Doubles.
	 * @return minvalue list
	 */
	public List<Double> getMinValueList() {
		List<Double> result= new ArrayList<Double>();
		double[] values= this.getMinValueArray();
		for (int i= 0; i< values.length; ++i) {
			result.add(Double.valueOf(values[i]));
		}
		return result;
	}
	
	/**
	 * Gets the MaxValue as an array of doubles.
	 * @return maxvalue array
	 */
	public double[] getMaxValueArray() {
		return this.rm[0].getColumn(6);
	}
	
	/**
	 * Gets the MaxValue as a list of Doubles.
	 * @return maxvalue array
	 */
	public List<Double> getMaxValueList() {
		List<Double> result= new ArrayList<Double>();
		double[] values= this.getMaxValueArray();
		for (int i= 0; i< values.length; ++i) {
			result.add(Double.valueOf(values[i]));
		}
		return result;
	}
}
