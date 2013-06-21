package customException;

public class AudioFileExtractorException extends Exception {

	private static final long serialVersionUID = -7253234974930138210L;

	public AudioFileExtractorException(String msg) {
		super(msg);
	}
	
	public AudioFileExtractorException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public AudioFileExtractorException() {
		super();
	}
}
