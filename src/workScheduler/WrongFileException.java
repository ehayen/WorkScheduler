package workScheduler;

/**
 * This class provides a custom exception for specific messages to be passed to the user.
 */
public class WrongFileException extends Exception
{
	public WrongFileException()
	{
		super("This is not the correct file.");
	}
}
