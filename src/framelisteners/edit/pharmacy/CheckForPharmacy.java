package framelisteners.edit.pharmacy;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Clients.RoadMapClient;
import source.CSVFrame;
import table.Record;

public class CheckForPharmacy implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		RoadMapClient client = new RoadMapClient();
		String[] pharmacies = client.getPharmacies();
		String pharmacy = (String) JOptionPane.showInputDialog(new JFrame(), "Select a pharmacy", "Pharmacies:", JOptionPane.QUESTION_MESSAGE, null, pharmacies, pharmacies[0]);
		for(Record record: CSVFrame.model.data) {
			if(client.CanPharmacyTake(record, pharmacy))
				record.setRowColor(Color.GREEN);
			else
				record.setRowColor(Color.RED);
		}
		
	}

}
