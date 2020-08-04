package framelisteners.Database;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Clients.DatabaseClient;
import source.CSVFrame;
import table.Record;

public class IsViciLead implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(true);
		for(Record record: CSVFrame.model.data) {
			if(client.HasRecording(record.getPhone()))
				record.setRowColor(Color.GREEN);
			else
				record.setRowColor(Color.RED);
		}
		client.close();
	}

}
