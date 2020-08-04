package framelisteners.edit.pharmacy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Clients.RoadMapClient;
import Fax.Pharmacy;
import source.CSVFrame;
import table.Record;

public class SetPharmacy implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		RoadMapClient client = new RoadMapClient();
		for(Record record: CSVFrame.model.data) {
			record.setPharmacy(Pharmacy.GetPharmacy(client,record));
		}
		client.close();
	}
	
}
