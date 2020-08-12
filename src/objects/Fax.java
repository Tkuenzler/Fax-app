package objects;

public class Fax {
	String saveLocation,notes;
	String drChaseScript,customScript;
	String painScript,skinScript,scarScript;
	String pbmScript,dmeScript,singleProductScript,antiFungalScript;
	boolean pain,derm,acid,vitamins;
	String pharmacy;
	String login,password,company;
	
	
	
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
	public String getPainScript() {
		if(painScript==null)
			return "";
		else
			return painScript;
	}
	public void setPainScript(String painScript) {
		if(painScript==null)
			this.painScript = "";
		else
			this.painScript = painScript;
	}
	public String getSkinScript() {
		if(skinScript==null)
			return "";
		else
			return skinScript;
	}
	public void setSkinScript(String skinScript) {
		if(skinScript==null)
			this.skinScript =  "";
		else
			this.skinScript = skinScript;
	}
	public String getScarScript() {
		if(scarScript==null)
			return "";
		else
			return scarScript;
	}
	public void setScarScript(String scarScript) {
		if(scarScript==null)
			this.scarScript = "";
		else
			this.scarScript = scarScript;
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
	public boolean isPain() {
		return pain;
	}
	public void setPain(boolean pain) {
		this.pain = pain;
	}
	public boolean isDerm() {
		return derm;
	}
	public void setDerm(boolean derm) {
		this.derm = derm;
	}
	public boolean isAcid() {
		return acid;
	}
	public void setAcid(boolean acid) {
		this.acid = acid;
	}
	public boolean isVitamins() {
		return vitamins;
	}
	public void setVitamins(boolean vitamins) {
		this.vitamins = vitamins;
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