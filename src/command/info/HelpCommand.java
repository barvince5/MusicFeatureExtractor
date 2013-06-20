package command.info;

import command.Command;

/**
 * This class simply prints on screen a help message for the user.
 */
public final class HelpCommand implements Command {

	private String msg= "";
	
	public HelpCommand(String msg) {
		this.msg= msg;
	}
	
	@Override
	public void start() 
			throws Exception {
		
		System.out.println(msg);
	}

}
