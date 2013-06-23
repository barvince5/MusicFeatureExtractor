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
public final class SongLogger {
	
	private static Logger log= null;
	private static SongLogger instance= null;
	
	/**
	 * This is the private constructor
	 * @throws LogException 
	 */
	private SongLogger() 
			throws LogException {
		
		SongLogger.log= Logger.getLogger(ArtistLogger.class.getName());
		FileHandler fh= null;
		
		try {
			
			fh = new FileHandler("SongLog " + new Date().toString());
			SongLogger.log.addHandler(fh);
			SongLogger.log.setUseParentHandlers(false);
			
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
	 * @return
	 * @throws LogException 
	 */
	public static SongLogger getInstance() 
			throws LogException {
		
		if(SongLogger.log == null) {
			synchronized (SongLogger.class) {
				if(SongLogger.log == null) {
					SongLogger.instance= new SongLogger();
				}
			}
		}
		
		return SongLogger.instance;
	}
	
	/**
	 * This method simply returns the log.
	 * @return
	 */
	public final Logger getLog() {
		return SongLogger.log;
	}
}
