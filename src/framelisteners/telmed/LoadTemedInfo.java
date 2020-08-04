package framelisteners.telmed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Clients.InfoDatabase;
import source.CSVFrame;
import table.Record;

public class LoadTemedInfo implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		InfoDatabase client = new InfoDatabase();
		for(Record record: CSVFrame.model.data) {
			client.LoadRecordInfo(record);
		}
	}
}
