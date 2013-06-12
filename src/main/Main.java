package main;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import feature.highLevel.HighLevelSongFeature;


public class Main {


	public static void main(String[] args) {
		
		try {
			File dir= new File("/home/sniper/Desktop/music/");
			File[] foundFiles = dir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".mp3");
				}
			});
			
						
			System.out.println("SONGS PHASE: Please wait...");
			int cnt= 0, nTask= 10, nThread= 4;
			ExecutorService esS= Executors.newFixedThreadPool(nThread);
			ArrayBlockingQueue<HighLevelSongFeature> tasks= new ArrayBlockingQueue<HighLevelSongFeature>(nTask);
			
			for(File f : foundFiles) {
				tasks.add(new HighLevelSongFeature(f)); //create the list of tasks to do.
				System.out.println(f.getName());
				if(++cnt == nTask) {
					
					List<Future<Boolean>> res= esS.invokeAll(tasks); //submit tasks to do.
					for(int j= 0; j < nTask; ++j) //Waiting until all tasks are done
						res.get(j).get(); //Waits if necessary for the computation to complete, and then retrieves its result.
					cnt= 0; //reset the counter
					tasks.clear(); //Clean the queue
				}
			}
			
			esS.shutdownNow();
	
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
