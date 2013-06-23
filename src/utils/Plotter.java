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

import java.io.File;
import java.util.logging.Logger;

import log.SongLogger;

import customException.LogException;
import customException.PlotterException;


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
