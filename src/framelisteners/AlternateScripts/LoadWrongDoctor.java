package framelisteners.AlternateScripts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Clients.DatabaseClient;
import Clients.RoadMapClient;

public class LoadWrongDoctor implements ActionListener {
		String pharmacy;
		@Override
		public void actionPerformed(ActionEvent event) {
			RoadMapClient roadmap = new RoadMapClient();
			pharmacy = roadmap.getPharmacy();
			roadmap.close();
			if(pharmacy==null)
				return;
			DatabaseClient client = new DatabaseClient(true);
			client.LoadWrongDoctor(pharmacy);
			client.close();
		}
}
