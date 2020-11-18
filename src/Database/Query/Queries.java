package Database.Query;

import Database.Columns.DMEColumns;

public class Queries {
	public class Select {
		public static final String LOAD_DME_FAXABLES = "("+DMEColumns.CONFIRM_DOCTOR+" = ? OR "+DMEColumns.CONFIRM_DOCTOR+" = ?) AND "+DMEColumns.FAX_DISPOSITION+" = '' AND "+DMEColumns.FAX_SENT_DATE+" < DATE_ADD(CURDATE(), INTERVAL -5 DAY)";
		public static final String MBI = "CHAR_LENGTH("+DMEColumns.POLICY_ID+") = 11";
	}
}
