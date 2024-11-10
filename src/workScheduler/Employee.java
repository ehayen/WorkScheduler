package workScheduler;

import java.io.File;
import java.io.PrintWriter;

public class Employee
{
	private String name;
	private String department;
	private int startTime;
	private int endTime;
	
	public Employee()
	{
		
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setDepartment(String department)
	{
		this.department = department;
	}
	
	public void setStartTime(int start)
	{
		startTime = start;
	}
	
	public void setEndTime(int end)
	{
		endTime = end;
	}
	
	public String getDepartment()
	{
		return department;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getStartTime()
	{
		return startTime;
	}
	
	public int getEndTime()
	{
		return endTime;
	}
}
