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

package command.info;

import command.Command;
import command.CommandParameter;

/**
 * This class simply prints on screen a help message for the user.
 */
public final class HelpCommand implements Command {

	private CommandParameter par;
	
	/**
	 * Standard command constructor which gets as input the command parameter.
	 * @param par contains all necessary data (user input and setup data).
	 */
	public HelpCommand(CommandParameter par) {
		
		if(par == null)
			throw new NullPointerException("HelpCommand has a null CommandParameter");
		
		this.par= par;
	}
	
	@Override
	public void start() 
			throws Exception {
		
		System.out.println(this.par.getMFESetup().getHelpMessage());
	}

}
