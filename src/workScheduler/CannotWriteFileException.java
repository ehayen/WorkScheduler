package workScheduler;

/**
 * Purpose: To act as a custom exception that can be thrown to pass a message to the user.
 */
public class CannotWriteFileException extends Exception
{
	public CannotWriteFileException()
	{
		super("Cannot write file. Ensure all current versions are closed.");
	}
}
