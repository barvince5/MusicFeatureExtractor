package customException;

/**
 * Custom exception raised when an error in obtaining low level features occurs.
 * @author Vincenzo Barone 
 *
 */
public class StringMethodsException extends Exception {

	private static final long serialVersionUID = 8569136452806667479L;

	public StringMethodsException(String msg) {
		super(msg);
	}
	
	public StringMethodsException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public StringMethodsException() {
		super();
	}

}
