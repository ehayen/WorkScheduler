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
	
	private final int sheetColumnWidth = 81;
	
	public static void main(String[] args)
	{

		new Spreadsheet();
		
		
//		CellStyle cellStyle = wb.createCellStyle();
//		cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
//		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//		cell.setCellStyle(cellStyle);

		

	}
	
	
	public Spreadsheet()
	{
		wb = new XSSFWorkbook();
		sheet = wb.createSheet("Sheet 1");
		
		createHeader();
		
		createOutline();
		
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

	public void createOutline()
	{
		String [] timeSlots = {"4am", "5am", "6am", "7am","8am", "9am", "10am", 
								"11am","12pm", "1pm", "2pm", "3pm","4pm", "5pm", 
								 "6pm", "7pm","8pm", "9pm", "10pm", "11pm"};
		
		for (int i=3; i<20; i++)
		{
			Row row = sheet.createRow(i);
			row.createCell(0).setCellValue("department");
			for (int j=1; j<sheetColumnWidth; j++)
			{
				row.createCell(j);
				if (j%4 == 0)
				{
					sheet.addMergedRegion(new CellRangeAddress(i,i,j-3,j));
					Cell mergedCell = row.getCell(j-3);
					mergedCell.setCellValue(timeSlots[j/4-1]);
				}
			}
			
		}
	}


}
