package customException;

/**
 * Custom exception raised when an error in obtaining low level features occurs.
 * @author Vincenzo Barone 
 *
 */
public class StatisticsException extends Exception {

	private static final long serialVersionUID = 6880434781628084729L;

	public StatisticsException(String msg) {
		super(msg);
	}
	
	public StatisticsException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public StatisticsException() {
		super();
	}

}
