package mp3;

import java.io.File;

import customException.MP3Exception;

public final class LowLevelSongFeature extends MP3Info{

	public LowLevelSongFeature(File song) 
			throws MP3Exception {
		
		super(song);	
	}
	
	public final void start() {
	
	}
	
	public final void stop() {
		
	}
	
}
