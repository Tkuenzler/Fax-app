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
	ArrayList<PharmacyMap> roadMap = new ArrayList<PharmacyMap>();
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		LoadRoadMap();
		for(Record record: CSVFrame.model.data) {
			/*
			 * Check if a pharmacy is located as same state as patient
			 */
			record.setPharmacy(Pharmacy.GetPharmacy(roadMap, record));
	
		}
	}
	public void LoadRoadMap() {
		RoadMapClient client = new RoadMapClient();
		/*
		 * Get all pharmacy names
		 */
		String[] pharmacies = client.getPharmacies();
		/*
		 * Create and populate all pharmacies
		 */
		for(String pharmacy: pharmacies) {
			PharmacyMap map = client.getPharmacy(pharmacy);
			client.LoadAllStates(map);
			roadMap.add(map);
		}
		client.close();
	}
	
}
