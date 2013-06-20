package command.plot;

import utils.Plotter;
import command.Command;

public final class PlotCommand implements Command {

	private String[] args;
	
	public PlotCommand(String[] args) {
		this.args= args;
	}
	
	@Override
	public void start() 
			throws Exception {
		
		int inputNumberValues= args.length - 1; //-1 for command word
		if(inputNumberValues < 1)
			throw new Exception("No file to plot");
		
		Plotter.plot(args);
	}

}
