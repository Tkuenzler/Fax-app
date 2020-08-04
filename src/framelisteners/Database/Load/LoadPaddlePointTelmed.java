package framelisteners.Database.Load;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import Clients.DatabaseClient;
import source.CSVFrame;
import table.Record;

public class LoadPaddlePointTelmed implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		DatabaseClient client = new DatabaseClient(false);
		ResultSet set = client.customQuery("SELECT * FROM `TELMED` WHERE `agent` = 'PADDLEPOINT' AND yearweek(`DATE_ADDED`) = yearweek(DATE_ADD(CURDATE(), INTERVAL -7 DAY))");
		try {
			while(set.next()) {
				CSVFrame.model.addRow(new Record(set,client.getDatabaseName(),client.getTableName()));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	//YEARWEEK(`date`, 1) = YEARWEEK(CURDATE(), 1)
}
