package framelisteners.Database.DoctorChase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import Clients.DatabaseClient;
import source.CSVFrame;
import table.Record;

public class LoadDMEFaxable implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(false);
		ResultSet set = client.LoadDMEFaxables();
		try {
			while(set.next()) {
				Record record = new Record();
				record.setDME(set);
				CSVFrame.model.addRow(record);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
}
