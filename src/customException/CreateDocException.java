package customException;

/**
 * Exception that occurs when it is not possible to create a doc.
 * @author Antonio Collarino
 *
 */
public class CreateDocException extends Exception {

	private static final long serialVersionUID = 8668158185252734002L;

	public CreateDocException(String msg) {
		super(msg);
	}
	
	public CreateDocException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public CreateDocException() {
		super();
	}
}
