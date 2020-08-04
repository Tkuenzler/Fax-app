package framelisteners.telmed;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Clients.DatabaseClient;
import source.CSVFrame;
import table.Record;

public class IsInTelmed implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(false);
		for(Record record: CSVFrame.model.data) {
			if(client.IsInTelmed(record.getPhone()))
					record.setRowColor(Color.GREEN);
			else
				record.setRowColor(Color.RED);
		}
		client.close();
	}

}
