package framelisteners.Database;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Clients.DatabaseClient;
import source.CSVFrame;
import table.Record;

public class IsInAnyDatabase implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(true);
		if(client.getDatabaseName()==null)
			return;
		String[] databases = {"CH1Marketing","CLN_Marketing","MT_MARKETING"};
		for(Record record: CSVFrame.model.data) {
			if(client.isClosed())
				break;
			String exists = client.isInDatabase(record);
			switch(exists) {
				case "ERROR": record.setRowColor(Color.GRAY); break;
				case "NOT IN DB": record.setRowColor(Color.BLACK); break;
				default: record.setRowColor(Color.GREEN); break;
			}
			record.setEmail(exists);
		}
		client.close();
		CSVFrame.model.fireTableDataChanged();
	}
}
