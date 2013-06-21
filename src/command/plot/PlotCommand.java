package command.plot;

import utils.Plotter;
import command.Command;
import command.CommandParameter;

public final class PlotCommand implements Command {

	private CommandParameter par;
	
	/**
	 * Standard command constructor which gets as input the command parameter.
	 * @param par contains all necessary data (user input and setup data).
	 */
	public PlotCommand(CommandParameter par) {
		
		if(par == null)
			throw new NullPointerException("PlotCommand has a null CommandParameter");
		
		this.par= par;
	}
	
	@Override
	public void start() 
			throws Exception {
		
		String[] args= this.par.getArgs();
		int inputNumberValues= args.length - 1; //-1 for command word
		if(inputNumberValues < 1)
			throw new Exception("No file to plot");
		
		Plotter.plot(args);
	}

}
