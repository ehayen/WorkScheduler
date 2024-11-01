package workScheduler;

/*
 * Used this site as an example for how to begin using POI
 * https://www.codejava.net/coding/how-to-write-excel-files-in-java-using-apache
 * -poi
 */

import org.apache.poi.xssf.usermodel.*;

import java.io.*;

import org.apache.poi.ss.usermodel.*;

public class Spreadsheet
{
	public static void main(String[] args)
	{

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("Sheet 1");
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue("Hello");
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cell.setCellStyle(cellStyle);

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
}
