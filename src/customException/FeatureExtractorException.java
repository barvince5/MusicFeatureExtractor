package customException;

/**
 * Custom exception raised when an error in obtaining low level features occurs.
 * @author Vincenzo Barone 
 *
 */
public class FeatureExtractorException extends Exception {

	private static final long serialVersionUID = -256432091657843011L;

	public FeatureExtractorException(String msg) {
		super(msg);
	}
	
	public FeatureExtractorException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public FeatureExtractorException() {
		super();
	}

}
