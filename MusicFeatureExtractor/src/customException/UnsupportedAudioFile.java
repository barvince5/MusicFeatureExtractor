package customException;

/**
 * Exception that occurs when an audio file is not supported.
 * @author Antonio Collarino
 *
 */
public final class UnsupportedAudioFile extends Exception {

	private static final long serialVersionUID = 3192475078654774202L;

	public UnsupportedAudioFile(String msg) {
		super(msg);
	}
	
	public UnsupportedAudioFile(String msg, Throwable e) {
		super(msg, e);
	}
	
	public UnsupportedAudioFile() {
		super();
	}
}
