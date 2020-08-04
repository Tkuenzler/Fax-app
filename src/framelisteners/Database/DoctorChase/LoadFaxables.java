package framelisteners.Database.DoctorChase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Clients.DatabaseClient;
import Clients.RoadMapClient;
import source.CSVFrame;
import table.Record;

public class LoadFaxables implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(false);
		RoadMapClient roadmap = new RoadMapClient();
		String pharmacy = roadmap.getPharmacy();
		roadmap.close();
		if(pharmacy==null)
			return;
		ResultSet set = client.loadFaxableLeads(pharmacy);
		try {
			while(set.next()) {
				CSVFrame.model.addRow(new Record(set,client.getDatabaseName(),client.getTableName()));
			}
			set.close();
			client.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
