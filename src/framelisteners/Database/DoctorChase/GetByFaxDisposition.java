package framelisteners.Database.DoctorChase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Clients.DatabaseClient;
import Clients.RoadMapClient;
import Fax.FaxStatus;
import source.CSVFrame;
import table.Record;

public class GetByFaxDisposition implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(false);
		String[] statuses = FaxStatus.FAX_STATUSES;
		String status = (String) JOptionPane.showInputDialog(new JFrame(), "Disposition", "Disposition", JOptionPane.INFORMATION_MESSAGE, null, statuses, statuses[0]);
		if(status==null)
			return;
		RoadMapClient roadmap = new RoadMapClient();
		String pharmacy = roadmap.getPharmacy();
		roadmap.close();
		if(pharmacy==null)
			return;
		ResultSet set = client.getByFaxDisposition(status,pharmacy);
		try {
			while(set.next()) 
				CSVFrame.model.addRow(new Record(set,client.getDatabaseName(),client.getTableName()));
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			client.close();
		}
	}
}
