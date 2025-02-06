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
 * This class provides a custom exception for specific messages to be passed to the user.
 */
public class WrongFileException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5610127374701748588L;

	public WrongFileException()
	{
		super("This is not the correct file.");
	}
}
