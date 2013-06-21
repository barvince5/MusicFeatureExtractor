package customException;

/**
 * This exception can happens when an error occurs during log operations.
 */
public final class LogException extends Exception {

	private static final long serialVersionUID = 3303839117474304910L;
	
	public LogException(String msg) {
		super(msg);
	}
	
	public LogException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public LogException() {
		super();
	}
}
