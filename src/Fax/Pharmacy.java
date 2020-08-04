package Fax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import Clients.RoadMapClient;
import Clients.RoadMapClient.PharmacyOdds;
import source.CSVFrame;
import table.Record;

public class Pharmacy {
	public static String GetPharmacy(RoadMapClient client,Record record) {
		if(EmdeonStatus.IsNotFoundStatus(record.getStatus())) 
			return NotFoundPharmacy(client,record);
		else if(client.InvalidPBM(record))
			return NotFoundPharmacy(client,record);
		int type = PBM.InsuranceFilter.GetInsuranceType(record);
		if(type==PBM.InsuranceType.Type.MEDICAID_INSURANCE) 
				return "Medicaid";
		PharmacyOdds[] pharmacies = client.GetInStatePharmacies(record);
		if(pharmacies!=null) {
			String pharmacy = GetPharmacyName(record,pharmacies,client);
			if(!pharmacy.equalsIgnoreCase("No Home")) {
				return pharmacy;
			}
		}
		pharmacies = client.GetPharmacyList(record);
		return GetPharmacyName(record,pharmacies,client);
	}
	private static String NotFoundPharmacy(RoadMapClient client,Record record) {
		PharmacyOdds[] pharmacies = client.GetNotFoundPharmacyList();
		if(pharmacies==null)
			return "Not Found";
		ArrayList<PharmacyOdds> pharmacies_that_can_take = new ArrayList<PharmacyOdds>();
		for(PharmacyOdds pharmacy: pharmacies) {
			if(client.CanTakeNotFound(record, pharmacy.getName())) {
				pharmacies_that_can_take.add(pharmacy);
			}
		}
		//Check if its a No Home or only 1 pharmacy available
		if(pharmacies_that_can_take.size()==0)
			return "Not Found";
		else if(pharmacies_that_can_take.size()==1)
			return pharmacies_that_can_take.get(0).getName();
				
		//Set The Odds 
		for(int i = 0;i<pharmacies_that_can_take.size();i++) {
			PharmacyOdds pharmacy = pharmacies_that_can_take.get(i);
			double odds = 1/(double)pharmacies_that_can_take.size();
			pharmacy.setOdds(odds);
		}		
		//Add records to the pot and draw 1
		ArrayList<PharmacyOdds> oddsList = new ArrayList<PharmacyOdds>();
		for(PharmacyOdds pharmacy: pharmacies_that_can_take) {
			int number = (int)(pharmacy.getOdds()*100);
			for(int x = 0;x<number;x++) {
				oddsList.add(pharmacy);
			}
		}
		Collections.shuffle(oddsList); 
		Random rand = new Random();
		return oddsList.get(rand.nextInt(oddsList.size())).getName();		
	}
	private static String GetPharmacyName(Record record,PharmacyOdds[] pharmacies,RoadMapClient client) {
		//Get Pharmacies that can take record
		record.printRecord();
		ArrayList<PharmacyOdds> pharmacies_that_can_take = new ArrayList<PharmacyOdds>();
		for(PharmacyOdds pharmacy: pharmacies) {
			if(client.CanPharmacyTake(record, pharmacy.getName())) {
				pharmacies_that_can_take.add(pharmacy);
			}
		}
		if(pharmacies_that_can_take.size()==0)
			return "No Home";
		else if(pharmacies_that_can_take.size()==1)
			return pharmacies_that_can_take.get(0).getName();
		//Set The Odds and get total
		for(int i = 0;i<pharmacies_that_can_take.size();i++) {
			PharmacyOdds pharmacy = pharmacies_that_can_take.get(i);
			double odds = 1/(double)pharmacies_that_can_take.size();
			pharmacy.setOdds(odds);
		}
		ArrayList<PharmacyOdds> oddsList = new ArrayList<PharmacyOdds>();
		for(PharmacyOdds pharmacy: pharmacies_that_can_take) {
			int number = (int)(pharmacy.getOdds()*100);
			for(int x = 0;x<number;x++) {
				oddsList.add(pharmacy);
			}
		}
		Collections.shuffle(oddsList); 
		Random rand = new Random();
		return oddsList.get(rand.nextInt(oddsList.size())).getName();		
	}
}
