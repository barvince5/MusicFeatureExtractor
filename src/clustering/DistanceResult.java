package clustering;

import customException.ClusterException;

/**
 * This class contains the result of the method that finds the closest
 * Cluster with respect to the current position. It contains the closest
 * cluster identifier, the cosine similarity with respect to it, and 
 * the name of the element in the current position (the song path). 
 */
public final class DistanceResult {
	
	private int cluster;
	private double similarity;
	private String name;
	
	/**
	 * The constructor initializes the name to an empty string. 
	 * The cluster id will be initialized to -1.
	 * Since (-1,1) is the acceptable range for the similarity, 
	 * it will be initialized to an impossible value, -2.
	 */
	public DistanceResult(){
		this.cluster= -1;
		this.similarity= -2.0;
		this.name= "";
	}
	
	/**
	 * Sets the cluster identifier.
	 * @param cluster cluster id
	 * @throws ClusterException 
	 */
	public final void setCluster(int cluster) 
			throws ClusterException {
		if(cluster < 0)
			throw new ClusterException("The cluster identifier cannot be negative!");
		
		this.cluster= cluster;
	}
	
	/**
	 * Gets the cluster identifier.
	 * @return cluster id
	 */
	public final int getCluster() {
		return this.cluster;
	}
	
	/**
	 * Sets the cosine similarity with respect to the best cluster centroid.
	 * @param similarity similarity w.r.t. the cluster
	 * @throws ClusterException 
	 */
	public final void setSimilarity(double similarity) 
			throws ClusterException {
		if(similarity < -1.0 || similarity > 1.0)
			throw new ClusterException("The similarity must be between -1 and 1!");
		
		this.similarity= similarity;
	}
	
	/**
	 * Gets the cosine similarity with respect to the best cluster centroid.
	 * @return similarity w.r.t. the cluster
	 */
	public final double getSimilarity() {
		return this.similarity;
	}
	
	/**
	 * Sets the name of the element in the current position.
	 * @param name element name
	 */
	public final void setName(String name) {
		this.name= name;
	}
	
	/**
	 * Gets the name of the element in the current position.
	 * @return element name
	 */
	public final String getName() {
		return this.name;
	}
}
