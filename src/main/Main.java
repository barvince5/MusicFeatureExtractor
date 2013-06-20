package main;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import command.Command;
import command.CommandParameter;

import setup.MFESetup;


public class Main {

	public static void main(String[] args) throws Exception {
		
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
