package customException;

/**
 * This exception can happens when an error occurs getting information about biography of an artist.
 * @author Antonio Collarino
 *
 */
public class ArtistBiographyException extends Exception {

	private static final long serialVersionUID = -5533253164314887668L;

	public ArtistBiographyException(String msg) {
		super(msg);
	}
	
	public ArtistBiographyException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public ArtistBiographyException() {
		super();
	}
}
