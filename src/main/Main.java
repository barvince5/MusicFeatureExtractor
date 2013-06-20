package main;

import java.util.HashMap;

import command.Command;

import setup.MFESetup;



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
				
				System.err.println("No class present for this command");
				System.exit(1);
				
			} else {
				
				Command c= (Command) Class.forName(clazz).newInstance();
				c.start();
			}

		} catch(Exception e) {
			System.err.println(e.getMessage());
			MasterMetadata.shutDownMFE();
			System.err.println("MFE is shutting down");
		}
	}
}
