package Fax;

public class ProductScripts {
	public static final String TOPICAL_SCRIPT = "Topical Script";
	public static final String ORAL_SCRIPT = "Oral Script";
	public static final String ANTI_FUNGAL_SCRIPT = "Anti-Fungal Script";
	public static final String CONSTIPATION_SCRIPT = "Constipation Script";

	
	public static final String[] ALL = {TOPICAL_SCRIPT,ORAL_SCRIPT,ANTI_FUNGAL_SCRIPT,CONSTIPATION_SCRIPT};
	
	public static final String TOPICAL_SCRIPT_FAX_DISPOSITION = "TOPICAL_SCRIPT_FAX_DISPOSITION";
	public static final String ORAL_SCRIPT_FAX_DISPOSITION = "ORAL_SCRIPT_FAX_DISPOSITION";
	public static final String ANTI_FUNGAL_FAX_DISPOSITION = "ANTI_FUNGAL_SCRIPT_FAX_DISPOSITION";
	public static final String CONSTIPATION_FAX_DISPOSITION = "CONSTIPATION_FAX_DISPOSITION";
	
	public static final String TOPICAL_SCRIPT_MESSAGE_STATUS = "TOPICAL_SCRIPT_MESSAGE_STATUS";
	public static final String ORAL_SCRIPT_MESSAGE_STATUS = "ORAL_SCRIPT_MESSAGE_STATUS";
	public static final String ANTI_FUNGAL_SCRIPT_MESSAGE_STATUS = "ANTI_FUNGAL_SCRIPT_MESSAGE_STATUS";
	public static final String CONSTIPATION_MESSAGE_STATUS = "CONSTIPATION_MESSAGE_STATUS";

	
	public static final String TOPICAL_SCRIPT_FAX_SENT_DATE = "TOPICAL_SCRIPT_FAX_SENT_DATE";
	public static final String ORAL_SCRIPT_FAX_SENT_DATE = "ORAL_SCRIPT_FAX_SENT_DATE";
	public static final String ANTI_FUNGAL_SCRIPT_FAX_SENT_DATE = "ANTI_FUNGAL_SCRIPT_FAX_SENT_DATE";
	public static final String CONSTIPATION_FAX_SENT_DATE = "CONSTIPATION_FAX_SENT_DATE";
	
	public static final String TOPICAL_SCRIPT_FAX_DISPOSITION_DATE = "TOPICAL_SCRIPT_FAX_DISPOSITION_DATE";
	public static final String ORAL_SCRIPT_FAX_DISPOSITION_DATE = "ORAL_SCRIPT_FAX_DISPOSITION_DATE";
	public static final String ANTI_FUNGAL_FAX_DISPOSITION_DATE = "ANTI_FUNGAL_SCRIPT_FAX_DISPOSITION_DATE";
	public static final String CONSTIPATION_FAX_DISPOSITION_DATE = "CONSTIPATION_FAX_DISPOSITION_DATE";
	
	
	public static String GetProductFaxDispositionColumn(String product) {
		switch(product) {
			case TOPICAL_SCRIPT:
				return TOPICAL_SCRIPT_FAX_DISPOSITION;
			case ORAL_SCRIPT:
				return ORAL_SCRIPT_FAX_DISPOSITION;
			case ANTI_FUNGAL_SCRIPT:
				return ANTI_FUNGAL_FAX_DISPOSITION;	
			case CONSTIPATION_SCRIPT:
				return CONSTIPATION_FAX_DISPOSITION;
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
			case CONSTIPATION_SCRIPT:
				return CONSTIPATION_FAX_DISPOSITION_DATE;
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
			case CONSTIPATION_SCRIPT:
				return CONSTIPATION_MESSAGE_STATUS;
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
			case CONSTIPATION_SCRIPT:
				return CONSTIPATION_FAX_SENT_DATE;
			default:
				return "";
		}
	}
	
	
}
