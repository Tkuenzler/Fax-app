package framelisteners.Database.DoctorChase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import Clients.DatabaseClient;
import Clients.RoadMapClient;
import source.CSVFrame;
import table.Record;

public class LoadBadFaxNumbers implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		RoadMapClient roadmap = new RoadMapClient();
		String pharmacy = roadmap.getPharmacy();
		roadmap.close();
		if(pharmacy==null)
			return;
		DatabaseClient db = new DatabaseClient(false);
		if(db==null)
			return;
		ResultSet set = db.loadBadFaxNumbers(pharmacy);
		try {
			while(set.next()) {
				CSVFrame.model.addRow(new Record(set,db.getDatabaseName(),db.getTableName()));
			}
			set.close();
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		db.close();
	}

}
