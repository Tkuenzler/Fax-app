package framelisteners.Database.Load;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Clients.DatabaseClient;
import source.CSVFrame;
import table.Record;

public class LoadAFID implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(false);
		new Thread() {
			@Override
			public void run() {
				for(Record record: CSVFrame.model.data) {
					String afid = client.getAfid(record.getId());
					System.out.println(afid);
					if(afid==null)
						record.setEmail("");
					else 
						record.setEmail(afid);
				}
				client.close();
			}
		}.start();
		
	}
}
