package customException;

/**
 * Custom exception raised when an error in obtaining low level features occurs.
 * @author Vincenzo Barone 
 *
 */
public class AudioSamplesException extends Exception {

	private static final long serialVersionUID = -2041465271428610757L;

	public AudioSamplesException(String msg) {
		super(msg);
	}
	
	public AudioSamplesException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public AudioSamplesException() {
		super();
	}

}
