package Clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.http.client.utils.URIBuilder;
import Info.Vici;
import source.LoadInfo;
import table.Record;

public class ViciClient {
	String server,admin,pass;
	public static final String URL = "/vicidial/non_agent_api.php";
	private class Parameters {
		public static final String FIRST_NAME = "first_name";
		public static final String LAST_NAME = "last_name";
		public static final String PHONE = "phone_number";
		public static final String DOB = "date_of_birth";
		public static final String GENDER = "gender";
		public static final String ADDRESS = "address1";
		public static final String CITY = "city";
		public static final String STATE = "state";
		public static final String ZIP = "postal_code";
		
		//
		public static final String USER = "user";
		public static final String PASS = "pass";
		public static final String FUNCTION = "function";
		public static final String LIST_ID = "list_id";
		public static final String DUPE_CHECK = "duplicate_check";
		public static final String SOURCE = "source";
	}
	public ViciClient() {
		Vici vici = LoadInfo.LoadVici();
		this.server = vici.getServer();
		this.admin = vici.getAdmin();
		this.pass = vici.getPass();
		System.out.println(server+" "+admin+" "+pass);
	}
	public void AddLead(Record record,String list_id) {
		String url = BuildURL(record,list_id);
		if(url==null)
			return;
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.connect();		
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = null;
			while((line=rd.readLine())!=null)
				System.out.println(line);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private String BuildURL(Record record,String list_id) {
		
		URIBuilder b = null;
		URL url = null;
		try {
			b = new URIBuilder(server+URL);
			b.addParameter(Parameters.USER, admin);
			b.addParameter(Parameters.PASS, pass);
			b.addParameter(Parameters.FUNCTION, "add_lead");
			b.addParameter(Parameters.FIRST_NAME, record.getFirstName());
			b.addParameter(Parameters.LAST_NAME, record.getLastName());
			b.addParameter(Parameters.PHONE, record.getPhone());
			b.addParameter(Parameters.DOB, convertDOB(record.getDob()));
			b.addParameter(Parameters.GENDER, record.getGender());
			b.addParameter(Parameters.ADDRESS, record.getAddress());
			b.addParameter(Parameters.CITY, record.getCity());
			b.addParameter(Parameters.STATE, record.getState());
			b.addParameter(Parameters.ZIP, record.getZip());
			b.addParameter(Parameters.LIST_ID, list_id);
			b.addParameter(Parameters.SOURCE, list_id);
			b.addParameter(Parameters.DUPE_CHECK, "DUPCAMP");
			url = b.build().toURL();
			return url.toString();
		} catch (URISyntaxException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	private String convertDOB(String dob) {
		String[] split = dob.split("/");
		String month = split[0];
		String day = split[1];
		String year = split[2];
		return year+"-"+month+"-"+day;
	}
}

