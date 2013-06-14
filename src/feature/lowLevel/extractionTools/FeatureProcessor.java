/*
 * @(#)FeatureProcessor.java	1.01	April 9, 2005.
 *
 * McGill Univarsity
 */

package feature.lowLevel.extractionTools;


import javax.sound.sampled.*;

import customException.FeatureExtractorException;
import customException.FeatureProcessorException;
import feature.lowLevel.audioFeatures.*;

import java.io.*;
import java.util.LinkedList;

/**
 * This class is used to pre-process and extract features from audio recordings.
 * An object of this class should be instantiated with parameters indicating the
 * details of how features are to be extracted.
 * <p>
 * The extractFeatures method should be called whenever recordings are available
 * to be analyzed. This mehtod should be called once for each recording. It will
 * write the extracted feature values to an XML file after each call. This will
 * also save feature definitions to another XML file.
 * <p>
 * The finalize method should be called when all features have been extracted.
 * this will finish writing the feature values to the XML file.
 * <p>
 * Features are extracted for each window and, when appropriate, the average and
 * standard deviation of each of these features is extracted for each recording.
 *
 * @author Cory McKay
 * @author modified by Vincenzo Barone
 */
public class FeatureProcessor {
	
	// The window size used for dividing up the recordings to classify.
	private int window_size;

	// The number of samples that windows are offset by. A value of zero
	// means that there is no window overlap.
	private int window_overlap_offset;

	// The sampling rate that all recordings are to be converted to before
	// feature extraction.
	private double sampling_rate;

	// Whether or not to normalize recordings before feature extraction.
	private boolean normalize;
	
	double[][] overall_feature_values = null;
	
	private FeatureDefinition[][] overall_feature_definitions;

	// The features that are to be extracted.
	private FeatureExtractor[] feature_extractors;

	// The dependencies of the features in the feature_extractors field.
	// The first indice corresponds to the feature_extractors indice
	// and the second identifies the number of the dependent feature.
	// The entry identifies the indice of the feature in feature_extractors
	// that corresponds to a dependant feature. The first dimension will be
	// null if there are no dependent features.
	private int[][] feature_extractor_dependencies;

	// The longest number of windows of previous features that each feature must
	// have before it can be extracted. The indice corresponds to that of
	// feature_extractors.
	private int[] max_feature_offsets;

	// Which features are to be saved after processing. Entries correspond to
	// the
	// feature_extractors field.
	private boolean[] features_to_save;


	/**
	 * Validates and stores the configuration to use for extracting features
	 * from audio recordings. Prepares the feature_vector_file and
	 * feature_key_file XML files for saving.
	 *
	 * @param window_size
	 *            The size of the windows that the audio recordings are to be
	 *            broken into.
	 * @param window_overlap
	 *            The fraction of overlap between adjacent windows. Must be
	 *            between 0.0 and less than 1.0, with a value of 0.0 meaning no
	 *            overlap.
	 * @param sampling_rate
	 *            The sampling rate that all recordings are to be converted to
	 *            before feature extraction
	 * @param normalize
	 *            Whether or not to normalize recordings before feature
	 *            extraction.
	 * @param all_feature_extractors
	 *            All features that can be extracted.
	 * @param features_to_save_among_all
	 *            Which features are to be saved. Entries correspond to the
	 *            all_feature_extractors parameter.
	 * @throws FeatureProcessorException Throws an informative exception if the input parameters are
	 *             invalid.
	 */
	public FeatureProcessor(int window_size, double window_overlap,	double sampling_rate, boolean normalize,
			FeatureExtractor[] all_feature_extractors,	boolean[] features_to_save_among_all) 
					throws FeatureProcessorException {
		
		if (window_overlap < 0.0 || window_overlap >= 1.0)
			throw new FeatureProcessorException("Window overlap fraction is " + window_overlap
					+ ".\n"
					+ "This value must be 0.0 or above and less than 1.0.");
		if (window_size < 3)
			throw new FeatureProcessorException("Window size is " + window_size + ".\n"
					+ "This value must be above 2.");
		boolean one_selected = false;
		for (int i = 0; i < features_to_save_among_all.length; i++)
			if (features_to_save_among_all[i])
				one_selected = true;
		if (!one_selected)
			throw new FeatureProcessorException("No features have been set to be saved.");


		// Save parameters as fields
		this.window_size = window_size;
		this.sampling_rate = sampling_rate;
		this.normalize = normalize;
		
		// Calculate the window offset
		this.window_overlap_offset = (int) (window_overlap * (double) window_size);

		// Find which features need to be extracted and in what order. Also find
		// the indices of dependencies and the maximum offsets for each feature.
		this.findAndOrderFeaturesToExtract(all_feature_extractors,
				features_to_save_among_all);
	}

	/**
	 * Extract the features from the provided audio file. This includes
	 * pre-processing involving sample rate conversion, windowing and, possibly,
	 * normalization. The feature values are automatically saved to the
	 * feature_vector_file XML file referred to by the values_writer field. The
	 * definitions of the features that are saved are also saved to the
	 * feature_key_file XML file referred to by the definitions_writer field.
	 *
	 * @param recording_file
	 *            The audio file to extract features from.
	 * @throws FeatureProcessorException
	 */
	public void extractFeatures(File recording_file) 
			throws FeatureProcessorException {
		// Pre-process the recording and extract the samples from the audio
		double[] samples = preProcessRecording(recording_file);
		
		// Calculate the window start indices
		LinkedList<Integer> window_start_indices_list = new LinkedList<Integer>();
		int this_start = 0;
		while (this_start < samples.length) {
			window_start_indices_list.add(new Integer(this_start));
			this_start += this.window_size - this.window_overlap_offset;
		}
		Integer[] window_start_indices_I = window_start_indices_list.toArray(new Integer[1]);
		int[] window_start_indices = new int[window_start_indices_I.length];

		
		for (int i = 0; i < window_start_indices.length; i++)
			window_start_indices[i] = window_start_indices_I[i].intValue();

		// Extract the feature values from the samples
		double[][][] window_feature_values = getFeatures(samples, window_start_indices);
		
		// Find the feature averages and standard deviations if appropriate
		this.overall_feature_definitions = new FeatureDefinition[1][];
		this.overall_feature_definitions[0] = null;
		
		this.overall_feature_values = this.getOverallRecordingFeatures(window_feature_values);
	}


	/**
	 * Fills the feature_extractors, feature_extractor_dependencies,
	 * max_feature_offsets and features_to_save fields. This involves finding
	 * which features need to be extracted and in what order and finding the
	 * indices of dependencies and the maximum offsets for each feature.
	 * <p>
	 * Daniel McEnnis 05-07-05 added feature offset of dependancies to
	 * max_offset
	 *
	 * @param all_feature_extractors
	 *            All features that can be extracted.
	 * @param features_to_save_among_all
	 *            Which features are to be saved. Entries correspond to the
	 *            all_feature_extractors parameter.
	 */
	private void findAndOrderFeaturesToExtract(FeatureExtractor[] all_feature_extractors, boolean[] features_to_save_among_all) {
		
		// Find the names of all features
		String[] all_feature_names = new String[all_feature_extractors.length];
		for (int feat = 0; feat < all_feature_extractors.length; feat++)
			all_feature_names[feat] = all_feature_extractors[feat].getFeatureDefinition().getName();

		// Find dependencies of all features marked to be extracted.
		// Mark as null if features are not to be extracted. Note that will also
		// be null if there are no dependencies.
		String[][] dependencies = new String[all_feature_extractors.length][];
		for (int feat = 0; feat < all_feature_extractors.length; feat++) {
			if (features_to_save_among_all[feat])
				dependencies[feat] = all_feature_extractors[feat].getDepenedencies();
			else
				dependencies[feat] = null;
		}

		// Add dependencies to dependencies and if any features are not marked
		// for saving but are marked as a dependency of a feature that is marked to
		// be saved. Also fill features_to_extract in order to know what features
		// to extract(but not necessarily save).
		boolean done = false;
		boolean[] features_to_extract = new boolean[dependencies.length];
		for (int feat = 0; feat < features_to_extract.length; feat++) {
			
			if (features_to_save_among_all[feat])
				features_to_extract[feat] = true;
			else
				features_to_extract[feat] = false;
		}
		
		while (!done) {
			
			done = true;
			for (int feat = 0; feat < dependencies.length; feat++)
				if (dependencies[feat] != null)
					for (int i = 0; i < dependencies[feat].length; i++) {
						String name = dependencies[feat][i];
						for (int j = 0; j < all_feature_names.length; j++) {
							if (name.equals(all_feature_names[j])) {
								if (!features_to_extract[j]) {
									features_to_extract[j] = true;
									dependencies[j] = all_feature_extractors[j].getDepenedencies();
									if (dependencies[j] != null)
										done = false;
								}
								j = all_feature_names.length;
							}
						}
					}
		}

		// Find the correct order to extract features in by filling the
		// feature_extractors field
		int number_features_to_extract = 0;
		for (int i = 0; i < features_to_extract.length; i++)
			if (features_to_extract[i])
				number_features_to_extract++;
		this.feature_extractors = new FeatureExtractor[number_features_to_extract];
		this.features_to_save = new boolean[number_features_to_extract];
		for (int i = 0; i < this.features_to_save.length; i++)
			this.features_to_save[i] = false;
		boolean[] feature_added = new boolean[dependencies.length];
		for (int i = 0; i < feature_added.length; i++)
			feature_added[i] = false;
		int current_position = 0;
		done = false;
		while (!done) {
			done = true;

			// Add all features that have no remaining dependencies and remove
			// their dependencies from all unadded features
			for (int feat = 0; feat < dependencies.length; feat++) {
				if (features_to_extract[feat] && !feature_added[feat])
					if (dependencies[feat] == null) // add feature if it has no
					// dependencies
					{
						feature_added[feat] = true;
						this.feature_extractors[current_position] = all_feature_extractors[feat];
						this.features_to_save[current_position] = features_to_save_among_all[feat];
						current_position++;
						done = false;

						// Remove this dependency from all features that have
						// it as a dependency and are marked to be extracted
						for (int i = 0; i < dependencies.length; i++)
							if (features_to_extract[i]
									&& dependencies[i] != null) {
								int num_defs = dependencies[i].length;
								for (int j = 0; j < num_defs; j++) {
									if (dependencies[i][j]
											.equals(all_feature_names[feat])) {
										if (dependencies[i].length == 1) {
											dependencies[i] = null;
											j = num_defs;
										} else {
											String[] temp = new String[dependencies[i].length - 1];
											int m = 0;
											for (int k = 0; k < dependencies[i].length; k++) {
												if (k != j) {
													temp[m] = dependencies[i][k];
													m++;
												}
											}
											dependencies[i] = temp;
											j--;
											num_defs--;
										}
									}
								}
							}
					}
			}
		}

		// Find the indices of the feature extractor dependencies for each
		// feature extractor
		this.feature_extractor_dependencies = new int[this.feature_extractors.length][];
		String[] feature_names = new String[this.feature_extractors.length];
		for (int feat = 0; feat < feature_names.length; feat++) {
			feature_names[feat] = this.feature_extractors[feat].getFeatureDefinition().getName();
		}
		
		String[][] feature_dependencies_str = new String[this.feature_extractors.length][];
		for (int feat = 0; feat < feature_dependencies_str.length; feat++)
			feature_dependencies_str[feat] = this.feature_extractors[feat].getDepenedencies();
		for (int i = 0; i < feature_dependencies_str.length; i++)
			if (feature_dependencies_str[i] != null) {
				this.feature_extractor_dependencies[i] = new int[feature_dependencies_str[i].length];
				for (int j = 0; j < feature_dependencies_str[i].length; j++)
					for (int k = 0; k < feature_names.length; k++)
						if (feature_dependencies_str[i][j].equals(feature_names[k]))
							this.feature_extractor_dependencies[i][j] = k;
			}

		// Find the maximum offset for each feature
		// Daniel McEnnis 5-07-05 added feature offset of dependancies to max_offset
		this.max_feature_offsets = new int[this.feature_extractors.length];
		for (int i = 0; i < this.max_feature_offsets.length; i++) {
			if (this.feature_extractors[i].getDepenedencyOffsets() == null)
				this.max_feature_offsets[i] = 0;
			else {
				int[] these_offsets = this.feature_extractors[i].getDepenedencyOffsets();
				this.max_feature_offsets[i] = Math.abs(these_offsets[0]	+ this.max_feature_offsets[this.feature_extractor_dependencies[i][0]]);
				for (int k = 0; k < these_offsets.length; k++) {
					int val = Math.abs(these_offsets[k]) + this.max_feature_offsets[this.feature_extractor_dependencies[i][k]];
					if (val > this.max_feature_offsets[i]) {
						this.max_feature_offsets[i] = val;
					}
				}
			}
		}
	}

	/**
	 * Returns the samples stored in the given audio file.
	 * <p>
	 * The samples are re-encoded using the sampling rate in the sampling_rate
	 * field. All channels are projected into one channel. Samples are
	 * normalized if the normalize field is true.
	 *
	 * @param recording_file
	 *            The audio file to extract samples from.
	 * @return The processed audio samples. Values will fall between a minimum
	 *         of -1 and +1. The indice identifies the sample number.
	 * @throws FeatureProcessorException
	 *             An exception is thrown if a problem occurs during file
	 *             reading or pre- processing.
	 */
	private double[] preProcessRecording(File recording_file) 
			throws FeatureProcessorException {
		
		AudioSamples audio_data;
		
		try {

			// Get the original audio and its format
			AudioInputStream original_stream = AudioSystem.getAudioInputStream(recording_file);
			AudioFormat original_format = original_stream.getFormat();
	
			// Set the bit depth
			int bit_depth = original_format.getSampleSizeInBits();
			if (bit_depth != 8 && bit_depth != 16)
				bit_depth = 16;
	
			// If the audio is not PCM signed big endian, then convert it to PCM signed
			// This is particularly necessary when dealing with MP3s
			AudioInputStream second_stream = original_stream;
			if (original_format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED || original_format.isBigEndian() == false) {
				AudioFormat new_format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, original_format.getSampleRate(), bit_depth, 
						original_format.getChannels(), original_format.getChannels() * (bit_depth / 8), original_format.getSampleRate(), true);
				second_stream = AudioSystem.getAudioInputStream(new_format, original_stream);
			}
	
			// Convert to the set sampling rate, if it is not already at this sampling rate.
			// Also, convert to an appropriate bit depth if necessary.
			AudioInputStream new_stream = second_stream;
			if (original_format.getSampleRate() != (float) sampling_rate || bit_depth != original_format.getSampleSizeInBits()) {
				AudioFormat new_format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, (float) sampling_rate, bit_depth, 
						original_format.getChannels(), original_format.getChannels() * (bit_depth / 8), original_format.getSampleRate(), true);
				new_stream = AudioSystem.getAudioInputStream(new_format, second_stream);
			}
	
			// Extract data from the AudioInputStream
			audio_data = new AudioSamples(new_stream, recording_file.getPath(), false);
	
			// normalize samples if this option has been requested
			if (this.normalize)
				audio_data.normalizeMixedDownSamples();
		
		} catch(IOException e) {
			throw new FeatureProcessorException("IOException "+e.getMessage(), e);	
		} catch (UnsupportedAudioFileException e) {
			throw new FeatureProcessorException("UnsupportedAudioFileException "+e.getMessage(), e);	
		} catch (Exception e) {
			throw new FeatureProcessorException("Exception "+e.getMessage(), e);	
		} 

		// Return all channels compressed into one
		return audio_data.getSamplesMixedDown();	
	}

	/**
	 * Breaks the given samples into the appropriate windows and extracts
	 * features from each window.
	 *
	 * @param samples
	 *            The samples to extract features from. Sample values should
	 *            generally be between -1 and +1.
	 * @param window_start_indices
	 *            The indices of samples that correspond to where each window
	 *            should start.
	 * @return The extracted feature values for this recording. The first indice
	 *         identifies the window, the second identifies the feature and the
	 *         third identifies the feature value. The third dimension will be
	 *         null if the given feature could not be extracted for the given
	 *         window.
	 * @throws FeatureProcessorException 
	 *             Throws an exception if a problem occurs.
	 */
	private double[][][] getFeatures(double[] samples,	int[] window_start_indices) 
			throws FeatureProcessorException {
		
		// The extracted feature values for this recording. The first indice
		// identifies the window, the second identifies the feature and the
		// third identifies the feature value.
		double[][][] results = new double[window_start_indices.length][this.feature_extractors.length][];

		// Extract features from each window one by one and add save the
		// results.
		// The last window is zero-padded at the end if it falls off the edge of
		// the
		// provided samples.
		for (int win = 0; win < window_start_indices.length; win++) {
	
			// Find the samples in this window and zero-pad if necessary
			double[] window = new double[window_size];
			int start_sample = window_start_indices[win];
			int end_sample = start_sample + this.window_size - 1;
			if (end_sample < samples.length)
				for (int samp = start_sample; samp <= end_sample; samp++)
					window[samp - start_sample] = samples[samp];
			else
				for (int samp = start_sample; samp <= end_sample; samp++) {
					if (samp < samples.length)
						window[samp - start_sample] = samples[samp];
					else
						window[samp - start_sample] = 0.0;
				}

			// Extract the features one by one
			for (int feat = 0; feat < this.feature_extractors.length; feat++) {
				// Only extract this feature if enough previous information
				// is available to extract this feature
				if (win >= this.max_feature_offsets[feat]) {
					// Find the correct feature
					FeatureExtractor feature = this.feature_extractors[feat];

					// Find previously extracted feature values that this
					// feature
					// needs
					double[][] other_feature_values = null;
					if (feature_extractor_dependencies[feat] != null) {
						other_feature_values = new double[feature_extractor_dependencies[feat].length][];
						for (int i = 0; i < feature_extractor_dependencies[feat].length; i++) {
							int feature_indice = feature_extractor_dependencies[feat][i];
							int offset = feature.getDepenedencyOffsets()[i];
							other_feature_values[i] = results[win + offset][feature_indice];
						}
					}

					// Store the extracted feature values
					try {
						results[win][feat] = feature.extractFeature(window,	sampling_rate, other_feature_values);
					} catch (FeatureExtractorException e) {
						throw new FeatureProcessorException("FeatureExtractorException "+e.getMessage(), e);
					}
				} else
					results[win][feat] = null;
			}
		}

		// Return the results
		return results;
	}
	
	public double[][] getFeatures() {
		return this.overall_feature_values;
	}
	
	public FeatureDefinition[] getFeatureDefinitions(){
		return this.overall_feature_definitions[0];
	}

	/**
	 * Calculates the averages and standard deviations over a whole recording of
	 * each of the windows-based features. Generates a feature definition for
	 * each such feature.
	 *
	 * @param window_feature_values
	 *            The extracted window feature values for this recording. The
	 *            first indice identifies the window, the second identifies the
	 *            feature and the third identifies the feature value. The third
	 *            dimension will be null if the given feature could not be
	 *            extracted for the given window.
	 * @param overall_feature_definitions
	 *            The feature definitions of the features that are returned by
	 *            this method. This array will be filled by this method, and
	 *            should be an empty FeatureDefintion[1][] when it is passed to
	 *            this method. The first indice will be filled by this method
	 *            with a single array of FeatureDefinitions, which have the same
	 *            order as the returned feature values.
	 * @return The extracted overall average and standard deviations of the
	 *         window feature values that were passed to this method. The first
	 *         indice identifies the feature and the second iddentifies the
	 *         feature value. The order of the features correspond to the
	 *         FeatureDefinitions that the overall_feature_definitions parameter
	 *         is filled with.
	 */
	private double[][] getOverallRecordingFeatures(
			double[][][] window_feature_values) {
		LinkedList<double[]> values = new LinkedList<double[]>();
		LinkedList<FeatureDefinition> definitions = new LinkedList<FeatureDefinition>();

		for (int feat = 0; feat < feature_extractors.length; feat++)
			if (window_feature_values[window_feature_values.length - 1][feat] != null
					&& features_to_save[feat]) {
				// Make the definitions
				FeatureDefinition this_def = feature_extractors[feat]
						.getFeatureDefinition();
				FeatureDefinition average_definition = new FeatureDefinition(
						this_def.getName() + " Overall Average",
						this_def.getDescription()
								+ "\nThis is the overall average over all windows.",
						this_def.isSequential(),
						window_feature_values[window_feature_values.length - 1][feat].length);
				FeatureDefinition stdv_definition = new FeatureDefinition(
						this_def.getName() + " Overall Standard Deviation",
						this_def.getDescription()
								+ "\nThis is the overall standard deviation over all windows.",
						this_def.isSequential(),
						window_feature_values[window_feature_values.length - 1][feat].length);

				// Find the averages and standard deviations
				double[] averages = new double[window_feature_values[window_feature_values.length - 1][feat].length];
				double[] stdvs = new double[window_feature_values[window_feature_values.length - 1][feat].length];
				for (int val = 0; val < window_feature_values[window_feature_values.length - 1][feat].length; val++) {
					// Find the number of windows that have values for this
					// value feature
					int count = 0;
					for (int win = 0; win < window_feature_values.length; win++)
						if (window_feature_values[win][feat] != null)
							count++;

					// Find the values to find the average and standard
					// deviations of
					double[] values_to_process = new double[count];
					int current = 0;
					for (int win = 0; win < window_feature_values.length; win++)
						if (window_feature_values[win][feat] != null) {
							values_to_process[current] = window_feature_values[win][feat][val];
							current++;
						}

					// Calculate the averages and standard deviations
					averages[val] = Statistics.getAverage(values_to_process);
					stdvs[val] = Statistics.getStandardDeviation(values_to_process);
				}

				// Store the results
				values.add(averages);
				definitions.add(average_definition);
				values.add(stdvs);
				definitions.add(stdv_definition);
			}

		// Finalize the values
		this.overall_feature_definitions[0] = definitions
				.toArray(new FeatureDefinition[1]);
		return values.toArray(new double[1][]);
	}
}