package customException;

/**
 * Custom exception raised when an error in obtaining low level features occurs.
 * @author Vincenzo Barone 
 *
 */
public class AudioMethodsException extends Exception {

	private static final long serialVersionUID = 6420434781628084729L;

	public AudioMethodsException(String msg) {
		super(msg);
	}
	
	public AudioMethodsException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public AudioMethodsException() {
		super();
	}

}
