package helloWorld;

public class HelloWorld
{
	
	public HelloWorld()
	{
		hello();
	}
	
	public static void main(String args[])
	{
		System.out.println("Hello Eric, this is Eric");
		new HelloWorld();
	}
	
	private void hello()
	{
		for (int i=0; i<10; i++)
		{
			System.out.println("hey");
		}
	}
}
