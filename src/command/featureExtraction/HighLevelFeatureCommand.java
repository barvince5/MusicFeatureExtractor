package command.featureExtraction;

import main.MasterMetadata;
import command.Command;

/**
 * This class implement the command -hl (for low level feature extraction).
 */
public final class HighLevelFeatureCommand implements Command {

	private String[] args;
	
	public HighLevelFeatureCommand(String[] args) {
		this.args= args;
	}
	
	@Override
	public void start() 
			throws Exception {
		
		int inputNumberValues= args.length - 1; //-1 for command word
		if(inputNumberValues != 1)
			throw new Exception("The input for -ll command is not correct");
		
		//because for song there are both low and high extraction feature
		boolean hlFlag= true;
		boolean llFlag= false; //no low level feature extraction for song
		String path= this.args[1];
		
		if(path.equals("."))
			path= System.getProperty("user.dir");
		
		System.out.println("ARTISTS PHASE: Please wait...");
		MasterMetadata.artistMetadata(path, hlFlag);
		
		System.out.println("ALBUMS PHASE: Please wait...");
		MasterMetadata.albumMetadata(path, hlFlag);
		
		System.out.println("SONGS PHASE: Please wait...");
		MasterMetadata.songMetadata(path, hlFlag, llFlag);
		
	}

}
