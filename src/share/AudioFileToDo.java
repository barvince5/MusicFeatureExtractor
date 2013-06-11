package share;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import customException.MP3Exception;


public final class AudioFileToDo {

	private static AudioFileToDo instance= null;
	private BlockingQueue<File> queue= null;
	private int capacity;
	
	/**
	 * Private constructor, just one instantiation is possible.
	 */
	private AudioFileToDo() {
		this.queue= new ArrayBlockingQueue<File>(capacity);
		this.capacity= 5; //This is the default value. 
	}
	
	/**
	 * This method (singleton)return the only instance of AudioFileToDo class.
	 * @return AudioFileToDo
	 */
	public final static AudioFileToDo getInstance() {
		
		if(AudioFileToDo.instance == null) {
			synchronized (AudioFileToDo.class) {
				if(AudioFileToDo.instance == null)
					AudioFileToDo.instance= new AudioFileToDo();
			}
		}
		
		return AudioFileToDo.instance;
	}

	
	/**
	 * Inserts the specified element into this queue, waiting if necessary for space to become available.<br>
	 * Note: The file is inserted only is is not null and is not a directory.
	 * @param song
	 * @throws InterruptedException if interrupted while waiting.
	 * @return true in case of success.
	 * @throws MP3Exception when the given file is not a MP3.
	 */
	public final boolean insertAudioFile(File song) 
			throws InterruptedException, MP3Exception {
		
		if(song != null) {
			if(song.getName().endsWith(".mp3") == false)
				throw new MP3Exception(song.getName()+" is not MP3 file.");
			else if(song.isFile()) {
				this.queue.put(song);
				return true;
			}
		}
		
		return false;
		
	}
	
	/**
	 * Retrieves and removes the head of this queue, waiting if necessary until an element becomes available.
	 * @return
	 * @throws InterruptedException if interrupted while waiting.
	 */
	public final File retriveAudioFile() 
			throws InterruptedException {
		
		return this.queue.take();
	}
}
