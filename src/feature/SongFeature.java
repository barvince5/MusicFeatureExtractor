package feature;

import java.io.File;
import java.util.concurrent.Callable;

import customException.MP3Exception;
import customException.SongFeatureException;
import feature.highLevel.HighLevelSongFeature;
import feature.lowLevel.LowLevelSongFeature;

public final class SongFeature extends MP3Info implements Callable<Boolean> {

	private File song;
	private boolean hlEvaluation, llEvaluation;
	
	public SongFeature(File song, boolean hlEvaluation, boolean llEvaluation) 
			throws MP3Exception {
		
		super(song);
		this.song= song;
		this.hlEvaluation= hlEvaluation;
		this.llEvaluation= llEvaluation;
	}

	@Override
	public Boolean call() 
			throws SongFeatureException {
		
		Boolean resH, resL;
		
		if(this.hlEvaluation)
			resH= false;
		else
			resH= true;
		
		if(this.llEvaluation)
			resL= false;
		else
			resL= true;
			
		if(this.hlEvaluation)
			resH= HighLevelSongFeature.start(this);
		
		if(this.llEvaluation)
			resL= LowLevelSongFeature.start(this, this.song);
				
		return (resH && resL);
	}

}