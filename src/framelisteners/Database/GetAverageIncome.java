package framelisteners.Database;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Clients.InfoDatabase;
import source.CSVFrame;
import table.Record;

public class GetAverageIncome implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		InfoDatabase client = new InfoDatabase();
		new Thread() {
				@Override
				public void run() {
				for(Record record: CSVFrame.model.data) {
					if(record.getType().equalsIgnoreCase(""))
						record.setType(client.getIncome(record.getZip()));
				}
			}
		}.start();
	}

}
