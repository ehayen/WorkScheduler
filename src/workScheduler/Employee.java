package workScheduler;

import java.io.File;
import java.io.PrintWriter;

public class Employee
{
	private String name;
	private String department;
	private double startTime;
	private double endTime;
	
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
	
	public void setStartTime(String start)
	{
		this.startTime = Double.parseDouble(start);
	}
	
	public void setEndTime(String end)
	{
		this.endTime = Double.parseDouble(end);
	}
}
