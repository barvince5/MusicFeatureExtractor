package customException;

public class ArtistFeatureException extends Exception {

	private static final long serialVersionUID = -6107071504867288174L;
	
	public ArtistFeatureException(String msg) {
		super(msg);
	}
	
	public ArtistFeatureException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public ArtistFeatureException() {
		super();
	}
}
