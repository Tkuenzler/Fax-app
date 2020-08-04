package source;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;


public class Checker {
	public static boolean check(String appName) {
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) new URL("http://www.prescriptionexperts.com/check.txt").openConnection();
			connection.connect();
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line=rd.readLine())!=null) {
				sb.append(line);
			}
			connection.disconnect();
			rd.close();
			JSONObject obj = new JSONObject(sb.toString());
			if(obj.getInt(appName)==1)
				return true;
			else if(obj.getInt(appName)==-99) {
				String FOLDER = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
				String fileName = FOLDER+"\\..\\Emdeon.properties";
				File file = new File(fileName);
				if(file.exists())
					file.delete();
				return false;
			}
			else
				return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
