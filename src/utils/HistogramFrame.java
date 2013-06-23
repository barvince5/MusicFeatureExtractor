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