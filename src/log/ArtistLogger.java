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

package log;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import customException.LogException;

/**
 * This class creates just one instance for the log file using the singleton pattern. The aim is to
 * share the log file with other threads.
 */
public final class ArtistLogger {
	
	private static Logger log= null;
	private static ArtistLogger instance= null;
	
	/**
	 * This is the private constructor
	 * @throws LogException 
	 * @throws IOException 
	 * @throws SecurityException 
	 */
	private ArtistLogger() 
		throws LogException {
		
		ArtistLogger.log= Logger.getLogger(ArtistLogger.class.getName());
		FileHandler fh= null;
		
		try {
			
			fh = new FileHandler("LogArtist " + new Date().toString());
			ArtistLogger.log.addHandler(fh);
			ArtistLogger.log.setUseParentHandlers(false);
			
		} catch (SecurityException e) {
			throw new LogException(e.getMessage(), e);
		} catch (IOException e) {
			throw new LogException(e.getMessage(), e);
		} catch (Exception e) {
			throw new LogException(e.getMessage(), e);
		}
		
	}
	
	/**
	 * There is only one instance of this class. (Singleton)
	 * @return artist logger instance
	 * @throws LogException 
	 */
	public static ArtistLogger getInstance() 
			throws LogException {
		
		if(ArtistLogger.log == null) {
			synchronized (ArtistLogger.class) {
				if(ArtistLogger.log == null) {
					ArtistLogger.instance= new ArtistLogger();
				}
			}
		}
		
		return ArtistLogger.instance;
	}
	
	/**
	 * This method simply returns the log.
	 * @return logger
	 */
	public final Logger getLog() {
		return ArtistLogger.log;
	}
}
