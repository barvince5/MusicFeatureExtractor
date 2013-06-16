package clustering;

import java.io.File;

import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
import customException.ClusterException;

import utils.LoadRhythmHistogram;

public class Song {
	
	/**
	 * This class contains the information about an item in the considered set (song)
	 * for the K-Means clustering algorithm. It contains the position, the cluster it 
	 * has been assigned to, and the filename path as identifier. 
	 */
	
	String path;
	int cluster;
	double[] position;
	
	/**
	 * Song constructor from a Low Level Feature file. The filename is set as
	 * the identifier for this song, and the Rhythm Histogram extracted from
	 * the file is set as Song Position. 
	 * @param featureFile file containing the Rhythm Histogram
	 * @throws ClusterException
	 */
	public Song(File featureFile) 
			throws ClusterException {
		
		this.path= featureFile.getAbsolutePath();
		try {
			this.position= LoadRhythmHistogram.getValues(featureFile);
		} catch (JAXBException e) {
			throw new ClusterException(e.getMessage(), e);
		} catch (SAXException e) {
			throw new ClusterException(e.getMessage(), e);
		}
	}
	
	/**
	 * Gets the current song position (Rhythm Histogram).
	 * @return song position
	 */
	public double[] getPosition() {
		return this.position;
	}
	
	/**
	 * Gets the current song path (used as identifier).
	 * @return song path
	 */
	public String getPath() {
		return this.path;
	}
	
	/**
	 * Sets the cluster this song has been assigned to.
	 * @param clusterID
	 */
	public void setCluster(int clusterID) {
		this.cluster= clusterID;
	}
	
	/**
	 * Gets the ID of the Cluster this song has assigned to.
	 * @return cluster ID
	 */
	public int getCluster() {
		return this.cluster;
	}
	
	/**
	 * Gets the number of dimensions for this position. (Rhythm Histogram 
	 * defaults to 60) 
	 * @return number of dimensions
	 */
	public int getDimensions() {
		return this.position.length;
	}

}
