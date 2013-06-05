package wikipedia;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import customException.ArtistBiographyException;

public final class ArtistBiography {

	private static Object lock;
	private static int timeOut, wait;
	private static ArtistBiography instance= null;
	
	/**
	 * This is the private constructor of this class.
	 */
	private ArtistBiography() {
		ArtistBiography.wait= 1000;		// 1000 milli second is the default value.
		ArtistBiography.timeOut= 7000; 	// 7000 milli second is the default value.
		ArtistBiography.lock= new Object();
	}
	
	/**
	 * This method (Singleton) return the only instance of ArtistBiography class.<br>
	 * Note: Default wait is 1 second and default tiemout is 7 seconds
	 * @return ArtistBiography
	 */
	public final ArtistBiography getInstance() {
		
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
	 * @param tiemOut in milli seconds
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
	 * @param wait in milli seconds
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
	 * @param url of the artist/band on wekipedia website.
	 * @return the biography
	 * @throws ArtistBiographyException in case of errors
	 */
	public final String getArtistBiography(String url) 
			throws ArtistBiographyException {
		
		if(url.contains("en.wikipedia") == false)
			throw new ArtistBiographyException("The expected url is not the english version of wikipedia web site");
		
		//get all info from wikipedia website
		String bio= null;
		Document doc= null;
		Elements paragraphs= null;
		Element firstParagraph= null;
		Element lastParagraph= null;
		Element p= null;
		
		try {
			
			synchronized(ArtistBiography.lock) {
				Connection conn= Jsoup.connect(url);
				conn.timeout(ArtistBiography.timeOut);
				doc = conn.get();
				try {
					Thread.sleep(ArtistBiography.wait);
				} catch(InterruptedException e) {
					return bio;	// Do nothing, just return
				}
			}
			
			paragraphs = doc.select(".mw-content-ltr p");
			firstParagraph = paragraphs.first();
			lastParagraph = paragraphs.last();
			p= firstParagraph;
			bio= p.text().replaceAll("\\[.*\\]", "") + '\n';
			for(int i= 1; p != lastParagraph ; ++i) {
				p= paragraphs.get(i);
				bio += p.text().replaceAll("\\[.*\\]", "") + '\n';
			}
			
			return bio;
			
		} catch (IOException e) {
			throw new ArtistBiographyException("IOException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new ArtistBiographyException("Exception "+e.getMessage(), e);
		}
	}
}
