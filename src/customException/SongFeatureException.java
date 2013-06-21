package customException;

/**
 * This exception can happens when an error occurs getting information about songs high/low level.
 */
public class SongFeatureException extends Exception {

	private static final long serialVersionUID = 3299816096119557971L;

	public SongFeatureException(String msg) {
		super(msg);
	}
	
	public SongFeatureException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public SongFeatureException() {
		super();
	}
}
