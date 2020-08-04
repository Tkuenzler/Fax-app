package framelisteners.file;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import Clients.DatabaseClient;
import Clients.InfoDatabase;
import Clients.PVerifyClient;
import Fax.MessageStatus;
import Fax.ProductScripts;
import Fax.TelmedStatus;
import PBM.InsuranceFilter;
import PBM.InsuranceType;
import source.CSVFrame;
import table.Record;

public class Test implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String password = JOptionPane.showInputDialog("Password");
		if(!password.equalsIgnoreCase("Winston4503"))
			return;
		Clear();
		
	}
	public void d() {
		DatabaseClient client = new DatabaseClient(false);
		ResultSet set = client.customQuery("SELECT * FROM `Leads` WHERE `EMDEON_STATUS` = 'FOUND' AND `EMDEON_TYPE` = ''");
		try {
			while(set.next()) {
				Record record = new Record(set,"","");
				String type = InsuranceFilter.Filter(record);
				String emdeon_type = null;
				if(!record.getStatus().equalsIgnoreCase("FOUND"))
					type = record.getStatus();
				else
					switch(type) {
						//Private 
						case InsuranceType.PRIVATE_NO_TELMED:
						case InsuranceType.PRIVATE_VERIFIED:
						case InsuranceType.PRIVATE_UNKNOWN:
						case InsuranceType.NOT_COVERED:
						case InsuranceType.PRIVATE_NOTVALIDATED:
						case InsuranceType.MOLINA:
							emdeon_type = "Private";
							break;
						//Medicare
						case InsuranceType.MEDICARE_TELMED:
						case InsuranceType.MEDICARE_COMMERCIAL:
						case InsuranceType.MAPD:
						case InsuranceType.MAPD_HMO:
						case InsuranceType.MAPD_PPO:
						case InsuranceType.PDP:
						case InsuranceType.MEDICAID_MEDICARE:
							emdeon_type = "Medicare";
							break;
						//Unknown
						case InsuranceType.UNKNOWN_PBM:
							emdeon_type = "Unknown";
							break;
						case InsuranceType.OUT_OF_NETWORK:
						case InsuranceType.MEDICAID:
							emdeon_type = "Medicaid";
							break;
						case InsuranceType.TRICARE:
							emdeon_type = "Tricare";
							break;
						default:
							emdeon_type = "NONE";
					}
				System.out.println(client.updateRecord("EMDEON_TYPE", emdeon_type, record));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void e() {
		DatabaseClient client = new DatabaseClient(false);
		ResultSet set = client.customQuery("SELECT * FROM `Leads` WHERE `dr_type` = '' AND `AFID` = 'MT-LIVE'");
		try {
			while(set.next()) {
				Record record = new Record(set,"","");
				if(record.getNpi().length()==10 && record.getNpi().startsWith("1")) {
					HttpURLConnection connection;
					try {
						connection = (HttpURLConnection) new URL("https://ltf5469.tam.us.siteprotect.com/API/rest/Verify/Doctor?npi="+record.getNpi()).openConnection();
						connection.connect();
						BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						JSONObject obj = new JSONObject(rd.readLine());
						if(obj.getString("VALID").equalsIgnoreCase("VALID")) {
							System.out.println(client.updateRecord("dr_type", obj.getString("DR_TYPE"), record));
							CSVFrame.model.addRow(record);
						}
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.close();
	}
	public void Clear() {
		DatabaseClient client = new DatabaseClient(false);
		String statusColumn = ProductScripts.GetProductMessageStatus(ProductScripts.ORAL_SCRIPT);
		String faxColumn = ProductScripts.GetProductFaxDispositionColumn(ProductScripts.ORAL_SCRIPT);
		String dateFax = ProductScripts.GetProductFaxDispositionColumn(ProductScripts.ORAL_SCRIPT);
		String dateColumn = ProductScripts.GetProductFaxDate(ProductScripts.ORAL_SCRIPT);
		for(Record record: CSVFrame.model.data) {
			record.setMessageStatus("");
			record.setFaxStatus("");
			System.out.println(client.UpdateProductMessageStatus(statusColumn, dateColumn, record));
			System.out.println(client.UpdateProductDisposition(faxColumn, dateFax,"", record.getId()));
		}
	}
	public void Test1() throws IOException, JSONException {
		DatabaseClient client = new DatabaseClient(false);
		PVerifyClient pverify = new PVerifyClient("Tkuenzler","Tommy6847!");
		if(!pverify.Login())
			return;
		InfoDatabase info = new InfoDatabase();
		String query = TelmedStatus.GetReXferStatusQuery();
		String sql = "SELECT * FROM `DME_TELMED` WHERE "+query;
		ResultSet set = client.customQuery(sql);
		try {
			while(set.next()) {
				System.out.println(set.getString("first_name")+" "+set.getString("last_name"));
				String getPverifyId = info.GetPVerifyId(set.getString("phonenumber"));
				Record record = client.getRecord(set.getString("phonenumber"));
				JSONObject json = pverify.GetFullEligibiltySummaryById("1013575406", getPverifyId);
				//System.out.println(json.toString());
				System.out.println(client.AddDMELead(record));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void Test2() {
		DatabaseClient client = new DatabaseClient(false);
		String sql = "SELECT * FROM `DME_Leads`";
		ResultSet set = client.customQuery(sql);
		try {
			while(set.next()) {
				Record record = client.getRecord(set.getString("phonenumber"));
				client.updateRecord("policy_id", record.getPolicyId(), record, "DME_Leads");
				client.updateRecord("npi", record.getNpi(), record, "DME_Leads");
				client.updateRecord("dr_first_name", record.getDrFirst(), record, "DME_Leads");
				client.updateRecord("dr_last_name", record.getDrLast(), record, "DME_Leads");
				client.updateRecord("dr_address1", record.getDrAddress1(), record, "DME_Leads");
				client.updateRecord("dr_city", record.getCity(), record, "DME_Leads");
				client.updateRecord("dr_state", record.getState(), record, "DME_Leads");
				client.updateRecord("dr_city", record.getCity(), record, "DME_Leads");
				client.updateRecord("dr_zip", record.getZip(), record, "DME_Leads");
				client.updateRecord("dr_fax", record.getDrFax(), record, "DME_Leads");
				client.updateRecord("dr_phone", record.getDrPhone(), record, "DME_Leads");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void ChecDMEStates() {
		for(Record record: CSVFrame.model.data) {
			switch(record.getState()) {
				case "AL":
				case "CO":
				case "CT":
				case "MS":
				case "NV":
				case "NJ":
				case "ND":
				case "OK":
				case "PA":
				case "TX":
					record.setRowColor(Color.RED);
					break;
				default:
					record.setRowColor(Color.GREEN);
					break;
			}
		}
	}
}
