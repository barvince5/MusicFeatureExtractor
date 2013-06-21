package customException;

/**
 * Exception that occurs when there are errors during MP3 analysis.
 */
public final class MP3Exception extends Exception {

	private static final long serialVersionUID = 4176008688289916270L;

	public MP3Exception(String msg) {
		super(msg);
	}
	
	public MP3Exception(String msg, Throwable e) {
		super(msg, e);
	}
	
	public MP3Exception() {
		super();
	}
}
