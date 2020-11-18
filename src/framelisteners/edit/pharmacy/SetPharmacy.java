package framelisteners.edit.pharmacy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import Clients.RoadMapClient;
import Fax.Pharmacy;
import objects.PharmacyMap;
import source.CSVFrame;
import table.Record;

public class SetPharmacy implements ActionListener {
	HashMap<String,PharmacyMap> roadMap = new HashMap<String,PharmacyMap>();
	RoadMapClient client = null;
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		LoadRoadMap();
		for(Record record: CSVFrame.model.data) {
			/*
			 * Check if a pharmacy is located as same state as patient
			 */
			String pharmacy = Pharmacy.GetPharmacy(roadMap, record);
			System.out.println(pharmacy);
			if(pharmacy.equalsIgnoreCase("No Home") && client.getTable().equalsIgnoreCase("TELMED_ROADMAP")) {
				if(Pharmacy.CanCarepointTake(roadMap.get("Carepoint"),record))
					record.setPharmacy("Carepoint");
				else
					record.setPharmacy("No Home");
			}
			else
				record.setPharmacy(pharmacy);
	
		}
	}
	public void LoadRoadMap() {
		client = new RoadMapClient();
		/*
		 * Get all pharmacy names
		 */
		String[] pharmacies = client.getPharmacies();
		/*
		 * Create and populate all pharmacies
		 */
		for(String pharmacy: pharmacies) {
			PharmacyMap map = client.getPharmacy(pharmacy);
			if(map==null)
				continue;
			client.LoadAllStates(map);
			roadMap.put(map.getPharmacyName(),map);
		}
		client.close();
	}
	
}
