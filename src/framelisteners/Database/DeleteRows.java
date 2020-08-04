package framelisteners.Database;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import Clients.DatabaseClient;
import source.CSVFrame;
import table.Record;

public class DeleteRows implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int confirm = JOptionPane.showConfirmDialog(null, "ARE YOU SURE YOU WANT TO DELETE RECORDS", "DELETE", JOptionPane.YES_NO_OPTION);
		if(confirm!=JOptionPane.YES_OPTION)
			return;
		int count = 0;
		DatabaseClient client = new DatabaseClient(false);
		if(client.getTableName()==null) {
			client.close();
			return;
		}
		confirm = JOptionPane.showConfirmDialog(null, "ARE YOU SURE YOU WANT TO DELETE RECORDS FROM "+client.getTableName(), "DELETE", JOptionPane.YES_NO_OPTION);
		if(confirm!=JOptionPane.YES_OPTION)
			return;
		for(Record record: CSVFrame.model.data) {
			count += client.deleteRecord
					(record);
		}
		JOptionPane.showMessageDialog(null, count+" out of "+CSVFrame.model.getRowCount()+" rows deleted");
		client.close();
	}
}
