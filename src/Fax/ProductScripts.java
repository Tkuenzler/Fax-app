package Fax;

public class ProductScripts {
	public static final String TOPICAL_SCRIPT = "Topical Script";
	public static final String ORAL_SCRIPT = "Oral Script";
	public static final String ANTI_FUNGAL_SCRIPT = "Anti-Fungal Script";
	public static final String MIGRAINE_SCRIPT = "Migraine Script";
	public static final String VITAMIN_SCRIPT = "Vitamin Script";
	public static final String COVERED_MEDS = "Covered Meds";
	
	public static final String[] ALL = {TOPICAL_SCRIPT,ORAL_SCRIPT,ANTI_FUNGAL_SCRIPT,MIGRAINE_SCRIPT,VITAMIN_SCRIPT,COVERED_MEDS};
	
	public static final String TOPICAL_SCRIPT_FAX_DISPOSITION = "TOPICAL_SCRIPT_FAX_DISPOSITION";
	public static final String ORAL_SCRIPT_FAX_DISPOSITION = "ORAL_SCRIPT_FAX_DISPOSITION";
	public static final String ANTI_FUNGAL_FAX_DISPOSITION = "ANTI_FUNGAL_SCRIPT_FAX_DISPOSITION";
	public static final String MIGRAINE_FAX_DISPOSITION = "MIGRAINE_SCRIPT_FAX_DISPOSITION";
	public static final String VITAMIN_FAX_DISPOSITION = "VITAMIN_SCRIPT_FAX_DISPOSITION";
	public static final String COVERED_FAX_DISPOSITION = "COVERED_SCRIPT_FAX_DISPOSITION";
	
	public static final String TOPICAL_SCRIPT_MESSAGE_STATUS = "TOPICAL_SCRIPT_MESSAGE_STATUS";
	public static final String ORAL_SCRIPT_MESSAGE_STATUS = "ORAL_SCRIPT_MESSAGE_STATUS";
	public static final String ANTI_FUNGAL_SCRIPT_MESSAGE_STATUS = "ANTI_FUNGAL_SCRIPT_MESSAGE_STATUS";
	public static final String MIGRAINE_MESSAGE_STATUS = "MIGRAINE_SCRIPT_MESSAGE_STATUS";
	public static final String VITAMIN_MESSAGE_STATUS = "VITAMIN_SCRIPT_MESSAGE_STATUS";
	public static final String COVERED_MESSAGE_STATUS = "COVERED_SCRIPT_MESSAGE_STATUS";

	public static final String TOPICAL_SCRIPT_FAX_SENT_DATE = "TOPICAL_SCRIPT_FAX_SENT_DATE";
	public static final String ORAL_SCRIPT_FAX_SENT_DATE = "ORAL_SCRIPT_FAX_SENT_DATE";
	public static final String ANTI_FUNGAL_SCRIPT_FAX_SENT_DATE = "ANTI_FUNGAL_SCRIPT_FAX_SENT_DATE";
	public static final String MIGRAINE_FAX_SENT_DATE = "MIGRAINE_SCRIPT_FAX_SENT_DATE";
	public static final String VITAMIN_FAX_SENT_DATE = "VITAMIN_SCRIPT_FAX_SENT_DATE";
	public static final String COVERED_FAX_SENT_DATE = "COVERED_SCRIPT_FAX_SENT_DATE";
	
	public static final String TOPICAL_SCRIPT_FAX_DISPOSITION_DATE = "TOPICAL_SCRIPT_FAX_DISPOSITION_DATE";
	public static final String ORAL_SCRIPT_FAX_DISPOSITION_DATE = "ORAL_SCRIPT_FAX_DISPOSITION_DATE";
	public static final String ANTI_FUNGAL_FAX_DISPOSITION_DATE = "ANTI_FUNGAL_SCRIPT_FAX_DISPOSITION_DATE";
	public static final String MIGRAINE_FAX_DISPOSITION_DATE = "MIGRAINE_SCRIPT_FAX_DISPOSITION_DATE";
	public static final String VITAMIN_FAX_DISPOSITION_DATE = "VITAMIN_SCRIPT_FAX_SENT_DATE";
	public static final String COVERED_FAX_DISPOSITION_DATE = "COVERED_SCRIPT_FAX_SENT_DATE";
	
	public static String GetProductFaxDispositionColumn(String product) {
		switch(product) {
			case TOPICAL_SCRIPT:
				return TOPICAL_SCRIPT_FAX_DISPOSITION;
			case ORAL_SCRIPT:
				return ORAL_SCRIPT_FAX_DISPOSITION;
			case ANTI_FUNGAL_SCRIPT:
				return ANTI_FUNGAL_FAX_DISPOSITION;	
			case MIGRAINE_SCRIPT:
				return MIGRAINE_FAX_DISPOSITION;
			case VITAMIN_SCRIPT:
				return VITAMIN_FAX_DISPOSITION;
			case COVERED_MEDS:
				return COVERED_FAX_DISPOSITION;
			default:
				return "";
		}
	}
	public static String GetProductFaxDispositionDateColumn(String product) {
		switch(product) {
			case TOPICAL_SCRIPT:
				return TOPICAL_SCRIPT_FAX_DISPOSITION_DATE;
			case ORAL_SCRIPT:
				return ORAL_SCRIPT_FAX_DISPOSITION_DATE;
			case ANTI_FUNGAL_SCRIPT:
				return ANTI_FUNGAL_FAX_DISPOSITION_DATE;
			case MIGRAINE_SCRIPT:
				return MIGRAINE_FAX_DISPOSITION_DATE;
			case VITAMIN_SCRIPT:
				return VITAMIN_FAX_DISPOSITION_DATE;
			case COVERED_MEDS:
				return COVERED_FAX_DISPOSITION_DATE;
			default:
				return "";
		}
	}
	public static String GetProductMessageStatus(String product) {
		switch(product) {
			case TOPICAL_SCRIPT:
				return TOPICAL_SCRIPT_MESSAGE_STATUS;
			case ORAL_SCRIPT:
				return ORAL_SCRIPT_MESSAGE_STATUS;
			case ANTI_FUNGAL_SCRIPT:
				return ANTI_FUNGAL_SCRIPT_MESSAGE_STATUS;
			case MIGRAINE_SCRIPT:
				return MIGRAINE_MESSAGE_STATUS;
			case VITAMIN_SCRIPT:
				return VITAMIN_MESSAGE_STATUS;
			case COVERED_MEDS:
				return COVERED_MESSAGE_STATUS;
			default:
				return "";
		}
	}
	public static String GetProductFaxDate(String product) {
		switch(product) {
			case TOPICAL_SCRIPT:
				return TOPICAL_SCRIPT_FAX_SENT_DATE;
			case ORAL_SCRIPT:
				return ORAL_SCRIPT_FAX_SENT_DATE;
			case ANTI_FUNGAL_SCRIPT:
				return ANTI_FUNGAL_SCRIPT_FAX_SENT_DATE;
			case MIGRAINE_SCRIPT:
				return MIGRAINE_FAX_SENT_DATE;
			case VITAMIN_SCRIPT:
				return VITAMIN_FAX_SENT_DATE;
			case COVERED_MEDS:
				return COVERED_FAX_SENT_DATE;
			default:
				return "";
		}
	}
	public static String NotFaxedInDays(int x) {
		StringBuilder sb = new StringBuilder();
		sb.append("( `FAX_SENT_DATE` < DATE_ADD(CURDATE(), INTERVAL - "+x+" DAY) AND ");
		for(String s: ALL) {
			String fax_sent =  GetProductFaxDate(s);
			sb.append("`Alternate_Scripts`.`"+fax_sent+"` < DATE_ADD(CURDATE(), INTERVAL - "+x+" DAY) AND ");
		}
		sb.delete(sb.length()-4, sb.length()-1);
		sb.append(")");
		return sb.toString();
	}
	
	
}
