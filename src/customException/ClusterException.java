package customException;

/**
 * This exception can happens when an error occurs during clustering operation.
 */
public class ClusterException extends Exception {

	private static final long serialVersionUID = 4250185144924423408L;

	public ClusterException(String msg) {
		super(msg);
	}
	
	public ClusterException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public ClusterException() {
		super();
	}
}
