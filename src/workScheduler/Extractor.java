package workScheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Extractor
{
	private String[] documentData;
	private ArrayList<String> rawHours;
	private ArrayList<String> department;
	private ArrayList<String> workers;
	private String[][] processedHours;
	private Map<String, Map<String, Map<String, String[][]>>> schedule;
	
	public Extractor()
	{
		schedule = new HashMap<String, Map<String, Map<String, String[][]>>>();
		parsePDF();
		getWorkers();
		getHoursAndDepartment();
		processHours();
		combine();
	}
	
	public static void main(String args[])
	{
		new Extractor();
	}
	
	public void parsePDF()
	{
		String filePath = "exampleSchedule.pdf";
		PDDocument document = null;
		
		try
		{
			document = Loader.loadPDF(new File(filePath));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error 1");
		}
		
		PDFTextStripper strip = new PDFTextStripper();
		
		String text = null;
		try
		{
			text = strip.getText(document);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] textArray = text.split("\n");
		
		
//		for (int i=0; i<textArray.length; i++)
//			System.out.println(i + ": " + textArray[i]);
		
		this.documentData = textArray;
	}
	
	public void getWorkers()
	{
		ArrayList<String> workers = new ArrayList<>();
		
		String regex = "(?!01411)\\d{5}";
		Pattern pattern = Pattern.compile(regex);
		
		for (int i=0; i < documentData.length; i++)
		{
			Matcher matcher = pattern.matcher(documentData[i]);
			if (matcher.find())
			{
				char firstLetter = Character.toUpperCase(documentData[i-1].charAt(0));
				workers.add(firstLetter + documentData[i-1].trim().substring(1));
			}
				
		}
		
		this.workers = workers;
	}
	
	public void getHoursAndDepartment()
	{
		rawHours = new ArrayList<>();
		department = new ArrayList<>();
		
		String regex = "Hrs\\d.*";
		Pattern pattern = Pattern.compile(regex);
		
		for (int i=0; i < documentData.length; i++)
		{
			Matcher matcher = pattern.matcher(documentData[i]);
			if (matcher.find())
			{
				rawHours.add(matcher.group().substring(3));
				department.add(documentData[i+1].trim());
			}
		}
	}

	public void processHours()
	{
		int DIMENSION = rawHours.size();
		processedHours = new String[DIMENSION][2];
		
		for (int i=0; i<DIMENSION; i++)
		{
			processedHours[i] = rawHours.get(i).split("-");
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma");
		
		for (int i=0; i<DIMENSION; i++)
		{
			for (int j=0; j<2; j++)
			{
				LocalTime time = LocalTime.parse(processedHours[i][j], formatter);
				double t = time.getHour() + ((double)time.getMinute()/60);
				processedHours[i][j] = Double.toString(t);
			}
		}
	}

	public void combine()
	{
		for (int i=0; i< workers.size(); i++)
		{
				Map<String, String[][]> hoursMap = new HashMap<>();
				hoursMap.put("Hours", processedHours);
				Map<String, Map<String, String[][]>> workersHoursMap = new HashMap<>();
				workersHoursMap.put(workers.get(i), hoursMap);
				if (schedule == null || !schedule.containsKey(department.get(i)))
				{
					schedule.put(department.get(i), workersHoursMap);
				}
				else
				{
					schedule.get(department.get(i)).put(workers.get(i), hoursMap);
				}	
		}
		
		String s = schedule.toString();
		String[] r = s.split(",");
		for(String q:r)
			System.out.println(q);
	}

}
