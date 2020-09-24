package Fax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import PBM.InsuranceFilter;
import PBM.InsuranceType;
import objects.PharmacyMap;
import objects.PharmacyOdds;
import objects.RoadMap;
import table.Record;
public class Pharmacy {
	
	
	public static String GetPharmacy(ArrayList<PharmacyMap> roadMap,Record record) {
		ArrayList<PharmacyMap> pharmacies_that_can_take = new ArrayList<PharmacyMap>();
		ArrayList<PharmacyOdds> odds = new ArrayList<PharmacyOdds>();
		int insurance_type = InsuranceFilter.GetInsuranceType(record);
		System.out.println(insurance_type);
		/*
		 * First we add all pharmacies that can take a particular insurance type
		 */
		for(PharmacyMap pharmacy: roadMap) {
			switch(insurance_type) {
				case InsuranceType.Type.PRIVATE_INSURANCE:
					if(pharmacy.canTakePrivate())
						pharmacies_that_can_take.add(pharmacy);
					break;
				case InsuranceType.Type.MEDICARE_INSURANCE:
					if(pharmacy.canTakeMedicare())
						pharmacies_that_can_take.add(pharmacy);
					break;
				case InsuranceType.Type.NOT_FOUND_INSRUACE:
					if(pharmacy.canTakeNotFound())
						pharmacies_that_can_take.add(pharmacy);
					break;
				case InsuranceType.Type.TRICARE_INSURANCE:
					if(pharmacy.canTakeTricare()) {
						System.out.println(pharmacy.getPharmacyName());
						pharmacies_that_can_take.add(pharmacy);
					}
					break;
				case InsuranceType.Type.MEDICAID_INSURANCE:
					return "Medicaid";
				default:
					continue;
			}
		}
		if(pharmacies_that_can_take.size()==0)
			return "No Home";
		/*
		 * Then we check if there is a pharmacy in the same state that can take the insurance
		 */
		for(PharmacyMap pharmacy: pharmacies_that_can_take) {
			if(pharmacy.isInSameState(record.getState()))
				if(CheckRoadMap(record,insurance_type,pharmacy)) 
					odds.add( new PharmacyOdds(pharmacy.getPharmacyName(),pharmacy.getExtra()));
				
		}
		if(odds.size()>0)
			return GetPharmacyName(odds);
		/*
		 * 
		 */
		for(PharmacyMap pharmacy: pharmacies_that_can_take) {
			if(CheckRoadMap(record,insurance_type,pharmacy)) 
				odds.add(new PharmacyOdds(pharmacy.getPharmacyName(),pharmacy.getExtra()));
		}
		if(odds.size()>0)
			return GetPharmacyName(odds);
		else
			return "No Home";
	}
	private static boolean CheckRoadMap(Record record,int insurance_type,PharmacyMap pharmacy) {
		RoadMap map = pharmacy.getRoadMap(record.getState());
		System.out.println("Checking type:"+insurance_type+" Pharmacy: "+pharmacy);
		if(map==null)
			return false;
		switch(record.getCarrier()) {
			case RoadMap.AETNA:	
				if(map.canTake(record,insurance_type,map.getAetna()))
					return true;
				else
					return false;
			case RoadMap.ANTHEM:
				if(map.canTake(record,insurance_type,map.getAnthem()))
					return true;
				else
					return false;
			case RoadMap.ARGUS:
				if(map.canTake(record,insurance_type,map.getArgus()))
					return true;
				else
					return false;
			case RoadMap.CAREMARK:
				if(map.canTake(record,insurance_type,map.getCaremark()))
					return true;
				else
					return false;
			case RoadMap.CATALYST_RX:
				if(map.canTake(record,insurance_type,map.getCatalyst()))
					return true;
				else
					return false;
			case RoadMap.CATAMARAN:
				if(map.canTake(record,insurance_type,map.getCatamaran()))
					return true;
				else
					return false;
			case RoadMap.CIGNA:
				if(map.canTake(record,insurance_type,map.getCigna()))
					return true;
				else
					return false;
			case RoadMap.ENVISION:
				if(map.canTake(record,insurance_type,map.getEnvision()))
					return true;
				else
					return false;
			case RoadMap.EXPRESS_SCRIPTS:
				System.out.println("ESI: "+map.getExpressScripts());
				if(map.canTake(record,insurance_type,map.getExpressScripts()))
					return true;
				else
					return false;
			case RoadMap.HUMANA:
				if(map.canTake(record,insurance_type,map.getHumana()))
					return true;
				else
					return false;
			case RoadMap.MEDIMPACT:
				if(map.canTake(record,insurance_type,map.getMedimpact()))
					return true;
				else
					return false;
			case RoadMap.NAVITUS:
				if(map.canTake(record,insurance_type,map.getNavitus()))
					return true;
				else
					return false;
			case RoadMap.NOT_FOUND:
				if(map.canTake(record,insurance_type,map.getNotFound()))
					return true;
				else
					return false;
			case RoadMap.OPTUM_RX:
				if(map.canTake(record,insurance_type,map.getOptumRx()))
					return true;
				else
					return false;
			case RoadMap.PRIME_THERAPEUTICS:
				if(map.canTake(record,insurance_type,map.getPrimeTherapeutics()))
					return true;
				else
					return false;
			case RoadMap.SILVER_SCRIPTS_WELL_CARE:
				if(map.canTake(record,insurance_type,map.getSilverScriptsWellCare()))
					return true;
				else
					return false;
			default:
				if(map.canTake(record,insurance_type,map.getNotFound()))
					return true;
				else
					return false;
	}
	}
	private static String GetPharmacyName(ArrayList<PharmacyOdds> list) {
		ArrayList<PharmacyOdds> temp = new ArrayList<PharmacyOdds>();
		for(PharmacyOdds pharmacy: list) {
			int number = (int)(pharmacy.getOdds()*100);
			for(int x = 0;x<number;x++) {
				temp.add(pharmacy);
			}
		}
		Collections.shuffle(temp); 
		Random rand = new Random();
		return temp.get(rand.nextInt(temp.size())).getName();
	}
}
