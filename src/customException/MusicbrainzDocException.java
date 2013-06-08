package customException;

/**
 * Exception that occurs when it is not possible to create a doc.
 * @author Antonio Collarino
 *
 */
public class MusicbrainzDocException extends Exception {

	private static final long serialVersionUID = 8668158185252734002L;

	public MusicbrainzDocException(String msg) {
		super(msg);
	}
	
	public MusicbrainzDocException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public MusicbrainzDocException() {
		super();
	}
}
