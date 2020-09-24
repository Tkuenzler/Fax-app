package objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PharmacyMap {
	String pharmacy,state;
	HashMap<String,RoadMap> states = new HashMap<String,RoadMap>();
	int privateInsurance,medicareInsurance,tricareInsurance,notFound,faxChase;
	double extra;
	public PharmacyMap(ResultSet set) throws SQLException {
		this.pharmacy = set.getString("Pharmacy");
		this.state = set.getString("state");
		this.privateInsurance = set.getInt("COMMERCIAL_TIER");
		this.medicareInsurance = set.getInt("MEDICARE_TIER");
		this.tricareInsurance = set.getInt("TRICARE_TIER");
		this.notFound = set.getInt("NOT_FOUND");
		this.faxChase= set.getInt("FAX_CHASE");
		this.extra = set.getDouble("EXTRA");
	}
	public String getPharmacyName() {
		if(pharmacy==null)
			return "";
		else
			return this.pharmacy;
	}
	public String getState() {
		if(state==null)
			return "";
		else
			return this.state;
	}
	public void addState(RoadMap map) {
		if(!states.containsKey(map.getState()))
			states.put(map.getState(), map);
	}
	public RoadMap getRoadMap(String state) {
		return states.get(state);
	}
	public boolean isInSameState(String state) {
		return this.getState().equalsIgnoreCase(state);
	}
	public double getExtra() {
		return this.extra;
	}
	public boolean canTakePrivate() {
		return privateInsurance>0;
	}
	public boolean canTakeMedicare() {
		return medicareInsurance>0;
	}
	public boolean canTakeTricare() {
		return tricareInsurance>0;
	}
	public boolean canTakeNotFound() {
		return notFound>0;
	}
}
