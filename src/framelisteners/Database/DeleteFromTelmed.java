package framelisteners.Database;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Clients.DatabaseClient;
import subframes.FileChooser;

public class DeleteFromTelmed implements ActionListener {

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
		DatabaseClient client = new DatabaseClient(true);	
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			while((line=br.readLine())!=null) {
				System.out.println(line);
				if(!list.contains(line))
					list.add(line);
			}
			System.out.println("LOADED FILE");
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
		int delete = 0;
		for(String phone: list) {
			String sql = "DELETE FROM `TELMED` WHERE `phonenumber` = '"+phone+"'";
			delete += client.customUpdate(sql);
		}
		client.close();
		JOptionPane.showMessageDialog(null, delete+" out of "+list.size()+" delete");
	}
}
