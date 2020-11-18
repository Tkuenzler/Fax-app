package framelisteners.Scripts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Database.Database;
import Database.Columns.LeadColumns;
import Database.Tables.Tables;
import source.CSVFrame;
import table.Record;

public class LoadScriptRecords implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		/*
		 *GET DATABASE AND LOGIN 
		 */
		
		Database client = new Database();
		try {
			if(!client.loginAndChooseDatabase()) {
				JOptionPane.showMessageDialog(null, "Login Failed");
				return;
			}
			/*
			 * SELECT * ROWS WITH A SCRIPT LOADED AND ADD TO FRAME
			 */
			ResultSet set  = client.select(Tables.LEADS, null, "LENGTH("+LeadColumns.SCRIPT+") > 0", null);
			while(set.next()) {
				CSVFrame.model.addRow(new Record(set,client.getDatabase(),Tables.LEADS));
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		}  finally {
			if(client!=null)client.close();
		}
	}

}
