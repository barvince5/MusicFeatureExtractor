package customException;

public class MFESetupException extends Exception {


	private static final long serialVersionUID = 3950820610699321196L;

	public MFESetupException(String msg) {
		super(msg);
	}
	
	public MFESetupException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public MFESetupException() {
		super();
	}
}
