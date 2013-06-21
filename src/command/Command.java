package command;

public interface Command {
	/**
	 * This is the method called by the main class after the recognition of the corresponding command class.
	 * @throws Exception in case of error.
	 */
	public void start() throws Exception;
}
