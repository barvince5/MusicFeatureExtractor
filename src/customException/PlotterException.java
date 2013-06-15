package customException;

public class PlotterException extends Exception {

	private static final long serialVersionUID = 6369813283817634107L;

	public PlotterException(String msg) {
		super(msg);
	}
	
	public PlotterException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public PlotterException() {
		super();
	}
}
