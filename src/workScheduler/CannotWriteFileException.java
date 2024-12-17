package workScheduler;

public class CannotWriteFileException extends Exception
{
	public CannotWriteFileException()
	{
		super("Cannot write file. Ensure all current versions are closed.");
	}
}
