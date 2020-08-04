package framelisteners.edit.pharmacy;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Clients.RoadMapClient;
import source.CSVFrame;
import table.Record;

public class CheckState implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		RoadMapClient client = new RoadMapClient();
		String[] pharmacies = client.getPharmacies();
		String pharmacy = (String) JOptionPane.showInputDialog(new JFrame(), "Select a pharmacy", "Pharmacies:", JOptionPane.QUESTION_MESSAGE, null, pharmacies, pharmacies[0]);
		if(pharmacy==null)
			return;
		for(Record record: CSVFrame.model.data) {
			if(client.hasState(record, pharmacy)>0)
				record.setRowColor(Color.GREEN);
			else
				record.setRowColor(Color.RED);
		}
		client.close();
	}

}
