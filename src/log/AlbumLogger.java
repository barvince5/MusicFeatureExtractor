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
public final class AlbumLogger {

	private static Logger log= null;
	private static AlbumLogger instance= null;
	
	/**
	 * This is the private constructor
	 * @throws LogException 
	 */
	private AlbumLogger() 
			throws LogException {
		
		AlbumLogger.log= Logger.getLogger(AlbumLogger.class.getName());
		FileHandler fh= null;
		
		try {
			
			fh = new FileHandler("LogAlbum " + new Date().toString());
			AlbumLogger.log.addHandler(fh);
			AlbumLogger.log.setUseParentHandlers(false);
			
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
	 * @return album logger instance
	 * @throws LogException 
	 */
	public static AlbumLogger getInstance() 
			throws LogException {
		
		if(AlbumLogger.log == null) {
			synchronized (AlbumLogger.class) {
				if(AlbumLogger.log == null) {
					AlbumLogger.instance= new AlbumLogger();
				}
			}
		}
		
		return AlbumLogger.instance;
	}
	
	/**
	 * This method simply returns the log.
	 * @return logger
	 */
	public final Logger getLog() {
		return AlbumLogger.log;
	}
}
