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
	static JTextField file_save = new JTextField("filename for saving",50);
	JButton filesave_btn = new JButton("Save File");
	JLabel filesave_lab = new JLabel("No file saved");
	
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
		pnl.add( file_save);
		pnl.add(filesave_btn);
		pnl.add(filesave_lab);
		reg_btn.setEnabled(false);
		filesave_btn.setEnabled(false);
		file_btn.addActionListener(this);
		reg_btn.addActionListener(this);
		filesave_btn.addActionListener(this);
	}
	
	//file output and GUI for ouput complete! Remote repo comment.
	
	public void actionPerformed(ActionEvent event) {
			String file_to_read, file_to_write;
			
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
					filesave_btn.setEnabled(true);
					file_writer("test_out.txt");
				} else {
					reg_output.setText("Could not perform regression!");
				}
			} else if (event.getSource() == filesave_btn) {
				file_to_write = file_save.getText();
				file_writer(file_to_write);
				filesave_lab.setText("Wrote file :" + file_to_write);
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
	
	public static void file_writer(String file_name) {
		
		try {
			FileWriter fw = new FileWriter(file_name);
			BufferedWriter buf = new BufferedWriter(fw);
			String line = "";
			String lineTemp;
			
			buf.write("m = " + Float.toString(m));
				buf.newLine();
			buf.write("b = " + Float.toString(b));
				buf.newLine();
			buf.close();
			
		} catch (IOException e) {
			System.out.println("Could not write to file");
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
