package framelisteners.Database;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Clients.DatabaseClient;
import source.CSVFrame;
import table.Record;

public class IsInTable implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(false);
		if(client.getTableName()==null || client.getDatabaseName()==null)
			return;
		for(Record record: CSVFrame.model.data) {
			if(client.isClosed())
				break;
			int exists = client.isInTable(record,client.getTableName());
			switch(exists) {
			case -1: record.setRowColor(Color.GRAY); break;
			case 0: record.setRowColor(Color.BLACK); break;
			case 1: record.setRowColor(Color.GREEN); break;
			}
		}
		client.close();
		CSVFrame.model.fireTableDataChanged();
	}
}
