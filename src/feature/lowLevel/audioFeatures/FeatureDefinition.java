/*
 * @(#)FeatureDefinition.java	0.5	Feb 27, 2005.
 *
 * McGill Univarsity
 */

package feature.lowLevel.audioFeatures;

import java.io.*;

/**
 * Objects of this class each hold meta-data about a feature, as specified by
 * the five public fields. Objects of this class do not hold any feature values
 * of particular instances.
 * <p>
 * Methods are available for viewing the features, veryifying the uniqueness of
 * their names, saving them to disk and loading the, from disk.
 * <p>
 * Daniel McEnnis 05-07-05 Added attributes to definition that describe the
 * names (and implicitly number) of editable features.
 *
 * @author Cory McKay
 * @author modified by Vincenzo Barone
 */
public class FeatureDefinition implements Serializable {
	/* FIELDS ***************************************************************** */

	/**
	 * The name of the feature. This name should be unique among each set of
	 * features.
	 */
	public String name;

	/**
	 * A description of what the feature represents. May be left as an empty
	 * string.
	 */
	public String description;

	/**
	 * Specifies whether a feature can be applied to sub-section of a data set
	 * (e.g. a window of audio). A value of true means that it can, and a value
	 * of false means that only one feature value may be extracted per data set.
	 */
	public boolean is_sequential;

	/**
	 * The number of values that exist for the feature for a given section of a
	 * data set. This value will be 1, except for multi-dimensional features.
	 */
	public int dimensions;

	/**
	 * An identifier for use in serialization.
	 */
	private static final long serialVersionUID = 2L;

	/**
	 * names of each editable attribute this feature has
	 */
	public String[] attributes;

	/* CONSTRUCTORS *********************************************************** */

	/**
	 * Generate an empty FeatureDefinition with the name "Undefined Feature".
	 */
	public FeatureDefinition() {
		this.name = "Undefined Feature";
		this.description = new String("");
		this.is_sequential = false;
		this.dimensions = 1;
		this.attributes = new String[] {};
	}

	/**
	 * Explicitly define a new Feature Definition with no editable attributes.
	 *
	 * @param name
	 *            The name of the feature. This name should be unique among each
	 *            set of features.
	 * @param description
	 *            A description of what the feature represents. May be left as
	 *            an empty string.
	 * @param is_sequential
	 *            Specifies whether a feature can be applied to sequential
	 *            windows of a data set. A value of true means that it can, and
	 *            a value of false means that only one feature value may be
	 *            extracted per data set.
	 * @param dimensions
	 *            The number of values that exist for the feature for a given
	 *            section of a data set. This value will be 1, except for
	 *            multi-dimensional features.
	 */
	public FeatureDefinition(String name, String description,
			boolean is_sequential, int dimensions) {
		this.name = name;
		this.description = description;
		this.is_sequential = is_sequential;
		this.dimensions = dimensions;
		this.attributes = new String[] {};
	}

	/**
	 * Explicitly define a feature along with a description of editable
	 * attributes.
	 *
	 * @param name
	 *            The name of the feature. This name should be unique among each
	 *            set of features.
	 * @param description
	 *            A description of what the feature represents. May be left as
	 *            an empty string.
	 * @param is_sequential
	 *            Specifies whether a feature can be applied to sequential
	 *            windows of a data set. A value of true means that it can, and
	 *            a value of false means that only one feature value may be
	 *            extracted per data set.
	 * @param dimensions
	 *            The number of values that exist for the feature for a given
	 *            section of a data set. This value will be 1, except for
	 *            multi-dimensional features.
	 * @param attributes
	 *            The names of all editable attributes in the feature
	 */
	public FeatureDefinition(String name, String description,
			boolean is_sequential, int dimensions, String[] attributes) {
		this.name = name;
		this.description = description;
		this.is_sequential = true;
		this.dimensions = dimensions;
		this.attributes = attributes;
	}

	/* PUBLIC METHODS ********************************************************* */

	/**
	 * Returns a formatted text description of the FeatureDescription object.
	 *
	 * @return The formatted description.
	 */
	public String getFeatureDescription() {
		String info = "NAME: " + name + "\n";
		info += "DESCRIPTION: " + description + "\n";
		info += "IS SEQUENTIAL: " + is_sequential + "\n";
		info += "DIMENSIONS: " + dimensions + "\n\n";
		return info;
	}

	/**
	 * Returns a formatted text description of the given FeatureDescription
	 * objects.
	 *
	 * @param definitions
	 *            The feature definitions to describe.
	 * @return The formatted description.
	 */
	public static String getFeatureDescriptions(FeatureDefinition[] definitions) {
		String combined_descriptions = new String();
		for (int i = 0; i < definitions.length; i++)
			combined_descriptions += definitions[i].getFeatureDescription();
		return combined_descriptions;
	}
}