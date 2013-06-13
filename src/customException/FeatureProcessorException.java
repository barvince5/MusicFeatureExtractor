package customException;

/**
 * Custom exception raised when an error in obtaining low level features occurs.
 * @author Vincenzo Barone 
 *
 */
public class FeatureProcessorException extends Exception {

	private static final long serialVersionUID = -991520689850041283L;

	public FeatureProcessorException(String msg) {
		super(msg);
	}
	
	public FeatureProcessorException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public FeatureProcessorException() {
		super();
	}

}
