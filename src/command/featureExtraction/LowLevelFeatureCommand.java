package command.featureExtraction;

import main.MasterMetadata;
import command.Command;
import command.CommandParameter;

/**
 * This class implement the command -ll (for low level feature extraction).
 */
public final class LowLevelFeatureCommand implements Command {

	private CommandParameter par;
	
	/**
	 * Standard command constructor which gets as input the command parameter.
	 * @param par contains all necessary data (user input and setup data).
	 */
	public LowLevelFeatureCommand(CommandParameter par) {
		
		if(par == null)
			throw new NullPointerException("LowLevelFeatureCommand has a null CommandParameter");
		
		this.par= par;
	}
	
	@Override
	public void start() 
			throws Exception {
		
		String[] args= this.par.getArgs();
		int inputNumberValues= args.length - 1; //-1 for command word
		if(inputNumberValues != 1)
			throw new Exception("The input for -ll command is not correct");
		
		//because for song there are both low and high extraction feature
		boolean hlFlag= false; //no low level feature extraction for song
		boolean llFlag= true;
		String path= args[1];
		
		if(path.equals("."))
			path= System.getProperty("user.dir");
		
		System.out.println("LOW LEVEL SONG PHASE: Please wait...");
		MasterMetadata.songMetadata(path, hlFlag, llFlag);
		System.out.println("LOW LEVEL SONG PHASE COMPLETED");

	}

}
