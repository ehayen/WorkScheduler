package workScheduler;

/*
 * Used this site as an example for how to begin using POI
 * https://www.codejava.net/coding/how-to-write-excel-files-in-java-using-apache
 * -poi
 */

import org.apache.poi.xssf.usermodel.*;

import java.awt.Desktop;
import java.io.*;

import org.apache.poi.ss.usermodel.*;

import org.apache.poi.ss.util.*;

/**
 * This class is used to create an Excel spreadsheet of a daily schedule for
 * Dick's Sporting Goods. The schedule shows the current date and has sections
 * of each department with the list of employees assigned to each department for
 * the day. The sheet has shaded blocks which show the shift hours each employee
 * is assigned for the specific day.
 */
public class Spreadsheet
{
	XSSFWorkbook workBook;		// HAS-A workbook
	XSSFSheet sheet;		// HAS-A sheet
	Schedule schedule;		// HAS-A Schedule
	//File file;				// HAS-A file
	int rowCount = 0;

	private final int sheetColumnWidth = 81;

	public Spreadsheet(Schedule schedule) throws CannotWriteFileException
	{
		this.schedule = schedule;

		workBook = new XSSFWorkbook();
		sheet = workBook.createSheet("Sheet 1");

		createHeader();

		String[] departments = schedule.getDepartments();
		for (int i = 0; i < departments.length; i++)
		{
			addDepartment(departments[i]);

			addScheduleGridSpace();
		}

		try
		{
			FileOutputStream outputStream = new FileOutputStream("test.xlsx");
			workBook.write(outputStream);
			workBook.close();
		}
		catch (IOException e)
		{
			throw new CannotWriteFileException();
		}

	}

	public void createHeader()
	{
		String[] header = { "Daily Lineup", schedule.getDate() };
		XSSFCellStyle headerStyle = createCellStyleScheduleHeader();

		for (int i = 0; i < header.length; i++)
		{
			Row row = sheet.createRow(i);
			rowCount++;
			for (int j = 0; j < sheetColumnWidth; j++)
			{
				if (j > 0) sheet.setColumnWidth(j, 325);
				else sheet.setColumnWidth(j, 4000);
				row.createCell(j);
			}
			sheet.addMergedRegion(
					new CellRangeAddress(i, i, 0, sheetColumnWidth - 1));
			Cell mergedCell = row.getCell(0);
			mergedCell.setCellStyle(headerStyle);
			mergedCell.setCellValue(header[i]);
		}
	}

	public void addDepartment(String department)
	{

		// Create various cell styles used for formatting
		XSSFCellStyle departmentStyle = createCellStyleDepartmentHeader();
		XSSFCellStyle departmentTitleStyle = createCellStyleDepartmentTitle();
		XSSFCellStyle emptyGrid = createCellStyleEmptyGrid();
		XSSFCellStyle employeeNameStyle = createCellStyleEmployeeName();

		// String array to provide times on the schedule
		String[] timeSlots = { "4am", "5am", "6am", "7am", "8am", "9am", "10am",
				"11am", "12pm", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm",
				"8pm", "9pm", "10pm", "11pm" };

		Row row = sheet.createRow(getNextRow());
		// set row size
		row.setHeightInPoints(12);
		// Set department title
		row.createCell(0).setCellStyle(departmentTitleStyle);
		Cell departementCell = row.getCell(0);
		departementCell.setCellValue(department);

		// create all cells in the row
		for (int i = 1; i < sheetColumnWidth; i++)
		{
			row.createCell(i).setCellStyle(departmentStyle);
			// every 4th cell
			if (i % 4 == 0)
			{
				// merge every set of 4 cells together
				sheet.addMergedRegion(new CellRangeAddress(rowCount - 1,
						rowCount - 1, i - 3, i));
				Cell mergedCell = row.getCell(i - 3);
				// set value of that cell to be a specific time slot
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
						XSSFCellStyle newWithBorder = workBook.createCellStyle();
						newWithBorder.cloneStyleFrom(emptyGrid);
						newWithBorder.setBorderRight(BorderStyle.MEDIUM);
						timeCell.setCellStyle(newWithBorder);
					}
					if (c > start && c <= end)
					{
						XSSFCellStyle newWithShading = workBook.createCellStyle();
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

	public XSSFCellStyle createCellStyleScheduleHeader()
	{
		Font arialNarrowBold = workBook.createFont();
		arialNarrowBold.setFontHeightInPoints((short) 12);
		arialNarrowBold.setFontName("Arial Narrow");
		arialNarrowBold.setBold(true);

		XSSFCellStyle headerStyle = workBook.createCellStyle();
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setFont(arialNarrowBold);

		return headerStyle;
	}

	public XSSFCellStyle createCellStyleEmployeeName()
	{
		Font arialNarrowBold = workBook.createFont();
		arialNarrowBold.setFontHeightInPoints((short) 10);
		arialNarrowBold.setFontName("Arial Narrow");
		arialNarrowBold.setBold(true);

		XSSFCellStyle employeeNameStyle = workBook.createCellStyle();
		employeeNameStyle.setBorderBottom(BorderStyle.THIN);
		employeeNameStyle.setBorderTop(BorderStyle.THIN);
		employeeNameStyle.setBorderLeft(BorderStyle.THIN);
		employeeNameStyle.setBorderRight(BorderStyle.THIN);
		employeeNameStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		employeeNameStyle.setFont(arialNarrowBold);

		return employeeNameStyle;
	}

	public XSSFCellStyle createCellStyleDepartmentHeader()
	{
		Font arialNarrow = workBook.createFont();
		arialNarrow.setFontHeightInPoints((short) 9);
		arialNarrow.setFontName("Arial Narrow");
		arialNarrow.setBold(false);

		// This cell style creates borders around all time slot cells
		XSSFCellStyle departmentHeaderStyle = workBook.createCellStyle();
		departmentHeaderStyle.setBorderBottom(BorderStyle.MEDIUM);
		departmentHeaderStyle.setBorderTop(BorderStyle.MEDIUM);
		departmentHeaderStyle.setBorderLeft(BorderStyle.MEDIUM);
		departmentHeaderStyle.setBorderRight(BorderStyle.MEDIUM);
		departmentHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
		departmentHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		departmentHeaderStyle.setFont(arialNarrow);

		return departmentHeaderStyle;
	}

	public XSSFCellStyle createCellStyleEmptyGrid()
	{
		XSSFCellStyle emptyGrid = workBook.createCellStyle();
		emptyGrid.setBorderBottom(BorderStyle.THIN);
		emptyGrid.setBorderTop(BorderStyle.THIN);
		emptyGrid.setBorderLeft(BorderStyle.THIN);
		emptyGrid.setBorderRight(BorderStyle.THIN);
		emptyGrid.setVerticalAlignment(VerticalAlignment.CENTER);

		return emptyGrid;
	}

	public XSSFCellStyle createCellStyleDepartmentTitle()
	{
		Font arialNarrow = workBook.createFont();
		arialNarrow.setFontHeightInPoints((short) 9);
		arialNarrow.setFontName("Arial Narrow");
		arialNarrow.setBold(true);

		// This cell style creates borders around all time slot cells
		XSSFCellStyle departmentTitleStyle = workBook.createCellStyle();
		departmentTitleStyle.setBorderBottom(BorderStyle.MEDIUM);
		departmentTitleStyle.setBorderTop(BorderStyle.MEDIUM);
		departmentTitleStyle.setBorderLeft(BorderStyle.MEDIUM);
		departmentTitleStyle.setBorderRight(BorderStyle.MEDIUM);
		// departmentTitleStyle.setAlignment(HorizontalAlignment.CENTER);
		departmentTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		departmentTitleStyle.setFont(arialNarrow);
		departmentTitleStyle.setFillForegroundColor(
				IndexedColors.GREY_25_PERCENT.getIndex());
		departmentTitleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return departmentTitleStyle;
	}

	public void addScheduleGridSpace()
	{
		XSSFCellStyle spacerStyle = workBook.createCellStyle();
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

	public static void openFile() throws FileNotFoundException
	{
		File scheduleFile = new File("test.xlsx");

		try
		{
			Desktop.getDesktop().open(scheduleFile);
		}
		catch (IOException e)
		{
			System.out.println("h");
			throw new FileNotFoundException(
					"Schedule not found. Please try creating the schedule again.");
		}

	}

}
