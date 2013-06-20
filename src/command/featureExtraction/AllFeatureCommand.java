package command.featureExtraction;

import main.MasterMetadata;
import command.Command;
import command.CommandParameter;

/**
 * This class implement the command -all (for low level feature extraction).
 */
public final class AllFeatureCommand implements Command {
	
	private CommandParameter par;
	
	public AllFeatureCommand(CommandParameter par) {
		
		if(par == null)
			throw new NullPointerException("AllFeatureCommand has a null CommandParameter");
		
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
		boolean hlFlag= true;
		boolean llFlag= true; 
		String path= args[1];
		
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
