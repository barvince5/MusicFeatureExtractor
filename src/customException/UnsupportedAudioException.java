package customException;

/**
 * Exception that occurs when an audio file is not supported.
 */
public final class UnsupportedAudioException extends Exception {

	private static final long serialVersionUID = 4993091377720383739L;

	public UnsupportedAudioException(String msg) {
		super(msg);
	}
	
	public UnsupportedAudioException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public UnsupportedAudioException() {
		super();
	}
}
