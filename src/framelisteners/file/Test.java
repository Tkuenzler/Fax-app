package framelisteners.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Database.Database;
import subframes.FileChooser;

public class Test implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String password = JOptionPane.showInputDialog("Password");
		if(!password.equalsIgnoreCase("Winston4503"))
			return;
		Test1();
		
	}
	public void Test1() {
		File file = FileChooser.OpenCsvFile("CBA ZIP CODES");
		BufferedReader br = null;
		Database client = new Database("contacts101");
		try {
			if(!client.login())
				return;
			
			br = new BufferedReader(new FileReader(file));
			String line = null;
			String headers = br.readLine();
			while((line=br.readLine())!=null) {
				String[] data = line.split(",");
				String state = data[0];
				String zip = data[1];
				String zipCode = null;
				switch(zip.length()) {
					case 3: zipCode = "00"+zip; break;
					case 4: zipCode = "0"+zip; break;
					default:zipCode = zip;
				
				}
				try {
					int add = client.insert("DME_CBA_ZIPCODES", new String[] {"ZIP_CODE","STATE"}, new String[] {zipCode,state});
					System.out.println(add);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch(IOException ex) {
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally {
			try {
				if(br!=null)br.close();
			} catch(IOException ex) {
				
			}
		}
	}
}
