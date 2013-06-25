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

package wikipedia;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import customException.ArtistBiographyException;

/**
 * This class creates just one instance (using singleton pattern) fro geting biography of an artist
 * from wikipedia website.  
 */
public final class ArtistBiography {

	private static Object lock;
	private static int timeOut, wait;
	private static long minTime;
	private static ArtistBiography instance= null;
	
	/**
	 * This is the private constructor of this class.
	 */
	private ArtistBiography() {
		ArtistBiography.wait= 1000;			//1000 milli second is the default value.
		ArtistBiography.timeOut= 7000; 		// 7000 milli second is the default value.
		ArtistBiography.lock= new Object();
		ArtistBiography.minTime= 1000000000; //in nano second. (it is one second)
	}
	
	/**
	 * This method (Singleton) return the only instance of ArtistBiography class.<br>
	 * Note: Default wait is 1 second and default tiemout is 7 seconds
	 * @return ArtistBiography
	 */
	public final static ArtistBiography getInstance() {
		
		if(ArtistBiography.instance == null)
			synchronized(ArtistBiography.class) {
				if(ArtistBiography.instance == null)
					ArtistBiography.instance= new ArtistBiography();
			}
			
		return ArtistBiography.instance;
	}
	
	/**
	 * This method allows to set a different timeOut.<br>
	 * Note1: it must be greater that 1000 [ms] and less that 20000 [ms].<br>
	 * NOte2: The default value is 7 seconds (7000 ms).
	 * @param timeOut in milliseconds
	 * @throws ArtistBiographyException if the timeout is not correct.
	 */
	public final void changeTimeout(int timeOut) 
			throws ArtistBiographyException {
		
		if(timeOut < 1000 || timeOut > 20000)
			throw new ArtistBiographyException("The timeout vale is not correct");
		ArtistBiography.timeOut= timeOut;
	}
	
	/**
	 * This method allows to set a different wait value.<br>
	 * Note1: it must be greater that 100 [ms] and less that 20000 [ms].<br>
	 * NOte2: The default value is 1 second (1000 ms).
	 * @param wait in milliseconds
	 * @throws ArtistBiographyException if the timeout is not correct.
	 */
	public final void changeWait(int wait) 
			throws ArtistBiographyException {
		
		if(wait < 100 || wait > 20000)
			throw new ArtistBiographyException("The wait vale is not correct");
		ArtistBiography.wait= wait;
	}
	
	/**
	 * This method gets the entire biography of an artist or a band from the wikipedia english website.<br>
	 * Note: It is allowed on the wikipedia in english language.
	 * @param url of the artist/band on wikipedia website.
	 * @return the biography or null if not found.
	 * @throws ArtistBiographyException in case of errors
	 */
	public final String getArtistBiography(String url) 
			throws ArtistBiographyException {
		
		if(url.contains("en.wikipedia") == false)
			throw new ArtistBiographyException("The expected url is not the english version of wikipedia web site");
		
		//gets all info from wikipedia website
		String bio= "[From en.wikipedia] ";
		Document doc= null;
		Elements paragraphs= null;
		Element lastParagraph= null;
		Element p= null;
		
		try {
			
			synchronized(ArtistBiography.lock) {
				Connection conn= Jsoup.connect(url);
				conn.timeout(ArtistBiography.timeOut);
				long startTime = System.nanoTime();   
				doc = conn.get();
				long estimatedTime = System.nanoTime() - startTime;
				try {
					if(estimatedTime < minTime)
						Thread.sleep(ArtistBiography.wait);
				} catch(InterruptedException e) {
					return bio;	// Do nothing, just return
				}
			}
			
			paragraphs = doc.select(".mw-content-ltr p");
			lastParagraph = paragraphs.last();
			for(int i= 0; p != lastParagraph ; ++i) {
				p= paragraphs.get(i);
				bio += p.text().replaceAll("\\[.*\\]", "") + '\n';
			}
			
			if(bio.equals("[From en.wikipedia] "))
				return null;
			
			return bio;
			
		} catch (IOException e) {
			throw new ArtistBiographyException("IOException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new ArtistBiographyException("Exception "+e.getMessage(), e);
		}
	}
}
