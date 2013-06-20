package command.featureExtraction;

import clustering.KMeans;
import command.Command;

public final class KmeansCommand implements Command {

	private String[] args;
	
	public KmeansCommand(String[] args) {
		this.args= args;
	}
	
	@Override
	public void start() 
			throws Exception {
		
		int inputNumberValues= args.length - 1; //-1 for command word
		if(inputNumberValues != 3)
			throw new Exception("Incorrect input for kmeans");
		
		int clusterNumber= Integer.valueOf(this.args[1]);
		int maxIter= Integer.valueOf(this.args[2]);
		String dir= this.args[3];
		
		KMeans kmeans= new KMeans(clusterNumber, maxIter, dir);
		kmeans.execute();
	}

}
