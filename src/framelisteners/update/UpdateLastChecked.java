package framelisteners.update;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import Clients.DatabaseClient;
import source.CSVFrame;
import table.Record;

public class UpdateLastChecked implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(false);
		int updated = 0;
		for(Record record: CSVFrame.model.data) {
			int complete = 0;
			complete += client.updateRecord(DatabaseClient.Columns.LAST_EMDEON_DATE,getCurrentDate("yyyy-MM-dd"),record);
			if(complete>0)
				updated += client.updateRecord(DatabaseClient.Columns.EMDEON_STATUS,record.getStatus(),record);
		}
		client.close();
		JOptionPane.showMessageDialog(null, updated+" out of "+CSVFrame.model.getRowCount()+" updated" );
	}
	private String getCurrentDate(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format); 
		Date date = new Date(); 
		return formatter.format(date);
	}
}
