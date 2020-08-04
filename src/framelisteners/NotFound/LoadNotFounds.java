package framelisteners.NotFound;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Clients.DatabaseClient;
import Clients.RoadMapClient;

public class LoadNotFounds implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		RoadMapClient map = new RoadMapClient();
		String pharmacy = map.getPharmacy();
		String[] states = map.getStates(pharmacy);
		String insuranceQuery = map.getInsuranceTypeQuery(pharmacy);
		map.close();
		if(pharmacy==null)
			return;
		DatabaseClient client = new DatabaseClient(false);
		for(String state: states) {
			System.out.println(state);
			client.LoadNotFounds(pharmacy,insuranceQuery,state);
		}
		client.close();
	}
}
