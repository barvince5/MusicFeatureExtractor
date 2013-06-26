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

package main;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import command.Command;
import command.CommandParameter;

import setup.MFESetup;

/**
 * This is the main class of the MFE program. At the beginning is done the setup and then a map of commands
 * is loaded from the xml setup file. If the user give a correct command on the fly is allocated the corresponding
 * object, and then the start command method is called to execute it.
 */
public class Main {

	public static void main(String[] args) {
		
		if(args.length == 0) {
			System.err.println("No command input");
			System.exit(1);
		}
		
		HashMap<String, String> cmd;
		
		try {

			//SETUP PHASE
			MFESetup setup= new MFESetup(); 
			cmd= setup.getCommands();
			if(cmd.isEmpty()) {
				System.err.println("No command present in setup.xml file");
				System.exit(1);
			}
				
			//START PROGRAM PHASE
			String userCommand= args[0];
			String clazz= cmd.get(userCommand);
			if(clazz == null || clazz.equals("")) {
				
				System.err.println("This command is not correct, please try -help");
				System.exit(1);
				
			} else {
				
				//creation of the corresponding class is done on the fly
				Constructor<?> con= Class.forName(clazz).getConstructor(CommandParameter.class);
				//for now only one command at time can be run.
				Command c= (Command) con.newInstance(new CommandParameter(args, setup));
				c.start();
			}

		} catch(Exception e) {
			System.err.println(e.getMessage());
			System.err.println("MFE is shutting down");
		}
	}
}
