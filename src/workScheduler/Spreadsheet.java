package workScheduler;

/*
 * Used this site as an example for how to begin using POI
 * https://www.codejava.net/coding/how-to-write-excel-files-in-java-using-apache
 * -poi
 */

import org.apache.poi.xssf.usermodel.*;

import java.io.*;

import org.apache.poi.ss.usermodel.*;

import org.apache.poi.ss.util.*;

public class Spreadsheet
{
	XSSFWorkbook wb;
	XSSFSheet sheet;
	Schedule schedule;
	
	private final int sheetColumnWidth = 81;
	
	public static void main(String[] args)
	{

		new Spreadsheet();
		
		
//		CellStyle cellStyle = wb.createCellStyle();
//		cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
//		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//		cell.setCellStyle(cellStyle);

		

	}
	
	
	public Spreadsheet(Schedule schedule)
	{
		this.schedule = schedule;
		wb = new XSSFWorkbook();
		sheet = wb.createSheet("Sheet 1");
		
		createHeader();
		
		addDepartment("Team Sports");
		
		try
		{
			FileOutputStream outputStream = new FileOutputStream("test.xlsx");
			wb.write(outputStream);
			System.out.println("Success");
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Error");
			e.printStackTrace();
		}
		catch (Exception e)
		{
			System.out.println("Exception");
		}
	}
	
	public void createHeader()
	{
		String [] header = {"Daily Lineup", "10/14/2024", "Monday"};
		CellStyle cs = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBold(true);
		cs.setAlignment(HorizontalAlignment.CENTER);
		cs.setFont(font);
		
		for (int i=0; i<3; i++)
		{
			Row row = sheet.createRow(i);
			for (int j=0; j<sheetColumnWidth; j++)
			{
				row.createCell(j);
			}
			sheet.addMergedRegion(new CellRangeAddress(i,i,0,sheetColumnWidth));
			Cell mergedCell = row.getCell(0);
			mergedCell.setCellStyle(cs);
			mergedCell.setCellValue(header[i]);
		}
	}

	public void addDepartment(String department)
	{
		String [] timeSlots = {"4am", "5am", "6am", "7am","8am", "9am", "10am", 
								"11am","12pm", "1pm", "2pm", "3pm","4pm", "5pm", 
								 "6pm", "7pm","8pm", "9pm", "10pm", "11pm"};
		
		Row row = sheet.createRow(3);
		row.createCell(0).setCellValue(department);
		for (int i=1; i<sheetColumnWidth; i++)
		{
			row.createCell(i);
			if (i%4 == 0)
			{
				sheet.addMergedRegion(new CellRangeAddress(3,3,i-3,i));
				Cell mergedCell = row.getCell(i-3);
				mergedCell.setCellValue(timeSlots[i/4-1]);
			}
		}
		
		int numberOfEmployees = schedule.getNumberOfEmployees();
		int counter = 4;
		
		XSSFCellStyle shaded = wb.createCellStyle();
		shaded.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		shaded.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		for (int i=0; i < numberOfEmployees-1; i++)
		{
			Employee e = schedule.getEmployee(i);
			if (e.getDepartment().equalsIgnoreCase(department))
			{
				Row employeeRow = sheet.createRow(counter++);
				employeeRow.createCell(0).setCellValue(e.getName());
				
				// calculate shaded cells coresponding to the time frames
				int start = e.getStartTime() / 15 - 16;
				int end = e.getEndTime() / 15 - 16;
				
				
				
				for (int c=1; c < sheetColumnWidth; c++)
				{
					Cell timeCell = employeeRow.createCell(c);
					if (c > start && c < end)
					{
						timeCell.setCellStyle(shaded);
					}
				}
				
				System.out.println("Start:" + start + " End:" + end);
				
				
//				for (int j=1; i<sheetColumnWidth; j++)
//				{
//					
//				}
			}
		}
		
		
		
	}


}
