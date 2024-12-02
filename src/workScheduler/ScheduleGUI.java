package workScheduler;

import java.awt.*;

import javax.swing.*;

public class ScheduleGUI extends JFrame
{
	
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
		
		this.setLayout(new GridLayout(1,4));
		createFileSelector();
		
		
		
		
		this.setVisible(true);
	}
	
	private void createFileSelector()
	{
		JFileChooser fc = new JFileChooser();
		fc.setAccessory(new ImagePreview(fc));
		
		this.add(fc);
	}
	

	
}
