package Clients;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import Fax.MessageStatus;
import Info.FaxNumber;
import images.Script;
import images.Script.ScriptException;
import table.Record;

public class RingCentralClient {
	private static class RateLimit {
		public static int HEAVY = 10;
		public static int MEDIUM = 40;
		public static int LIGHT = 50;
		public static int AUTH = 5;
	}
	private class ResponseHeaders {
		public static final String RATE_LIMIT_GROUP = "X-Rate-Limit-Group";
		public static final String RATE_LIMIT_REMAINING = "X-Rate-Limit-Remaining";
		public static final String RATE_LIMIT_WINDOW = "X-Rate-Limit-Window";
		public static final String RATE_LIMIT_GROUP_LIMIT = "X-Rate-Limit-Limit";
		public static final String RETRY_AFTER = "Retry-After";
	}
	private class StatusCodes {
		public static final int SUCCESFUL_200 = 200;
		public static final int SUCCESFUL_201 = 201;
		public static final int SUCCESFUL_202 = 202;
		public static final int SUCCESFUL_204 = 204;
		public static final int SUCCESFUL_206 = 206;
		public static final int SUCCESFUL_BULK = 207;
		public static final int EXPIRED_TOKEN = 401;
		public static final int INVALID_URL = 404;
		public static final int TOO_MANY_REQUESTS = 429;
		public static final int SERVICE_UNAVAILABLE = 503;
	}
	public class Errors {
		public static final String INVALID_URL = "INVALID URL";
		public static final String TOO_MANY_REQUEST = "TOO MANY REQUESTS";
		public static final String SERVICE_UNAVAILABLE = "SERVICE UNAVAILABLE";
		public static final String UNKNOWN_ERROR = "UNKNOWN_ERROR";
		public static final String EXPIRED_TOKEN = "EXPIRED TOKEN";
	}
	private class URLS {
		public static final String URL = "https://platform.ringcentral.com";
	    //public static final String URL = "https://platform.devtest.ringcentral.com";
		public static final String AUTHORIZE = "/restapi/oauth/token";
		public static final String REVOKE = "/restapi/oauth/revoke";
		public static final String CALL_LOG = "/restapi/v1.0/account/~/extension/~/call-log";
		public static final String FAX = "/restapi/v1.0/account/~/extension/~/fax";
		public static final String CHECK_CONNECTION = "/restapi/";
		public static final String GET_MESSAGE_LIST = "/restapi/v1.0/account/~/extension/~/message-store";
		public static final String GET_MESSAGE = "/restapi/v1.0/account/~/extension/~/message-store/";
		public static final String CALL = "/restapi/v1.0/account/~/extension/~/ring-out";
	}
	private String ACCESS_TOKEN;
	private String REFRESH_TOKEN;
	private FaxNumber fax;
	public RingCentralClient(FaxNumber fax) {
		this.fax = fax;
	}
	public boolean login() {
		HttpPost post = new HttpPost(URLS.URL+URLS.AUTHORIZE);
		try {
			String basic = Base64.getEncoder().encodeToString((fax.getKey()+":"+fax.getSecret()).getBytes("utf-8"));
			post.setHeader("Accept","application/json");
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			post.setHeader("Authorization", "Basic "+basic);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("grant_type", "password"));
			params.add(new BasicNameValuePair("username", fax.getNumber()));
			params.add(new BasicNameValuePair("password", fax.getPassword()));
			params.add(new BasicNameValuePair("access_token_ttl","0"));
			post.setEntity(new UrlEncodedFormEntity(params));
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
		    String responseString = EntityUtils.toString(entity, "UTF-8");
		    System.out.println(responseString);
		    extractTokens(responseString);
		    boolean status = response.getStatusLine().getStatusCode()==200;
		    LogResponse(URLS.AUTHORIZE,response.getStatusLine().getStatusCode());
		    client.close();
		    response.close();
		    return status;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public void revokeToken() {
		HttpPost post = new HttpPost(URLS.URL+URLS.REVOKE);
		try {
			String encode = Base64.getEncoder().encodeToString((fax.getKey()+":"+fax.getSecret()).getBytes("utf-8"));
			post.addHeader("Content-Type", "application/x-www-form-urlencoded");
			post.addHeader("Authorization","Basic "+encode);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("token", ACCESS_TOKEN));
			post.setEntity(new UrlEncodedFormEntity(params));
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse responseClient = client.execute(post);
			LogResponse(URLS.REVOKE,responseClient.getStatusLine().getStatusCode());
		    client.close();
		    responseClient.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String refreshToken() {
		HttpPost post = new HttpPost(URLS.URL+URLS.AUTHORIZE);
		try {
			String basic = Base64.getEncoder().encodeToString((fax.getKey()+":"+fax.getSecret()).getBytes("utf-8"));
			post.setHeader("Accept","application/json");
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			post.setHeader("Authorization", "Basic "+basic);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("refresh_token", REFRESH_TOKEN));
			params.add(new BasicNameValuePair("grant_type", "refresh_token"));
			post.setEntity(new UrlEncodedFormEntity(params));
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(post);
			LogResponse(URLS.AUTHORIZE,response.getStatusLine().getStatusCode());
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
		    client.close();
		    response.close();
		    extractTokens(responseString);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String GetMessageList() throws IOException {
		HttpGet get = new HttpGet(URLS.URL+URLS.GET_MESSAGE_LIST);
		CloseableHttpClient client = HttpClients.createDefault();
		get.setHeader("Authorization", "Bearer "+ACCESS_TOKEN);
		get.setHeader("Accept", "application/json");
		CloseableHttpResponse response = client.execute(get);
		int status_code = response.getStatusLine().getStatusCode();
		LogResponse(URLS.GET_MESSAGE_LIST,status_code);
		switch(status_code) {
			case StatusCodes.SUCCESFUL_200:
			case StatusCodes.SUCCESFUL_201:
			case StatusCodes.SUCCESFUL_202:
			case StatusCodes.SUCCESFUL_204:
			case StatusCodes.SUCCESFUL_206:
			case StatusCodes.SUCCESFUL_BULK:
				HttpEntity entity = response.getEntity();
				String responseString = EntityUtils.toString(entity, "UTF-8");
				client.close();
				response.close();
				return responseString;
			case StatusCodes.EXPIRED_TOKEN:
				refreshToken();
				return GetMessageList();
			case StatusCodes.INVALID_URL: 
				return Errors.INVALID_URL;
			case StatusCodes.TOO_MANY_REQUESTS:
				return Errors.TOO_MANY_REQUEST;
			case StatusCodes.SERVICE_UNAVAILABLE:
				return Errors.SERVICE_UNAVAILABLE;
			default: 
				return Errors.UNKNOWN_ERROR;
		}
	}
	public String DeleteMessage(String id) throws IOException {
		HttpDelete delete = new HttpDelete(URLS.URL+URLS.GET_MESSAGE+id);
		CloseableHttpClient client = HttpClients.createDefault();
		delete.setHeader("Authorization", "Bearer "+ACCESS_TOKEN);
		delete.setHeader("Accept", "application/json");
		CloseableHttpResponse response = client.execute(delete);
		int status_code = response.getStatusLine().getStatusCode();
		LogResponse(URLS.GET_MESSAGE+id,status_code);
		switch(status_code) {
			case StatusCodes.SUCCESFUL_200:
			case StatusCodes.SUCCESFUL_201:
			case StatusCodes.SUCCESFUL_202:
			case StatusCodes.SUCCESFUL_204:
			case StatusCodes.SUCCESFUL_206:
			case StatusCodes.SUCCESFUL_BULK:
				HttpEntity entity = response.getEntity();
				String responseString = EntityUtils.toString(entity, "UTF-8");
				client.close();
				response.close();
				return responseString;
			case StatusCodes.EXPIRED_TOKEN:
				refreshToken();
				return GetMessageById(id);
			case StatusCodes.INVALID_URL: 
				return Errors.INVALID_URL;
			case StatusCodes.TOO_MANY_REQUESTS:
				return Errors.TOO_MANY_REQUEST;
			case StatusCodes.SERVICE_UNAVAILABLE:
				return Errors.SERVICE_UNAVAILABLE;
			default: 
				return Errors.UNKNOWN_ERROR;
		}
	}
	public String GetMessageById(String id) throws IOException {
		//API-GROUP LIGHT
		System.out.println(URLS.URL+URLS.GET_MESSAGE+id);
		HttpGet get = new HttpGet(URLS.URL+URLS.GET_MESSAGE+id);
		CloseableHttpClient client = HttpClients.createDefault();
		get.setHeader("Authorization", "Bearer "+ACCESS_TOKEN);
		get.setHeader("Accept", "application/json");
		CloseableHttpResponse response = client.execute(get);
		int status_code = response.getStatusLine().getStatusCode();
		LogResponse(URLS.GET_MESSAGE+id,status_code);
		switch(status_code) {
			case StatusCodes.SUCCESFUL_200:
			case StatusCodes.SUCCESFUL_201:
			case StatusCodes.SUCCESFUL_202:
			case StatusCodes.SUCCESFUL_204:
			case StatusCodes.SUCCESFUL_206:
			case StatusCodes.SUCCESFUL_BULK:
				HttpEntity entity = response.getEntity();
				String responseString = EntityUtils.toString(entity, "UTF-8");
				client.close();
				response.close();
				return responseString;
			case StatusCodes.EXPIRED_TOKEN:
				refreshToken();
				return GetMessageById(id);
			case StatusCodes.INVALID_URL: 
				return Errors.INVALID_URL;
			case StatusCodes.TOO_MANY_REQUESTS:
				return Errors.TOO_MANY_REQUEST;
			case StatusCodes.SERVICE_UNAVAILABLE:
				return Errors.SERVICE_UNAVAILABLE;
			default: 
				return Errors.UNKNOWN_ERROR;
		}
	}
	public String MakePhoneCall(String phoneFrom,String phoneTo) {
		HttpPost post = new HttpPost(URLS.URL+URLS.CALL);
		try {
			JSONObject json = createCallJSON(phoneFrom,phoneTo);
			post.setHeader("Authorization", "Bearer "+ACCESS_TOKEN);
			post.setHeader("Accept", "application/json");
			post.setEntity(new StringEntity(json.toString()));
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(post);
			LogResponse(URLS.CALL,response.getStatusLine().getStatusCode());
			HttpEntity entity = response.getEntity();
		    String responseString = EntityUtils.toString(entity, "UTF-8");
		    System.out.println(responseString);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private JSONObject createCallJSON(String phoneFrom,String phoneTo) throws JSONException {
		JSONObject object = new JSONObject();
		JSONObject from = new JSONObject();
		from.put("phoneNumber", phoneFrom);
		object.put("from",from);
		JSONObject to = new JSONObject();
		to.put("phoneNumber", phoneTo);
		object.put("to", to);
		object.put("playPrompt", true);
		return object;
		
	}
	public String SendFax(Record record,Script script) throws ScriptException, IOException {
		//API-GROUP HEAVY
		if(record.getDrFax().length()!=10) {
			record.setMessageStatus(MessageStatus.BAD_FAX_NUMBER);
			return null;
		}
		String responseString = null;
		System.out.println("ATTEMPTING TO FAX "+record.getFirstName()+" "+record.getLastName());
		String boundary = "--Boundary_1_14413901_1361871080888";
		File file = new File(script.fileName);
		JSONObject json = createFaxJSON(record.getDrFax());
		HttpPost post = new HttpPost(URLS.URL+URLS.FAX);
		post.setHeader("Authorization", "Bearer "+ACCESS_TOKEN);
		post.setHeader("Content-Type","multipart/form-data; boundary="+boundary);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setBoundary(boundary);
		builder.addTextBody("json", json.toString(), ContentType.APPLICATION_JSON);
		builder.addBinaryBody("content", file,ContentType.APPLICATION_OCTET_STREAM, "Fax.pdf");
		HttpEntity multipart = builder.build();
		post.setEntity(multipart);
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = client.execute(post);		
		HttpEntity entity = response.getEntity();
		responseString = EntityUtils.toString(entity, "UTF-8");
		int status_code = response.getStatusLine().getStatusCode();
		LogResponse(URLS.FAX,status_code);
		System.out.println("Status Code: "+status_code);
		switch(status_code) {
		case StatusCodes.SUCCESFUL_200:
		case StatusCodes.SUCCESFUL_201:
		case StatusCodes.SUCCESFUL_202:
		case StatusCodes.SUCCESFUL_204:
		case StatusCodes.SUCCESFUL_206:
		case StatusCodes.SUCCESFUL_BULK:
				client.close();
				response.close();
				return responseString;
			case StatusCodes.EXPIRED_TOKEN:
				refreshToken();
				return SendFax(record,script);
			case StatusCodes.INVALID_URL: 
				client.close();
				response.close();
				return Errors.INVALID_URL;
			case StatusCodes.TOO_MANY_REQUESTS:
				System.out.println(responseString);
				client.close();
				response.close();
				return Errors.TOO_MANY_REQUEST;
			case StatusCodes.SERVICE_UNAVAILABLE:
				client.close();
				response.close();
				return Errors.SERVICE_UNAVAILABLE;
			default: 
				client.close();
				response.close();
				return Errors.UNKNOWN_ERROR;
		}
	}
	public String FaxFile(File file,String faxLine) throws IOException {
		//API-GROUP HEAVY
		String responseString = null;
		String boundary = "--Boundary_1_14413901_1361871080888";
		JSONObject json = createFaxJSON(faxLine);
		HttpPost post = new HttpPost(URLS.URL+URLS.FAX);
		post.setHeader("Authorization", "Bearer "+ACCESS_TOKEN);
		post.setHeader("Content-Type","multipart/form-data; boundary="+boundary);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setBoundary(boundary);
		builder.addTextBody("json", json.toString(), ContentType.APPLICATION_JSON);
		builder.addBinaryBody("content", file,ContentType.APPLICATION_OCTET_STREAM, "Fax.pdf");
		HttpEntity multipart = builder.build();
		post.setEntity(multipart);
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = client.execute(post);		
		HttpEntity entity = response.getEntity();
		responseString = EntityUtils.toString(entity, "UTF-8");
		int status_code = response.getStatusLine().getStatusCode();
		LogResponse(URLS.FAX,status_code);
		switch(status_code) {
		case StatusCodes.SUCCESFUL_200:
		case StatusCodes.SUCCESFUL_201:
		case StatusCodes.SUCCESFUL_202:
		case StatusCodes.SUCCESFUL_204:
		case StatusCodes.SUCCESFUL_206:
		case StatusCodes.SUCCESFUL_BULK:
				client.close();
				response.close();
				return responseString;
			case StatusCodes.EXPIRED_TOKEN:
				refreshToken();
				return FaxFile(file,faxLine);
			case StatusCodes.INVALID_URL: 
				return Errors.INVALID_URL;
			case StatusCodes.TOO_MANY_REQUESTS:
				return Errors.TOO_MANY_REQUEST;
			case StatusCodes.SERVICE_UNAVAILABLE:
				return Errors.SERVICE_UNAVAILABLE;
			default: 
				System.out.println(status_code);
				return Errors.UNKNOWN_ERROR;
		}
	}
	public boolean checkConnection() {
		HttpGet get = new HttpGet(URLS.URL+URLS.CHECK_CONNECTION);
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response;
		try {
			response = client.execute(get);
			HttpEntity entity = response.getEntity();
			int status_code = response.getStatusLine().getStatusCode();
			LogResponse(URLS.CHECK_CONNECTION,status_code);
			if(status_code==200) 
				return true;			
			else 
				return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}	
	}
	private void extractTokens(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			ACCESS_TOKEN = obj.getString("access_token");
			REFRESH_TOKEN = obj.getString("refresh_token");
			System.out.println("ACCESS_TOKEN: "+ACCESS_TOKEN);
			System.out.println("REFRESH_TOKEN: "+REFRESH_TOKEN);
			int expired = obj.getInt("expires_in")*1000;
			System.out.println("EXPIRED: "+expired);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private JSONObject createFaxJSON(String faxLine) {
		JSONObject json = null;
		try {
			json = new JSONObject("{\"to\":[{\"phoneNumber\":\"+1"+faxLine+"\"}],\"faxResolution\":\"High\",\"coverIndex\":0}");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	public void close() {
		System.out.println("CLOSING");
		revokeToken();
		
	}
	private void LogResponse(String endpoint,int status_code) {
 		switch(status_code) {
 		case StatusCodes.SUCCESFUL_200:
 		case StatusCodes.SUCCESFUL_201:
		case StatusCodes.SUCCESFUL_202:
		case StatusCodes.SUCCESFUL_204:
		case StatusCodes.SUCCESFUL_206:
		case StatusCodes.SUCCESFUL_BULK:
			return;
		case StatusCodes.EXPIRED_TOKEN:
			ErrorSQLLog.AddErrorMessage(status_code, Errors.EXPIRED_TOKEN, endpoint, "APP",fax.getNumber());
			break;
		case StatusCodes.INVALID_URL: 
			ErrorSQLLog.AddErrorMessage(status_code, Errors.INVALID_URL, endpoint, "APP",fax.getNumber());
			break;
		case StatusCodes.TOO_MANY_REQUESTS:
			ErrorSQLLog.AddErrorMessage(status_code, Errors.TOO_MANY_REQUEST, endpoint, "APP",fax.getNumber());
			break;
		case StatusCodes.SERVICE_UNAVAILABLE:
			ErrorSQLLog.AddErrorMessage(status_code, Errors.SERVICE_UNAVAILABLE, endpoint, "APP",fax.getNumber());
			break;
		default: 
			ErrorSQLLog.AddErrorMessage(status_code, Errors.UNKNOWN_ERROR, endpoint, "APP",fax.getNumber());
			break;
 		}
 	}
}