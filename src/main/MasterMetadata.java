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

package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import customException.MP3Exception;
import customException.MasterException;

import feature.SongFeature;
import feature.highLevel.AlbumFeature;
import feature.highLevel.ArtistFeature;

/**
 * This class is the master which commands the features extraction for high and low level.
 */
public final class MasterMetadata {
	
	private final static int maxTask= 10, nThread= 4;
	private static ExecutorService artistES= null;
	private static ExecutorService albumES= null;
	private static ExecutorService songES= null;
	private static ArrayList<ArtistFeature> artistTasks= null;
	private static ArrayList<AlbumFeature> albumTasks= null;
	private static ArrayList<SongFeature> songTasks= null;
	private static File dir= null;
	private static List<File> fileList= null;
	private static boolean initFlag= false;
	
	/**
	 * Private constructor to avoid instantiation.
	 */
	private MasterMetadata() {
		
	}
	
	/**
	 * This method allows to stop immediately the entire MFE program.
	 */
	public final static void shutDownMM() {
		
		if(MasterMetadata.artistES != null)
			MasterMetadata.artistES.shutdownNow();
		
		if(MasterMetadata.albumES != null)
			MasterMetadata.albumES.shutdownNow();
		
		if(MasterMetadata.songES != null)
			MasterMetadata.songES.shutdownNow();
		
	}
	
	/**
	 * Produce the xml metadata of each artist.
	 * @param path the starting directory where find the mp3 files.
	 * @throws MasterException in case of error.
	 */
	public final static void artistMetadata(String path) 
			throws MasterException {
		
		MasterMetadata.artistES= Executors.newFixedThreadPool(nThread);
		MasterMetadata.artistTasks= new ArrayList<ArtistFeature>(MasterMetadata.maxTask);
		MasterMetadata.init(path); //create file list, filter and dir
		MasterMetadata.startArtistAnalysis(MasterMetadata.fileList);
	}
	
	/**
	 * Produce the xml metadata of each album.
	 * @param path the starting directory where find the mp3 files.
	 * @throws MasterException in case of error.
	 */
	public final static void albumMetadata(String path) 
			throws MasterException {

		MasterMetadata.albumES= Executors.newFixedThreadPool(MasterMetadata.nThread);
		MasterMetadata.albumTasks= new ArrayList<AlbumFeature>(MasterMetadata.maxTask);
		MasterMetadata.init(path); //create file list, filter and dir
		MasterMetadata.startAlbumAnalysis(MasterMetadata.fileList);
	}
	
	/**
	 * Produce the xml metadata of each song.
	 * @param path the starting directory where find the mp3 files.
	 * @throws MasterException in case of error.
	 */
	public final static void songMetadata(String path, boolean hlEvaluation, boolean llEvaluation) 
			throws MasterException {
		
		if(hlEvaluation == false && llEvaluation == false)
			return;
		
		MasterMetadata.songES= Executors.newFixedThreadPool(MasterMetadata.nThread);
		MasterMetadata.songTasks= new ArrayList<SongFeature>(MasterMetadata.maxTask);
		MasterMetadata.init(path); //create file list, filter and dir
		MasterMetadata.startSongAnalysis(MasterMetadata.fileList, hlEvaluation, llEvaluation);
	}
	
	/**
	 * Create file list, filter and dir. Only if it is not done yet.
	 * @param path 
	 * @throws MasterException in case the path is not correct
	 */
	private final static void init(String path) 
			throws MasterException {
		
		if(path == null || path.equals(""))
			throw new MasterException("The path is not null or empty string");
		
		if(MasterMetadata.initFlag) //If it is already initialized.
			return;
		
		MasterMetadata.dir= new File(path);
		MasterMetadata.fileList= MasterMetadata.getFiles(MasterMetadata.dir, new LinkedList<File>());
		MasterMetadata.initFlag= true;
	}

    
	/**
	 * This method gets all mp3 files.
	 * @return a list of files
	 * @throws MasterException in case of error.
	 */
	private final static List<File> getFiles(File dir, List<File> files) 
			throws MasterException {
		
		if(dir == null)
			throw new MasterException("The directory is null");
		
	    if (files == null)
	        files = new LinkedList<File>();
	    
	    if(dir.isDirectory() == false) {
	    	if(dir.getName().toLowerCase().endsWith(".mp3")) 
	    		files.add(dir);
	    	return files;
	    }
		
	    for (File file : dir.listFiles()) //filter useful to get only files that ends with .mp3
	    	MasterMetadata.getFiles(file, files); //recursive approach.
	    
		return files;
	}
	
	
	//######################## ARTIST PART
	
	/**
	 * This method submit some tasks to a thread pool.
	 * @throws MasterException in case of error.
	 */
	private final static void submitArtistTasks() 
			 throws MasterException {

		try {
			
			//submit tasks to do.
			//Waiting until all tasks are done
			MasterMetadata.artistES.invokeAll(MasterMetadata.artistTasks); 
			
			//Clean the queue
			MasterMetadata.artistTasks.clear(); 
		
		} catch (Exception e) {
			throw new MasterException("Exception "+e.getMessage(), e);
		}
	}
	
	/**
	 * This method fill the queue with tasks to do, and start the artist analysis.
	 * @param foundFiles
	 * @throws MasterException
	 */
	private final static void startArtistAnalysis(List<File> foundFiles) 
			throws MasterException {
			
		int cnt= 0;
		try {
			
			Iterator<File> iter= foundFiles.iterator();
			while(iter.hasNext()) {
				//create the list of tasks to do.
				MasterMetadata.artistTasks.add(new ArtistFeature(iter.next()));
				if(++cnt == maxTask) {
					MasterMetadata.submitArtistTasks();
					cnt= 0; //reset the counter
				}
			}
			
			if(MasterMetadata.artistTasks.isEmpty() == false) {
				MasterMetadata.submitArtistTasks();
			}

			if(MasterMetadata.artistES != null) {
				MasterMetadata.artistES.shutdownNow(); //Stop all thread at the end.
				MasterMetadata.artistES= null;
			}
			
		} catch (MP3Exception e) {
			throw new MasterException("MP3Exception "+e.getMessage(), e);
		} catch (MasterException e) {
			throw new MasterException("MasterException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new MasterException("Exception "+e.getMessage(), e);
		}
	}
	
	
	//######################## ALBUM PART
	
	/**
	 * This method submit some tasks to a thread pool.
	 * @throws MasterException in case of error.
	 */
	private final static void submitAlbumTasks() 
			 throws MasterException {
			
		try {
			
			//submit tasks to do.
			//Waiting until all tasks are done
			MasterMetadata.albumES.invokeAll(MasterMetadata.albumTasks); 

			//Clean the queue
			MasterMetadata.albumTasks.clear(); 
		
		} catch (Exception e) {
			throw new MasterException("Exception "+e.getMessage(), e);
		}
	}
	
	/**
	 * This method fill the queue with tasks to do, and start the album analysis.
	 * @param foundFiles
	 * @throws MasterException
	 */
	private final static void startAlbumAnalysis(List<File> foundFiles) 
			throws MasterException {
			
		int cnt= 0;
		try {
			
			Iterator<File> iter= foundFiles.iterator();
			while(iter.hasNext()) {
				//create the list of tasks to do.
				MasterMetadata.albumTasks.add(new AlbumFeature(iter.next())); 
				if(++cnt == maxTask) {
					MasterMetadata.submitAlbumTasks();
					cnt= 0; //reset the counter
				}
			}
			
			if(MasterMetadata.albumTasks.isEmpty() == false) {
				MasterMetadata.submitAlbumTasks();
			}

			if(MasterMetadata.albumES != null) {
				MasterMetadata.albumES.shutdownNow(); //Stop all thread at the end.
				MasterMetadata.albumES= null;
			}
			
		} catch (MP3Exception e) {
			throw new MasterException("MP3Exception "+e.getMessage(), e);
		} catch (MasterException e) {
			throw new MasterException("MasterException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new MasterException("Exception "+e.getMessage(), e);
		}
	}
	
	
	//######################## SONG PART
	
	/**
	 * This method submit some tasks to a thread pool.
	 * @throws MasterException in case of error.
	 */
	private final static void submitSongTasks() 
			 throws MasterException {
		
		try {
			
			//submit tasks to do.
			//Waiting until all tasks are done
			MasterMetadata.songES.invokeAll(MasterMetadata.songTasks); 
			
			//Clean the queue
			MasterMetadata.songTasks.clear(); 
		
		} catch (Exception e) {
			throw new MasterException("Exception "+e.getMessage(), e);
		}
	}
	
	/**
	 * This method fill the queue with tasks to do, and start the song analysis.
	 * @param foundFiles
	 * @throws MasterException
	 */
	private final static void startSongAnalysis(List<File> foundFiles, boolean hlEvaluation, boolean llEvaluation) 
			throws MasterException {
			
		int cnt= 0;
		try {
			
			Iterator<File> iter= foundFiles.iterator();
			while(iter.hasNext()) {
				//create the list of tasks to do.
				MasterMetadata.songTasks.add(new SongFeature(iter.next(), hlEvaluation, llEvaluation)); 
				if(++cnt == maxTask) {
					MasterMetadata.submitSongTasks();
					cnt= 0; //reset the counter
				}
			}
			
			if(MasterMetadata.songTasks.isEmpty() == false) {
				MasterMetadata.submitSongTasks();
			}

			if(MasterMetadata.songES != null) {
				MasterMetadata.songES.shutdownNow(); //Stop all thread at the end.
				MasterMetadata.songES= null;
			}
			
		} catch (MP3Exception e) {
			throw new MasterException("MP3Exception "+e.getMessage(), e);
		} catch (MasterException e) {
			throw new MasterException("MasterException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new MasterException("Exception "+e.getMessage(), e);
		}
	}
}
