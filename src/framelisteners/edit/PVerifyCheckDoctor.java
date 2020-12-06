package framelisteners.edit;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Clients.PVerifyClient;
import Database.Database;
import Database.Columns.PVerifyColumns;
import Database.Tables.Tables;
import source.CSVFrame;
import table.Record;

public class PVerifyCheckDoctor implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String password = JOptionPane.showInputDialog("What is the password?");
		if(!password.equalsIgnoreCase("Winston4503"))
			return;
		Database client = new Database("Info_Table");
		try {
			PVerifyClient pverify = new PVerifyClient("tkuenzler","Tommy6847!");
			if(!client.login() || !pverify.Login())
				return;
			for(int r = 0;r<CSVFrame.model.data.size();r++) {
				Record record = CSVFrame.model.getRowAt(r);
				String request_id = null;
				JSONObject obj = null;
				String response = null;
				ResultSet set = client.select(Tables.PVERIFY, null, PVerifyColumns.PHONE+" = ?", new String[] {record.getPhone()});
				if(set.next()) {
					request_id = ""+set.getInt(PVerifyColumns.REQUEST);
					response = pverify.GetEligibiltySummaryById(request_id);
					obj = new JSONObject(response);
					if(!obj.has(PVerifyClient.Keys.REQUEST_ID)) {
						record.setRowColor(Color.BLACK);
						continue;
					}
					System.out.println("PULLED FROM DATABASE");
				}
				else {
					response = pverify.GetEligibiltySummary(record);
					obj = new JSONObject(response);
					if(!obj.has(PVerifyClient.Keys.REQUEST_ID)) {
						record.setRowColor(Color.BLACK);
						continue;
					}
					request_id = ""+obj.getInt(PVerifyClient.Keys.REQUEST_ID);
					client.insert(Tables.PVERIFY, PVerifyColumns.ADD_TO_DATABASE, new String[] {record.getFirstName(),record.getLastName(),record.getPhone(),request_id});
				}				
				set.close();
				record.setDrFirst("");
				record.setDrLast("");
				record.setDrAddress1("");
				record.setDrCity("");
				record.setDrState("");
				record.setDrFax("");
				record.setDrPhone("");
				System.out.println(obj.toString());
				JSONObject patientInfo = obj.getJSONObject(PVerifyClient.Keys.DEMOGRAPHIC_INFO);
				JSONArray serviceDetails = obj.getJSONArray(PVerifyClient.Keys.SERVICE_DETAILS);
				UpdatePatientDemographics(patientInfo,record);
				UpdateDoctorDemographics(serviceDetails,record);
			}
		} catch(SQLException ex) {
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	private void UpdatePatientDemographics(JSONObject patientInfo,Record record) throws JSONException {
		JSONObject subscriber = patientInfo.getJSONObject(PVerifyClient.Keys.SUBSCRIBER);
		if(!subscriber.isNull(PVerifyClient.Keys.ADDRESS) &&
				!subscriber.isNull(PVerifyClient.Keys.CITY) &&
				!subscriber.isNull(PVerifyClient.Keys.STATE) &&
				!subscriber.isNull(PVerifyClient.Keys.ZIP))
		record.setAddress(subscriber.getString(PVerifyClient.Keys.ADDRESS));
		record.setCity(subscriber.getString(PVerifyClient.Keys.CITY));
		record.setState(subscriber.getString(PVerifyClient.Keys.STATE));
		record.setZip(subscriber.getString(PVerifyClient.Keys.ZIP).substring(0,5));
		record.setGender(subscriber.getString(PVerifyClient.Keys.GENDER));
	}
	private void UpdateDoctorDemographics(JSONArray serviceDetails,Record record) throws JSONException {
		for(int i = 0;i<serviceDetails.length();i++) {
			JSONObject object = serviceDetails.getJSONObject(i);
			System.out.println(object.getString(PVerifyClient.Keys.SERVICE_NAME));
			if(!object.getString(PVerifyClient.Keys.SERVICE_NAME).equalsIgnoreCase("OTHERS"))
				continue;
			JSONArray eligibiltyDetails = object.getJSONArray(PVerifyClient.Keys.ELIGIBILITY_DETAILS);
			//System.out.println(eligibiltyDetails.toString());
			for(int x = 0; x< eligibiltyDetails.length();x++) {
				JSONObject eligibility = eligibiltyDetails.getJSONObject(x);
				if(!eligibility.getString(PVerifyClient.Keys.ELIGIBILITY_OR_BENEFIT).equalsIgnoreCase("PRIMARY CARE PROVIDER"))
					continue;
				JSONArray benefitEntities = eligibility.getJSONArray(PVerifyClient.Keys.BENEFIT_ENTITIES);
				for(int y = 0;y<benefitEntities.length();y++) {
					JSONObject benefit = benefitEntities.getJSONObject(y);
					if(benefit.getString(PVerifyClient.Keys.ENTITY_TYPE).equalsIgnoreCase("PRIMARY CARE PROVIDER")) {
						if(!benefit.isNull(PVerifyClient.Keys.ADDRESS))
							record.setDrAddress1(benefit.getString(PVerifyClient.Keys.ADDRESS));
						if(!benefit.isNull(PVerifyClient.Keys.CITY))
							record.setDrCity(benefit.getString(PVerifyClient.Keys.CITY));
						if(!benefit.isNull(PVerifyClient.Keys.STATE))
							record.setDrState(benefit.getString(PVerifyClient.Keys.STATE));
						if(!benefit.isNull(PVerifyClient.Keys.ZIP))
							record.setDrZip(benefit.getString(PVerifyClient.Keys.ZIP).substring(0,5));
						JSONArray communicationNumbers = benefit.getJSONArray(PVerifyClient.Keys.COMMUNICATION_NUMBER);
						System.out.println(communicationNumbers.toString());
						for(int z = 0;z<communicationNumbers.length();z++) {
							JSONObject communication = communicationNumbers.getJSONObject(z);
							if(communication.getString(PVerifyClient.Keys.TYPE).equalsIgnoreCase("Telephone")) {
								if(!communication.isNull(PVerifyClient.Keys.NUMBER)) {
									String phone = communication.getString(PVerifyClient.Keys.NUMBER);
									if(phone.equalsIgnoreCase(record.getDrPhone()))
										record.setRowColor(Color.YELLOW);
									else {
										record.setDrPhone(phone);
										record.setRowColor(Color.GREEN);
									}
								}
							}
							else
								System.out.println(communication.getString(PVerifyClient.Keys.TYPE));
						}
					}
				}
			}
		}
	}
}
