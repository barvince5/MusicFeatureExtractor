package utils;

import java.io.File;
import java.util.logging.Logger;

import customException.LogException;
import customException.PlotterException;

import share.log.SongLogger;

/**
 * This class plots the rhythm histogram.
 */
public final class Plotter {

	/**
	 * To avoid instantiation of this class.
	 */
	private Plotter() {
		
	}
	
	/**
	 * 
	 * @param files list of files (SONG_LL_[song name]) containing low level features.
	 * @throws PlotterException
	 */
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
