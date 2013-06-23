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
 * Exception that occurs when it is not possible to create a doc.
 */
public class CreateDocException extends Exception {

	private static final long serialVersionUID = 8668158185252734002L;

	public CreateDocException(String msg) {
		super(msg);
	}
	
	public CreateDocException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public CreateDocException() {
		super();
	}
}
