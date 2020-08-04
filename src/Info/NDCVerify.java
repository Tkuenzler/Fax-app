package Info;

import org.json.JSONException;
import org.json.JSONObject;

public class NDCVerify {
	String username,password,npi;
	public class Key {
		public static final String USER_NAME = "USER_NAME";
		public static final String PASSWORD = "PASSWORD";
		public static final String NPI = "NPI";
	}
	public NDCVerify(JSONObject obj) throws JSONException {
		if(obj.has(Key.USER_NAME))
			setUsername(obj.getString(Key.USER_NAME));
		if(obj.has(Key.PASSWORD))
			setPassword(obj.getString(Key.PASSWORD));
		if(obj.has(Key.NPI))
			setNpi(obj.getString(Key.NPI));
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNpi() {
		return npi;
	}
	public void setNpi(String npi) {
		this.npi = npi;
	}
	
}
