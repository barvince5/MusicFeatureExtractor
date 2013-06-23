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

package utils;

/**
 * This class contains the function for drawing the frame given an array of values.
 */

public final class HistogramPlotter {
	
	private int width;
	private int height;
	private double[] values= null;
	private String title= "";
	private String label= "";
	private HistogramFrame frame= null;
	
	/**
	 * Constructor for the Histogram Plotter. 
	 * The default size of the Histogram window is set as 500x300.
	 * The default value for Label is "Modulation Frequency [Hz]".
	 * @param values an array of doubles containing the values to be plotted
	 * @param title the title to display for the Histogram
	 */
	public HistogramPlotter(double[] values, String title) {
		this.values= values;
		this.title= title;
		this.width= 500;
		this.height= 300;
		this.label= "Modulation Frequency [Hz]";
	}
	
	/**
	 * Sets the dimensions for the Frame Window. 
	 * @param width
	 * @param height
	 */
	public final void setDimensions(int width, int height) {
		this.width= width;
		this.height= height;
	}
	
	/**
	 * Sets the label to be shown on the X axis of the Histogram.
	 * @param label
	 */
	public final void setLabel(String label) {
		this.label= label;
	}
	
	/**
	 * Shows the HistogramFrame on screen.
	 */	
	public final void drawFrame() {
		this.buildFrame();
		this.frame.pack();
		this.frame.setVisible(true);
	}	
	
	/**
	 * Constructs the HistogramFrame that will contain the Histogram.
	 */
	private void buildFrame() {
		this.frame = new HistogramFrame(this.values, this.title, this.width, this.height, this.label);
	}
}