package Database.Columns;

import java.util.Date;
import table.Record;


public class DMEColumns {
	public static final String FIRST_NAME = "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String DOB = "dob";
	public static final String ADDRESS = "address";
	public static final String CITY = "city";
	public static final String STATE = "state";
	public static final String ZIP = "zip";
	public static final String PHONE = "phonenumber";
	public static final String GENDER = "gender";
	public static final String CARRIER = "carrier";
	public static final String POLICY_ID = "policy_id";
	public static final String AGENT = "agent";
	public static final String SOURCE = "SOURCE";
	public static final String DATE_ADDED = "DATE_ADDED";
	public static final String BRACES = "braces";
	public static final String ID = "_id";
	public static final String LAST_UPDATED = "LAST_UPDATED";
	public static final String NOTES = "NOTES";
	public static final String ATTENTION = "ATTENTION";
	public static final String USED = "USED";
	public static final String CONFIRM_DOCTOR = "CONFIRM_DOCTOR";
	public static final String RECEIVED = "RECEIVED";
	
	//Doctor Columns
	public static final String NPI = "npi";
	public static final String DR_TYPE = "dr_type";
	public static final String DR_FIRST = "dr_first";
	public static final String DR_LAST = "dr_last";
	public static final String DR_ADDRESS = "dr_address";
	public static final String DR_CITY = "dr_city";
	public static final String DR_STATE = "dr_state";
	public static final String DR_ZIP = "dr_zip";
	public static final String DR_PHONE = "dr_phone";
	public static final String DR_FAX = "dr_fax";
	
	
	//FAX DISPOSITION
	public static final String FAX_DISPOSITION = "FAX_DISPOSITION";
	public static final String FAX_SENT_DATE = "FAX_SENT_DATE";
	public static final String FAXES_SENT = "FAXES_SENT";
	public static final String MESSAGE_STATUS = "MESSAGE_STATUS";
	
	public static final String[] DR_COLUMNS = {NPI,DR_FIRST,DR_LAST,DR_ADDRESS,DR_CITY,DR_STATE,DR_ZIP,DR_PHONE,DR_FAX};
	public static final String[] ALL_COLUMNS = {FIRST_NAME,LAST_NAME,DOB,ADDRESS,CITY,STATE,ZIP,GENDER,PHONE,CARRIER,POLICY_ID,
			NPI,DR_FIRST,DR_LAST,DR_ADDRESS,DR_CITY,DR_STATE,DR_ZIP,DR_PHONE,DR_FAX,DATE_ADDED,ID,BRACES,AGENT}; 
	
	public static String[] ToStringArray(Record record) {
		String[] array = new String[ALL_COLUMNS.length];
		for(int i = 0;i < array.length;i++) {
			String value = ALL_COLUMNS[i];
			switch(value) {
				case FIRST_NAME: array[i] = record.getFirstName(); break;
				case LAST_NAME: array[i] = record.getLastName(); break;
				case DOB: array[i] = record.getDob(); break;
				case PHONE: array[i] = record.getPhone(); break;
				case ADDRESS: array[i] = record.getAddress(); break;
				case CITY: array[i] = record.getCity(); break;
				case STATE:  array[i] = record.getState(); break;
				case ZIP: array[i] = record.getZip(); break;
				case GENDER:  array[i] = record.getGender(); break;
				case ID: array[i] = record.getId(); break;
				case CARRIER: array[i] = record.getCarrier(); break;
				case POLICY_ID: array[i] = record.getPolicyId(); break;
				case NPI: array[i] = record.getNpi(); break;
				case DR_FIRST: array[i] = record.getDrFirst(); break;
				case DR_LAST: array[i] = record.getDrLast(); break;
				case DR_ADDRESS: array[i] = record.getDrAddress1(); break;
				case DR_CITY: array[i] = record.getDrCity(); break;
				case DR_STATE: array[i] = record.getDrState(); break;
				case DR_ZIP: array[i] = record.getDrZip(); break;
				case DR_PHONE: array[i] = record.getDrPhone(); break;
				case DR_FAX: array[i] = record.getDrFax(); break;
				case DATE_ADDED: array[i] = DateObject.CustomDate.getCurrentDate("yyyy-MM-dd"); break;
				case BRACES: array[i] = record.getPainLocation(); break;
				case AGENT: array[i] = record.getAgent(); break;
			}
		}
		return array;
	}
	
	
}
