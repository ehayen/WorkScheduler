package workScheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.*;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * Lead Author(s):
 * @author Eric Hayen

 * References:
 * Morelli, R., & Walde, R. (2016). Java, Java, Java: Object-Oriented Problem Solving.
 * Retrieved from https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
 * 
 * Other Resources:
 * https://javadoc.io/doc/org.apache.pdfbox/pdfbox/latest/index.html
 * https://www.tutorialspoint.com/pdfbox/pdfbox_reading_text.htm
 * 
 * Version/date: V 1.0 	December 16, 2024
 */

/**
 * This class accepts a PDF file of a schedule for Dick's Sporting Goods and
 * processes the information in it. This class uses regular expressions and
 * contains methods to find relevant schedule data such as the schedule date,
 * employee names, working hours, and the department each employee is assigned
 * to. This class also provides methods to access the arrays containing the
 * shift information and the date.
 */
public class PDFParser
{
	private File file; // HAS-A input file
	private String[] documentData; // HAS-MANY strings of data from the document
	private ArrayList<String> rawHours; // HAS-MANY shift times
	private ArrayList<String> department; // HAS-MANY departments
	private ArrayList<String> workers; // HAS-MANY employee names
	private int[][] processedHours; // HAS-MANY shift start/stop times
	private LocalDate date; // HAS-A date of the schedule

	/**
	 * Purpose: Create a new parser object, accept a file passed in to the
	 * constructor, and throw an exception if an incorrect file is passed in.
	 * 
	 * @param file the file to be parsed
	 * @throws WrongFileException if an incompatible file is selected
	 */
	public PDFParser(File file) throws WrongFileException
	{
		// initialize the file passed in
		this.file = file;

		// pull data from file and store it in a string array, throws exception
		// if file is not a PDF
		parsePDF();
		// check file, throws exception if it is not a schedule file
		checkFileIsSchedule();
		// get workers from string array and store
		setWorkers();
		// get hours and department, set in array
		setHoursAndDepartment();
		// process hours into two-dimensional array and edit times for easy
		// calculations later
		processHours();
		// combine departments that have the same function for clearer display
		// on schedule
		processDepartments();
		// get date from the file
		date = setDate();
	}

	/**
	 * Purpose: To strip text from a PDF schedule and place into an array.
	 * 
	 * @throws WrongFileException if an incompatible file is selected.
	 */
	public void parsePDF() throws WrongFileException
	{
		// try loading the document, automatically closes after use
		try (PDDocument document = Loader.loadPDF(file))
		{
			// initialize PDFTextStripper to extract text from document
			PDFTextStripper strip = new PDFTextStripper();
			// extract text from the document
			String text = strip.getText(document);
			// split the text at each newLine into an array
			this.documentData = text.split("\n");
		}
		catch (IOException e)
		{
			// throw exception if there is an issue with loading the file
			throw new WrongFileException();
		}
	}

	/**
	 * Purpose: To check that the PDF loaded in was a valid schedule PDF
	 * 
	 * @throws WrongFileException if the selected PDF was not a valid schedule
	 */
	public void checkFileIsSchedule() throws WrongFileException
	{
		// if shift notes are not found in the document
		if (!(documentData[1].trim().equalsIgnoreCase("Shift Notes")))
		{
			// throw new exception
			throw new WrongFileException();
		}
	}

	/**
	 * Purpose: To create an array of employees on the daily schedule
	 */
	public void setWorkers()
	{
		// initialize and arraylist to hold the employee names
		workers = new ArrayList<>();

		// set a new regular expression pattern to look for the specific place
		// in the document the employee names will be
		String regex = "(?!01411)\\d{5}";
		Pattern pattern = Pattern.compile(regex);

		// iterate through the array containing the entire document
		for (int i = 0; i < documentData.length; i++)
		{
			// pass each element to a matcher
			Matcher matcher = pattern.matcher(documentData[i]);
			// if match is found
			if (matcher.find())
			{
				// capitalize the first letter of the employee name
				char firstLetter = Character
						.toUpperCase(documentData[i - 1].charAt(0));
				// add the employee to the array
				workers.add(
						firstLetter + documentData[i - 1].trim().substring(1));
			}
		}
	}

	/**
	 * Purpose: To create an array of hours and departments from the schedule.
	 */
	public void setHoursAndDepartment()
	{
		// initialize arrays to hold hours and departments
		rawHours = new ArrayList<>();
		department = new ArrayList<>();

		// initialize regular expression to find locations in the text of shift
		// hours and department
		String regex = "Hrs\\d.*";
		Pattern pattern = Pattern.compile(regex);

		// iterate through the array of schedule information
		for (int i = 0; i < documentData.length; i++)
		{
			// pass each element to a matcher
			Matcher matcher = pattern.matcher(documentData[i]);
			// if a match is found
			if (matcher.find())
			{
				// add hours to the rawhours array
				rawHours.add(matcher.group().substring(3));
				// add department to the department array
				department.add(documentData[i + 1].trim());
			}
		}
	}

	/**
	 * Purpose: Processes the data from the raw hours into integers for easy
	 * calculations later and stores in a multidimensional array for easy
	 * access.
	 */
	public void processHours()
	{
		// get size of array
		final int DIMENSION = rawHours.size();
		// initialize array to hold finalized processed hours
		processedHours = new int[DIMENSION][2];
		// initialized array to be used for processing
		String[][] preProcessedHours = new String[DIMENSION][2];

		// iterate through elements in array
		for (int i = 0; i < DIMENSION; i++)
		{
			// split each element of the raw hours into an array
			preProcessedHours[i] = rawHours.get(i).split("-");
		}

		// initialize pattern recognition to read times from each array
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma");

		// iterate through the two-dimensional array
		for (int i = 0; i < DIMENSION; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				// time of each element here is from a 12-hour clock with am/pm
				// parse the time of each element, store as a LocalTime object
				LocalTime time = LocalTime.parse(preProcessedHours[i][j],
						formatter);
				// get the time of each element, time will be returned as on a 24-hour clock
				// convert each time into an integer that represents the minutes of the day
				int t = time.getHour() * 60 + time.getMinute();
				// store the integer in the array
				processedHours[i][j] = t;
			}
		}
	}

	/**
	 * Purpose: To combine multiple department codes that serve the same function into a single department
	 */
	public void processDepartments()
	{
		// iterate through the list of departments
		for (int i = 0; i < department.size(); i++)
		{
			// Footwear and Footwear Ops serve the same function, rename all "Footwear Ops" departments to "Footwear"
			if (department.get(i).equalsIgnoreCase("Footwear Ops"))
			{
				department.set(i, "Footwear");
			}
			// CSS and Cashier serve the same function, rename all "CSS" departments to "Cashier"
			else if (department.get(i).equalsIgnoreCase("CSS"))
			{
				department.set(i, "Cashier");
			}
		}
	}

	/**
	 * Purpose: To return a list of employees
	 * @return employees, the list of employees
	 */
	public String[] getWorkers()
	{
		// initialize new array of employees
		String[] employees = new String[workers.size()];
		//iterate through array of workers
		for (int i = 0; i < workers.size(); i++)
		{
			// add current worker to same position on employee list
			employees[i] = workers.get(i);
		}
		return employees;
	}

	/**
	 * Purpose: To return an array of hours
	 * @return hours, the hours worked by each employee
	 */
	public int[][] getHours()
	{
		// initialize new two-dimensional array of hours to the proper size
		int[][] hours = new int[processedHours.length][2];
		// iterate through two-dimensional array of processed hours
		for (int i = 0; i < processedHours.length; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				// set each element of hours to that of processedHours
				hours[i][j] = processedHours[i][j];
			}
		}
		return hours;
	}

	/**
	 * Purpose: To return an array of departments
	 * @return departments, the department each employee is assigned to
	 */
	public String[] getDepartments()
	{
		// initialize new array of departments
		String[] departments = new String[department.size()];
		// iterate through each element of the department array
		for (int i = 0; i < department.size(); i++)
		{
			// set each element in departments to that same as that of the department array
			departments[i] = department.get(i);
		}
		return departments;
	}

	/**
	 * Purpose: To parse and set the date from the document
	 * @return parsedDate the date of the schedule
	 */
	public LocalDate setDate()
	{
		// initialize a formatter to recognize a date pattern, set to case-insensitive
		DateTimeFormatter f = new DateTimeFormatterBuilder()
				.parseCaseInsensitive().appendPattern("EEEE, MMM dd, yyyy")
				.toFormatter();

		// Iterate through each element in the array
		for (String element : documentData)
		{
			try
			{
				// try to parse a date from each element in the array
				LocalDate parsedDate = LocalDate.parse(element.trim(), f);
				// return the first date found
				return parsedDate;
			}
			catch (Exception e)
			{
				// if a date cannot be parsed from that element, do nothing, continue to next element
			}
		}
		return null;
	}

	/**
	 * Purpose: To return the date from the schedule as a string in a desired format
	 * @return date, the formatted date
	 */
	public String getDate()
	{
		// set format for date to be returned
		// ex: date should read "Monday - 10/14/2024"
		DateTimeFormatter f = DateTimeFormatter.ofPattern("EEEE - MM/dd/yyyy");
		return date.format(f);
	}
}
