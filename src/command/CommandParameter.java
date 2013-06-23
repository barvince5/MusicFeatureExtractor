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

package command;

import setup.MFESetup;

/**
 * This class provides the most important parameters for the commands. 
 */
public final class CommandParameter {
	
	private String[] args;
	private MFESetup setup;
	
	/**
	 * This is the constructor.
	 * @param args are the user inputs
	 * @param setup is the MFE object containing some data stored in setup.xml file.
	 */
	public CommandParameter(String[] args, MFESetup setup) {
		
		if(args == null)
			throw new NullPointerException("CommandParameter has a null args");
		if(setup == null)
			throw new NullPointerException("CommandParameter has a null mfe setup");
		
		this.args= args;
		this.setup= setup;
	}
	
	/**
	 * To get the inputs given by the user.
	 * @return args
	 */
	public final String[] getArgs() {
		return this.args;
	}
	
	/**
	 * To get the setup of MFE containing some data stored in setup.xml file.
	 * @return setup.
	 */
	public final MFESetup getMFESetup() {
		return this.setup;
	}
	
	
}
