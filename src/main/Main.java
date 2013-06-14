package main;


import java.io.File;
import java.util.LinkedList;
import java.util.List;

import customException.DataModelException;
import customException.MasterException;
import feature.lowLevel.DataModel;

public class Main {

	
	
	private final static List<File> getFiles(File dir, List<File> files) 
			throws MasterException {
		
		if(dir == null)
			throw new MasterException("The directory is null");
		
	    if (files == null)
	        files = new LinkedList<File>();
	    
	    if(dir.isDirectory() == false) {
	    	if(dir.getName().endsWith(".mp3")) 
	    		files.add(dir);
	    	return files;
	    }
		
	    for (File file : dir.listFiles()) //filter useful to get only files that ends with .mp3
	    	Main.getFiles(file, files); //recursive approach.
	    
		return files;
	}

	public static void main(String[] args) 
			throws MasterException {
		
//		try {
			List<File> l= getFiles(new File("/home/sniper/Desktop/music/"), null);	
//			System.err.println("ARTISTS PHASE: Please wait...");
//			MasterMetadata.artistMetadata("/home/sniper/Desktop/music/");
//		
//			System.err.println("ALBUMS PHASE: Please wait...");
//			MasterMetadata.albumMetadata("/home/sniper/Desktop/music/");
//			
//			System.err.println("SONGS PHASE: Please wait...");
//			MasterMetadata.songMetadata("/home/sniper/Desktop/music/");
			
			int cnt= 0;
			for(File f : l) {
				try {
					DataModel dm= new DataModel();
					dm.setFile(f);
					dm.extract();
					++cnt;
					System.out.println(f.getName() + " " + cnt);
				} catch( Exception e) {
					System.err.println(e.getMessage());
				}
			}
			
			
//		} catch (MasterException e) {
//			System.err.println(e.getMessage());
//			MasterMetadata.shutDownMFE();
//			System.err.println("MFE is shutting down");
//		} catch (DataModelException e) {
//			System.err.println(e.getMessage());
//			MasterMetadata.shutDownMFE();
//			System.err.println("MFE is shutting down");
//		}
	}
}
