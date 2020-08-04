package objects;

public class Emdeon {
	String username,password,npi;
	int pause;
	boolean privateOnly,autoExport;
	public Emdeon() {
		
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		if(username==null)
			this.username = "";
		else
			this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		if(password==null)
			this.password = "";
		else
			this.password = password;
	}
	public String getNpi() {
		return npi;
	}
	public void setNpi(String npi) {
		if(npi==null)
			this.npi = "";
		else
			this.npi = npi;
	}
	public boolean isPrivateOnly() {
		return privateOnly;
	}
	public void setPrivateOnly(String bool) {
		if(bool==null)
			privateOnly = false;
		else if(bool.equalsIgnoreCase("true")) 
			privateOnly = true;
		else 
			privateOnly = false;
	}
	public int getPause() {
		return pause;
	}
	public void setPause(String pause) {
		try {
			this.pause = Integer.parseInt(pause);
		} catch(NumberFormatException e) {
			this.pause = 1;
		}
	}
	public boolean isAutoExport() {
		return autoExport;
	}
	public void setAutoExport(String autoExport) {
		if(autoExport==null)
			this.autoExport = false;
		else if(autoExport.equalsIgnoreCase("true"))
			this.autoExport = true;
		else
			this.autoExport = false;
	}
	
}
