package framelisteners.Database.DoctorChase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Clients.DatabaseClient;
import Clients.RoadMapClient;
import source.CSVFrame;
import table.Record;

public class LoadApprovals implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		int daysBack = 0;
		try {
			daysBack = Integer.parseInt(JOptionPane.showInputDialog(null, "How many days back?"));
		} catch(NumberFormatException ne) {
			JOptionPane.showMessageDialog(null, "Not a valid number");
			return;
		}
		DatabaseClient db = new DatabaseClient(false);
		if(db==null)
			return;
		RoadMapClient roadmap = new RoadMapClient();
		String pharmacy = roadmap.getPharmacy();
		roadmap.close();
		if(pharmacy==null)
			return;
		ResultSet set = db.loadApprovals(daysBack,pharmacy);
		try {
			while(set.next()) {
				CSVFrame.model.addRow(new Record(set,db.getDatabaseName(),db.getTableName()));
			}
			set.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.close();

	}	
}
