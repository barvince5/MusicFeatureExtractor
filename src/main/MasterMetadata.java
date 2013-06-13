package main;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import customException.MP3Exception;
import customException.MasterException;

import feature.highLevel.AlbumFeature;
import feature.highLevel.ArtistFeature;
import feature.highLevel.HighLevelSongFeature;

public final class MasterMetadata {
	
	private final static int maxTask= 10, nThread= 4;
	private static String path= "";
	private static ExecutorService artistES= null;
	private static ExecutorService albumES= null;
	private static ExecutorService songES= null;
	private static ArrayBlockingQueue<ArtistFeature> artistTasks= null;
	private static ArrayBlockingQueue<AlbumFeature> albumTasks= null;
	private static ArrayBlockingQueue<HighLevelSongFeature> songTasks= null;
	
	/**
	 * Private constructor to avoid instantiation.
	 */
	private MasterMetadata() {
		
	}
	
	public final static void shutDownMFE() {
		
		if(MasterMetadata.artistES != null)
			MasterMetadata.artistES.shutdown();
		
		if(MasterMetadata.albumES != null)
			MasterMetadata.albumES.shutdown();
		
		if(MasterMetadata.songES != null)
			MasterMetadata.songES.shutdown();
		
	}
	
	public final static void artistMetadata(String path) 
			throws MasterException {
		
		MasterMetadata.path= path;
		MasterMetadata.artistES= Executors.newFixedThreadPool(nThread);
		MasterMetadata.artistTasks= new ArrayBlockingQueue<ArtistFeature>(MasterMetadata.maxTask);
		MasterMetadata.startArtistAnalisys(MasterMetadata.getFiles());
	}
	
	public final static void albumMetadata(String path) 
			throws MasterException {
		
		MasterMetadata.path= path;
		MasterMetadata.albumES= Executors.newFixedThreadPool(MasterMetadata.nThread);
		MasterMetadata.albumTasks= new ArrayBlockingQueue<AlbumFeature>(MasterMetadata.maxTask);
		MasterMetadata.startAlbumAnalisys(MasterMetadata.getFiles());
	}
	
	public final static void songMetadata(String path) 
			throws MasterException {
		
		MasterMetadata.path= path;
		MasterMetadata.songES= Executors.newFixedThreadPool(MasterMetadata.nThread);
		MasterMetadata.songTasks= new ArrayBlockingQueue<HighLevelSongFeature>(MasterMetadata.maxTask);
		MasterMetadata.startSongAnalisys(MasterMetadata.getFiles());
	}
	
	private final static File[] getFiles() 
			throws MasterException {
		
		if(MasterMetadata.path == null || MasterMetadata.path.equals(""))
			throw new MasterException("The given path is null");
		
		File dir= new File(MasterMetadata.path);
		if(dir.isDirectory() == false)
			throw new MasterException("The input is not a directory");
		
		File[] foundFiles = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".mp3");
			}
		});
		
		return foundFiles;
	}
	
	
	//######################## ARTIST PART
	
	private final static void submitArtistTasks() 
			 throws MasterException {
		
		int length= MasterMetadata.artistTasks.size();
		
		try {
			
			//submit tasks to do.
			List<Future<Boolean>> res= MasterMetadata.artistES.invokeAll(MasterMetadata.artistTasks); 
			
			//Waiting until all tasks are done
			for(int j= 0; j < length; ++j) 
				try {
					res.get(j).get();
				} catch (ExecutionException e) {
					//do nothing, just skip this task.
				} catch (InterruptedException e) {
					//do nothing, just skip this task.
				} catch (CancellationException e) {
					//do nothing, just skip this task.
				}
			
			//Clean the queue
			MasterMetadata.artistTasks.clear(); 
		
		} catch (Exception e) {
			throw new MasterException("Exception "+e.getMessage(), e);
		}
	}
	
	private final static void startArtistAnalisys(File[] foundFiles) 
			throws MasterException {
			
		int cnt= 0;
		try {
			
			for(File f : foundFiles) {
				MasterMetadata.artistTasks.add(new ArtistFeature(f)); //create the list of tasks to do.
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
	
	private final static void submitAlbumTasks() 
			 throws MasterException {
		
		int length= MasterMetadata.albumTasks.size();
		
		try {
			
			//submit tasks to do.
			List<Future<Boolean>> res= MasterMetadata.albumES.invokeAll(MasterMetadata.albumTasks); 
			
			//Waiting until all tasks are done
			for(int j= 0; j < length; ++j) 
				try {
					res.get(j).get();
				} catch (ExecutionException e) {
					//do nothing, just skip this task.
				} catch (InterruptedException e) {
					//do nothing, just skip this task.
				} catch (CancellationException e) {
					//do nothing, just skip this task.
				}
			
			//Clean the queue
			MasterMetadata.albumTasks.clear(); 
		
		} catch (Exception e) {
			throw new MasterException("Exception "+e.getMessage(), e);
		}
	}
	
	private final static void startAlbumAnalisys(File[] foundFiles) 
			throws MasterException {
			
		int cnt= 0;
		try {
			
			for(File f : foundFiles) {
				MasterMetadata.albumTasks.add(new AlbumFeature(f)); //create the list of tasks to do.
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
	
	private final static void submitSongTasks() 
			 throws MasterException {
		
		int length= MasterMetadata.songTasks.size();
		
		try {
			
			//submit tasks to do.
			List<Future<Boolean>> res= MasterMetadata.songES.invokeAll(MasterMetadata.songTasks); 
			
			//Waiting until all tasks are done
			for(int j= 0; j < length; ++j) {
				try {
					res.get(j).get();
				} catch (ExecutionException e) {
					//do nothing, just skip this task.
				} catch (InterruptedException e) {
					//do nothing, just skip this task.
				} catch (CancellationException e) {
					//do nothing, just skip this task.
				}
			}
			
			//Clean the queue
			MasterMetadata.songTasks.clear(); 
		
		} catch (Exception e) {
			throw new MasterException("Exception "+e.getMessage(), e);
		}
	}
	
	private final static void startSongAnalisys(File[] foundFiles) 
			throws MasterException {
			
		int cnt= 0;
		try {
			
			for(File f : foundFiles) {
				MasterMetadata.songTasks.add(new HighLevelSongFeature(f)); //create the list of tasks to do.
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
