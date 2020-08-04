package framelisteners.telmed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import Clients.DatabaseClient;
import Fax.TelmedStatus;
import source.CSVFrame;
import table.Record;

public class LoadUnSentTelmed implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(false);
		ResultSet set = client.customQuery("SELECT * FROM `TELMED` WHERE (`PHARMACY` = 'La_Plaza' OR `PHARMACY` = 'Oaks') AND "+TelmedStatus.GetApprovedPaidStatusQuery()+" AND `PHARMACY_STATUS` = ''");
		//("SELECT * FROM `TELMED` WHERE `PHARMACY` = 'Lake_Ida' AND `TELMED_STATUS` = '"+TelmedStatus.XFER+"'");
		try {
			while(set.next()) {
				Record record = new Record(set,"","");
				record.setType(set.getString("DATE_MODIFIED"));
				record.setStatus(set.getString("TELMED_ID"));
				CSVFrame.model.addRow(record);
			}
			set.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
