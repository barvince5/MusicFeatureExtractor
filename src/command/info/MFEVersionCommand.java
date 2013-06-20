package command.info;

import command.Command;
import command.CommandParameter;

/**
 * This class simply prints on screen informations about the MFE version.
 */
public final class MFEVersionCommand implements Command {

	private CommandParameter par;
	
	public MFEVersionCommand(CommandParameter par) {
		
		if(par == null)
			throw new NullPointerException("MFEVersionCommand has a null CommandParameter");
		
		this.par= par;
	}
	
	@Override
	public void start() 
			throws Exception {
		
		System.out.println(this.par.getMFESetup().getCompleteVersionInfo());
	}

}
