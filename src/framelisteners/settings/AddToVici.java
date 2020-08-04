package framelisteners.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import Clients.ViciClient;
import source.CSVFrame;
import table.Record;

public class AddToVici implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String list_id = JOptionPane.showInputDialog("What list id would you like to add to?");
		if(list_id==null)
			return;
		ViciClient client = new ViciClient();
		for(Record record: CSVFrame.model.data) {
			client.AddLead(record,list_id);
		}
		JOptionPane.showMessageDialog(null,"COMPLETE");
	}
}
