package DME;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Database.Database;
import Database.Query.Queries;
import Database.Tables.Tables;
import source.CSVFrame;
import table.Record;

public class LoadDMEFaxables implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		int confirm2 = 0;
		int confirm = JOptionPane.showConfirmDialog(null, "Do you want to only load confirmed doctors?","Confirmed Doctors", JOptionPane.YES_NO_OPTION);
		if(confirm==JOptionPane.YES_OPTION)
			confirm2 = 1;
		else
			confirm2 = 0;
		Database client = new Database();
		try {
			if(!client.loginAndChooseDatabase()) {
				JOptionPane.showMessageDialog(null, "FAILED TO LOG IN");
				return;
			}
			client.setTable();
			ResultSet set = client.select(client.getTable(), null, Queries.Select.LOAD_DME_FAXABLES, new Object[] {confirm,confirm2});
			//ResultSet set = client.select(client.getTable(), null, Queries.Select.MBI, null);
			while(set.next()) {
				Record record = new Record();
				record.setDME(set);
				CSVFrame.model.addRow(record);
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			if(client!=null) client.close();
		}
	}
}
