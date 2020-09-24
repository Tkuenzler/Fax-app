package Tests;


public class Queries {
	private static String WITH_ANSWERING_MACHINE = "(("+LeadColumns.DOCTOR_ANSWER+" = "+DoctorAnswer.DOCTOR_ANSWER+" OR "+LeadColumns.DOCTOR_ANSWER+" = "+DoctorAnswer.DEFAULT+") OR ("+LeadColumns.DOCTOR_ANSWER+" = "+DoctorAnswer.NO_ONE_ANSWERED+" AND "
			+ ""+LeadColumns.LAST_UPDATED+" < DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL - 60 MINUTE)))";
	
	private static String WRONG_OR_BLANK_FAX = "("+LeadColumns.FAX_DISPOSITION+" = '' OR "+LeadColumns.FAX_DISPOSITION+" = '"+FaxStatus.WRONG_FAX+"')";
	
	private static String WITH_BIN = "(`bin` = '610014' OR `bin` = '017010' OR `bin` = '015581' OR `bin` = '003858')";
	/*
	private static String CONFIRMED_WITH_ANSWERING_MACHINE = "("+LeadColumns.CONFIRM_DOCTOR+" = "+ConfirmDoctor.CONFIRMED+" OR ("+LeadColumns.CONFIRM_DOCTOR+" = "+DoctorAnswer.NO_ONE_ANSWERED+" AND "
			+ ""+LeadColumns.LAST_UPDATED+" < DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL - 60 MINUTE)))";
	
	private static String NOT_CONFIRMED_WITH_ANSWERING_MACHINE = "("+LeadColumns.CONFIRM_DOCTOR+" = "+ConfirmDoctor.NOT_CONFIRMED+" OR ("+LeadColumns.CONFIRM_DOCTOR+" = "+ConfirmDoctor.ANSWERING_MACHINE+" AND "
			+ ""+LeadColumns.LAST_UPDATED+" < DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL - 60 MINUTE)))";
	*/
	
	public static class Select {
		public static final String RECEIVED_NO_RESPONSE = "("+LeadColumns.USED+" = "+Used.NOT_USED+" AND "+WRONG_OR_BLANK_FAX+" AND "+LeadColumns.PHARMACY+" = ? AND "+WITH_ANSWERING_MACHINE+" AND "
									+LeadColumns.MESSAGE_STATUS+" = '"+MessageStatus.SENT+"' AND "+LeadColumns.RECEIVED_DATE+" <= DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL - 5 DAY) AND "+LeadColumns.RECEIVED+" = "+Received.RECEIVED+" AND "+LeadColumns.CONFIRM_DOCTOR+" = "+ConfirmDoctor.CONFIRMED;
		
		public static final String CONFIRMED_NOT_RECEIVED = "("+LeadColumns.USED+" = "+Used.NOT_USED+" AND "+WRONG_OR_BLANK_FAX+" AND "+LeadColumns.PHARMACY+" = ? AND "+WITH_ANSWERING_MACHINE+" AND "
				+LeadColumns.MESSAGE_STATUS+" = '"+MessageStatus.SENT+"' AND "+LeadColumns.RECEIVED+" = "+Received.NOT_RECEIVED+" AND "+WITH_ANSWERING_MACHINE+" AND "+LeadColumns.CONFIRM_DOCTOR+" = "+ConfirmDoctor.CONFIRMED+")";
		
		
		public static final String NOT_CONFIRMED = "("+LeadColumns.PHARMACY+" = ? AND "+LeadColumns.USED+" = "+Used.NOT_USED+" AND "+LeadColumns.RECEIVED+" = "+Received.NOT_RECEIVED+" AND "+WITH_ANSWERING_MACHINE+" AND "+WRONG_OR_BLANK_FAX
				+" AND "+LeadColumns.CONFIRM_DOCTOR+" = "+ConfirmDoctor.NOT_CONFIRMED+" AND "+WITH_BIN+")";
		
		
		public static final String RECEIVED_NO_RESPONSE_WITH_BIN = "("+LeadColumns.USED+" = "+Used.NOT_USED+" AND "+WRONG_OR_BLANK_FAX+" AND "+LeadColumns.PHARMACY+" = ? AND "+WITH_ANSWERING_MACHINE+" AND "
				+LeadColumns.MESSAGE_STATUS+" = '"+MessageStatus.SENT+"' AND "+LeadColumns.RECEIVED_DATE+" <= DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL - 5 DAY) AND "+LeadColumns.RECEIVED+" = "+Received.RECEIVED+" AND "+LeadColumns.CONFIRM_DOCTOR+" = "+ConfirmDoctor.CONFIRMED+" AND "+WITH_BIN+")";

		public static final String CONFIRMED_NOT_RECEIVED_WITH_BIN = "("+LeadColumns.USED+" = "+Used.NOT_USED+" AND "+WRONG_OR_BLANK_FAX+" AND "+LeadColumns.PHARMACY+" = ? AND "
		+LeadColumns.MESSAGE_STATUS+" = '"+MessageStatus.SENT+"' AND "+LeadColumns.RECEIVED+" = "+Received.NOT_RECEIVED+" AND "+WITH_ANSWERING_MACHINE+" AND "+LeadColumns.CONFIRM_DOCTOR+" = "+ConfirmDoctor.CONFIRMED+" AND "+WITH_BIN+")";
		
		
		public static final String NOT_CONFIRMED_WITH_BIN = "("+LeadColumns.PHARMACY+" = ? AND "+LeadColumns.USED+" = "+Used.NOT_USED+" AND "+LeadColumns.RECEIVED+" = "+Received.NOT_RECEIVED+" AND "+WITH_ANSWERING_MACHINE+" AND "+WRONG_OR_BLANK_FAX
		+" AND "+LeadColumns.CONFIRM_DOCTOR+" = "+ConfirmDoctor.NOT_CONFIRMED+" AND "+WITH_BIN+")";
	}
}
