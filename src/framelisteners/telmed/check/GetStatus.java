package framelisteners.telmed.check;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Clients.DatabaseClient;
import source.CSVFrame;
import table.Record;

public class GetStatus implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(true);
		for(Record record: CSVFrame.model.data) {
			record.setStatus(client.GetTelmedStatus(record.getPhone()));
		}
		client.close();
	}
}
