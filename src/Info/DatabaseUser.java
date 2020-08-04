package Info;

import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseUser {
	String user,password,localHost;
	int port;
	boolean blank = true;
	public DatabaseUser(JSONObject obj) throws JSONException {
		setUser(obj.getString(Keys.USERNAME));
		setPassword(obj.getString(Keys.PASSWORD));
		setLocalHost(obj.getString(Keys.LOCAL_HOST));
		setPort(obj.getInt(Keys.PORT));
		setBlank();
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
	public String getLocalHost() {
		return localHost;
	}
	public void setLocalHost(String localHost) {
		this.localHost = localHost;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public void setBlank() {
		if(this.user==null || this.user.equalsIgnoreCase(""))
			blank = true;
		else
			blank = false;
	}
	public boolean isBlank() {
		return this.blank;
	}
	private class Keys {
		public static final String USERNAME = "USER_NAME";
		public static final String PASSWORD = "PASSWORD";
		public static final String PORT = "PORT";
		public static final String LOCAL_HOST = "LOCAL_HOST";
		
	}
}
