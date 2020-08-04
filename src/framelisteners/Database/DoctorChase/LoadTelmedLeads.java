package framelisteners.Database.DoctorChase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Clients.DatabaseClient;
import source.CSVFrame;
import table.Record;

public class LoadTelmedLeads implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(false);
		if(client.getDatabaseName()==null || client.getTableName()==null) 
			return;
		ResultSet set = client.loadTelmedLeads();
		try {
			while(set.next()) {
				CSVFrame.model.addRow(new Record(set,client.getDatabaseName(),client.getTableName()));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.close();
	}

}
