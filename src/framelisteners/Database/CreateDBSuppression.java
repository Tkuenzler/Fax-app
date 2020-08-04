package framelisteners.Database;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Clients.DatabaseClient;
import subframes.FileChooser;

public class CreateDBSuppression implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		File file = FileChooser.SaveCsvFile();
		ArrayList<String> list = new ArrayList<String>() {
			@Override 
			public boolean contains(Object o) {
				String s = (String) o;
				for(String s2: this) {
					if(s.equalsIgnoreCase(s2))
						return true;
				}
				return false;
			}
		};
		DatabaseClient client = new DatabaseClient(false);
		BufferedWriter bw = null;
		ResultSet set = client.getPhoneNumbers("Leads");
		try {
			while(set.next()) {
				String phone = set.getString("phonenumber");
				if(!list.contains(phone))
					list.add(phone);
			}
			bw = new BufferedWriter(new FileWriter(file));
			bw.write("Phonenumber");
			bw.newLine();
			for(String s: list) {
				bw.write(s);
				bw.newLine();
			}
			bw.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e) {
		
		} finally {
			try {
				set.close();
				client.close();
				bw.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(IOException e) {
				
			}	
		} 
		JOptionPane.showMessageDialog(null, "Completed Suppression file");
	}
}

