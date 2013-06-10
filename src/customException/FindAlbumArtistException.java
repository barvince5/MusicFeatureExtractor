package customException;

public class FindAlbumArtistException extends Exception {

	private static final long serialVersionUID = 8638700221232350085L;
	
	public FindAlbumArtistException(String msg) {
		super(msg);
	}
	
	public FindAlbumArtistException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public FindAlbumArtistException() {
		super();
	}

}
