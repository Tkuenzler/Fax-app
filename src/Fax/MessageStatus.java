package Fax;

public class MessageStatus {
	
	public static final String BLANK = "";
	public static final String DNF = "DNF";
	public static final String RECEIVED = "RECEIVED";
	public static final String SENT = "Sent";
	public static final String QUEUED = "Queued";
	public static final String BAD_FAX_NUMBER = "Bad Fax Number";
	public static final String SENDING_FAILED = "SendingFailed";
	public static final String ILLEGAL_ARGUMENT = "Illegal Argument";
	public static final String ON_HOLD = "On Hold";
	
	public static final String[] MESSAGE_STATUS = {BLANK,DNF,RECEIVED,SENT,QUEUED,BAD_FAX_NUMBER,SENDING_FAILED,ON_HOLD};

	public static final boolean IsMessageStatus(String status) {
		for(String s: MESSAGE_STATUS) {
			if(status.equalsIgnoreCase(s))
				return true;
		}
		return false;
	}
}
