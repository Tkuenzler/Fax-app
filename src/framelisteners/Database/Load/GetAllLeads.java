package framelisteners.Database.Load;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Clients.DatabaseClient;
import Clients.RoadMapClient;
import source.CSVFrame;
import table.Record;

public class GetAllLeads implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(false);
		if(client.getDatabaseName()==null || client.getTableName()==null)
			return;
		RoadMapClient roadmap = new RoadMapClient();
		String pharmacy = roadmap.getPharmacy();
		roadmap.close();
		if(pharmacy==null)
			return;
		client.getAllRecords(pharmacy);
		client.close();
	}
	

}
