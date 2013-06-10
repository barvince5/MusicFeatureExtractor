package musicbrainz;


import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import customException.MusicbrainzUrlException;

public final class MusicbrainzUrl {
	
	/**
	 * Private constructor, to avoid instantiation 
	 */
	private MusicbrainzUrl() {
		
	}
	
	/**
	 * Creates a correct musicbrainz url for this song.<br>
	 * Note: At least the title must not be null or empty.
	 * @param artist
	 * @param title
	 * @param album
	 * @return url musicbrainz style to perform a gethttp, or null.
	 * @throws MusicbrainzUrlException in case of errors (e.g. title null).
	 */
	public final static URL getMbRecordingUrl(String artist, String title, String album) 
			throws MusicbrainzUrlException {
		
		if(title == null || title.equals(""))
			throw new MusicbrainzUrlException("The title is null or empty");
		
		URL url= null;
		URI uri= null;
		
		try {
			
			if(artist != null && !artist.equals(""))
				if(album != null && !album.equals("")) {
						uri= new URI("http","www.musicbrainz.org",
									"/ws/2/recording/", 
									"query=artist:\""+artist+"\" AND recording:\""+title+"\" AND release:\""+album+"\"", 
									null);
						if(uri != null)
							return url= uri.toURL();
				}
			
			if(album != null && !album.equals("")) {
				uri= new URI("http","www.musicbrainz.org",
							"/ws/2/recording/", 
							"query=recording:\""+title+"\" AND release:\""+album+"\"", 
							null);
				if(uri != null)
					return url= uri.toURL();
				}
			
			if(artist != null && !artist.equals("")) {
				uri= new URI("http","www.musicbrainz.org",
							"/ws/2/recording/", 
							"query=artist:\""+artist+"\" AND recording:\""+title+"\"", 
							null);
				if(uri != null)
					return url= uri.toURL();
				}
			
			uri= new URI("http","www.musicbrainz.org",
					"/ws/2/recording/", 
					"query=recording:\""+title+"\"&limit=1", 
					null);
			if(uri != null)
				url= uri.toURL();
						
		} catch (URISyntaxException e) {
			throw new MusicbrainzUrlException("URISyntaxException "+e.getMessage(), e);
		} catch (MalformedURLException e) {
			throw new MusicbrainzUrlException("MalformedURLException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new MusicbrainzUrlException("Exception "+e.getMessage(), e);
		}
	
		return url;
	}
	
	/**
	 * Creates a correct musicbrainz url for this song, returning all matches.<br>
	 * Note: At least the title must not be null or empty.
	 * @param artist
	 * @param title
	 * @param album
	 * @return url musicbrainz style to perform a gethttp, or null.
	 * @throws MusicbrainzUrlException in case of errors (e.g. title null).
	 */
	public final static URL getMbAllRecordingsUrl(String title) 
			throws MusicbrainzUrlException {
		
		if(title == null || title.equals(""))
			throw new MusicbrainzUrlException("The title is null or empty");
		
		URL url= null;
		URI uri= null;
		
		try {
			
			uri= new URI("http","www.musicbrainz.org",
					"/ws/2/recording/", 
					"query=recording:\""+title+"\"", 
					null);
			if(uri != null)
				url= uri.toURL();
						
		} catch (URISyntaxException e) {
			throw new MusicbrainzUrlException("URISyntaxException "+e.getMessage(), e);
		} catch (MalformedURLException e) {
			throw new MusicbrainzUrlException("MalformedURLException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new MusicbrainzUrlException("Exception "+e.getMessage(), e);
		}
	
		return url;
	}
	
	/**
	 * Creates a correct musicbrainz url for this artist.
	 * @param artist must not be null or empty.
	 * @return musicbrainz url style to perform a gethttp, or null.
	 * @throws MusicbrainzUrlException in case of errors (e.g. artist null).
	 */
	public final static URL getMbArtistUrl(String artist) 
			throws MusicbrainzUrlException {
		
		if(artist == null || artist.equals(""))
			throw new MusicbrainzUrlException("The artist is null or empty");
		
		URL url= null;
		URI uri= null;
	
		try {
			
			//In case of more than one matches, only the first one (with high score) is got. (&limit=1)
			uri= new URI("http","www.musicbrainz.org",
						"/ws/2/artist/", 
						"query=artist:\""+artist+"\"&limit=1", 
						null);
			
			if(uri != null)
				url= uri.toURL();
			
		} catch (URISyntaxException e) {
			throw new MusicbrainzUrlException("URISyntaxException "+e.getMessage(), e);
		} catch (MalformedURLException e) {
			throw new MusicbrainzUrlException("MalformedURLException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new MusicbrainzUrlException("Exception "+e.getMessage(), e);
		}
		
		return url;
	}
	
	/**
	 *  Creates a correct musicbrainz url for this artist to get all links.
	 * @param artistID must not be null or empty.
	 * @return musicbrainz url style to perform a gethttp, or null.
	 * @throws MusicbrainzUrlException in case of errors (e.g. artist null).
	 */
	public final static URL getMbLinksUrl(String artistID) 
			throws MusicbrainzUrlException {
		
		if(artistID == null || artistID.equals(""))
			throw new MusicbrainzUrlException("The artistID is null or empty");
		
		URL url= null;
		URI uri= null;
	
		try {
			
			//In case of more than one matches, only the first one (with high score) is got. (&limit=1)
			uri= new URI("http","www.musicbrainz.org",
						"/ws/2/artist/"+artistID, 
						"inc=url-rels", 
						null);
			
			if(uri != null)
				url= uri.toURL();
			
		} catch (URISyntaxException e) {
			throw new MusicbrainzUrlException("URISyntaxException "+e.getMessage(), e);
		} catch (MalformedURLException e) {
			throw new MusicbrainzUrlException("MalformedURLException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new MusicbrainzUrlException("Exception "+e.getMessage(), e);
		}
		
		return url;
	}
	
	/**
	 * Creates a correct musicbrainz url to get all albums of this artist.
	 * @param artistID musicbrainz id. It must not be null or empty.
	 * @return musicbrainz url style to perform a gethttp, or null.
	 * @throws MusicbrainzUrlException in case of errors (e.g. artistID null).
	 */
	public final static URL getMbAlbumsOfArtist(String artistID) 
			throws MusicbrainzUrlException {
		
		if(artistID == null || artistID.equals(""))
			throw new MusicbrainzUrlException("The artistID is null or empty");
		
		URL url= null;
		URI uri= null;
		
		try {
			
			//lookup of musicbrainz
			uri= new URI("http","www.musicbrainz.org",
					"/ws/2/release-group",
					"artist="+artistID+"&type=album|ep", 
					null);
			
			if(uri != null)
				url= uri.toURL();
		
		} catch (URISyntaxException e) {
			throw new MusicbrainzUrlException("URISyntaxException "+e.getMessage(), e);
		} catch (MalformedURLException e) {
			throw new MusicbrainzUrlException("MalformedURLException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new MusicbrainzUrlException("Exception "+e.getMessage(), e);
		}
		
		return url;
	}
	
	/**
	 * Creates a correct musicbrainz url to get all songs inside an album.
	 * @param releaseGroupID musicbrainz id. It must not be null or empty.
	 * @return musicbrainz url style to perform a gethttp, or null.
	 * @throws MusicbrainzUrlException in case of errors (e.g. artistID null).
	 */
	public final static URL getMbSongsOfAlbum(String releaseGroupID) 
			throws MusicbrainzUrlException {
		
		if(releaseGroupID == null || releaseGroupID.equals(""))
			throw new MusicbrainzUrlException("The releaseGroupID is null or empty");
		
		URL url= null;
		URI uri= null;
		
		try {
			
			uri= new URI("http","www.musicbrainz.org",
					"/ws/2/recording/", 
					"query=rgid:"+releaseGroupID, 
					null);
			
			if(uri != null)
				url= uri.toURL();
		
		} catch (URISyntaxException e) {
			throw new MusicbrainzUrlException("URISyntaxException "+e.getMessage(), e);
		} catch (MalformedURLException e) {
			throw new MusicbrainzUrlException("MalformedURLException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new MusicbrainzUrlException("Exception "+e.getMessage(), e);
		}
		
		return url;
	}
	
	public final static URL getMbAlbum(String artist, String album) 
			throws MusicbrainzUrlException {
		
		if(artist == null || artist.equals(""))
			throw new MusicbrainzUrlException("The artist is null or empty");
		
		if(album == null || album.equals(""))
			throw new MusicbrainzUrlException("The album is null or empty");
		
		URL url= null;
		URI uri= null;
		
		try {
			
			uri= new URI("http","www.musicbrainz.org",
					"/ws/2/release/", 
					"query=release:\""+album+"\" AND artist:\""+artist+"\"", 
					null);
			
			if(uri != null)
				url= uri.toURL();
		
		} catch (URISyntaxException e) {
			throw new MusicbrainzUrlException("URISyntaxException "+e.getMessage(), e);
		} catch (MalformedURLException e) {
			throw new MusicbrainzUrlException("MalformedURLException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new MusicbrainzUrlException("Exception "+e.getMessage(), e);
		}
		
		return url;
	}
}
