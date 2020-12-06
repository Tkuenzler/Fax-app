package Clients;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import table.Record;

public class PVerifyClient {
	String TOKEN_URL = "https://api.pverify.com/Token";
	String ELIGIBILITY_SUMMARY = "https://api.pverify.com/API/EligibilitySummary";
	String ELIGIBILITY_SUMMARY_BY_ID = "https://api.pverify.com/API/GetEligibilitySummary/";
	String token = null; 
	String username,password;
	public class PayorCodes {
		public static final String HUMANA = "00112";
		public static final String AETNA = "00001";
		public static final String UNITED_HEALTHCARE = "00192";
		public static final String ANTHEM = "000931";
		public static final String CARE_PLUS = "00223";
	}
	public PVerifyClient(String username,String password) throws ParseException, IOException, JSONException {
		this.username = username;
		this.password = password;
	}
	public boolean Login()  {
		try {
			HttpPost post = new HttpPost(TOKEN_URL);
			post.setHeader("Content-Type","application/x-www-form-urlencoded");
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("grant_type", "password"));
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));
			post.setEntity(new UrlEncodedFormEntity(params));
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
		    String responseString = EntityUtils.toString(entity, "UTF-8");
		    boolean status = response.getStatusLine().getStatusCode()==200;
		    if(status) {
		    	SetToken(responseString);
		    	return true;
		    }
		    else
		    	return false;
		} catch(IOException ex) {
			return false;
		} catch (JSONException e) {
			return false;
		}
	}
	public String GetEligibiltySummary(Record record) throws JSONException, ClientProtocolException, IOException {
		try {
			HttpPost post = new HttpPost(ELIGIBILITY_SUMMARY);
			post.setHeader("Content-Type","application/json");
			post.setHeader("Client-API-Id","7719820d-8da6-4518-9046-73403bca00e7");
			post.setHeader("Authorization","Bearer "+this.token);
			StringEntity body = new StringEntity(CreateEligibilty(record).toString());
			post.setEntity(body);
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
		    String responseString = EntityUtils.toString(entity, "UTF-8");
		    boolean status = response.getStatusLine().getStatusCode()==200;
			return responseString;
		} catch(JSONException ex) {
			return ex.getMessage();
		} catch(IOException ex) {
			return ex.getMessage();
		}
	}
	public String GetEligibiltySummaryById(String id) {
		try {
			HttpGet get = new HttpGet(ELIGIBILITY_SUMMARY_BY_ID+id);
			get.setHeader("Content-Type","application/json");
			get.setHeader("Client-API-Id","7719820d-8da6-4518-9046-73403bca00e7");
			get.setHeader("Authorization","Bearer "+this.token);
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
		    String responseString = EntityUtils.toString(entity, "UTF-8");
		    boolean status = response.getStatusLine().getStatusCode()==200;
		    return responseString;
		} catch(IOException ex) {
			return ex.getMessage();
		}
	}
	public void SetToken(String json) throws JSONException {
		JSONObject obj = new JSONObject(json);
		if(obj.has("access_token"))
			this.token = obj.getString("access_token");
		System.out.println(token);
	}
	private JSONObject CreateEligibilty(Record record) throws JSONException {
		JSONObject object = new JSONObject();
		switch(record.getBin()) {
			case "015581":
			case "610649":
				object.put("payerCode", PayorCodes.HUMANA);
				object.put("payerName", "Humana");
				break;
			case "610097":
				object.put("payerCode", PayorCodes.UNITED_HEALTHCARE);
				object.put("payerName", "United Healthcare");
				break;
			case "610502":
				object.put("payerCode", PayorCodes.AETNA);
				object.put("payerName", "Aetna");
				break;
			default:
				return null;
		}
		JSONObject provider = new JSONObject();
		provider.put("firstName", record.getDrFirst());
		provider.put("middleName", "");
		provider.put("lastName", record.getDrLast());
		provider.put("npi", record.getNpi());
		object.put("provider", provider);
		
		JSONObject subscriber = new JSONObject();
		subscriber.put("firstName", record.getFirstName());
		subscriber.put("dob", record.getDob());
		subscriber.put("lastName", record.getLastName());
		subscriber.put("memberID", record.getPolicyId());
		object.put("subscriber", subscriber);
		
		JSONObject dependent = new JSONObject();
		JSONObject patient = new JSONObject();
		patient.put("FirstName", JSONObject.NULL);
		patient.put("MiddleName", JSONObject.NULL);
		patient.put("LastName", JSONObject.NULL);
		patient.put("DOB", JSONObject.NULL);
		patient.put("Gender", JSONObject.NULL);
		patient.put("Suffix", JSONObject.NULL);
		patient.put("MemberID", JSONObject.NULL);
		patient.put("SSN", JSONObject.NULL);
		patient.put("EIN", JSONObject.NULL);
		patient.put("GroupNo", JSONObject.NULL);
		patient.put("IssueDate", JSONObject.NULL);
		patient.put("MedicareId", JSONObject.NULL);
		patient.put("MedicaidId", JSONObject.NULL);
		
		dependent.put("Paitent", patient);
		dependent.put("RelationWithSubscriber", JSONObject.NULL);
		
		object.put("Dependent", dependent);
		object.put("isSubscriberPatient", "True");
		object.put("PracticeTypeCode", "3");
		object.put("doS_StartDate", "01/01/2020");
		object.put("doS_EndDate", "12/31/2020");
		
		System.out.println(object.toString());
		return object;
	}
	private JSONObject ParseError(String message) {
		try {
			JSONObject response = new JSONObject();
			response.put("Response", "Failed");
			response.put("Error", message);
			return response;
		} catch(JSONException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	private JSONObject CreateException(String message,String excpetion,String id)  {
		try {
			JSONObject response = new JSONObject();
			response.put("Response", "Error");
			response.put("Error", message);
			response.put("Exception", excpetion);
			response.put("id", id);
			return response;
		} catch(JSONException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	public class Keys {
		public static final String VALUE = "Value";
		public static final String STATUS = "Status";
		public static final String TYPE = "Type";
		public static final String CODE = "Code";
		public static final String REQUEST_ID = "RequestID";
		public static final String API_RESPONSE_MESSAGE = "APIResponseMessage";
		public static final String ERROR_MESSAGE = "EDIErrorMessage";
		public static final String PAYER_NAME = "PayerName";
		public static final String MEMBER_ID = "Member ID";
		public static final String PLAN_COVERAGE_SUMMARY = "PlanCoverageSummary";
		public static final String DEMOGRAPHIC_INFO = "DemographicInfo";
		public static final String SUBSCRIBER = "Subscriber";
		public static final String DATE_OF_BIRTH = "DOB_R";
		public static final String IDENTIFICATION = "Identification";
		public static final String ADDITIONAL_INFO = "AddtionalInfo";
		public static final String DME_SUMMARY = "DMESummary";
		public static final String POLICY_TYPE = "PolicyType";
		public static final String PLAN_NAME = "PlanName";
		public static final String CO_INSURANCE_OUT_OF_NETWORK = "CoInsOutNet";
		public static final String INDIVIDUAL_DEDUCTIBLE_REMAINING_OUT_OF_NETWORK = "IndividualDeductibleRemainingOutNet";
		public static final String DEDUCTIBLE_OOP_SUMMARY = "HBPC_Deductible_OOP_Summary";
		public static final String INDIVIDUAL_DEDUCTIBLE_OUT_OF_NET = "IndividualDeductibleRemainingOutNet";
		
		
		public static final String SERVICE_DETAILS = "ServiceDetails";
		public static final String SERVICE_NAME = "ServiceName";
		public static final String ELIGIBILITY_DETAILS = "EligibilityDetails";
		public static final String ELIGIBILITY_OR_BENEFIT = "EligibilityOrBenefit";
		public static final String BENEFIT_ENTITIES = "BenefitEntities";
		public static final String ENTITY_TYPE = "EntityType";
		public static final String COMMUNICATION_NUMBER = "CommunicationNumber";
		
		//Address fields
		
		public static final String ADDRESS = "Address1";
		public static final String CITY = "City";
		public static final String STATE = "State";
		public static final String ZIP = "Zip";
		public static final String NUMBER = "Number";
		
		//
		public static final String GENDER = "Gender_R";
		
		//Doctor fields
		public static final String PCP_INFO = "PCPAuthInfoSummary";
	}
	private class PlanType {
		public static final String HMO = "HMO";
		public static final String MEDICARE_PRIMARY = "Medicare Primary";
		public static final String REGIONAL_PPO = "RPPO";
	}
}
