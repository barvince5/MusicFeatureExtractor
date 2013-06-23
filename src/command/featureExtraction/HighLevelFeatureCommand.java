package command.featureExtraction;

import main.MasterMetadata;
import command.Command;
import command.CommandParameter;

/**
 * This class implement the command -hl (for low level feature extraction).
 */
public final class HighLevelFeatureCommand implements Command {

	private CommandParameter par;
	
	/**
	 * Standard command constructor which gets as input the command parameter.
	 * @param par contains all necessary data (user input and setup data).
	 */
	public HighLevelFeatureCommand(CommandParameter par) {
		
		if(par == null)
			throw new NullPointerException("HighLevelFeatureCommand has a null CommandParameter");
		
		this.par= par;
	}
	
	@Override
	public void start() 
			throws Exception {
		
		String[] args= this.par.getArgs();
		int inputNumberValues= args.length - 1; //-1 for command word
		if(inputNumberValues != 1)
			throw new Exception("The input for -hl command is not correct");
		
		//because for song there are both low and high extraction feature
		boolean hlFlag= true;
		boolean llFlag= false; //no low level feature extraction for song
		String path= args[1];
		
		if(path.equals("."))
			path= System.getProperty("user.dir");
		
		System.out.println("ARTISTS PHASE STARTED: Please wait...");
		MasterMetadata.artistMetadata(path);
		System.out.println("ARTISTS PHASE COMPLETED");
		
		System.out.println("ALBUMS PHASE STARTED: Please wait...");
		MasterMetadata.albumMetadata(path);
		System.out.println("ALBUMS PHASE COMPLETED");
		
		System.out.println("SONGS PHASE STARTED: Please wait...");
		MasterMetadata.songMetadata(path, hlFlag, llFlag);
		System.out.println("SONGS PHASE COMPLETED");
		
	}

}
