package framelisteners.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Clients.ViciClient;
import Database.Database;
import Database.Columns.LeadColumns;
import Database.Tables.Tables;
import source.CSVFrame;
import table.Record;

public class AddToVici implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String password = JOptionPane.showInputDialog("What is the password?");
		if(!password.equalsIgnoreCase("Winston4503"))
			return;
		Database database = new Database("MT_MARKETING");
		try {
			if(!database.login())
				return;
			String list_id = JOptionPane.showInputDialog("What list id would you like to add to?");
			if(list_id==null)
				return;
			ViciClient client = new ViciClient();
			for(Record record: CSVFrame.model.data) {
				ResultSet set = database.select(Tables.LEADS, null, LeadColumns.PHONE_NUMBER+" = ?", new String[] {record.getPhone()});
				if(set.next())
					continue;
				client.AddLead(record,list_id);
				set.close();
			}
		} catch(SQLException ex) {
			JOptionPane.showConfirmDialog(null, ex.getMessage());
		}
		JOptionPane.showMessageDialog(null,"COMPLETE");
	}
}
