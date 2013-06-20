package command;

import setup.MFESetup;

/**
 * This class provides the most important parameters for the commands. 
 */
public final class CommandParameter {
	
	private String[] args;
	private MFESetup setup;
	
	public CommandParameter(String[] args, MFESetup setup) {
		
		if(args == null)
			throw new NullPointerException("CommandParameter has a null args");
		if(setup == null)
			throw new NullPointerException("CommandParameter has a null mfe setup");
		
		this.args= args;
		this.setup= setup;
	}
	
	public final String[] getArgs() {
		return this.args;
	}
	
	public final MFESetup getMFESetup() {
		return this.setup;
	}
	
	
}
