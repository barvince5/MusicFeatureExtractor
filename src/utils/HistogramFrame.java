package utils;

import java.awt.Dimension;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

/**
 * This class contains all functions for creating a Frame containing a Histogram.
 *
 */
public class HistogramFrame extends ApplicationFrame {

	private static final long serialVersionUID = 8980678869535504762L;
	private String title= "";
	private String label= "";
	private double[] values= null;
	private IntervalXYDataset dataset= null;
	private JFreeChart chart= null;
	
	/**
	 * Constructor for the HistogramFrame.
	 * @param values an array of doubles containing the values to be plotted
	 * @param title the title to display for the Histogram
	 */
	public HistogramFrame(double[] values, String title, int w, int h, String label){
		
		super(title);
		
		this.title= title;
		this.values= values;
		this.label= label;
		this.createDataset();
		this.createChart();
		
		final ChartPanel chartPanel= new ChartPanel(this.chart);
		chartPanel.setPreferredSize(new Dimension(w, h));
		super.setContentPane(chartPanel);
	}
	
	/**
	 * Loads the values provided in the constructor into a XYDataset for drawing the values.
	 */
	private void createDataset() {
		final XYSeries series= new XYSeries("data");
			for (int i=0; i<this.values.length; i++)
				series.add(i, this.values[i]);
		this.dataset= new XYSeriesCollection(series);
	}
	
	/**
	 * Uses the XYDataset to fill a XYBarChart Histogram. 
	 */
	private void createChart() {
		this.chart = ChartFactory.createXYBarChart (this.title, this.label, false, "", this.dataset, PlotOrientation.VERTICAL, false, false, false);
		this.chart.getPlot();        
	}
}