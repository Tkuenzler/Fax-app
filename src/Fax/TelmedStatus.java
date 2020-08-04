package Fax;

public class TelmedStatus {
	//NOT PAID
	public static final String NEW = "New Patient";
	public static final String IN_PROCESS = "Verifications - In Process";
	public static final String UNABLE_TO_CONTACT = "Verifications - Unable to Contact";
	public static final String NOT_INTERESTED = "Verifications - Not Interested";
	public static final String REFUSED = "Telemed - Patient Refused";
	public static final String DUPLICATE = "Verifications - Duplicate Record";
	public static final String FOLLOW_UP = "Verifications - Long Term Follow Up";
	public static final String DENIED_FEE = "Denied Fee";
	
	public static final String[] NON_PAID_STATUS = {NEW,IN_PROCESS,UNABLE_TO_CONTACT,NOT_INTERESTED,REFUSED,DUPLICATE,FOLLOW_UP};
	public static String GetNonPaidStatusQuery() {
		StringBuilder query = new StringBuilder();
		for(int i = 0;i<NON_PAID_STATUS.length;i++) {
			if(i==0)
				query.append(" (`TELMED_STATUS` =  '"+NON_PAID_STATUS[i]+"' OR ");
			else if(i<=NON_PAID_STATUS.length-2)
				query.append("`TELMED_STATUS` =  '"+NON_PAID_STATUS[i]+"' OR ");
			else if(i==NON_PAID_STATUS.length-1)
				query.append("`TELMED_STATUS` = '"+NON_PAID_STATUS[i]+"')");
		}
		return query.toString();
	}
	
	//RE-XTRER 
	public static final String[] RE_XFER = {UNABLE_TO_CONTACT,NOT_INTERESTED,REFUSED};
	public static String GetReXferStatusQuery() {
		return "(`TELMED_STATUS` = '"+UNABLE_TO_CONTACT+"' OR `TELMED_STATUS` = '"+NOT_INTERESTED+"' OR `TELMED_STATUS` = '"+REFUSED+"')";
	}
	
	//PAID
	public static final String XFER = "Telemed - Xfer to Doctor";
	public static final String DISQUALIFY = "Telemed - Disqualify";
	public static final String CONSULT_APPROVED = "Telemed - Consult Approved";
	public static final String SENT_TO_TELMED = "Sent to Telemed";
	public static final String APPROVED = "Telemed - Sent to Pharmacy";
	
	public static final String[] PAID_STATUS = {XFER,DISQUALIFY,CONSULT_APPROVED,APPROVED,SENT_TO_TELMED};
	
	public static final String[] APPROVED_PAID_STATUS = {XFER,CONSULT_APPROVED,APPROVED,SENT_TO_TELMED};
	
	public static String GetPaidStatusQuery() {
		StringBuilder query = new StringBuilder();
		for(int i = 0;i<PAID_STATUS.length;i++) {
			if(i==0)
				query.append(" (`TELMED_STATUS` =  '"+PAID_STATUS[i]+"' OR ");
			else if(i<=PAID_STATUS.length-2)
				query.append("`TELMED_STATUS` =  '"+PAID_STATUS[i]+"' OR ");
			else if(i==PAID_STATUS.length-1)
				query.append("`TELMED_STATUS` = '"+PAID_STATUS[i]+"')");
		}
		return query.toString();
	}
	
	public static String GetApprovedPaidStatusQuery() {
		StringBuilder query = new StringBuilder();
		for(int i = 0;i<APPROVED_PAID_STATUS.length;i++) {
			if(i==0)
				query.append(" (`TELMED_STATUS` =  '"+APPROVED_PAID_STATUS[i]+"' OR ");
			else if(i<=APPROVED_PAID_STATUS.length-2)
				query.append("`TELMED_STATUS` =  '"+APPROVED_PAID_STATUS[i]+"' OR ");
			else if(i==APPROVED_PAID_STATUS.length-1)
				query.append("`TELMED_STATUS` = '"+APPROVED_PAID_STATUS[i]+"')");
		}
		return query.toString();
	}
	
	public static String NGetPaidStatusQuery() {
		StringBuilder query = new StringBuilder();
		for(int i = 0;i<PAID_STATUS.length;i++) {
			if(i==0)
				query.append(" (`TELMED_STATUS` <>  '"+PAID_STATUS[i]+"' AND ");
			else if(i<=PAID_STATUS.length-2)
				query.append("`TELMED_STATUS` <>  '"+PAID_STATUS[i]+"' AND ");
			else if(i==PAID_STATUS.length-1)
				query.append("`TELMED_STATUS` <> '"+PAID_STATUS[i]+"')");
		}
		return query.toString();
	}
	public static String GetSuccesfulQuery() {
		return "(`TELMED_STATUS` = '"+XFER+"' OR `TELMED_STATUS` = '"+DISQUALIFY+"' OR `TELMED_STATUS` = '"+CONSULT_APPROVED+"' OR `TELMED_STATUS` = '"+APPROVED+"')";
	}
	
}
