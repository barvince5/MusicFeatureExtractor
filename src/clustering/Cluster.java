package clustering;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class Cluster {

	/**
	 * This class contains the information about an cluster in the 
	 * K-Means clustering algorithm. It has a numerical identifier, 
	 * the number of item dimensions (60 for Rhythm Histogram), a
	 * map with the songs assigned to this cluster, and the current
	 * position of the cluster centroid, computed again each time an
	 * item is assigned or removed from the cluster.
	 */
	
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
		this.centroid= null;
		this.dimensions= 60;
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
		this.centroid= null;
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
	 * Adds a Song to the Cluster and recalculates the centroid.
	 * @return true if added correctly, false if already present.
	 */
	public boolean assignSong(Song s) {
		
		String path= s.getPath();
		if(this.songMap.containsKey(path))
			return false;
		
		this.songMap.put(path, s);
		this.resetCentroid();
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
		this.resetCentroid();
		
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
		Iterator<String> iter= this.songMap.keySet().iterator();
		while(iter.hasNext()) {
			result.add(this.songMap.get(iter.next()));
		}
		
		return result;
	}

	/**
	 * Computes the new position of the centroid, where each dimension of the 
	 * new centroid is the average of the values of that dimension for each 
	 * song in the Cluster.
	 * 
	 */
	private void resetCentroid() {
		
		int size= this.songMap.size();
		
		// if there's no songs, there can be no centroid
		if(size == 0)
			return;
		
		// if there's only one song, the centroid will be in the same position as that song
		if(size == 1) {			
			Song[] songs= this.songMap.values().toArray(new Song[size]);
			this.centroid= songs[0].getPosition();
		}
		
		double[] newCentroid= new double [this.dimensions];
		
		for(int i= 0; i< this.dimensions; ++i) 
			newCentroid[i]= 0.0;
		
		Iterator<String> iter= this.songMap.keySet().iterator();
		while(iter.hasNext()) {
			double[] currSong= this.songMap.get(iter.next()).getPosition();

			for (int i=0; i< this.dimensions; ++i) {
				newCentroid[i] += currSong[i]; 
			}
		
			for(int i= 0; i< this.dimensions; ++i)
				newCentroid[i] = newCentroid[i] / (double) this.songMap.size();
			
		}
		
		this.centroid= newCentroid;
	}
}
