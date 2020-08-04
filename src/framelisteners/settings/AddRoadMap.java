package framelisteners.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Clients.RoadMapClient;
import subframes.FileChooser;

public class AddRoadMap implements ActionListener {
	RoadMapClient client;
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String password = JOptionPane.showInputDialog(null, "What is password?");
		if(!password.equalsIgnoreCase("Winston4503"))
			return;
		int add = JOptionPane.showConfirmDialog(null, "Do you want to add a Road Map", "Road Map", JOptionPane.YES_OPTION);
		File file = FileChooser.OpenCsvFile("Road Map");
		if(add==JOptionPane.YES_OPTION) {
			String tableName = JOptionPane.showInputDialog(null, "What is the phamracy name?");
			client = new RoadMapClient(tableName);
			client.createTable(tableName);
		}
		else {
			client = new RoadMapClient("");
			String[] pharmacies = client.getPharmacies();
			client.setTable((String) JOptionPane.showInputDialog(new JFrame(), "Select a table", "tables:", JOptionPane.QUESTION_MESSAGE, null,pharmacies, pharmacies[0]));
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			String[] HEADERS = br.readLine().split(",");
			while((line = br.readLine())!=null) {
				String[] split = line.split(",");
				for(int i = 1;i<split.length;i++) {
					String state = split[0];
					String pbm = HEADERS[i];
					String accept = split[i];
					client.updateRow(state, pbm, accept);
				}
			}
		} catch(IOException ex) {
			 ex.printStackTrace();
		}
	}
}
