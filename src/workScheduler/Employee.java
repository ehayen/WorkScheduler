package workScheduler;

public class Employee
{
	private String name;			// HAS-A name
	private String department;		// HAS-A department
	private int startTime;			// HAS-A start time
	private int endTime;			// HAS-A end time
	
	/**
	 * Purpose: To set the employees name
	 * @param name the name to be set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Purpose: To set the employees department
	 * @param department the department to be set
	 */
	public void setDepartment(String department)
	{
		this.department = department;
	}
	
	/**
	 * Purpose: To set the start time of the employee
	 * @param start the start time to be set
	 */
	public void setStartTime(int start)
	{
		startTime = start;
	}
	
	/**
	 * Purpose: To set the end time of the employee
	 * @param end the end time to be set
	 */
	public void setEndTime(int end)
	{
		endTime = end;
	}
	
	/**
	 * Purpose: To return the department of the employee
	 * @return the employee's department
	 */
	public String getDepartment()
	{
		return department;
	}
	
	/**
	 * Purpose: To return the name of the employee
	 * @return the employee's name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Purpose: To return the start time of the employee
	 * @return the employee's start time
	 */
	public int getStartTime()
	{
		return startTime;
	}
	
	/**
	 * Purpose: To return the end time of the employee
	 * @return the employee's end time
	 */
	public int getEndTime()
	{
		return endTime;
	}
}
