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
	int rowCount;

	private final int sheetColumnWidth = 81;

	public Spreadsheet(Schedule schedule)
	{
		this.schedule = schedule;
		wb = new XSSFWorkbook();
		sheet = wb.createSheet("Sheet 1");
		rowCount = 3;

		createHeader();

		String[] departments = schedule.getDepartments();
		for (int i=0; i < departments.length; i++)
		{
			addDepartment(departments[i]);
			if (i < departments.length - 1)
			{
				addScheduleGridSpace();
			}
		}

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
		String[] header = { "Daily Lineup", "10/14/2024", "Monday" };
		XSSFCellStyle headerStyle = createCellStyleHeader();

		for (int i = 0; i < 3; i++)
		{
			Row row = sheet.createRow(i);
			for (int j = 0; j < sheetColumnWidth; j++)
			{
				if (j > 0) sheet.setColumnWidth(j, 325);
				else sheet.setColumnWidth(j, 4000);
				row.createCell(j);
			}
			sheet.addMergedRegion(
					new CellRangeAddress(i, i, 0, sheetColumnWidth));
			Cell mergedCell = row.getCell(0);
			mergedCell.setCellStyle(headerStyle);
			mergedCell.setCellValue(header[i]);
		}
	}

	public void addDepartment(String department)
	{

		// Create various cell styles used for formatting
		XSSFCellStyle departmentStyle = createCellStyleDepartment();
		XSSFCellStyle emptyGrid = createCellStyleEmptyGrid();
		XSSFCellStyle employeeNameStyle = createCellStyleEmployeeName();

		// String array to provide times on the schedule
		String[] timeSlots = { "4am", "5am", "6am", "7am", "8am", "9am", "10am",
				"11am", "12pm", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm",
				"8pm", "9pm", "10pm", "11pm" };

		Row row = sheet.createRow(getNextRow());
		row.setHeightInPoints(12);
		row.createCell(0).setCellValue(department);

		for (int i = 1; i < sheetColumnWidth; i++)
		{
			row.createCell(i).setCellStyle(departmentStyle);
			if (i % 4 == 0)
			{
				sheet.addMergedRegion(new CellRangeAddress(rowCount - 1,
						rowCount - 1, i - 3, i));
				Cell mergedCell = row.getCell(i - 3);
				mergedCell.setCellValue(timeSlots[i / 4 - 1]);
			}
		}

		int numberOfEmployees = schedule.getNumberOfEmployees();

		for (int i = 0; i < numberOfEmployees - 1; i++)
		{
			Employee e = schedule.getEmployee(i);
			if (e.getDepartment().equalsIgnoreCase(department))
			{
				Row employeeRow = sheet.createRow(getNextRow());
				Cell employeeCell = employeeRow.createCell(0);
				employeeCell.setCellValue(e.getName());
				employeeCell.setCellStyle(employeeNameStyle);
				// calculate shaded cells corresponding to the time frames
				int start = e.getStartTime() / 15 - 16;
				int end = e.getEndTime() / 15 - 16;

				for (int c = 1; c < sheetColumnWidth; c++)
				{
					Cell timeCell = employeeRow.createCell(c);
					timeCell.setCellStyle(emptyGrid);
					if (c % 4 == 0)
					{
						XSSFCellStyle newWithBorder = wb.createCellStyle();
						newWithBorder.cloneStyleFrom(emptyGrid);
						newWithBorder.setBorderRight(BorderStyle.MEDIUM);
						timeCell.setCellStyle(newWithBorder);
					}
					if (c > start && c <= end)
					{
						XSSFCellStyle newWithShading = wb.createCellStyle();
						newWithShading.cloneStyleFrom(timeCell.getCellStyle());
						newWithShading.setFillForegroundColor(
								IndexedColors.GREY_50_PERCENT.getIndex());
						newWithShading.setFillPattern(
								FillPatternType.SOLID_FOREGROUND);
						timeCell.setCellStyle(newWithShading);
					}
				}
			}
		}
	}

	public XSSFCellStyle createCellStyleHeader()
	{
		XSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setAlignment(HorizontalAlignment.CENTER);

		return headerStyle;
	}

	public XSSFCellStyle createCellStyleEmployeeName()
	{
		Font arialNarrowBold = wb.createFont();
		arialNarrowBold.setFontHeightInPoints((short) 9);
		arialNarrowBold.setFontName("Arial Narrow");
		arialNarrowBold.setBold(true);

		XSSFCellStyle employeeNameStyle = wb.createCellStyle();
		employeeNameStyle.setBorderBottom(BorderStyle.THIN);
		employeeNameStyle.setBorderTop(BorderStyle.THIN);
		employeeNameStyle.setBorderLeft(BorderStyle.THIN);
		employeeNameStyle.setBorderRight(BorderStyle.THIN);
		employeeNameStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		employeeNameStyle.setFont(arialNarrowBold);

		return employeeNameStyle;
	}

	public XSSFCellStyle createCellStyleDepartment()
	{
		Font arialNarrow = wb.createFont();
		arialNarrow.setFontHeightInPoints((short) 9);
		arialNarrow.setFontName("Arial Narrow");
		arialNarrow.setBold(false);

		// This cell style creates borders around all time slot cells
		XSSFCellStyle departmentStyle = wb.createCellStyle();
		departmentStyle.setBorderBottom(BorderStyle.MEDIUM);
		departmentStyle.setBorderTop(BorderStyle.MEDIUM);
		departmentStyle.setBorderLeft(BorderStyle.MEDIUM);
		departmentStyle.setBorderRight(BorderStyle.MEDIUM);
		departmentStyle.setAlignment(HorizontalAlignment.CENTER);
		departmentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		departmentStyle.setFont(arialNarrow);

		return departmentStyle;
	}

	public XSSFCellStyle createCellStyleEmptyGrid()
	{
		XSSFCellStyle emptyGrid = wb.createCellStyle();
		emptyGrid.setBorderBottom(BorderStyle.THIN);
		emptyGrid.setBorderTop(BorderStyle.THIN);
		emptyGrid.setBorderLeft(BorderStyle.THIN);
		emptyGrid.setBorderRight(BorderStyle.THIN);
		emptyGrid.setVerticalAlignment(VerticalAlignment.CENTER);

		return emptyGrid;
	}
	
	public void addScheduleGridSpace()
	{
		XSSFCellStyle spacerStyle = wb.createCellStyle();
		spacerStyle.setBorderBottom(BorderStyle.MEDIUM);
		spacerStyle.setBorderTop(BorderStyle.MEDIUM);
		
		Row row = sheet.createRow(getNextRow());
		row.setHeightInPoints(4);
		for (int i = 0; i < sheetColumnWidth; i++)
		{
			row.createCell(i).setCellStyle(spacerStyle);
		}
	
	}

	public int getNextRow()
	{
		return rowCount++;
	}

}
