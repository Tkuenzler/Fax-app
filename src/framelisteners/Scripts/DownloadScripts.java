package framelisteners.Scripts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Database.Database;
import Database.Columns.LeadColumns;
import Database.Tables.Tables;
import source.CSVFrame;
import subframes.FileChooser;
import table.Record;

public class DownloadScripts implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		int count = 0;
		Database client = new Database();
		ResultSet set = null;
		try {
			if(!client.loginAndChooseDatabase()) {
				JOptionPane.showMessageDialog(null, "Login Failed");
				return;
			}
			String folder = FileChooser.OpenFolder("Save Files to:");
			if(folder==null)
				return;
			for(Record record: CSVFrame.model.data) {
				File file = new File(folder+"//"+record.getFirstName()+" "+record.getLastName()+".pdf");
				FileOutputStream output = new FileOutputStream(file);
				set = client.select(Tables.LEADS, null, LeadColumns.PHONE_NUMBER+" = ? AND LENGTH("+LeadColumns.SCRIPT+") > 0", new String[] {record.getPhone()});
				while(set.next()) {
					 InputStream input = set.getBinaryStream(LeadColumns.SCRIPT);
					 byte[] buffer = new byte[1024];
					 while (input.read(buffer) > 0) {
						 output.write(buffer);
					 }
					 output.close();
					 count++;
				}
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JOptionPane.showMessageDialog(null, "DOWNLOADED "+count+" SCRIPTS");
			if(client!=null)client.close();
		}
	}

}
