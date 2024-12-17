package workScheduler;

import java.util.ArrayList;

/**
 * This class models a schedule for Dick's Sporting Goods. This schedule has a
 * date, many employees, and an array of departments. This class provides
 * methods to add employees and to set the date and departments of the schedule.
 */
public class Schedule
{
	private String date;					// HAS-A date
	private ArrayList<Employee> employees;	// HAS-MANY Employees
	private String[] departments;			// HAS-MANY departments

	/**
	 * Constructor
	 */
	public Schedule()
	{
		// initialize array of employees
		employees = new ArrayList<Employee>();
	}

	/**
	 * Purpose: To add an employee to the schedule.
	 * @param employee the employee to be added
	 */
	public void addEmployee(Employee employee)
	{
		employees.add(employee);
	}

	/**
	 * Purpose: To add departments to the schedule.
	 * @param departments the departments to be added
	 */
	public void setDepartments(String[] departments)
	{
		// initialize arraylist to hold departments
		ArrayList<String> departmentList = new ArrayList<>();
		// iterate through departments passed in
		for (String department : departments)
		{
			// set flag to false
			boolean isIn = false;
			// iterate through the department list
			for (String s : departmentList)
			{
				// if a department already exists in the list, set the flag to true
				if (s.equalsIgnoreCase(department)) isIn = true;
			}
			// if the flag is false, the department does not exist in the list yet
			if (!isIn)
			{
				// add it to the list
				departmentList.add(department);
			}
		}

		// alphabetically sort the departments to ensure consistent layout
		departmentList.sort(String::compareToIgnoreCase);

		// convert departments to an array, stores in instance variable
		this.departments = departmentList.toArray(new String[0]);
	}

	/**
	 * Purpose: To set the date of the schedule
	 * @param date the date to set the schedule to
	 */
	public void setDate(String date)
	{
		this.date = date;
	}

	/**
	 * Purpose: To return the number of employees in the schedule
	 * @return the number of employees on the schedule
	 */
	public int getNumberOfEmployees()
	{
		return employees.size();
	}

	/**
	 * Purpose: To return an employee from the schedule
	 * @param i the position of the employee
	 * @return Employee at specific position
	 */
	protected Employee getEmployee(int i)
	{
		return employees.get(i);
	}

	/**
	 * Purpose: To get the departments in the schedule
	 * @return returnDepartments the list of departments from the schedule
	 */
	public String[] getDepartments()
	{
		// initialize new department list to return
		String[] returnDepartments = new String[departments.length];
		// iterate through the departments
		for (int i = 0; i < departments.length; i++)
		{
			// set current returnDepartement to the current department
			returnDepartments[i] = departments[i];
		}
		return returnDepartments;
	}

	/**
	 * Purpose: To get the date from the schedule
	 * @return date the date of the schedule
	 */
	public String getDate()
	{
		return date;
	}
}
