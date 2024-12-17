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
	private XSSFWorkbook workBook; // HAS-A workbook
	private XSSFSheet sheet; // HAS-A sheet
	private Schedule schedule; // HAS-A Schedule
	private String saveFile;	// HAS-A file to save to
	private int rowCount = 0; // HAS-A row count

	private final int sheetColumnWidth = 81; // HAS-A number of columns wide

	/**
	 * Constructor
	 * 
	 * @param schedule the Schedule the spreadsheet will be created from
	 * @throws CannotWriteFileException if the file cannot be written
	 */
	public Spreadsheet(Schedule schedule, String saveFile) throws CannotWriteFileException
	{
		// initialize schedule
		this.schedule = schedule;
		// initialize the workbook
		workBook = new XSSFWorkbook();
		// initialize the sheet
		sheet = workBook.createSheet("Sheet 1");
		// initialize file name to save file to
		this.saveFile = saveFile;

		// create the header at the top of the lineup with title and date
		createHeader();

		// get list of departments from the Schedule
		String[] departments = schedule.getDepartments();
		// iterate through each department
		for (int i = 0; i < departments.length; i++)
		{
			// add the current department and all its employees to the schedule
			addDepartment(departments[i]);
			// add a spaces between departments
			addScheduleGridSpace();
		}

		// try-with-resources to open an outputStream to write to a file
		try (FileOutputStream outputStream = new FileOutputStream(saveFile))
		{
			// write the contents of the workbook using the output stream
			workBook.write(outputStream);
		}
		catch (IOException e)
		{
			// if the file cannot be written, throw new custom exception to pass
			// message to user
			throw new CannotWriteFileException();
		}
		finally
		{
			try
			{
				// try to close the workbook
				workBook.close();
			}
			catch (IOException e)
			{
				// catch any exceptions caused by trying to close
				e.printStackTrace();
			}
		}
	}

	public void createHeader()
	{
		// array of items to be placed in the header
		String[] header = { "Daily Lineup", schedule.getDate() };
		// create a new cell style for use in the header
		XSSFCellStyle headerStyle = createCellStyleScheduleHeader();

		// iterate through list of items in the header
		for (int i = 0; i < header.length; i++)
		{
			// create a new row, increment the row with the built in method
			Row row = sheet.createRow(getNextRow());
			// iterate through each column in the sheet
			for (int j = 0; j < sheetColumnWidth; j++)
			{
				// for all columns except the first one
				if (j > 0)
				{
					// set the column width to 325
					sheet.setColumnWidth(j, 325);
				}
				else
				{
					// set the first column to a width of 4000
					sheet.setColumnWidth(j, 4000);
				}
				// create a cell in that column
				row.createCell(j);
			}
			// merge every cell across every column in the sheet for the header
			sheet.addMergedRegion(
					new CellRangeAddress(i, i, 0, sheetColumnWidth - 1));
			// get the cell of the header
			Cell mergedCell = row.getCell(0);
			// set a cell style
			mergedCell.setCellStyle(headerStyle);
			// set the value of the cell as one of the items to be placed in the
			// header
			mergedCell.setCellValue(header[i]);
		}
	}

	/**
	 * Purpose: This method creates the format for each department in the
	 * schedule. It adds a department block that consists of the department name
	 * and a list of time slots to show employee shifts. This method also adds
	 * each employee and shades the hours they are working on the grid.
	 * 
	 * @param department the name of the department to add
	 */
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

		// create a new row at the next available position
		Row row = sheet.createRow(getNextRow());
		// set row height
		row.setHeightInPoints(12);
		// set the style of the cell
		row.createCell(0).setCellStyle(departmentTitleStyle);
		// get the cell object
		Cell departementCell = row.getCell(0);
		// set the name of the department in the cell
		departementCell.setCellValue(department);

		// create all cells in the row across the span of the schedule
		for (int i = 1; i < sheetColumnWidth; i++)
		{
			// create a new cell and set its style
			row.createCell(i).setCellStyle(departmentStyle);
			// this section creates merged cells in sets of 4. Each merged cell
			// represents 1 hour and has 4 corresponding cells underneath it
			// that are used to represent each quarter of the hour.
			if (i % 4 == 0)
			{
				// merge every set of 4 cells together
				sheet.addMergedRegion(new CellRangeAddress(rowCount - 1,
						rowCount - 1, i - 3, i));
				// get the cell object representing the merged cell
				Cell mergedCell = row.getCell(i - 3);
				// set value of that cell to be a specific hour of time from the
				// timeSlot array
				mergedCell.setCellValue(timeSlots[i / 4 - 1]);
			}
		}

		// get the number of employees on the schedule that day
		int numberOfEmployees = schedule.getNumberOfEmployees();

		// iterate through the list of employees
		for (int i = 0; i < numberOfEmployees - 1; i++)
		{
			// get the employee in position i
			Employee e = schedule.getEmployee(i);
			// if that employee is assigned to the current department
			if (e.getDepartment().equalsIgnoreCase(department))
			{
				// create a new row on the next available line
				Row employeeRow = sheet.createRow(getNextRow());
				// create a new cell for the employee
				Cell employeeCell = employeeRow.createCell(0);
				// set the cell to the name of the employee
				employeeCell.setCellValue(e.getName());
				// set the cell style to the desired style
				employeeCell.setCellStyle(employeeNameStyle);
				// calculate shaded cells corresponding to the time frames
				// each integer here is the minute of the day the employee
				// starts or stops working. Since our schedule begins at 4am,
				// dividing by 15 gives the starting cell, subtracting 16 after
				// moves the starting point from 12:00am (0) to 4:00am (16)
				int start = e.getStartTime() / 15 - 16;
				int end = e.getEndTime() / 15 - 16;

				// iterate through cells in the employee row
				for (int c = 1; c < sheetColumnWidth; c++)
				{
					// create new cells in each column
					Cell timeCell = employeeRow.createCell(c);
					// set the cell style
					timeCell.setCellStyle(emptyGrid);
					// every 4th cell
					if (c % 4 == 0)
					{
						// update the style
						XSSFCellStyle newWithBorder = workBook
								.createCellStyle();
						// clone the current cell style
						newWithBorder.cloneStyleFrom(emptyGrid);
						// update the border to be a thicker divider between the hours
						newWithBorder.setBorderRight(BorderStyle.MEDIUM);
						// set the style of the cell
						timeCell.setCellStyle(newWithBorder);
					}
					// for the cells that correspond to the hours the employee is scheduled
					if (c > start && c <= end)
					{
						// create a new cell style
						XSSFCellStyle newWithShading = workBook
								.createCellStyle();
						// clone the current cell style
						newWithShading.cloneStyleFrom(timeCell.getCellStyle());
						// add shading to the cell
						newWithShading.setFillForegroundColor(
								IndexedColors.GREY_50_PERCENT.getIndex());
						// set the shading to be visible
						newWithShading.setFillPattern(
								FillPatternType.SOLID_FOREGROUND);
						// set the style of the cell
						timeCell.setCellStyle(newWithShading);
					}
				}
			}
		}
	}

	/**
	 * Purpose: To create a cell style for the header of the schedule
	 * @return headerStyle the style used in the header of the schedule
	 */
	public XSSFCellStyle createCellStyleScheduleHeader()
	{
		// create a new font object
		Font arialNarrowBold = workBook.createFont();
		// set the size of the font
		arialNarrowBold.setFontHeightInPoints((short) 12);
		// set the font style
		arialNarrowBold.setFontName("Arial Narrow");
		// set the font to be bold
		arialNarrowBold.setBold(true);

		// initialize the cell style
		XSSFCellStyle headerStyle = workBook.createCellStyle();
		// set the alignment of the text to be centered in the cell
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		// set the font style
		headerStyle.setFont(arialNarrowBold);

		return headerStyle;
	}

	/**
	 * Purpose: To create a cell style used for employee names
	 * @return employeeNameStyle the style used in the cells containing employee names
	 */
	public XSSFCellStyle createCellStyleEmployeeName()
	{
		// initialize a new font object
		Font arialNarrowBold = workBook.createFont();
		// set the font size
		arialNarrowBold.setFontHeightInPoints((short) 10);
		// set the font style
		arialNarrowBold.setFontName("Arial Narrow");
		// set the font to be bold
		arialNarrowBold.setBold(true);

		// initialize new cell style
		XSSFCellStyle employeeNameStyle = workBook.createCellStyle();
		// set the borders of the cell to be thin
		employeeNameStyle.setBorderBottom(BorderStyle.THIN);
		employeeNameStyle.setBorderTop(BorderStyle.THIN);
		employeeNameStyle.setBorderLeft(BorderStyle.THIN);
		employeeNameStyle.setBorderRight(BorderStyle.THIN);
		// set the text in the cell to be aligned vertically
		employeeNameStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		// set the font style of the cell
		employeeNameStyle.setFont(arialNarrowBold);

		return employeeNameStyle;
	}

	/**
	 * Purpose: To create a cell style used in each department header
	 * @return departmentHeaderStyle the style used in each department header
	 */
	public XSSFCellStyle createCellStyleDepartmentHeader()
	{
		// initialize new font object
		Font arialNarrow = workBook.createFont();
		// set the font size
		arialNarrow.setFontHeightInPoints((short) 9);
		// set the font style
		arialNarrow.setFontName("Arial Narrow");

		// initialize new cell style
		XSSFCellStyle departmentHeaderStyle = workBook.createCellStyle();
		// set borders around the cell to be medium
		departmentHeaderStyle.setBorderBottom(BorderStyle.MEDIUM);
		departmentHeaderStyle.setBorderTop(BorderStyle.MEDIUM);
		departmentHeaderStyle.setBorderLeft(BorderStyle.MEDIUM);
		departmentHeaderStyle.setBorderRight(BorderStyle.MEDIUM);
		// set the text to align in the center of the cell
		departmentHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
		departmentHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		// set the font style of the cell
		departmentHeaderStyle.setFont(arialNarrow);

		return departmentHeaderStyle;
	}

	/**
	 * Purpose: To create a cell style used in the grid where employee shift times will be displayed.
	 * @return emptyGrid the cell style used where the grid will be empty
	 */
	public XSSFCellStyle createCellStyleEmptyGrid()
	{
		// initialize new cell style
		XSSFCellStyle emptyGrid = workBook.createCellStyle();
		// set borders of cell to thin
		emptyGrid.setBorderBottom(BorderStyle.THIN);
		emptyGrid.setBorderTop(BorderStyle.THIN);
		emptyGrid.setBorderLeft(BorderStyle.THIN);
		emptyGrid.setBorderRight(BorderStyle.THIN);
		// set text to align in the vertical center of cell
		emptyGrid.setVerticalAlignment(VerticalAlignment.CENTER);

		return emptyGrid;
	}

	/**
	 * Purpose: To create a cell style used for the department title.
	 * @return departmentTitleStyle the style used for the cells containing department titles
	 */
	public XSSFCellStyle createCellStyleDepartmentTitle()
	{
		// initialize a new font object
		Font arialNarrow = workBook.createFont();
		// set font size
		arialNarrow.setFontHeightInPoints((short) 9);
		// set the font style
		arialNarrow.setFontName("Arial Narrow");
		// set the font to bold
		arialNarrow.setBold(true);

		// initialize new cell style
		XSSFCellStyle departmentTitleStyle = workBook.createCellStyle();
		// set cell borders to medium thickness
		departmentTitleStyle.setBorderBottom(BorderStyle.MEDIUM);
		departmentTitleStyle.setBorderTop(BorderStyle.MEDIUM);
		departmentTitleStyle.setBorderLeft(BorderStyle.MEDIUM);
		departmentTitleStyle.setBorderRight(BorderStyle.MEDIUM);
		// set the text to align in the vertical center of the cell
		departmentTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		// set the font of the cell
		departmentTitleStyle.setFont(arialNarrow);
		// set the shading of the cell
		departmentTitleStyle.setFillForegroundColor(
				IndexedColors.GREY_25_PERCENT.getIndex());
		// set the shading to be visible
		departmentTitleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return departmentTitleStyle;
	}

	/**
	 * Purpose: To add a spacer between departments to improve the readability of the schedule.
	 */
	public void addScheduleGridSpace()
	{
		// initialize a new cell style
		XSSFCellStyle spacerStyle = workBook.createCellStyle();
		// set the top border of the cell to medium thickness
		spacerStyle.setBorderTop(BorderStyle.MEDIUM);

		// create the next available row
		Row row = sheet.createRow(getNextRow());
		// set the height of the row to be relatively small
		row.setHeightInPoints(4);
		// iterate through all columns in row
		for (int i = 0; i < sheetColumnWidth; i++)
		{
			// create each cell and set the style to that of the spacer
			row.createCell(i).setCellStyle(spacerStyle);
		}
	}

	/**
	 * Purpose: To keep track of the next available row that can be used
	 * @return rowCount the next available row 
	 */
	public int getNextRow()
	{
		// returns the rowCount and post-increments
		return rowCount++;
	}

	/**
	 * Purpose: To open the file that was created from this object.
	 * @throws FileNotFoundException if the file is unable to be opened
	 */
	public static void openFile(String saveFile) throws FileNotFoundException
	{
		// create a file object from the file that was saved
		File scheduleFile = new File(saveFile);

		try
		{
			// try to open the saved file
			Desktop.getDesktop().open(scheduleFile);
		}
		catch (IOException e)
		{
			// if file cannot be opened, throw new exception and pass message to user.
			throw new FileNotFoundException(
					"Schedule not found. Please try creating the schedule again.");
		}
	}
}
