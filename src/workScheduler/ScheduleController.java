package workScheduler;

import java.io.File;

/**
 * This class handles the initialization of components necessary for parsing and creating an entire schedule.
 */
public class ScheduleController
{
	/**
	 * Constructor
	 * @param file the file of the schedule to be parsed
	 * @throws WrongFileException if an incompatible file is passed in
	 * @throws CannotWriteFileException if a schedule cannot be created and written to file
	 */
	public ScheduleController(File file) throws WrongFileException, CannotWriteFileException
	{
		// initialize PDFParser, pass in the schedule file
		PDFParser p = new PDFParser(file);
		// get array of departments
		String[] d = p.getDepartments();
		// get array of the employees
		String[] e = p.getWorkers();
		// get two-dimensional array of the employee hours
		int[][] h = p.getHours();
		// initialize a new schedule
		Schedule schedule = new Schedule();
		// pass the array of departments to the schedule
		schedule.setDepartments(d);
		// set the date of the schedule
		schedule.setDate(p.getDate());
		
		// iterate through the list of employees
		for (int i=0; i < e.length; i++)
		{
			// create a new employee
			Employee employee = new Employee();
			// set their name
			employee.setName(e[i]);
			// set their assigned department
			employee.setDepartment(d[i]);
			// set their starting time
			employee.setStartTime(h[i][0]);
			// set their ending time
			employee.setEndTime(h[i][1]);
			// add the employee to the schedule
			schedule.addEmployee(employee);
		}
		
		// initialize a spreadsheet, pass the schedule to it
		new Spreadsheet(schedule);
	}
}
