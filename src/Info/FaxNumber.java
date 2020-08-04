package Info;

import org.json.JSONException;
import org.json.JSONObject;

public class FaxNumber {
	String number,password,ext,name,secret,key;
	private class Keys {
		public static final String NUMBER = "number";
		public static final String PASSWORD = "password";
		public static final String EXT = "ext";
		public static final String NAME = "name";
		public static final String SECRET = "SECRET";
		public static final String KEY = "KEY";
	}
	public FaxNumber(JSONObject obj) throws JSONException {
		setNumber(obj.getString(Keys.NUMBER));
		setPassword(obj.getString(Keys.PASSWORD));
		setExt(obj.getString(Keys.EXT));
		setName(obj.getString(Keys.NAME));
		setSecret(obj.getString(Keys.SECRET));
		setKey(obj.getString(Keys.KEY));
	}
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	
	
}
