package Clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.namsor.api.client.ApiException;
import com.namsor.api.extractgender.api.GendreApi;
import com.namsor.api.extractgender.model.Genderize;

import table.Record;

public class GenderClient {
	public static String getGender(Record record) {
		 Genderize response = null;
		try {
		    GendreApi gendreApi = new GendreApi();
		    response = gendreApi.genderize(record.getFirstName(), record.getLastName(), "us", "restunited_v0.17.1");
		    if(record.getFirstName().length()==0)
		    	return "";
		    System.out.println(response.getGender());
		} catch (ApiException e) {
			System.out.printf("ApiException caught: %s\n", e.getMessage());
			return null;
		}
		return response.getGender();
	}
	public static String getGender(String name) {
		String url = "https://api.genderize.io/?name="+name;
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.connect();
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = null;
			StringBuilder builder = new StringBuilder();
			while((line=rd.readLine())!=null)
				builder.append(line);
			JSONObject obj = new JSONObject(builder.toString());
			if(obj.has("gender"))
				return obj.getString("gender");
			else 
				return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "ERROR";
	}
}
