package workScheduler;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.*;

/**
 * This class acts as an interface for a user to create a schedule of employees.
 * The interface has a simple display with a graphic showing the company logo
 * and a field to display a file that is selected. This GUI has two buttons, one
 * to open and use a specific file to create a schedule and another that will
 * open the schedule in Excel, allowing the user to continue to edit for a
 * finalized copy.
 */
public class ScheduleGUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JTextField fileDisplay; // HAS-A field to display the input file
	private JFileChooser fileChooser; // HAS-A File Chooser
	JButton createScheduleButton;
	private Color colorDSG = Color.decode("#006756"); // HAS-A specific color
	private int WIDTH = 300; // HAS-A width
	private int logoHeight = 150; // HAS-A logo height
	private String saveFile = "test.xlsx"; // HAS-A file name to save to

	public static void main(String agrgs[])
	{
		new ScheduleGUI();
	}

	/**
	 * Purpose: Create a JFrame window and add all components for display.
	 */
	public ScheduleGUI()
	{
		// assign window x-button an action
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// set window title
		this.setTitle("Daily Lineup");

		// create new panel to add components to
		JPanel mainPanel = new JPanel();
		// choose box layout, set new components to be added vertically
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// create each component panel and add it to main
		mainPanel.add(createLogoPanel());
		mainPanel.add(createFileDisplayPanel());
		mainPanel.add(createChooseFileButtonPanel());
		mainPanel.add(createOpenScheduleButtonPanel());

		// add the main panel to the frame
		this.add(mainPanel);
		// set frame size based on included components
		this.pack();
		// set window to open in center of screen
		this.setLocationRelativeTo(null);
		// set window visible
		this.setVisible(true);
	}

	/**
	 * Purpose: To create a panel for displaying a selected input file.
	 * 
	 * @return fileDisplayPanel the panel which displays the selected file.
	 */
	private JPanel createFileDisplayPanel()
	{
		// create a new panel to hold the text field
		JPanel fileDisplayPanel = new JPanel();
		// add title to jpanel to describe what it it shows.
		fileDisplayPanel.setBorder(BorderFactory.createTitledBorder("File:"));

		// set the initial size of the text field
		fileDisplay = new JTextField(25);
		// user cannot edit the text field
		fileDisplay.setEditable(false);
		// user cannot select the text field
		fileDisplay.setFocusable(false);
		// add the text field to the panel
		fileDisplayPanel.add(fileDisplay);

		return fileDisplayPanel;
	}

	/**
	 * Purpose: To create a panel and a button that allow the user to select an
	 * input file from their computer.
	 * 
	 * @return buttonPanel a panel with a button for file selection
	 */
	private JPanel createChooseFileButtonPanel()
	{
		// create a new panel to hold the button
		JPanel buttonPanel = new JPanel();

		// set the background color to DSG's company color
		buttonPanel.setBackground(colorDSG);
		// set the width to half of the overall frame width, height is 1/3 of
		// the logo height
		buttonPanel.setSize(WIDTH / 2, logoHeight / 3);

		// initialize the file chooser, open to current working directory
		fileChooser = new JFileChooser(
				new File(System.getProperty("user.dir")));
		// create button, give it a title
		JButton selectFileButton = new JButton("Choose File");
		// add action listener to the button
		selectFileButton.addActionListener(new SelectFileButtonListener());
		// add button to panel
		buttonPanel.add(selectFileButton);

		return buttonPanel;
	}

	/**
	 * Purpose: To create a panel with the DSG logo
	 * 
	 * @return logoPanel a panel to show the DSG company logo
	 */
	private JPanel createLogoPanel()
	{
		// create a new panel to hold the logo
		JPanel logoPanel = new JPanel();
		// create a new label to hold the image
		JLabel logo = new JLabel();
		// get image from file, scale to desired size
		ImageIcon logoIcon = new ImageIcon(
				new ImageIcon("dicks-sporting-goods-logo.jpg").getImage()
						.getScaledInstance(WIDTH, logoHeight,
								Image.SCALE_DEFAULT));
		// add image to the label
		logo.setIcon(logoIcon);
		// add label to the panel
		logoPanel.add(logo);

		return logoPanel;
	}

	/**
	 * Purpose: To create a panel and button which opens the created schedule in
	 * excel and allows the user to continue making edits.
	 * 
	 * @return buttonPanel the panel which has a button to open the created
	 *         schedule
	 */
	private JPanel createOpenScheduleButtonPanel()
	{
		// create new panel to hold button
		JPanel buttonPanel = new JPanel();
		// set background color to DSG company color
		buttonPanel.setBackground(colorDSG);
		// set size to half the width of the frame and height to 1/3 of the logo
		// height
		buttonPanel.setSize(WIDTH / 2, logoHeight / 3);
		// create button and give it a label
		createScheduleButton = new JButton("Create Schedule");
		// disable button so it cannot be clicked by user
		createScheduleButton.setEnabled(false);
		// add action listener to button
		createScheduleButton
				.addActionListener(new OpenScheduleButtonListener());
		// add button to the panel
		buttonPanel.add(createScheduleButton);

		return buttonPanel;
	}

	/**
	 * Purpose: When the select file button is clicked by the user a file
	 * chooser is opened and allows the user to select a specific file for
	 * input. A schedule is generated from the file that was selected. This
	 * class also contains exception handling and will display messages if the
	 * user has selected an incompatible file or if the file created is unable
	 * to be opened.
	 */
	private class SelectFileButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// show file chooser window centered on the screen.
			// initialize value as the return value from file chooser
			int value = fileChooser.showOpenDialog(null);
			// if the user selected a file
			if (value == JFileChooser.APPROVE_OPTION)
			{
				// initialize new file object with the selected file
				File file = fileChooser.getSelectedFile();
				// display selected file name in text field
				fileDisplay.setText(file.getName());

				try
				{
					// pass schedule file and file name to save as to schedule
					// controller and try to create a schedule
					new ScheduleController(file, saveFile);
					// enable the button to open a created schedule
					createScheduleButton.setEnabled(true);
				}
				// if a user selects an incompatible file, display an error
				// message
				catch (WrongFileException e1)
				{
					// open a new option pane displaying error message to the
					// user
					JOptionPane.showMessageDialog(null, e1.getMessage());
					// disable the button to open the schedule
					createScheduleButton.setEnabled(false);
				}
				// if a file cannot be written, catch the exception and display
				// a message
				catch (CannotWriteFileException e2)
				{
					// open a new option pane displaying the error message to
					// the user
					JOptionPane.showMessageDialog(null, e2.getMessage());
					// disable the button to open the schedule
					createScheduleButton.setEnabled(false);
				}
			}
		}
	}

	/**
	 * Purpose: When the user clicks the button to open the schedule, open the
	 * schedule using Excel. This Class contains exception handling and will
	 * display a message if the file cannot be opened.
	 */
	private class OpenScheduleButtonListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				// try to open the created schedule
				Spreadsheet.openFile(saveFile);
			}
			// catch an exception where the file is unable to be opened
			catch (FileNotFoundException e1)
			{
				// open a new option pane and display the exception message to
				// the user
				JOptionPane.showMessageDialog(null, e1.getMessage());
			}
			finally
			{
				// always disable the button after it has been used
				createScheduleButton.setEnabled(false);
			}
		}
	}
}
