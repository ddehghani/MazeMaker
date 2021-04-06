import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.awt.geom.*;
import java.awt.datatransfer.*;

public class MazeMaker extends JFrame {

	final int MAZE_MIN_SIZE = 3, MAZE_MAX_SIZE = 15;
	private JButton[][] tableButtons;
	private JSpinner rowSpinner, colSpinner;
	private JTextArea textArea;
	private JPanel table;
	
	public MazeMaker() {

		try {
  			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
 		} catch(Exception e) {
  			e.printStackTrace(); 
		}

		setTitle("Maze Maker");
		setSize(600, 630);
		setMinimumSize(new Dimension(450, 550));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(createSelectionArea(),BorderLayout.PAGE_START);
		add(createTableArea(),BorderLayout.CENTER);
		add(createResultArea(),BorderLayout.PAGE_END);	

		setVisible(true);
	}

	public JPanel createSelectionArea() {
		//create the spinners
		rowSpinner = new JSpinner(new SpinnerNumberModel(3, MAZE_MIN_SIZE, MAZE_MAX_SIZE, 1));
		colSpinner = new JSpinner(new SpinnerNumberModel(5, MAZE_MIN_SIZE, MAZE_MAX_SIZE, 1));
		rowSpinner.addChangeListener(event -> {refreshTableContent();	refreshTextPanelContent();});
		colSpinner.addChangeListener(event -> {refreshTableContent();	refreshTextPanelContent();});

		//layout
		JPanel selectionArea = new JPanel();
		selectionArea.setLayout(new FlowLayout(FlowLayout.CENTER, 6,20));
		selectionArea.add(new JLabel("Number of rows:"));   
		selectionArea.add(rowSpinner);
		selectionArea.add(new JLabel("       "));//creates space
		selectionArea.add(new JLabel("Number of columns:"));
		selectionArea.add(colSpinner);
		return selectionArea;
	}

	public JPanel createTableArea() {
		table = new JPanel();
		
		TitledBorder titleOfTable = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Click to Draw Your Maze!");
		titleOfTable.setTitleJustification(TitledBorder.CENTER);
		table.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10,25,10,25) , titleOfTable));

		refreshTableContent();

		JPanel tableArea = new JPanel();
		tableArea.setLayout(new BorderLayout());
		tableArea.add(table, BorderLayout.CENTER);
		return tableArea;
	}

	public void refreshTableContent() {
		table.removeAll();
		table.setLayout(new GridLayout((int)rowSpinner.getValue(), (int)colSpinner.getValue()));
		tableButtons = new JButton[(int)rowSpinner.getValue()][(int)colSpinner.getValue()];
		for (int rows = 0; rows < (int)rowSpinner.getValue(); rows++)
			for (int cols = 0; cols < (int)colSpinner.getValue(); cols++)
			{	
				tableButtons[rows][cols] = new JButton();
				tableButtons[rows][cols].setBackground(Color.white);
				tableButtons[rows][cols].addActionListener(
					event -> {
								((JButton)event.getSource()).setBackground((((JButton)event.getSource()).getBackground().equals(Color.black))? Color.white : Color.black);
								refreshTextPanelContent();
							});
				table.add(tableButtons[rows][cols]);
			}
	}

	public JPanel createResultArea() {
		textArea = new JTextArea();
		refreshTextPanelContent();
		
		//text field layout
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		textPanel.add(new JPanel());

		c.gridx = 1;
		c.gridwidth = 4;
		c.fill = GridBagConstraints.BOTH;

		textPanel.add(textArea);

		c.gridx = 5;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		textPanel.add(new JPanel());
		

		//General layout
		JPanel resultArea = new JPanel();
		resultArea.setLayout(new BoxLayout(resultArea, BoxLayout.PAGE_AXIS));
		
		resultArea.add(Box.createRigidArea(new Dimension(0,15)));
		
		JPanel lable = new JPanel(); 
		lable.add(new JLabel("Equivalant table is:"));
		resultArea.add(lable);
		
		resultArea.add(Box.createRigidArea(new Dimension(0,8))); //empty space

		resultArea.add(textPanel);

		resultArea.add(Box.createRigidArea(new Dimension(0,15))); //empty space
		
		JButton copyButton = new JButton("Copy to clipboard!");
		copyButton.addActionListener(event -> {
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(textArea.getText()), null);
				});

		JPanel buttonPanel = new JPanel(); 
		buttonPanel.add(copyButton);
		resultArea.add(buttonPanel);

		resultArea.add(Box.createRigidArea(new Dimension(0,20))); //empty space

		return resultArea;
	}

	public void refreshTextPanelContent() {
		//create the string equivalent of maze
		String result = "int[][] maze = {\n";
		for (int rows = 0; rows < (int)rowSpinner.getValue(); rows++)
		{
			result += "\t{";
			for (int cols = 0; cols < (int)colSpinner.getValue(); cols++)
				result += (tableButtons[rows][cols].getBackground().equals(Color.black)? "1" : "0") + ", ";
			result = result.substring(0,result.length()-2) + "},\n";
		}
		result = result.substring(0,result.length()-2) + "\n\t};";
		textArea.setText(result);
	}

	public static void main(String[] args) {    
    	new MazeMaker();    
	}
}