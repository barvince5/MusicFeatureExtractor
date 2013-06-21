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
	 * @return
	 */
	public final MFESetup getMFESetup() {
		return this.setup;
	}
	
	
}
