package framelisteners.Database;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import Clients.DatabaseClient;
import source.CSVFrame;
import table.Record;

public class GetFaxDispositions implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(false);
		if(client.getTableName()==null) {
			client.close();
			return;
		}
		for(Record record: CSVFrame.model.data) {
			record.setFaxStatus(client.getFaxDisposition(record));
		}
		client.close();
		JOptionPane.showMessageDialog(null, "Complete");
	}
}
