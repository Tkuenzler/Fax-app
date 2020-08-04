package framelisteners.Database.Load;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Clients.DatabaseClient;
import source.CSVFrame;
import table.Record;

public class LoadVendorId implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		DatabaseClient client = new DatabaseClient(false);
		new Thread() {
			@Override
			public void run() {
				for(Record record: CSVFrame.model.data) {
					if(!record.getStatus().equalsIgnoreCase(""))
						continue;
					record.setStatus(client.getVendorId(record.getPhone()));
					
				}
			}
		}.start();
		
	}
}
