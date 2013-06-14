/*
 * @(#)BeatHistogram.java	1.0	April 5, 2005.
 *
 * McGill Univarsity
 */

package feature.lowLevel.audioFeatures;

import customException.FeatureExtractorException;
import feature.lowLevel.extractionTools.DSPMethods;

/**
 * A feature extractor that extracts the Beat Histogram from a signal. This is
 * histogram showing the strength of different rhythmic periodicities in a
 * signal.
 * <p>
 * This is calculated by taking the RMS of 256 windows and then taking
 * the FFT of the result.
 * <p>
 * No extracted feature values are stored in objects of this class.
 * <p>
 * <b>IMPORTANT:
 * </P>
 * The window size of 256 RMS windows used here is hard-coded into the class
 * BeatHistogramLabels. Any changes to the window size in this class must be
 * made there as well.</b>
 * <p>
 * Daniel McEnnis 05-07-05 Added setElement, getElement, setElement, and clone
 * functions
 *
 * @author Cory McKay
 * @author modified by Vincenzo Barone
 */
public class BeatHistogram extends FeatureExtractor {

	private int number_windows = 256;

	/* CONSTRUCTOR ************************************************************* */

	/**
	 * Basic constructor that sets the definition and dependencies (and their
	 * offsets) of this feature.
	 */
	public BeatHistogram() {
		String name = "Beat Histogram";
		String description = "A histogram showing the relative strength of different "
				+ "rhythmic periodicities (tempi) in a signal. Found by "
				+ "calculating the auto-correlation of the RMS.";
		boolean is_sequential = true;
		int dimensions = 0;
		super.definition = new FeatureDefinition(name, description, is_sequential,
				dimensions);

		// int number_windows = 256;

		super.dependencies = new String[this.number_windows];
		for (int i = 0; i < this.dependencies.length; i++)
			super.dependencies[i] = "Root Mean Square";

		super.offsets = new int[this.number_windows];
		for (int i = 0; i < this.offsets.length; i++)
			super.offsets[i] = 0 - i;
	}

	/* PUBLIC METHODS ********************************************************* */

	/**
	 * Extracts this feature from the given samples at the given sampling rate
	 * and given the other feature values.
	 * <p>
	 * In the case of this feature the sampling_rate is ignored.
	 *
	 * @param samples
	 *            The samples to extract the feature from.
	 * @param sampling_rate
	 *            The sampling rate that the samples are encoded with.
	 * @param other_feature_values
	 *            The values of other features that are needed to calculate this
	 *            value. The order and offsets of these features must be the
	 *            same as those returned by this class's getDependencies and
	 *            getDependencyOffsets methods respectively. The first index
	 *            indicates the feature/window and the second indicates the
	 *            value.
	 * @return The extracted feature value(s).
	 * @throws FeatureExtractorException
	 *             Throws an informative exception if the feature cannot be
	 *             calculated.
	 */
	public double[] extractFeature(double[] samples, double sampling_rate,
			double[][] other_feature_values) throws FeatureExtractorException {
		double[] rms = new double[other_feature_values.length];
		for (int i = 0; i < rms.length; i++)
			rms[i] = other_feature_values[i][0];

		double effective_sampling_rate = sampling_rate / ((double) rms.length);

		int min_lag = (int) (0.286 * effective_sampling_rate);
		int max_lag = (int) (3.0 * effective_sampling_rate);
		double[] auto_correlation = DSPMethods
				.getAutoCorrelation(rms, min_lag, max_lag);
		return auto_correlation;
	}

	/**
	 * Helper function to set window length for this feature. Note that this
	 * feature does *not* conform to the syntax of setWindow so this feature is
	 * not affected by a global window change. This is necessary since the beat
	 * bins have a different meaning than most windowed features.
	 *
	 * @param n
	 *            new number of beat bins
	 * @throws FeatureExtractorException
	 *             thrown if the new value is less than 2
	 */
	public void setWindowLength(int n) throws FeatureExtractorException {
		if (n < 2) {
			throw new FeatureExtractorException(
					"BeatHistogram window length must be greater than 1");
		} else {
			this.number_windows = n;
			super.dependencies = new String[this.number_windows];
			super.offsets = new int[this.number_windows];
			for (int i = 0; i < number_windows; ++i) {
				super.dependencies[i] = "Root Mean Square";
				super.offsets[i] = 0 - i;
			}
		}
	}

	/**
	 * Function permitting an unintelligent outside function (ie. EditFeatures
	 * frame) to get the default values used to populate the table's entries.
	 * The correct index values are inferred from definition.attribute value.
	 *
	 * @param index
	 *            which of Beat Histograms's attributes should be edited.
	 * @throws FeatureExtractorException
	 */
	public String getElement(int index) throws FeatureExtractorException {
		if (index != 0) {
			throw new FeatureExtractorException("INTERNAL ERROR: invalid index " + index
					+ " sent to AreaMoments:getElement");
		} else {
			return Integer.toString(this.number_windows);
		}
	}

	/**
	 * Function permitting an unintelligent outside function (i.e. EditFeatures
	 * frame) to set the default values used to populate the table's entries.
	 * Like getElement, the correct index values are inferred from the
	 * definition.attributes value.
	 *
	 * @param index
	 *            attribute to be set
	 * @param value
	 *            new value of the attribute
	 * @throws FeatureExtractorException
	 */
	public void setElement(int index, String value) throws FeatureExtractorException {
		if (index != 0) {
			throw new FeatureExtractorException("INTERNAL ERROR: invalid index " + index
					+ " sent to AreaMoments:setElement");
		} else {
			try {
				int type = Integer.parseInt(value);
				setWindowLength(type);
			} catch (NumberFormatException e) {
				throw new FeatureExtractorException("NumberFormatException " +
						"Length of Beat Histogram must be an integer", e);
			} catch (Exception e) {
				throw new FeatureExtractorException("Exception " +
						"Length of Beat Histogram must be an integer", e);
			}
		}
	}

	/**
	 * Create an identical copy of this feature. This permits FeatureExtractor
	 * to use the prototype pattern to create new composite features using
	 * metafeatures.
	 */
	public Object clone() {
		BeatHistogram ret = new BeatHistogram();
		ret.number_windows = this.number_windows;
		return ret;
	}

}