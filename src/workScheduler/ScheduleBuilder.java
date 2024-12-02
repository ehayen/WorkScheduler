package workScheduler;

import java.io.File;

public class ScheduleBuilder
{
	private Schedule schedule;
	
	public ScheduleBuilder(File file)
	{
		PDFParser p = new PDFParser(file);
		String[] d = p.getDepartments();
		String[] e = p.getWorkers();
		int[][] h = p.getHours();
		schedule = new Schedule();
		
		schedule.setDepartments(d);
		
		
		for (int i=0; i < e.length; i++)
		{
			Employee employee = new Employee();
			employee.setName(e[i]);
			employee.setDepartment(d[i]);
			employee.setStartTime(h[i][0]);
			employee.setEndTime(h[i][1]);
			schedule.addEmployee(employee);
		}
		
		Spreadsheet spreadsheet = new Spreadsheet(schedule);
		
	}
	
//	public static void main(String[] args)
//	{
//		new ScheduleBuilder();
//	}
}
