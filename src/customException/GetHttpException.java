package customException;

/**
 * Exception that occurs when there are errors to get http page content.
 * @author sniper
 *
 */
public final class GetHttpException extends Exception {

	private static final long serialVersionUID = 3085826726774849347L;

	public GetHttpException(String msg) {
		super(msg);
	}
	
	public GetHttpException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public GetHttpException() {
		super();
	}
}
