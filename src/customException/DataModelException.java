package customException;

/**
 * Custom exception raised when an error in obtaining low level features occurs.
 * @author Vincenzo Barone 
 *
 */
public class DataModelException extends Exception {

	private static final long serialVersionUID = -7484303006229843144L;

	public DataModelException(String msg) {
		super(msg);
	}
	
	public DataModelException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public DataModelException() {
		super();
	}

}
