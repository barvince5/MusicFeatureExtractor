package customException;

/**
 * Exception that occurs when an audio file is not supported.
 * @author Antonio Collarino
 *
 */
public final class UnsupportedAudioFileException extends Exception {

	private static final long serialVersionUID = 4993091377720383739L;

	public UnsupportedAudioFileException(String msg) {
		super(msg);
	}
	
	public UnsupportedAudioFileException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public UnsupportedAudioFileException() {
		super();
	}
}
