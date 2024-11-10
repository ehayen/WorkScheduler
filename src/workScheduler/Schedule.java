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
		this.departments = departments;
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
}
