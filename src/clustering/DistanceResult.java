package clustering;

public class DistanceResult {

	/**
	 * This class contains the result of the method that finds the closest
	 * Cluster with respect to the current position. It contains the closest
	 * cluster identifier, the distance from it, and the name of the element
	 * in the current position (the song path). 
	 */
	
	private int cluster;
	private double distance;
	private String name;
	
	/**
	 * The constructor initializes the name to an empty string, and
	 * both the cluster name and distance to -1.
	 */
	public DistanceResult(){
		this.cluster= -1;
		this.distance= -1.0;
		this.name= "";
	}
	
	/**
	 * Sets the cluster identifier.
	 * @param cluster cluster id
	 */
	public void setCluster(int cluster) {
		this.cluster= cluster;
	}
	
	/**
	 * Gets the cluster identifier.
	 * @return cluster id
	 */
	public int getCluster() {
		return this.cluster;
	}
	
	/**
	 * Sets the distance from the best cluster centroid.
	 * @param distance distance from cluster
	 */
	public void setDistance(double distance) {
		this.distance= distance;
	}
	
	/**
	 * Gets the distance from the best cluster centroid.
	 * @return distance from cluster
	 */
	public double getDistance() {
		return this.distance;
	}
	
	/**
	 * Sets the name of the element in the current position.
	 * @param name element name
	 */
	public void setName(String name) {
		this.name= name;
	}
	
	/**
	 * Gets the name of the element in the current position.
	 * @return element name
	 */
	public String getName() {
		return this.name;
	}
}
