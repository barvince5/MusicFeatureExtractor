/* Copyright 2013 Antonio Collarino, Vincenzo Barone

This file is part of Music Feature Extractor (MFE).

Music Feature Extractor (MFE) is free software; you can redistribute it 
and/or modify it under the terms of the GNU Lesser General Public License 
as published by the Free Software Foundation; either version 3 of the 
License, or (at your option) any later version.

Music Feature Extractor (MFE) is distributed in the hope that it will be 
useful, but WITHOUT ANY WARRANTY; without even the implied warranty of 
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser 
General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with Music Feature Extractor (MFE).  If not, see 
http://www.gnu.org/licenses/.  */

package clustering;

import customException.ClusterException;


/**
 * This class contains the result of the method that finds the closest
 * Cluster with respect to the current position. It contains the closest
 * cluster identifier, the cosine similarity with respect to it, and 
 * the name of the element in the current position (the song path). 
 */
public class DistanceResult {
	
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
	public void setCluster(int cluster) 
			throws ClusterException {
		
		if(cluster < 0)
			throw new ClusterException("The cluster identifier cannot be negative!");
		
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
	 * Sets the cosine similarity with respect to the best cluster centroid.
	 * @param similarity similarity w.r.t. the cluster
	 * @throws ClusterException 
	 */
	public void setSimilarity(double similarity) 
			throws ClusterException {

		this.similarity= similarity;
	}
	
	/**
	 * Gets the cosine similarity with respect to the best cluster centroid.
	 * @return similarity w.r.t. the cluster
	 */
	public double getSimilarity() {
		return this.similarity;
	}
	
	/**
	 * Sets the name of the element in the current position.
	 * @param name element name
	 * @throws ClusterException 
	 */
	public void setName(String name) 
			throws ClusterException {
		
		if(name == null || name.equals(""))
			throw new ClusterException("The file name of the song cannot be null or empty string");
			
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