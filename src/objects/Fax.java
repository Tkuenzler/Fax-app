package objects;

public class Fax {
	String saveLocation,notes;
	String drChaseScript,customScript;
	String rxPlusScript,rxPlusScript2,rxPlusCaremark;
	String pbmScript,dmeScript,singleProductScript,antiFungalScript,coveredScript;
	String pharmacy;
	String login,password,company;
	
	
	public String getCoveredScript() {
		if(coveredScript==null)
			return "";
		else
			return coveredScript;
	}
	public void setCoveredScript(String coveredScript) {
		if(coveredScript==null)
			this.coveredScript = "";
		else
			this.coveredScript = coveredScript;
	}
	
	public String getAntiFungalScript() {
		if(antiFungalScript==null)
			return "";
		else
			return antiFungalScript;
	}
	public void setAntiFungalScript(String antiFungalScript) {
		if(antiFungalScript==null)
			this.antiFungalScript = "";
		else
			this.antiFungalScript = antiFungalScript;
	}
	
	public void setDMEScript(String dmeScript) {
		if(dmeScript==null)
			this.dmeScript = "";
		else
			this.dmeScript = dmeScript;
	}
	public String getDMEScript() {
		if(this.dmeScript==null)
			return "";
		else
			return dmeScript;
	}
	
	public String getCustomScript() {
		if(this.customScript==null)
			return "";
		else
			return customScript;
	}
	public void setCustomScript(String customScript) {
		if(customScript==null)
			this.customScript = "";
		else
			this.customScript = customScript;
	}
	public String getPbmScript() {
		if(this.pbmScript==null)
			return "";
		else
			return this.pbmScript;
	}
	public void setPbmScript(String pbmScript) {
		if(pbmScript==null)
			this.pbmScript = "";
		else
			this.pbmScript = pbmScript;
	}
	
	public String getSingleProductScript() {
		if(singleProductScript==null)
			return "";
		else
			return singleProductScript;
	}
	public void setSingleProductScript(String singleProductScript) {
		if(singleProductScript==null)
			this.singleProductScript = "";
		else
			this.singleProductScript = singleProductScript;
	}
	public String getRxPlusScript2() {
		if(rxPlusScript2==null)
			return "";
		else
			return rxPlusScript2;
	}
	public void setRxPlusScript2(String rxPlusScript2) {
		if(rxPlusScript2==null)
			this.rxPlusScript2 = "";
		else
			this.rxPlusScript2 = rxPlusScript2;
	}
	
	public String getRxPlusCaremark() {
		if(rxPlusCaremark==null)
			return "";
		else
			return rxPlusCaremark;
	}
	public void setRxPlusCaremark(String rxPlusCaremark) {
		if(rxPlusCaremark==null)
			this.rxPlusCaremark = "";
		else
			this.rxPlusCaremark = rxPlusCaremark;
	}
	
	public String getRxPlusScript() {
		if(rxPlusScript==null)
			return "";
		else
			return rxPlusScript;
	}
	public void setRxPlusScript(String rxPlusScript) {
		if(rxPlusScript==null)
			this.rxPlusScript = "";
		else
			this.rxPlusScript = rxPlusScript;
	}
	
	public String getDrChaseScript() {
		if(drChaseScript==null) 
			return "";
		return drChaseScript;
	}
	public void setDrChaseScript(String drChaseScript) {
		this.drChaseScript = drChaseScript;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		if(company==null)
			this.company = "";
		this.company = company;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		if(notes==null)
			this.notes = null;
		this.notes = notes;
	}
	public String getSaveLocation() {
		return saveLocation;
	}
	public void setSaveLocation(String saveLocation) {
		if(saveLocation==null)
			this.saveLocation = "";
		this.saveLocation = saveLocation;
	}
	public String getPharmacy() {
		if(this.pharmacy==null) 
			return "";
		else
			return pharmacy;
	}
	public void setPharmacy(String pharmacy) {
		if(pharmacy==null)
			this.pharmacy = "";
		else 
			this.pharmacy = pharmacy;
	}
}