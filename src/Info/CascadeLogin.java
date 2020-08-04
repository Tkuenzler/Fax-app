package Info;

import org.json.JSONException;
import org.json.JSONObject;

public class CascadeLogin {
	String client,secret;
	private class Keys {
		public static final String SECRET = "SECRET";
		public static final String CLIENT = "CLIENT";

	}
	public CascadeLogin(JSONObject obj) throws JSONException {
		setClient(obj.getString(Keys.CLIENT));
		setSecret(obj.getString(Keys.SECRET));
	}
	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
}
