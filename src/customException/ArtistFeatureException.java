/* Copyright 2013 Antonio Collarino, Vincenzo Barone

This file is part of Music Feature Extractor (MFE).

Music Feature Extractor (MFE) is free software; you can redistribute it 
and/or modify it under the terms of the GNU Lesser General Public License 
as published by the Free Software Foundation; either version 3 of the 
License, or (at your option) any later version.

Music Feature Extractor (MFE) is distributed in the hope that it will be 
useful, but WITHOUT ANY WARRANTY; without even the implied warranty of 
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser 
General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with Music Feature Extractor (MFE).  If not, see 
http://www.gnu.org/licenses/.  */

package customException;

/**
 * This exception can happens when an error occurs getting information about an artist.
 */
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
