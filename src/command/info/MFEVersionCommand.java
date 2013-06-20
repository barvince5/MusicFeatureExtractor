package command.info;

import command.Command;

/**
 * This class simply prints on screen informations about the MFE version.
 */
public final class MFEVersionCommand implements Command {

	private String info= "";
	
	public MFEVersionCommand(String info) {
		this.info= info;
	}
	
	@Override
	public void start() 
			throws Exception {
		System.out.println(info);
	}

}
