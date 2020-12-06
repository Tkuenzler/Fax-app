package Database.Columns;

import table.Record;

public class CarepointColumns {
	public static final String SCRIPT = "SCRIPT";
	public static final String FIRST_NAME ="first_name";
	public static final String LAST_NAME ="last_name";
	public static final String PHONE_NUMBER = "phonenumber";
	public static final String DOB = "dob";
	public static final String GENDER = "gender";
	public static final String SSN = "ssn";
	public static final String ADDRESS = "address";
	public static final String CITY = "city";
	public static final String STATE = "state";
	public static final String ZIP = "zip";
	public static final String NPI = "npi";
	public static final String DR_FIRST = "dr_first";
	public static final String DR_LAST = "dr_last";
	public static final String DR_ADDRESS = "dr_address";
	public static final String DR_CITY = "dr_city";
	public static final String DR_STATE = "dr_state";
	public static final String DR_ZIP = "dr_zip";
	public static final String DR_PHONE = "dr_phone";
	public static final String DR_FAX = "dr_fax";
	public static final String ALTERNATE_NUMBERS = "alternate_numbers";
	public static final String CALL_DISPOSITION = "CALL_DISPOSITION";
	public static final String MESSAGE_ID = "MESSAGE_ID";
	public static final String MESSAGE_STATUS = "MESSAGE_STATUS";
	public static final String LAST_UPDATED = "LAST_UPDATED";
	public static final String DATE_ADDED = "DATE_ADDED";
	public static final String MARKETING_TEAM = "MARKETING_TEAM";
	public static final String CALL_ATTEMPTS = "CALL_ATTEMPTS";
	public static final String EMDEON_STAUTS = "EMDEON_STATUS";
	
	public static final String[] DATA = {DOB,GENDER,SSN};
	
	public static final String[] ADD_TO_DATABASE = {SCRIPT,FIRST_NAME,LAST_NAME,PHONE_NUMBER,GENDER,DOB,SSN,ADDRESS,CITY,STATE,ZIP,
			NPI,DR_FIRST,DR_LAST,DR_ADDRESS,DR_CITY,DR_STATE,DR_ZIP,DR_PHONE,DR_FAX,DATE_ADDED,MARKETING_TEAM};
	
	public static Object[] CreateArray(Record record,byte[] pdfData,String marketingTeam) {
		Object[] data = new Object[ADD_TO_DATABASE.length];
		for(int i = 0;i<data.length;i++) {
			switch(ADD_TO_DATABASE[i]) {
				case FIRST_NAME: data[i] = record.getFirstName(); break; 
				case LAST_NAME: data[i] = record.getLastName(); break; 
				case PHONE_NUMBER: data[i] = record.getPhone(); break; 
				case GENDER: data[i] = record.getGender(); break;
				case SSN: data[i] = record.getSsn(); break;
				case DOB: data[i] = record.getDob(); break;
				case ADDRESS: data[i] = record.getAddress(); break; 
				case CITY: data[i] = record.getCity(); break; 
				case STATE: data[i] = record.getState(); break; 
				case ZIP: data[i] = record.getZip(); break; 
				case NPI: data[i] = record.getNpi(); break; 
				case DR_FIRST: data[i] = record.getDrLast(); break; 
				case DR_LAST: data[i] = record.getDrFirst(); break; 
				case DR_ADDRESS: data[i] = record.getDrAddress1(); break; 
				case DR_CITY: data[i] = record.getDrCity(); break; 
				case DR_STATE: data[i] = record.getDrState(); break; 
				case DR_ZIP: data[i] = record.getDrZip(); break; 
				case DR_PHONE: data[i] = record.getDrPhone(); break; 
				case DR_FAX: data[i] = record.getDrFax(); break; 
				case SCRIPT: data[i] = pdfData; break;
				case DATE_ADDED: data[i] = DateObject.CustomDate.getCurrentDate("yyyy-MM-dd"); break;
				case MARKETING_TEAM: data[i] = marketingTeam; break;
			}
		}
		return data;
	}
	
	public static Object[] CreateArray(Record record) {
		Object[] data = new Object[DATA.length];
		for(int i = 0;i<data.length;i++) {
			switch(DATA[i]) {
				case DOB: data[i] = record.getDob(); break; 
				case GENDER: data[i] = record.getGender(); break; 
				case SSN: data[i] = record.getSsn(); break; 
			}
		}
		return data;
	}
}
