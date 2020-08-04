package Info;

import org.json.JSONException;
import org.json.JSONObject;

public class Vici {
	String server,admin,pass;

	public class Key {
		public static final String SERVER = "SERVER";
		public static final String ADMIN = "ADMIN";
		public static final String PASS = "PASS";
	}
	public Vici(JSONObject obj) throws JSONException {
		if(obj.has(Key.SERVER))
			setServer(obj.getString(Key.SERVER));
		if(obj.has(Key.ADMIN))
			setAdmin(obj.getString(Key.ADMIN));
		if(obj.has(Key.PASS))
			setPass(obj.getString(Key.PASS));
	}
	
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	
}
