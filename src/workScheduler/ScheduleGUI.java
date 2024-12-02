package workScheduler;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

public class ScheduleGUI extends JFrame
{
	JButton openScheduleButton;
	
	
	public static void main(String agrgs[])
	{
		new ScheduleGUI();
	}
	
	public ScheduleGUI()
	{
		int WIDTH = 650;
		int HEIGHT = 500;
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setTitle("Schedul Creator");
		this.setLayout(new GridLayout(4,1));
		
		JLabel fieldLabel = new JLabel("File");
		this.add(fieldLabel);
		
		JTextField fileDisplay = new JTextField();
		this.add(fileDisplay);
		
		JFileChooser fc = new JFileChooser();
		
		JButton selectFileButton = new JButton("Create Schedule");
		selectFileButton.addActionListener(new CreateScheduleButtonListener(fc, fileDisplay));
		this.add(selectFileButton);
		
		openScheduleButton = new JButton("Open Schedule");
		openScheduleButton.setEnabled(false);
		openScheduleButton.addActionListener(new OpenScheduleButtonListener());
		this.add(openScheduleButton);
	
		
		this.setVisible(true);
	}
	
	private class CreateScheduleButtonListener implements ActionListener
	{
		private JFileChooser fileChooser;
		private JTextField textField;
		
		public CreateScheduleButtonListener(JFileChooser fileChooser, JTextField textField)
		{
			this.fileChooser = fileChooser;
			this.textField = textField;
		}
		@Override
		public void actionPerformed(ActionEvent e)
		{			
			int value = fileChooser.showOpenDialog(null);			
			if (value == JFileChooser.APPROVE_OPTION)
			{
				File file = fileChooser.getSelectedFile();
				textField.setText(file.getName());
				new ScheduleBuilder(file);
				openScheduleButton.setEnabled(true);
			}
		}
		
		
	}
	
	private class OpenScheduleButtonListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			Spreadsheet.openFile();
		}
		
	}
}
