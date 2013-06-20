package command.featureExtraction;

import main.MasterMetadata;
import command.Command;

/**
 * This class implement the command -ll (for low level feature extraction).
 */
public final class LowLevelFeatureCommand implements Command {

	private String[] args;
	
	public LowLevelFeatureCommand(String[] args) {
		this.args= args;
	}
	
	@Override
	public void start() 
			throws Exception {
		
		int inputNumberValues= args.length - 1; //-1 for command word
		if(inputNumberValues != 1)
			throw new Exception("The input for -ll command is not correct");
		
		//because for song there are both low and high extraction feature
		boolean hlFlag= false; //no low level feature extraction for song
		boolean llFlag= true;
		String path= this.args[1];
		
		if(path.equals("."))
			path= System.getProperty("user.dir");
		
		System.out.println("LOW LEVEL SONG PHASE: Please wait...");
		MasterMetadata.songMetadata(path, hlFlag, llFlag);
		System.out.println("LOW LEVEL SONG PHASE COMPLETED");

	}

}
