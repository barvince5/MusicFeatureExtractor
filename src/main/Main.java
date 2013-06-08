package main;

import java.io.File;

import customException.MP3Exception;

import mp3.ArtistFeature;

public class Main {


	public static void main(String[] args) throws MP3Exception {
		
		ArtistFeature a= new ArtistFeature(new File("/home/sniper/Desktop/music/hords.mp3"));
		a.start();
	}

}
