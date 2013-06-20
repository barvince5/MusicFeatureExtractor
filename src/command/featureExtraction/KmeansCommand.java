package command.featureExtraction;

import clustering.KMeans;
import command.Command;
import command.CommandParameter;

public final class KmeansCommand implements Command {

	private CommandParameter par;
	
	public KmeansCommand(CommandParameter par) {
		
		if(par == null)
			throw new NullPointerException("KmeansCommand has a null CommandParameter");
		
		this.par= par;
	}
	
	@Override
	public void start() 
			throws Exception {
		
		String[] args= this.par.getArgs();
		int inputNumberValues= args.length - 1; //-1 for command word
		if(inputNumberValues != 3)
			throw new Exception("Incorrect input for kmeans");
		
		int clusterNumber= Integer.valueOf(args[1]);
		int maxIter= Integer.valueOf(args[2]);
		String dir= args[3];
		
		KMeans kmeans= new KMeans(clusterNumber, maxIter, dir);
		kmeans.execute();
	}

}
