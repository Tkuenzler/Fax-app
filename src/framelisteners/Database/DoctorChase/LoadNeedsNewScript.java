package framelisteners.Database.DoctorChase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Clients.DatabaseClient;
import source.CSVFrame;
import table.Record;

public class LoadNeedsNewScript implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		DatabaseClient db = new DatabaseClient(false);
		if(db==null)
			return;
		ResultSet set = db.loadNeedsNewScript();
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
