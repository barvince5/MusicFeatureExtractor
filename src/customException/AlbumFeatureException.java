package customException;

/**
 * This exception can happens when an error occurs getting information about albums of an artist.
 */
public class AlbumFeatureException extends Exception {

	private static final long serialVersionUID = -3734606809776444151L;

	public AlbumFeatureException(String msg) {
		super(msg);
	}
	
	public AlbumFeatureException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public AlbumFeatureException() {
		super();
	}
}
