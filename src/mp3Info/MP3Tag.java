package mp3Info;

import java.io.File;
import java.util.Map;

import customException.MP3Exception;

/**
 * This class gets information from mp3 tags.<br>
 * Note: Some parameters might be unavailable.
 * @author Antonio Collarino
 *
 */
public final class MP3Tag {
	
	private Map<String, Object> properties= null;
	
	/**
	 * This is the constructor.
	 * @param audioFile .mp3 audio file.
	 * @throws MP3Exception in case of error.
	 */
	public MP3Tag(File audioFile) 
			throws MP3Exception {
		
		this.properties= MP3MetaData.getMetaDateMP3(audioFile);
	}
	
	/**
	 * This method returns the entire map of tags, You can use the following key-tag.<br>
	 * duration [Long], duration in microseconds.<br>
	 * title [String], Title of the stream.<br>
	 * author [String], Name of the artist of the stream.<br>
	 * album [String], Name of the album of the stream.<br>
	 * date [String], The date (year) of the recording or release of the stream.<br>
	 * copyright [String], Copyright message of the stream.<br>
	 * comment [String], Comment of the stream.<br>
	 * MP3 parameters.<br>
	 * mp3.version.mpeg [String], mpeg version : 1,2 or 2.5<br>
	 * mp3.version.layer [String], layer version 1, 2 or 3<br>
	 * mp3.version.encoding [String], mpeg encoding : MPEG1, MPEG2-LSF, MPEG2.5-LSF<br>
	 * mp3.channels [Integer], number of channels 1 : mono, 2 : stereo.<br>
	 * mp3.frequency.hz [Integer], sampling rate in hz.<br>
	 * mp3.bitrate.nominal.bps [Integer], nominal bitrate in bps.<br>
	 * mp3.length.bytes [Integer], length in bytes.<br>
	 * mp3.length.frames [Integer], length in frames.<br>
	 * mp3.framesize.bytes [Integer], framesize of the first frame. framesize is not constant for VBR streams.<br>
	 * mp3.framerate.fps [Float], framerate in frames per seconds.<br>
	 * mp3.header.pos [Integer], position of first audio header (or ID3v2 size).<br>
	 * mp3.vbr [Boolean], vbr flag.<br>
	 * mp3.vbr.scale [Integer], vbr scale.<br>
	 * mp3.crc [Boolean], crc flag.<br>
	 * mp3.original [Boolean], original flag.<br>
	 * mp3.copyright [Boolean], copyright flag.<br>
	 * mp3.padding [Boolean], padding flag.<br>
	 * mp3.mode [Integer], mode 0:STEREO 1:JOINT_STEREO 2:DUAL_CHANNEL 3:SINGLE_CHANNEL<br>
	 * mp3.id3tag.genre [String], ID3 tag (v1 or v2) genre.<br>
	 * mp3.id3tag.track [String], ID3 tag (v1 or v2) track info.<br>
	 * mp3.id3tag.encoded [String], ID3 tag v2 encoded by info.<br>
	 * mp3.id3tag.composer [String], ID3 tag v2 composer info.<br>
	 * mp3.id3tag.grouping [String], ID3 tag v2 grouping info.<br>
	 * mp3.id3tag.disc [String], ID3 tag v2 track info.<br>
	 * mp3.id3tag.publisher [String], ID3 tag v2 publisher info.<br>
	 * mp3.id3tag.orchestra [String], ID3 tag v2 orchestra info.<br>
	 * mp3.id3tag.length [String], ID3 tag v2 file length in seconds.<br>
	 * mp3.id3tag.v2 [InputStream], ID3v2 frames.<br>
	 * mp3.id3tag.v2.version [String], ID3v2 major version (2=v2.2.0, 3=v2.3.0, 4=v2.4.0).<br>
	 * mp3.shoutcast.metadata.key [String], Shoutcast meta key with matching value. <br>
	 * @return all tags
	 */
	public final Map<String, Object> getTags() {
		return this.properties;
	}
	
	/**
	 * Gets name of the artist from ID3v.
	 * @return name
	 */
	public final String getArtist() {
		return (String) this.properties.get("author");
	}
	
	/**
	 * Gets title of song from ID3v.
	 * @return title
	 */
	public final String getTitle() {
		return (String) this.properties.get("title");
	}
	
	/**
	 * Gets the album name of the song from ID3v.
	 * @return album
	 */
	public final String getAlbum() {
		return (String) this.properties.get("album");
	}
	
	/**
	 * Gets the mpeg version from the ID3v.
	 * @return mpeg version
	 */
	public final String getMpegVersion() {
		return (String) this.properties.get("mp3.version.mpeg");
	}
	
	/**
	 * Gets the layer version 1, 2 or 3 from ID3v.
	 * @return version layer
	 */
	public final String getVersionLayer() {
		return (String) this.properties.get("mp3.version.layer");
	}
	
	/**
	 * Gets mpeg encoding : MPEG1, MPEG2-LSF, MPEG2.5-LSF from ID3v.
	 * @return the encoding.
	 */
	public final String getEncodingVersion() {
		return (String) this.properties.get("mp3.version.encoding");
	}
	
	/**
	 * Gets number of channels 1: mono, 2: stereo from ID3v.
	 * @return channels number
	 */
	public final Integer getChannels() {
		return (Integer) this.properties.get("mp3.channels");
	}
	
	/**
	 * Gets sampling rate in [hz] from ID3v.
	 * @return frequency
	 */
	public final Integer getFrequency() {
		return (Integer) this.properties.get("mp3.frequency.hz");
	}
	
	/**
	 * Gets nominal bitrate in [bps]from ID3v.
	 * @return nominal bitrate
	 */
	public final Integer getNominalBitrate() {
		return (Integer) this.properties.get("mp3.bitrate.nominal.bps");
	}
	
	/**
	 * Gets ength in bytes from ID3v.
	 * @return length in bytes.
	 */
	public final Integer getLegth() {
		return (Integer) this.properties.get("mp3.length.bytes");
	}
	
	/**
	 * Gets length in frames from ID3v.
	 * @return
	 */
	public final Integer getFrameSize() {
		return (Integer) this.properties.get("mp3.framesize.bytes");
	}
	
	/**
	 * Gets the mode: Stereo, Joint stereo, dual channel, single channel from ID3v.
	 * @return the mode
	 */
	public final String getMode() {
		
		String mode= "UNKNOWN";
		Integer res= (Integer) this.properties.get("mp3.mode");
		
		if(res == null)
			return mode;
		int val= res.intValue();
		if(val == 0)
			mode= "STEREO";
		if(val == 1)
			mode= "JOINT_STEREO";
		if(val == 2)
			mode= "DUAL_CHANNEL";
		if(val == 3)
			mode= "SINGLE_CHANNEL";
		return mode;
	}
	
	/**
	 * Gets encoded informations from ID3v.
	 * @return encoded info
	 */
	public final String getEncoded() {
		return (String) this.properties.get("mp3.id3tag.encoded");
	}
}
