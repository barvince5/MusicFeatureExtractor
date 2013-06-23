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
			
			fh = new FileHandler("AlbumLog " + new Date().toString());
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
	 * @return
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
	 * @return
	 */
	public final Logger getLog() {
		return AlbumLogger.log;
	}
}
