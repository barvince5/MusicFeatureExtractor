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
