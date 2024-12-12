package workScheduler;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class ScheduleGUI extends JFrame
{
	private JButton openScheduleButton;
	private JTextField fileDisplay;
	private JFileChooser fc;
	private JButton selectFileButton;
	private Color color = Color.decode("#006756");
	private int WIDTH = 300;
	JPanel mainPanel;
	
	
	public static void main(String agrgs[])
	{
		new ScheduleGUI();
	}
	
	public ScheduleGUI()
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setTitle("Daily Lineup");
		this.setLayout(new BorderLayout());
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JPanel logoPanel = new JPanel();
		JLabel logo = new JLabel();
		
		ImageIcon logoIcon = new ImageIcon(new ImageIcon("dicks-sporting-goods-logo.jpg").getImage().getScaledInstance(WIDTH, 150, Image.SCALE_DEFAULT));
		
		logo.setIcon(logoIcon);
		logoPanel.add(logo);
		mainPanel.add(logoPanel);
		
		
		fileDisplay = new JTextField();
		fileDisplay.setEditable(false);
		fileDisplay.setFocusable(false);
		mainPanel.add(fileDisplay);
		
		
		createCreateScheduleButtonPanel();
		
		
		createOpenScheduleButtonPanel();
		
	
		this.add(mainPanel, BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
	}
	
	private void createCreateScheduleButtonPanel()
	{
		JPanel p = new JPanel(new GridBagLayout());
		JPanel q = new JPanel();
		
		p.setBackground(color);
		q.setBackground(color);
		p.setSize(WIDTH, 100);
		q.setSize(WIDTH/2, 50);
		
		
		fc = new JFileChooser(new File(System.getProperty("user.dir")));
		selectFileButton = new JButton("Create Schedule");
		selectFileButton.addActionListener(new CreateScheduleButtonListener(fc, fileDisplay));
		
		q.add(selectFileButton);
		p.add(q);
		
		mainPanel.add(p);
	}
	
	private void createLogoPanel()
	{
		
	}
	
	private void createOpenScheduleButtonPanel()
	{
		JPanel p = new JPanel(new GridBagLayout());
		JPanel q = new JPanel();
		
		p.setBackground(color);
		q.setBackground(color);
		p.setSize(WIDTH, 100);
		q.setSize(WIDTH/2, 50);
		
		
		
		openScheduleButton = new JButton("Open Schedule");
		openScheduleButton.setEnabled(false);
		openScheduleButton.addActionListener(new OpenScheduleButtonListener());
		
		q.add(openScheduleButton);
		p.add(q);
		
		mainPanel.add(p);
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
			openScheduleButton.setEnabled(false);
		}
		
	}
}
