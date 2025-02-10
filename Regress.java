import java.util.ArrayList;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

class Regress extends JFrame implements ActionListener
{
	static public ArrayList<Float> xArray = new ArrayList<Float>();
	static public ArrayList<Float> yArray = new ArrayList<Float>();
	static public float m,b;
	static public int file_flag = -1, reg_flag = -1;
	
	JPanel pnl = new JPanel();
	JLabel title_lab = new JLabel("Linear Regression");
	JLabel filestatus_lab = new JLabel("No file opened");
	static JTextField reg_output = new JTextField("regression output will show here",50);
	static JTextField file_input = new JTextField("file to open",50);
	JButton file_btn = new JButton("OpenFile");
	JButton reg_btn = new JButton("Perform Regression");
	
	public Regress() {
		super( "Swing Window");
		setSize(500,300);
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		add( pnl );
		setVisible( true );
		title_lab.setHorizontalTextPosition(JLabel.CENTER);
		//filestatus_lab.setHorizontalTextPosition(JLabel.RIGHT);
		pnl.add(title_lab);
		pnl.add(file_input);
		pnl.add(file_btn);
		pnl.add(filestatus_lab);
		pnl.add(reg_btn);
		pnl.add( reg_output );
		reg_btn.setEnabled(false);
		file_btn.addActionListener(this);
		reg_btn.addActionListener(this);
	}
	
	//add button for file saving and add function to implement it.
	
	public void actionPerformed(ActionEvent event) {
			String file_to_read;
			
			if (event.getSource() == file_btn) {
				file_to_read = file_input.getText();
				file_opener(file_to_read);
				if (file_flag != -1) {
					filestatus_lab.setText(file_to_read +" opened and read");
				        reg_btn.setEnabled(true);
				} else {
					filestatus_lab.setText("Could not open file!");
				}
					
			} else if (event.getSource() == reg_btn) {
				regression();
				if (reg_flag != -1) {
					reg_output.setText("best fit line is: y = " + m + "x + " +b);
				} else {
					reg_output.setText("Could not perform regression!");
				}
			}
	}
	
	public static void main ( String[] args )
	{
		Regress gui = new Regress();
		
	}
	
	public static void file_opener(String file_name) {
		int sepInd;
		float xTemp,yTemp;
		
		try {
			FileReader fr = new FileReader(file_name);
			BufferedReader buf = new BufferedReader(fr);
			String line = "";
			String lineTemp;
			
			while((line = buf.readLine()) != null) {
				sepInd = line.indexOf(",");
				if (sepInd != -1) {
					lineTemp = line.substring(0,sepInd-1);
					xTemp = Float.parseFloat(lineTemp);
					xArray.add(xTemp);
					lineTemp = line.substring(sepInd+1);
					yTemp = Float.parseFloat(lineTemp);
					yArray.add(yTemp);
					file_flag = 1;
				} else {
					System.out.println("Could not parse file!");
				}
			}
		} catch (IOException e) {
			System.out.println("Could not open file");
		}
	}
	
	public static void regression() {
		float sumx = 0.0f, sumy = 0.0f, xx = 0.0f, xy = 0.0f;
		int num_points;
		num_points = xArray.size();
		
		for (int i = 0; i < num_points; i++) {
			sumx += xArray.get(i);
			xx += xArray.get(i)*xArray.get(i);
			sumy += yArray.get(i);
			xy += xArray.get(i)*yArray.get(i);
		}
		
		m = (num_points*xy - sumx*sumy)/(num_points*xx - sumx*sumx);
		b = (sumy - m*sumx)/num_points;
		reg_flag = 1;
	}
}
