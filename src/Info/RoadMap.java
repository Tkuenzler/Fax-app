package Info;

import org.json.JSONException;
import org.json.JSONObject;

public class RoadMap {
	String roadMap;
	public class Key {
		public static final String NAME = "NAME";
	}
	public RoadMap(JSONObject obj) throws JSONException {
		if(obj.has(Key.NAME))
			setRoadMap(obj.getString(Key.NAME));
	}
	public String getRoadMap() {
		return roadMap;
	}
	public void setRoadMap(String roadMap) {
		this.roadMap = roadMap;
	}
	
}
