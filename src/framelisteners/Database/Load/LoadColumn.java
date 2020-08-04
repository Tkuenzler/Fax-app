package framelisteners.Database.Load;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Clients.DatabaseClient;
import source.CSVFrame;
import table.Record;

public class LoadColumn implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(false);
		for(Record record: CSVFrame.model.data) {
			String dob = client.loadColumn(record,"dob");
			System.out.println(dob);
			if(dob==null)
				continue;
			else
				record.setDob(dob);
		}
		client.close();
	}

}
