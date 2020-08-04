package framelisteners.Database.Load;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Clients.DatabaseClient;
import Clients.DatabaseClient.Columns;
import source.CSVFrame;
import subframes.FileChooser;
import table.Record;

public class LoadbyPhone implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		ArrayList<String> list = new ArrayList<String>() {
			@Override
			public boolean contains(Object o) {
				String compare = o.toString();
				for(String s: this){
					if(s.equalsIgnoreCase(compare))
						return true;
				}
				return false;
			}
		};
		File file  = FileChooser.OpenCsvFile("Phonenumber list");
		DatabaseClient client = new DatabaseClient(false);	
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		
			String line = null;
			while((line=br.readLine())!=null) {
				if(!list.contains(line))
					list.add(line);
			}
			System.out.println("LOADED FILE");
			for(String phone: list) {
				String query = "SELECT * FROM `"+client.getTableName()+"` WHERE `"+Columns.PHONE_NUMBER+"` = '"+phone+"'";
				ResultSet set = client.customQuery(query);
				try {
					while(set.next()) {
						CSVFrame.model.addRow(new Record(set,client.getDatabaseName(),client.getTableName()));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
				System.out.println("CLOSED");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		JOptionPane.showMessageDialog(null, CSVFrame.model.getRowCount()+" out of "+CSVFrame.model.getRowCount()+" loaded");
	}
}
