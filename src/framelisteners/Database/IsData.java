package framelisteners.Database;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Clients.DatabaseClient;
import source.CSVFrame;
import table.Record;

public class IsData implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		DatabaseClient client = new DatabaseClient("contacts101","MASTER_DATA");
		for(Record record: CSVFrame.model.data) {
			int check = client.isInTable(record,"MASTER_DATA");
			if(check==1)
				record.setRowColor(Color.GREEN);
			else if(check==0)
				record.setRowColor(Color.BLACK);
			else
				record.setRowColor(Color.DARK_GRAY);
		}
		client.close();
	}

}
