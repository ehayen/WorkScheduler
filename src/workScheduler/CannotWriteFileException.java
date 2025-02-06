package workScheduler;

/**
 * Lead Author(s):
 * @author Eric Hayen

 * References:
 * Morelli, R., & Walde, R. (2016). Java, Java, Java: Object-Oriented Problem Solving.
 * Retrieved from https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
 * 
 * Other Resources:
 *
 * Version/date: V 1.0 	December 16, 2024
 */

/**
 * Purpose: To act as a custom exception that can be thrown to pass a message to the user.
 */
public class CannotWriteFileException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3328154873499723019L;

	public CannotWriteFileException()
	{
		super("Cannot write file. Ensure all current versions are closed.");
	}
}
