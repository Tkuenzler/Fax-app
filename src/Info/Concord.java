package Info;

import org.json.JSONException;
import org.json.JSONObject;

public class Concord {
	String number,name,user,password;
	private class Keys {
		public static final String NAME = "name";
		public static final String NUMBER = "number";
		public static final String PASSWORD = "password";
		public static final String USER = "user";
	}
	public Concord(JSONObject obj) throws JSONException {
		if(obj.has(Keys.NAME))
			setName(obj.getString(Keys.NAME));
		if(obj.has(Keys.NUMBER))
			setNumber(obj.getString(Keys.NUMBER));
		if(obj.has(Keys.PASSWORD))
			setPassword(obj.getString(Keys.PASSWORD));
		if(obj.has(Keys.USER))
			setUser(obj.getString(Keys.USER));
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
