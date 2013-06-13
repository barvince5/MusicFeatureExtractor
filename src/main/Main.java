package main;


import customException.MasterException;

public class Main {


	public static void main(String[] args) {
		
		try {
			
			System.err.println("ARTISTS PHASE: Please wait...");
			MasterMetadata.artistMetadata("/home/sniper/Desktop/music/");
		
			System.err.println("ALBUMS PHASE: Please wait...");
			MasterMetadata.albumMetadata("/home/sniper/Desktop/music/");
			
			System.err.println("SONGS PHASE: Please wait...");
			MasterMetadata.songMetadata("/home/sniper/Desktop/music/");
			
		} catch (MasterException e) {
			System.err.println(e.getMessage());
			System.err.println("MFE is shuting down");
			MasterMetadata.shutDownMFE();
		}
	}
}
