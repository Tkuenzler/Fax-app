package framelisteners.update.frames;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import Doctor.Doctor;
import source.CSVFrame;
import source.JSONParser;
import table.MyTableModel;

@SuppressWarnings("serial")
public class UpdateDoctorFrame extends JFrame {
	ArrayList<Integer> badDoctors;
	JProgressBar progressBar;
	public UpdateDoctorFrame()  {
		this.setBounds(50, 50, 300, 75);
	    Container content = this.getContentPane();
	    progressBar = new JProgressBar(0,CSVFrame.table.getRowCount());
	    progressBar.setStringPainted(true);
	    progressBar.setValue(0);
	    Border border = BorderFactory.createTitledBorder("Loading...");
	    progressBar.setBorder(border);
	    content.add(progressBar, BorderLayout.NORTH);
	    this.setVisible(true);
	    loadDoctors();
  }
  private void loadDoctors() {
	  MyTableModel model = CSVFrame.model;
	  int rows = model.getRowCount();
	  try {
		    badDoctors = new ArrayList<Integer>();
		  	for(int i = 0;i<rows;i++) {	
				String npi = model.getValueAt(i, MyTableModel.NPI).trim();
				if(npi.equalsIgnoreCase("")) {
					badDoctors.add(i);
					continue;
				}
				HttpURLConnection connection = (HttpURLConnection) new URL("https://npiregistry.cms.hhs.gov/api/?version=2.0&number="+npi).openConnection();
				connection.connect();
				BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				Doctor d = JSONParser.CreateDoctor(rd.readLine());
					if(d==null) {
						badDoctors.add(i);
						continue;
				}
				model.updateValue(d.firstName, i, MyTableModel.DR_FIRST);
				model.updateValue(d.lastName, i, MyTableModel.DR_LAST);
				model.updateValue(d.practiceAddress1, i, MyTableModel.DR_ADDRESS1);
				model.updateValue(d.practiceCity, i, MyTableModel.DR_CITY);
				model.updateValue(d.practiceState, i, MyTableModel.DR_STATE);
				model.updateValue(d.practiceZipeCode, i, MyTableModel.DR_ZIP);
				model.updateValue(d.practiceFax, i, MyTableModel.DR_FAX);
				model.updateValue(d.practicePhone, i, MyTableModel.DR_PHONE);	
				final int count = i;
				SwingUtilities.invokeLater(new Runnable() {
		               public void run() {
		            	   progressBar.setValue(count);
		               }
		            });
				connection.disconnect();
				rd.close();
		}
	} catch (MalformedURLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	if(!badDoctors.isEmpty()) {
		int dialogButton = JOptionPane.showConfirmDialog (null, "Would you like to remove records with incorrect bad doctor infrmation?","WARNING", JOptionPane.YES_NO_OPTION);
		if(dialogButton==JOptionPane.YES_OPTION) {
			for(int i = 0;i<badDoctors.size();i++) {
				int row = badDoctors.get(i).intValue();
				CSVFrame.model.deleteRow(row-i);
			}
			JOptionPane.showMessageDialog(new JFrame(), badDoctors.size()+" rows removed");
		}
		
	}
	this.dispose();
  }
}