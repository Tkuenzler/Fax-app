package framelisteners.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Database.Database;
import Database.Columns.CarepointColumns;
import Database.Columns.LeadColumns;
import Database.Tables.Tables;
import subframes.FileChooser;
import table.Record;

public class Test implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String password = JOptionPane.showInputDialog("Password");
		if(!password.equalsIgnoreCase("Winston4503"))
			return;
		Test2();
		
	}
	public void Test2() {
		Database client = new Database("MT_MARKETING");
		try {
			if(!client.login())
				return;
			ResultSet set = client.select(Tables.CAREPOINT, null, CarepointColumns.DOB+" = ''", null);
			while(set.next()) {
				String phone = set.getString(CarepointColumns.PHONE_NUMBER);
				ResultSet set2 = client.select(Tables.LEADS, null, LeadColumns.PHONE_NUMBER+" = ?", new String[] {phone});
				if(set2.next()) {
					Record record = new Record(set2,"","");
					System.out.println(record.getDrAddress1()+" LENGTH: "+record.getDrAddress1().length());
					int update = client.update(Tables.CAREPOINT, CarepointColumns.DATA, CarepointColumns.CreateArray(record), CarepointColumns.PHONE_NUMBER+" = '"+record.getPhone()+"'");
					System.out.println(record.getFirstName()+" "+record.getLastName()+" "+update);
				}
			}
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally {
			if(client!=null)client.close();
		}
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
