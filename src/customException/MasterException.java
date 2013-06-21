package customException;

/**
 * This exception can happens when an error occurs in the master meta data which commands the features extractor classes.
 */
public class MasterException extends Exception {

	private static final long serialVersionUID = 2006510181582667149L;

	public MasterException(String msg) {
		super(msg);
	}
	
	public MasterException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public MasterException() {
		super();
	}
}
