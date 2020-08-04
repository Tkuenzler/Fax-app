package framelisteners.Database;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Clients.InfoDatabase;
import source.CSVFrame;
import table.Record;

public class SetLookUps implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		InfoDatabase client = new InfoDatabase();
		int update = 0;
		for(Record record: CSVFrame.model.data) {
			if(record.getStatus().equalsIgnoreCase("FOUND"))
				update += client.SetInsurance(record);
		}
		System.out.println("UPDATED "+update);
		client.close();
	}

}
