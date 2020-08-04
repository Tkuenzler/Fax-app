package framelisteners.Database.Delete;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import Clients.InfoDatabase;
import source.CSVFrame;
import table.Record;

public class DeleteAllRecordsChecked implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		InfoDatabase client = new InfoDatabase();
		int delete = 0;
		for(Record record: CSVFrame.model.data) {
			if(client.DeleteCheckedRecord(record.getPhone())==1)
					delete++;
		}
		JOptionPane.showMessageDialog(null, "Deleted "+delete+" records");
		client.close();
	}

}
