package main;

import java.io.File;
import java.io.FilenameFilter;

import mp3.ArtistFeature;

import customException.MP3Exception;


public class Main {


	public static void main(String[] args) throws MP3Exception {
		
		File dir= new File("/home/sniper/Desktop/music/");
		File[] foundFiles = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".mp3");
			}
		});
		
		for(File f : foundFiles) {
			ArtistFeature ar= new ArtistFeature(f);
			ar.start();
			System.out.println(f.getName());
		}
	}

}
