package source;


import java.awt.HeadlessException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import Tests.Database;
import Tests.LeadColumns;
import Tests.Queries;
import Tests.Query;
import table.Record;



public class Main {
	
	public static void main(String[] args) throws ClientProtocolException, IOException, JSONException, SQLException {			
		//FCCClient.GetComplaintsFromNumbers();
		//EZScriptRxClient rxClient = new EZScriptRxClient();
		//rxClient.Login("allfamilyrheem", "@11Fam1lyPh@rm%");
		System.out.println(Queries.Select.CONFIRMED_NOT_RECEIVED_WITH_BIN);
		Database client = new Database("MT_MARKETING");
		client.login();
		ResultSet set  = client.selectSort("Leads", null, Queries.Select.CONFIRMED_NOT_RECEIVED_WITH_BIN, new String[] {"All_Pharmacy"},new String[] {LeadColumns.LAST_UPDATED},new String[] {Query.Order.DESCENDING});
		while(set.next()) {
			Record record = new Record(set,"","");
			System.out.println(record.getFirstName()+" "+record.getLastName());
			
		}
		try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {					
					try {
						UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						if(Checker.check(LoadInfo.getAppName())) {
							
							new CSVFrame();
						}
						else {
							JOptionPane.showMessageDialog(new JFrame(), "Error 4503 has occured.");
							System.exit(0);
						}
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException| UnsupportedLookAndFeelException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(new JFrame(), e.getMessage());				
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, e.getMessage());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, e.getMessage());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
			    	
			    }
			});		
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	public static void test() {
		URIBuilder b = null;
		URL url = null;
		try {
			b = new URIBuilder("http://telemed.quivvytech.com/api/v4/api.php");
			b.addParameter(Parameters.FIRST_NAME, "Tommy")
			.addParameter(Parameters.LAST_NAME, "Kuenzler")
			.addParameter(Parameters.GENDER, "Male")
			.addParameter(Parameters.PHONE_NUMBER, "5618436847")
			.addParameter(Parameters.DOB_MONTH, "11")
			.addParameter(Parameters.DOB_DAY, "21")
			.addParameter(Parameters.DOB_YEAR, "1986")
			.addParameter(Parameters.ADDRESS, "address")
			.addParameter(Parameters.CITY, "city")
			.addParameter(Parameters.STATE, "fl")
			.addParameter(Parameters.POSTAL_CODE, "33445")
			.addParameter(Parameters.INSURANCE_CARRIER,  "bcbs")
			.addParameter(Parameters.PATIENT_ID, "")
			.addParameter(Parameters.BIN, "")
			.addParameter(Parameters.PCN, "")
			.addParameter(Parameters.GROUP_ID, "")
			.addParameter(Parameters.SOURCE_ID, "5618436847")
			.addParameter(Parameters.PRODUCT_SUGGESTIONS, "")
			.addParameter(Parameters.SUB_PROGRAM, "")
			.addParameter(Parameters.INSERT_TIME, GetInsertTime())
			.addParameter(Parameters.MEDICATIONS, "")
			.addParameter(Parameters.ALLERGIES, "")
			.addParameter(Parameters.SIGNATURE_VALIDATION, "jDOBAf$3");
			url = b.build().toURL();
			System.out.println(url.toString());
		} catch (URISyntaxException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static String GetInsertTime()  {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	class Parameters {
			public static final String FIRST_NAME = "first_name";
			public static final String LAST_NAME = "last_name";
			public static final String PHONE_NUMBER ="phone_number";
			public static final String DOB_MONTH = "dob_month";
			public static final String DOB_DAY = "dob_day";
			public static final String DOB_YEAR = "dob_year";
			public static final String GENDER = "gender";
			public static final String ADDRESS = "address1";
			public static final String CITY = "city";
			public static final String STATE = "state";
			public static final String POSTAL_CODE = "postal_code";
			public static final String INSURANCE_CARRIER = "insurance_carrier";
			public static final String PATIENT_ID = "patient_id";
			public static final String GROUP_ID = "group_id";
			public static final String BIN = "bin_number";
			public static final String PCN = "pcnNumber";
			public static final String SOURCE_ID = "SOURCE_ID";
			public static final String MEDICATIONS = "CFT_Sub_Otc_Medications";
			public static final String ALLERGIES = "CFT_Sub_Allergies";
			public static final String PRODUCT_SUGGESTIONS = "productSuggestions";
			public static final String INSERT_TIME = "InsertTime";
			public static final String SIGNATURE_VALIDATION = "SignatureValidation";
			public static final String SUB_PROGRAM = "subProgram";
		}
	
}