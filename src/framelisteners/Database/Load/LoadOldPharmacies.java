package framelisteners.Database.Load;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Clients.DatabaseClient;
import Clients.RoadMapClient;

public class LoadOldPharmacies implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		RoadMapClient map = new RoadMapClient();
		String query = map.getNotPharmacyQuery();
		map.close();
		DatabaseClient client = new DatabaseClient(false);
		client.GetLeadsByPharmacyQuery(query);
		client.close();
	}
}
