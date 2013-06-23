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

import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the information about an cluster in the 
 * K-Means clustering algorithm. It has a numerical identifier, 
 * the number of item dimensions (60 for Rhythm Histogram), a
 * map with the songs assigned to this cluster, and the current
 * position of the cluster centroid, computed again each time an
 * item is assigned or removed from the cluster.
 */
public class Cluster {

	private int id;
	private int dimensions;
	private HashMap<String, Song> songMap;
	private double[] centroid;
	
	/**
	 * Creates a new empty Cluster.
	 * @param id the unique identifier of the Cluster
	 */
	public Cluster(int id) {
		this.id= id;
		this.songMap= new HashMap<String, Song>();
		this.dimensions= 60;
		this.centroid= new double[this.dimensions];
	}

	/**
	 * Creates a new empty Cluster.
	 * @param id the unique identifier of the Cluster
	 * @param dimensions the position dimension number for the data 
	 * (RhythmHistogram defaults to 60)
	 */
	public Cluster(int id, int dimensions) {
		this.id= id;
		this.dimensions= dimensions;
		this.songMap= new HashMap<String, Song>();
		this.centroid= new double[this.dimensions];
	}
	
	/**
	 * Sets the number of dimensions of the elements in the itemset 
	 * (The Rhythm Histogram defaults to 60).
	 * @param dim the number of dimensions
	 */
	public void setDimensions(int dim) {
		this.dimensions= dim;
	}
	
	/**
	 * Gets the number of dimensions.
	 * @return number of dimensions
	 */
	public int getDimensions() {
		return this.dimensions;
	}
	
	/**
	 * Gets the identifier of the current Cluster.
	 * @return cluster identifier
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Adds a Song to the Cluster.
	 * @return true if added correctly, false if already present.
	 */
	public boolean assignSong(Song s) {
		
		String path= s.getPath();
		if(this.songMap.get(path) != null)
			return false;
		
		this.songMap.put(path, s);
		return true;
	}
	
	/**
	 * Removes a Song from the Cluster and recalculates the centroid.
	 * @return the song if it was present, null otherwise
	 */
	public Song removeSong(String path) {
		
		if(path == null || path.equals(""))
			return null;
		
		Song result= this.songMap.remove(path);
		
		return result;
	}
	
	/**
	 * Gets the current position of the Cluster centroid
	 * @return position of the centroid
	 */
	public double[] getCentroid() {
		return this.centroid;
	}
	
	/**
	 * Gets the number of songs in the cluster
	 * @return song count
	 */
	public int getSongCount() {
		return this.songMap.size();
	}
	
	/**
	 * Returns a copy of the list of songs in the Cluster.
	 * @return copy of song List
	 */
	public List<Song> getSongListCopy() {
		
		List<Song> result= new ArrayList<Song>();
		result.addAll(this.songMap.values());
		return result;
	}

	/**
	 * Computes the new position of the centroid, where each dimension of the 
	 * new centroid is the average of the values of that dimension for each 
	 * song in the Cluster.
	 * 
	 */
	public void resetCentroid() {
		
		int size= this.songMap.size();
		
		// if there are no songs, there can be no centroid
		if(size == 0)
			return;
		
		// if there's only one song, the centroid will be in the same position as that song
		if(size == 1) {			
			Iterator<Song> iterm= this.songMap.values().iterator();
			this.centroid= iterm.next().getPosition();
			return;
		}
		
		for(int i= 0; i< this.dimensions; ++i) 
			this.centroid[i]= 0.0;
		
		Iterator<Song> iter= this.songMap.values().iterator();
		while(iter.hasNext()) {
			double[] currSong= iter.next().getPosition();
			for (int i= 0; i< this.dimensions; ++i) {
				this.centroid[i] += currSong[i]; 
			}	
		}

		for(int i= 0; i< this.dimensions; ++i)
			this.centroid[i] = this.centroid[i] / (double) this.songMap.size();

		return;
	}
}