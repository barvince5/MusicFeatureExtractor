package utils;

import java.io.File;
import java.util.logging.Logger;

import customException.LogException;
import customException.PlotterException;

import share.log.SongLogger;


public final class Plotter {

	private Plotter() {
		
	}
	
	public final static void plot(String[] files) 
			throws PlotterException {
				
		for(int i= 1; i< files.length; ++i) {
			try {
				File file= new File(files[i]);
				double[] values= LoadRhythmHistogram.getValues(file);
				HistogramPlotter hp= new HistogramPlotter(values, file.getName());
				hp.drawFrame();
			} catch(Exception e) {
				
				Logger log= null; 
				try {
					log= SongLogger.getInstance().getLog();
				} catch (LogException e1) {
					throw new PlotterException("LogException "+e.getMessage(), e);
				}
				log.warning(files[i]+" PLOT FAILED");
				
			}
		}
	}
	
	
}
