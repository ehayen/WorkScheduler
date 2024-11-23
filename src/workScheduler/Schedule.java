package workScheduler;

import java.time.LocalDate;
import java.util.ArrayList;


public class Schedule
{
	private String date;
	private ArrayList<Employee> employees;
	private String[] departments;
	
	public Schedule()
	{
		employees = new ArrayList<Employee>();
	}
	
	public void addEmployee(Employee employee)
	{
		employees.add(employee);
	}
	
	public void setDepartments(String[] departments)
	{
		ArrayList<String> departmentList = new ArrayList<>();
		for (String department : departments)
		{
			boolean isIn = false;
			for (String s : departmentList)
			{
				if (s.equalsIgnoreCase(department))
					isIn = true;
			}
			if (!isIn)
			{
				departmentList.add(department);
			}
		}
		
		departmentList.sort(String::compareToIgnoreCase);
		
		this.departments = departmentList.toArray(new String[0]);
}
	
	public void setDate(String date)
	{
		this.date = date;
	}
	
	public int getNumberOfEmployees()
	{
		return employees.size();
	}
	
	protected Employee getEmployee(int i)
	{
		return employees.get(i);
	}
	
	public String[] getDepartments()
	{
		String[] returnDepartments = new String[departments.length];
		for (int i=0; i<departments.length; i++)
		{
			returnDepartments[i] = departments[i];
		}
		
		return returnDepartments;
	}
}
