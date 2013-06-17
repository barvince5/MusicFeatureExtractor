package clustering;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import utils.DateConverter;

import clusterArtifacts.ClusterDataType;
import clusterArtifacts.ClusterListType;
import clusterArtifacts.ClusterType;
import clusterArtifacts.ObjectFactory;

import customException.ClusterException;
import customException.DateConverterException;

public class KMeans {

	/**
	 * This class executes the K-Means algorithm to perform clustering on a set of
	 * Low Level Song Feature files starting from a directory. The measure used for 
	 * position is the Rhythm Histogram (60-dimensional array) and the measure of 
	 * distance used is the Cosine Similarity. 
	 */
	
	
	private List<Song> songList= null;
	private int clusterNumber= -1;
	private int maxIter= -1;
	private List<Cluster> clusterList= null;
	
	
	/**
	 * Constructor for the K-Means Clustering Algorithm.
	 * @param clusterNumber number of clusters. Must be at least equal to the number of songs
	 * @param maxIter maximum number of iterations, to avoid excessive looping
	 * @param path path containing the low level files (with a filename starting with "SONG_LL_"
	 * @throws ClusterException
	 */
	public KMeans(int clusterNumber, int maxIter, String path) 
		throws ClusterException {
		
		if(clusterNumber <= 0)
			throw new ClusterException("The number of clusters can't be negative!");
		
		if(maxIter < 1)
			throw new ClusterException("Must iterate at least once!");
		
		if(path == null || path.equals(""))
			throw new ClusterException("The pathname can't be null or empty");
			
		this.clusterNumber= clusterNumber;
		this.maxIter= maxIter;
		this.songList= new ArrayList<Song>();
		
		// loads the songs list and returns the number of dimensions
		int dimensions= this.loadSongs(path);
		
		if (this.songList.size() < this.clusterNumber)
			throw new ClusterException("The number of files can't be less than the number of clusters!");
		
		this.clusterList= new ArrayList<Cluster>();
		
		for(int i= 0; i< this.clusterNumber; ++i) {
			Cluster c= new Cluster(i, dimensions);
			this.clusterList.add(c);
		}
		
		
		this.init();
	
	}
	
	/**
	 * Initializes the algorithm. With k clusters, the first k songs are each assigned to
	 * a different cluster, whereas the eventual remaining ones are assigned to the cluster
	 * they are the closest to.  
	 * @throws ClusterException
	 */
	private void init() 
			throws ClusterException {
		
		int i= 0;
		for(i= 0; i< this.clusterNumber; ++i) {
			Song s= this.songList.get(i);
			Cluster c= this.clusterList.get(i);
			c.assignSong(s);
			c.resetCentroid();
		}
		
		for(; i<this.songList.size(); ++i) {
			Song s= this.songList.get(i);
			int clust= this.findNearestCluster(s.getPosition()).getCluster();
			Cluster c= this.clusterList.get(clust);
			c.assignSong(s);
		}	
		
	}

	/**
	 * Executes the algorithm for the parameters specified in the constructor.
	 * @throws ClusterException
	 */
	public void start() 
			throws ClusterException {
		
		boolean bestResult= false;
		for(int i= 0; i< maxIter && bestResult == false ; ++i) {
			
			// at the start of each iteration, recalculate each centroid
			Iterator<Cluster> iterC= this.clusterList.iterator();
			while(iterC.hasNext())
				iterC.next().resetCentroid();
			
			boolean moved= false;
			for(int currCluster=0; currCluster< this.clusterNumber; ++currCluster) {
						
				// for each cluster, check if a song is closer to the centroid of another cluster than to its own
				Cluster thisCluster= this.clusterList.get(currCluster);
				boolean last= false;
				
				// to return exactly k clusters, don't risk leaving any cluster empty by skipping clusters with one song in them; 
				if(thisCluster.getSongCount() == 1) 
					last= true;
					
				while(last == false) {
						
					// until it's possible, loop through all songs to find the worst fit for this cluster 
					ArrayList<Song> songs= (ArrayList<Song>) thisCluster.getSongListCopy();
					DistanceResult bestMove= new DistanceResult();
					Iterator<Song> iterS= songs.iterator();
					while(iterS.hasNext() && last == false) {
							
						Song currSong= iterS.next();
						DistanceResult thisMove= this.findNearestCluster(currSong.getPosition());
						if(thisMove.getCluster() != currCluster) {
								
							// if it's the first cycle or the similarity w.r.t. another cluster is better than 
							// the temporary best, this is the new best
							if (bestMove.getSimilarity() == -2.0 || thisMove.getSimilarity() > bestMove.getSimilarity())
			
								bestMove= thisMove;
								bestMove.setName(currSong.getPath());
							}
						}
					
					// if no move can be made at the end of the while, exit the loop
					if(bestMove.getSimilarity() == -2.0)
						last= true;
					else {
						// if there was a good move in the song list, do it
						// the best cluster for this song isn't the current one, so it's moved to the found one
						Song s= thisCluster.removeSong(bestMove.getName());
						this.clusterList.get(bestMove.getCluster()).assignSong(s);
						moved= true;	
						
						// if after deletion there is only one file, go to the next cluster / iteration 
						if(thisCluster.getSongCount() == 1)
							last= true;	
					}
				}
			} 
			
			
			//if i didn't move any song in any cluster in this iteration, stop looping
			if(moved == false)
				bestResult= true;
			}
		
		// once the algorithm has ended, save the results.
		this.saveResults();
	}	
	
	/**
	 * Writes the results of the clustering algorithm procedure to XML.
	 * @throws ClusterException
	 */
	private void saveResults() 
			throws ClusterException {
		
		ObjectFactory obf= new ObjectFactory();
		ClusterDataType cdt= obf.createClusterDataType();
		
		//set the creation date
		try {
			cdt.setXMLFileCreation(DateConverter.CurrentDateToXMLGregorianCalendar());
		} catch (DateConverterException e) {
			throw new ClusterException(e.getMessage(), e);
		}
		
		ClusterListType clusters= obf.createClusterListType();
		Iterator<Cluster> iterC= this.clusterList.iterator();
		
		while(iterC.hasNext()) {
			Cluster c= iterC.next();
			ClusterType ct= obf.createClusterType();
			ct.setId(BigInteger.valueOf(c.getId()));
			Iterator<Song> iterS= c.getSongListCopy().iterator();
			
			while(iterS.hasNext()) {
				Song s= iterS.next();
				ct.getSongPath().add(s.getPath());
			}
			
			clusters.getCluster().add(ct);
		}
		
		cdt.setClusterList(clusters);
		
		File output= null;
		
		try {
			//marshall this JaxbElement
			JAXBContext jc= JAXBContext.newInstance("clusterArtifacts");
			JAXBElement<ClusterDataType> je= obf.createClusterData(cdt);
			Marshaller m= jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			SchemaFactory sf= SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
			Schema schema= sf.newSchema(new File("MetadataSchema/cluster.xsd"));
			m.setSchema(schema);
			m.setEventHandler(new ValidationEventHandler() {
				
				@Override
				public boolean handleEvent(ValidationEvent event) {
					return false;
				}
			});
			
			output= new File("KMEANS "+new Date().toString()+".xml");
			m.marshal(je, output);
	
		} catch (JAXBException e) {
			if(output != null)
				output.delete();
			throw new ClusterException("JAXBException "+e.getMessage(), e);
		} catch (SAXException e) {
			throw new ClusterException("SAXException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new ClusterException("Exception "+e.getMessage(), e);
		}
		
	}
	
	
	/**
	 * Gets a list of files recursively, starting from a directory.
	 * @param dir the directory to search
	 * @param files the list of files found so far
	 * @return the list of files in this directory
	 * @throws ClusterException
	 */
	private final static List<File> getFiles(File dir, List<File> files) 
			throws ClusterException {
		
		if(dir == null)
			throw new ClusterException("The directory is null");
		
	    if(files == null)
	        files = new ArrayList<File>();
	    
	    if(dir.isDirectory() == false) {
	    	if(dir.getName().startsWith("SONG_LL_")) 
	    		files.add(dir);
	    	return files;
	    }
		
	    for (File file : dir.listFiles())
	    	KMeans.getFiles(file, files); //recursive approach.
	    
		return files;
	}
	
	/**
	 * Loads the list of Songs starting from a directory.
	 * @param dir the directory containing the low level feature files
	 * @return number of dimensions for the files loaded (RhythmHistogram defaults to 60)
	 * @throws ClusterException
	 */
	private int loadSongs(String dir) 
		throws ClusterException {
		
		try {
			File f= new File(dir);
			return this.loadSongs(f);
		} catch (NullPointerException e) {
			throw new ClusterException(e.getMessage(), e);
		}
	}
	
	/**
	 * Loads the list of Songs starting from a directory.
	 * @param dir the directory containing the low level feature files
	 * @return number of dimensions for the files loaded (RhythmHistogram defaults to 60)
	 * @throws ClusterException
	 */
	private int loadSongs(File dir) 
			throws ClusterException {
		
		int dimensions= -1;
		List<File> fileList= KMeans.getFiles(dir, new ArrayList<File>());
		Iterator<File> iter= fileList.iterator();
		while(iter.hasNext()) {
			Song newSong= new Song(iter.next());
			
			// check the number of dimensions, if it is not the same for each song, throw an exception.
			if(dimensions == -1)
				dimensions= newSong.getDimensions();
			else 
				if(dimensions != newSong.getDimensions())
					throw new ClusterException("All songs must have the same number dimensions");
			this.songList.add(newSong);
		}
		
		return dimensions;
	}
	
	/**
	 * Finds the identifier or the cluster whose centroid is closest to the current position,
	 * and how similar it is to the currently chosen element.
	 * @param position the current position
	 * @return closest cluster identifier and distance from it
	 * @throws ClusterException
	 */
	private DistanceResult findNearestCluster(double[] position) 
			throws ClusterException {

		DistanceResult res= new DistanceResult();
		for(int i= 0; i< this.clusterNumber; ++i) {
			
			double[] centroid= this.clusterList.get(i).getCentroid();
			double similarity= this.cosineSimilarity(position, centroid);

			if(res.getSimilarity() == -2.0 || similarity > res.getSimilarity()) {
				res.setSimilarity(similarity);
				res.setCluster(i);
			}
		}
		
		if (res.getCluster() == -1)
			throw new ClusterException("Can't assign to any cluster");
	
		return res;
	}

	/**
	 * Computes a Dot Product for two position arrays a and b.
	 * @param a
	 * @param b
	 * @return dot product
	 * @throws ClusterException
	 */
	private double dotProduct(double[] a, double[] b) 
		throws ClusterException {
		
		int size = a.length;
		if(size != b.length)
			throw new ClusterException("Can't compute dot product on different size matrices!");
			
		double result= 0;
		for (int i=0; i<size; ++i)
			result += (a[i]*b[i]);
		
		return result;
	}

	/**
	 * Computes the Norm for an array.
	 * @param a array
	 * @return norm
	 */
	private double getNorm(double[] a) {
		
		int size = a.length;
		
		double result= 0;
		for (int i=0; i<size; ++i)
			result += (a[i]*a[i]);
		
		return Math.sqrt(result);
	
	}
	
	/**
	 * Computes the Cosine Similarity between two arrays a and b.
	 * @param a
	 * @param b
	 * @return cosine similarity
	 * @throws ClusterException
	 */
	private double cosineSimilarity(double[] a, double[] b) 
			throws ClusterException {
		
		double dot= this.dotProduct(a, b);
		double normProduct= this.getNorm(a) * this.getNorm(b);
		if (normProduct == 0)
			throw new ClusterException("Can't compute cosine similarity on arrays with all zeros!");
		return dot / normProduct;
	  }
}
