package mp3;

import songArtifacts.highLevel.ArtistType;
import songArtifacts.highLevel.ObjectFactory;
import songArtifacts.highLevel.SongMetadataType;

import java.io.File;
import java.math.BigInteger;


import customException.MP3Exception;
import entagged.audioformats.AudioFile;

public final class HighLevelFeature extends MP3Info {

	private SongMetadataType smt= null;
	private ObjectFactory obf;
	
	public HighLevelFeature(File song) 
			throws MP3Exception {
		
		super(song);
		this.obf= new ObjectFactory();
		
	}

	public final void start() 
			throws MP3Exception {
		
		this.setAudioFile();
	}
	
	public final void stop() {
		
	}
	
	private final void setAudioFile() 
			throws MP3Exception {
		
		try { 
			
			AudioFile audioFile= super.getAudioFile();
			ArtistType artist= this.obf.createArtistType();
			artist.setName(audioFile.getName());
			
			//TODO
			
			//file name can be different of the title. 
			this.smt.setArtist(artist);
			//KB/s
			this.smt.setBitrate(BigInteger.valueOf(audioFile.getBitrate()));
			//1 mono, 2 stereo
			this.smt.setChannelsNum(BigInteger.valueOf(audioFile.getChannelNumber()));
			this.smt.setEncoding(audioFile.getEncodingType());
			//freq. in hz
			this.smt.setFrequency(BigInteger.valueOf(audioFile.getSamplingRate()));
			// seconds
			this.smt.setLength(BigInteger.valueOf(audioFile.getLength())); 
			
		} catch (Exception e) {
			throw new MP3Exception("Exception during setAudioFile");
		}
	}

}
