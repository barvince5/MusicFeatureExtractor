package share.log;

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
			
			fh = new FileHandler("ArtistLog " + new Date().toString());
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
	 * @return
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
	 * @return
	 */
	public final Logger getLog() {
		return ArtistLogger.log;
	}
}
