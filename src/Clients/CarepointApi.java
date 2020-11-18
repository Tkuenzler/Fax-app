package Clients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import table.Record;

public class CarepointApi {
	public static String URL = "https://cp.azure-api.net/register/manual/paths/invoke";
	public static final String PATIENT = "patient";
	public static final String INSURANCE = "insurance";
	public static final String POLICY_ID = "idNumber";
	public static final String BIN = "bin";
	public static final String GRP = "groupNumber";
	public static final String PCN = "pcn";
	public static final String PERSON_CODE = "personCode";
	public static final String PHONE_NUMBER = "phoneNumber";
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String GENDER = "gender";
	public static final String DOB = "dateOfBirth";
	public static final String ADDRESS1 = "address1";
	public static final String ADDRESS2 = "address2";
	public static final String CITY = "city";
	public static final String STATE = "state";
	public static final String ZIP = "zip";
	public static final String ID = "id";
	
	public static final String INSURANCE_CARDS = "insuranceCards";
	public static final String EMAIL = "email";
	public static final String ALLERGIES = "allergies";
	public static final String CELL_NUMBER = "cellNumber";
	public static boolean RegisterPatient(Record record) {
		System.out.println(ConvertToJSON(record));
		 try {
			URIBuilder builder = new URIBuilder(URL);
			URI uri = builder.build();
            HttpPost post = new HttpPost(uri);
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Ocp-Apim-Subscription-Key", "74eeec22d62647739e9a1da2c5acabe0");
            
            StringEntity reqEntity = new StringEntity(ConvertToJSON(record));
            post.setEntity(reqEntity);
            
            CloseableHttpClient client = HttpClients.createDefault();
    		CloseableHttpResponse response = client.execute(post);
    		HttpEntity entity = response.getEntity();
    	    String responseString = EntityUtils.toString(entity, "UTF-8");
    	    int status_code = response.getStatusLine().getStatusCode();
    	    System.out.println(status_code+": "+responseString);
    	    switch(status_code) {
    	    case 200:
    	    case 202:
    	    	return true;
    	    default:
    	    	System.out.println("FAILED: "+status_code);
    	    	return false;
    	    }

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private static String ConvertToJSON(Record record) {
		JSONObject obj = new JSONObject();
		try {
			JSONObject patient = new JSONObject();
			patient.put(FIRST_NAME, record.getFirstName());
			patient.put(LAST_NAME, record.getLastName());
			patient.put(GENDER, record.getGender().substring(0, 1));
			patient.put(DOB, record.getDob());
			patient.put(ADDRESS1, record.getAddress());
			patient.put(CITY, record.getCity());
			patient.put(STATE, record.getState());
			patient.put(ZIP, record.getZip());
			patient.put(PHONE_NUMBER, record.getPhone());
			patient.put(ID, record.getPhone());
			
			JSONObject insurance = new JSONObject();
			insurance.put(POLICY_ID, record.getPolicyId());
			insurance.put(BIN, record.getBin());
			insurance.put(GRP, record.getGrp());
			insurance.put(PCN, record.getPcn());
			insurance.put(PERSON_CODE, "");
			patient.put(INSURANCE, insurance);
			
			JSONArray allergies = new JSONArray();
			String[] aller = record.getEmail().split(",");
			if(aller.length==0) {
				allergies.put(0, "None");
			}
			else {
				for(int i = 0;i<aller.length;i++) {
					allergies.put(i, aller[i]);
				}
			}
			patient.put(ALLERGIES, allergies);
			//Empty Array
			patient.put(EMAIL, "");
			patient.put(INSURANCE_CARDS, new JSONArray());
			patient.put(CELL_NUMBER, JSONObject.NULL);
			
			//Add Object to parent object
			obj.put(PATIENT, patient);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj.toString();
	}
}
