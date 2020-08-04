package source;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import Info.CascadeLogin;
import Info.Concord;
import Info.DatabaseUser;
import Info.FaxNumber;
import Info.NDCVerify;
import Info.RoadMap;
import Info.Vici;

public class LoadInfo {
	private class Keys {
		public static final String CASCADE_LOGIN = "CASCADE_LOGIN";
		public static final String FAX_NUMBERS = "FAX_NUMBERS";
		public static final String DATABASE_LOGIN = "DATABASE_LOGIN";
		public static final String APP_NAME = "APP_NAME";
		public static final String CONCORD = "CONCORD_FAX";
		public static final String HUB_LOGIN = "HUB_LOGIN";
		public static final String VICI = "VICI_CLIENT";
		public static final String MEDCORE = "MEDCORE";
		public static final String NDC_VERIFY = "NDC_VERIFY";
		public static final String ROAD_MAP = "ROAD_MAP";
	}
	public static NDCVerify LoadNDCVerify() {
		JSONObject obj;
		try {
			obj = getObject(Keys.NDC_VERIFY);
			return new NDCVerify(obj);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Hub Login not found");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Info file not found");
		}
		return null;
	}
	public static Vici LoadVici() {
		JSONObject obj;
		try {
			obj = getObject(Keys.VICI);
			return new Vici(obj);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Hub Login not found");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Info file not found");
		}
		return null;
	}
	public static DatabaseUser LoadDatabaseUser() throws URISyntaxException, JSONException, IOException {
		JSONObject obj = getObject(Keys.DATABASE_LOGIN);
		return new DatabaseUser(obj);
	}
	public static HashMap<String,FaxNumber> LoadFaxNumbers() throws URISyntaxException, JSONException, IOException {
		JSONArray array = getArray(Keys.FAX_NUMBERS);
		HashMap<String,FaxNumber> list = new HashMap<String,FaxNumber>();
		for(int i = 0;i<array.length();i++) {
			FaxNumber fax = new FaxNumber(array.getJSONObject(i));
			list.put(fax.getName(), fax);
		}
		return list;
	}
	public static CascadeLogin LoadCascadeLogin() throws URISyntaxException, JSONException, IOException {
		JSONObject obj = getObject(Keys.CASCADE_LOGIN);
		return new CascadeLogin(obj);
	}
	public static HashMap<String,Concord> LoadConcordLines() throws JSONException, URISyntaxException, IOException {
		JSONArray array = getArray(Keys.CONCORD);
		HashMap<String,Concord> list = new HashMap<String,Concord>();
		for(int i = 0;i<array.length();i++) {
			Concord concord = new Concord(array.getJSONObject(i));
			list.put(concord.getName(), concord);
		}
		return list;
	}
	public static String getAppName() throws URISyntaxException, IOException, JSONException {
		return (String) getData(Keys.APP_NAME);
	}
	public static RoadMap getRoadMap() throws JSONException, URISyntaxException, IOException {
		return new RoadMap(getObject(Keys.ROAD_MAP));
	}
	private static JSONObject getObject(String key) throws URISyntaxException, JSONException, IOException {
		String infoFile = getFile();
		//File infoFile = FileChooser.OpenInfoFile("OPEN INFO FILE");
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		br = new BufferedReader(new FileReader(infoFile));
		String line = null;
		while((line=br.readLine())!=null) {
			sb.append(line);
		}
		br.close();
		return new JSONObject(sb.toString()).getJSONObject(key);
		
	}
	private static JSONArray getArray(String key) throws URISyntaxException, JSONException, IOException {
		String infoFile = getFile();
		//File infoFile = FileChooser.OpenInfoFile("OPEN INFO FILE");
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		br = new BufferedReader(new FileReader(infoFile));
		String line = null;
		while((line=br.readLine())!=null) {
			sb.append(line);
		}
		br.close();
		return new JSONObject(sb.toString()).getJSONArray(key);
	}
	private static Object getData(String key) throws URISyntaxException, IOException, JSONException {
		String infoFile = getFile();
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		br = new BufferedReader(new FileReader(infoFile));
		String line = null;
		while((line=br.readLine())!=null) {
			sb.append(line);
		}
		br.close();
		return new JSONObject(sb.toString()).get(key);
	}
	private static String getFile() throws URISyntaxException {
		String OS = System.getProperty("os.name").toLowerCase();
		String FOLDER = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		if(OS.indexOf("win") >= 0)
			return FOLDER+"\\..\\info.json";
		else if(OS.indexOf("mac") >= 0)
			return FOLDER.replace("FAX APP.jar", "info.json");
		else
			return FOLDER+"\\..\\info.json";
	}
}
