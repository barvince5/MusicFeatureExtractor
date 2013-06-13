package utils;

/**
 * This class contains the function for drawing the frame given an array of values.
 */

public class HistogramPlotter {
	
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
	public void setDimensions(int width, int height) {
		this.width= width;
		this.height= height;
	}
	
	/**
	 * Sets the label to be shown on the X axis of the Histogram.
	 * @param label
	 */
	public void setLabel(String label) {
		this.label= label;
	}
	
	/**
	 * Shows the HistogramFrame on screen.
	 */	
	public void drawFrame() {
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