package customException;

/**
 * Exception that occurs when input values are not correct or when an error occurs.
 * @author Antonio Collarino
 *
 */
public class MusicbrainzUrlException extends Exception {

	private static final long serialVersionUID = 332802924810478186L;

	public MusicbrainzUrlException(String msg) {
		super(msg);
	}
	
	public MusicbrainzUrlException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public MusicbrainzUrlException() {
		super();
	}
}
