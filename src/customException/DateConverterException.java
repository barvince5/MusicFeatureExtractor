package customException;

public class DateConverterException extends Exception {

	private static final long serialVersionUID = 8078371464708203007L;

	public DateConverterException(String msg) {
		super(msg);
	}
	
	public DateConverterException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public DateConverterException() {
		super();
	}
}
