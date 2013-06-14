package customException;

/**
 * Custom exception raised when an error in obtaining low level features occurs.
 * @author Vincenzo Barone 
 *
 */
public class FFTException extends Exception {

	private static final long serialVersionUID = -256432091657843011L;

	public FFTException(String msg) {
		super(msg);
	}
	
	public FFTException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public FFTException() {
		super();
	}

}
