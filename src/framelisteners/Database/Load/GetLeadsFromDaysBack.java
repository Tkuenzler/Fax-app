package framelisteners.Database.Load;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;

import Clients.DatabaseClient;
import source.CSVFrame;
import table.Record;

public class GetLeadsFromDaysBack implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int daysBack = 0;
		try {
			daysBack = Integer.parseInt(JOptionPane.showInputDialog(null, "How many days back?"));
		} catch(NumberFormatException ne) {
			JOptionPane.showMessageDialog(null, "Not a valid number");
			return;
		}
	
		DatabaseClient client = new DatabaseClient(false);
		List<Record> list = client.GetLeadsByDaysBack(daysBack);
		for(Record record: list) {
			CSVFrame.model.addRow(record);
		}
	}
}
