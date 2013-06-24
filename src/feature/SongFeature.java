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

package feature;

import java.io.File;
import java.util.concurrent.Callable;

import customException.MP3Exception;
import customException.SongFeatureException;
import feature.highLevel.HighLevelSongFeature;
import feature.lowLevel.LowLevelSongFeature;

/**
 * This class is used to manage the extraction of both high and low level information about songs. 
 */
public final class SongFeature extends MP3Info implements Callable<Boolean> {

	private File song;
	private boolean hlEvaluation, llEvaluation;
	
	/**
	 * This is the constructor which need the audio song file and same flags as input.
	 * @param song the .mp3 file to analyze.
	 * @param hlEvaluation if true high level feature will be extracted.
	 * @param llEvaluation if true low level feature will be extracted.
	 * @throws MP3Exception in case of error.
	 */
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
