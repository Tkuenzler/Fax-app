package Clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.client.utils.URIBuilder;

import table.Record;

public class Five9Client {
	private static String URL = "https://api.five9.com/web2campaign/AddToList";
	private static String DOMAIN = "F9domain";
	private static String LIST = "F9list";
	private static String F9 = "F9key";

	public static URL createURL(Record r,String list) {
		URIBuilder b = null;
		URL url = null;
		try {
			b = new URIBuilder(URL);
			b.addParameter(DOMAIN, "Prescription Experts");
			b.addParameter(LIST, list);
			b.addParameter(F9, "number1").addParameter("number1", r.getPhone());
			b.addParameter(F9, "first_name").addParameter("first_name", r.getFirstName());
			b.addParameter(F9, "last_name").addParameter("last_name", r.getLastName());
			b.addParameter(F9, "street").addParameter("street", r.getAddress());
			b.addParameter(F9, "city").addParameter("city", r.getCity());
			b.addParameter(F9, "state").addParameter("state", r.getState());
			b.addParameter(F9, "zip").addParameter("zip", r.getZip());
			b.addParameter(F9, "Gender").addParameter("Gender", r.getGender());
			b.addParameter(F9, "SSN").addParameter("SSN", r.getSsn());
			b.addParameter(F9, "Date Of Birth").addParameter("Date Of Birth", r.getDob());
			//Insruance
			b.addParameter(F9, "Primary Insurance").addParameter("Primary Insurance", r.getCarrier());
			b.addParameter(F9, "PI Policy Number").addParameter("PI Policy Number", r.getPolicyId());
			b.addParameter(F9, "BIN").addParameter("BIN", r.getBin());
			b.addParameter(F9, "PCN").addParameter("PCN", r.getPcn());
			b.addParameter(F9, "city").addParameter("city", r.getCity());
			b.addParameter(F9, "Group ID").addParameter("Group ID", r.getGrp());
			b.addParameter(F9, "Email").addParameter("Email", r.getEmail());
			//Physician
			b.addParameter(F9, "Physician NPI").addParameter("Physician NPI", r.getNpi());
			b.addParameter(F9, "Physician First Name").addParameter("Physician First Name", r.getDrFirst());
			b.addParameter(F9, "Physician Last Name").addParameter("Physician Last Name", r.getDrLast());
			b.addParameter(F9, "Physician Address1").addParameter("Physician Address1", r.getDrAddress1());
			b.addParameter(F9, "Physician City").addParameter("Physician City", r.getDrCity());
			b.addParameter(F9, "Physician State").addParameter("Physician State", r.getDrState());
			b.addParameter(F9, "Physician Zip").addParameter("Physician Zip", r.getDrZip());
			b.addParameter(F9, "Physician Phone").addParameter("Physician Phone", r.getDrPhone());
			b.addParameter(F9, "Physician Fax").addParameter("Physician Fax", r.getDrFax());
			
			System.out.println(b.toString());
			url = b.build().toURL();
		} catch (URISyntaxException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}
	public static URL createURLForRequalify(Record r,String marketingTeam) {
		URIBuilder b = null;
		URL url = null;
		try {
			b = new URIBuilder(URL);
			b.addParameter(DOMAIN, "Prescription Experts");
			b.addParameter(LIST, "Requalify");
			b.addParameter(F9, "number1").addParameter("number1", r.getPhone());
			b.addParameter(F9, "first_name").addParameter("first_name", r.getFirstName());
			b.addParameter(F9, "last_name").addParameter("last_name", r.getLastName());
			b.addParameter(F9, "street").addParameter("street", r.getAddress());
			b.addParameter(F9, "city").addParameter("city", r.getCity());
			b.addParameter(F9, "state").addParameter("state", r.getState());
			b.addParameter(F9, "zip").addParameter("zip", r.getZip());
			b.addParameter(F9, "Gender").addParameter("Gender", r.getGender());
			b.addParameter(F9, "SSN").addParameter("SSN", r.getSsn());
			b.addParameter(F9, "Date Of Birth").addParameter("Date Of Birth", r.getDob());
			b.addParameter(F9, "medication_category").addParameter("medication_category", r.getEmail());
			//Insruance
			b.addParameter(F9, "Primary Insurance").addParameter("Primary Insurance", r.getCarrier());
			b.addParameter(F9, "PI Policy Number").addParameter("PI Policy Number", r.getPolicyId());
			b.addParameter(F9, "BIN").addParameter("BIN", r.getBin());
			b.addParameter(F9, "PCN").addParameter("PCN", r.getPcn());
			b.addParameter(F9, "city").addParameter("city", r.getCity());
			b.addParameter(F9, "Group ID").addParameter("Group ID", r.getGrp());
			b.addParameter(F9, "Email").addParameter("Email", r.getEmail());
			//Physician
			b.addParameter(F9, "Physician NPI").addParameter("Physician NPI", r.getNpi());
			b.addParameter(F9, "Physician First Name").addParameter("Physician First Name", r.getDrFirst());
			b.addParameter(F9, "Physician Last Name").addParameter("Physician Last Name", r.getDrLast());
			b.addParameter(F9, "Physician Address1").addParameter("Physician Address1", r.getDrAddress1());
			b.addParameter(F9, "Physician City").addParameter("Physician City", r.getDrCity());
			b.addParameter(F9, "Physician State").addParameter("Physician State", r.getDrState());
			b.addParameter(F9, "Physician Zip").addParameter("Physician Zip", r.getDrZip());
			b.addParameter(F9, "Physician Phone").addParameter("Physician Phone", r.getDrPhone());
			b.addParameter(F9, "Physician Fax").addParameter("Physician Fax", r.getDrFax());
			//Pharmacy + Marketing Info
			b.addParameter(F9, "PHARMACY").addParameter("PHARMACY", r.getPharmacy());
			b.addParameter(F9, "Marketing Team").addParameter("Marketing Team", marketingTeam);
			
			System.out.println(b.toString());
			url = b.build().toURL();
		} catch (URISyntaxException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}
	public static void addToList(Record r,String list) {
		URL url = createURL(r,list);
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
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
	public static boolean addToRequalifyList(Record r,String marketingName) {
		URL url = createURLForRequalify(r,marketingName);
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = null;
			while((line=rd.readLine())!=null)
				System.out.println(line);
			boolean success = connection.getResponseCode()==200?true:false;
			connection.disconnect();
			return success;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
