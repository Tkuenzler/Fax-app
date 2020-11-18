package Clients;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.logging.LogFactory;
import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import Fax.EmdeonStatus;
import PBM.InsuranceFilter;
import PBM.InsuranceType;
import Properties.EmdeonProperties;
import objects.Emdeon;
import objects.Insurance;
import objects.InsuranceInfo;
import table.Record;

public class EmdeonClient {
	WebClient webClient = new WebClient(BrowserVersion.CHROME);
	CookieManager cookieManager;
	HtmlPage cardFinderPage;
	Emdeon emd;
	public EmdeonClient() {
		EmdeonProperties properties = new EmdeonProperties();
		emd = properties.getEmdeonProperties();
		setWebClientOptions();
	}
	private void setWebClientOptions() {
		cookieManager = webClient.getCookieManager();
		cookieManager.setCookiesEnabled(true);
		webClient.setCookieManager(cookieManager);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setUseInsecureSSL(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.setAjaxController(new AjaxController(){
			@Override
		    public boolean processSynchron(HtmlPage page, WebRequest request, boolean async) {
		        return true;
		    }
		});
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
	    java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
	}
	public boolean login(){
		System.out.println("Logging in");
		HtmlPage page1;
		try {
			page1 = webClient.getPage(FieldNames.EMDEON_URL);
			HtmlForm form = (HtmlForm) page1.getByXPath("//form[@id='frmMaster']").get(0);
		
			HtmlTextInput user = form.getInputByName(ElementIds.USER_NAME);
			form.removeAttribute("onsubmit");
			HtmlPasswordInput password = form.getInputByName(ElementIds.PASSWORD);
			HtmlSubmitInput button = form.getInputByName(ElementIds.LOGIN);
			user.setValueAttribute(emd.getUsername());
			password.setValueAttribute(emd.getPassword());
	
			cardFinderPage = button.click();	
			
			cardFinderPage = webClient.getPage(FieldNames.CARDFINDER_URL);
			HtmlForm form2 = cardFinderPage.getFormByName("aspnetForm");
			HtmlTextInput coverageType = form2.getInputByName("ctl00$cphMain$pnlCallBack$ASPxRoundPanel1$ddServiceType");
			coverageType.setValueAttribute("Commercial Only");
			coverageType.removeAttribute("readonly");
			HtmlButtonInput b = (HtmlButtonInput) form2.getByXPath("(//input[@type='button'])").get(1);
			cardFinderPage = b.click();
			System.out.println("Login Successful");
			return true;
		} catch (FailingHttpStatusCodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ElementNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	public void logout() {
		System.out.println("Logging Out");
		HtmlAnchor anchor = cardFinderPage.getAnchorByText("Logout");
		try {
			cardFinderPage = anchor.click();
			webClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		webClient.waitForBackgroundJavaScript(2000);
		System.out.println(cardFinderPage.asText());
	}
	public void close() {
		webClient.close();
	}

	public InsuranceInfo fillOutForm(Record record,int pause) {
		record.printRecord();
		record.setBeenEmdeoned(true);
		InsuranceInfo info = new InsuranceInfo();
		if(!checkRecord(record)) {
			info.setStatus("Record Incomplete");
			return info;
		}
		else if(record.getFirstName().length()>=15 || record.getLastName().length()>=15) {
			info.setStatus("Name too long");
			return info;
		}
		WebRequest request = cardFinderPage.getWebResponse().getWebRequest();
		List<NameValuePair> oldParameters = request.getRequestParameters();
		List<NameValuePair> newParameters = new ArrayList<NameValuePair>() {
			@Override
			public boolean contains(Object o) {
				String name = (String) o;
				//System.out.println(name);
				for(NameValuePair nvp: this) {
					if(nvp.getName().equalsIgnoreCase(name))
						return true;
					else 
						continue;
				}
				return false;
			}
		};
		for(NameValuePair name: oldParameters) {
			switch(name.getName()) {
			case EmdeonParameters.SERVICE_TYPE:
				if(record.getSsn().length()==4)
					newParameters.add(new NameValuePair(EmdeonParameters.SERVICE_TYPE,EmdeonParameters.MEDICARE_PART_D));
				else
					newParameters.add(new NameValuePair(EmdeonParameters.SERVICE_TYPE,EmdeonParameters.COMMERCIAL));
				break;
			case EmdeonParameters.SERVICE_TYPE_VI:
				if(record.getSsn().length()==4)
					newParameters.add(new NameValuePair(EmdeonParameters.SERVICE_TYPE_VI,EmdeonParameters.COMMERCIAL_PART_D));
				else
					newParameters.add(new NameValuePair(EmdeonParameters.SERVICE_TYPE_VI,EmdeonParameters.COMMERCIAL_VALUE));
				break;
			case EmdeonParameters.SERVICE_TYPE_DL:
				if(record.getSsn().length()==4)
					newParameters.add(new NameValuePair(EmdeonParameters.SERVICE_TYPE_DL,EmdeonParameters.COMMERCIAL_PART_D));
				else
					newParameters.add(new NameValuePair(EmdeonParameters.SERVICE_TYPE_DL,EmdeonParameters.COMMERCIAL_VALUE));
				break;
			case EmdeonParameters.SSN:
				if(record.getSsn().length()==4)
					newParameters.add(new NameValuePair(EmdeonParameters.SSN,record.getSsn()));
				break;
			case EmdeonParameters.NPI:
				newParameters.add(new NameValuePair(EmdeonParameters.NPI,emd.getNpi()));
				break;
			case EmdeonParameters.FIRST_NAME:
				newParameters.add(new NameValuePair(EmdeonParameters.FIRST_NAME,record.getFirstName().trim()));
				break;
			case EmdeonParameters.LAST_NAME:
				newParameters.add(new NameValuePair(EmdeonParameters.LAST_NAME,record.getLastName().trim()));
				break;
			case EmdeonParameters.DOB:
				newParameters.add(new NameValuePair(EmdeonParameters.DOB,record.getDob()));
				break;
			case EmdeonParameters.DOB_STATE:
				String dob_state = "{&quot;rawValue&quot;:&quot;"+record.getDob()+"&quot;,&quot;validationState&quot;:&quot;&quot;}";
				newParameters.add(new NameValuePair(EmdeonParameters.DOB_STATE,dob_state));
				break;
			case EmdeonParameters.ZIP:
				newParameters.add(new NameValuePair(EmdeonParameters.ZIP,record.getZip()));
				break;
			case EmdeonParameters.GENDER:
				newParameters.add(new NameValuePair(EmdeonParameters.GENDER,Integer.toString(EmdeonParameters.getGenderValue(record))));
				break;
			default:
				newParameters.add(name);
			}
		}
		try {
			if(!newParameters.contains(EmdeonParameters.SSN) && record.getSsn().length()==4) {
				System.out.println("ADDED SSN");
				newParameters.add(new NameValuePair(EmdeonParameters.SSN,record.getSsn()));
			}
			request.setRequestParameters(newParameters);
			request.setUrl(new URL(FieldNames.CARDFINDER_URL));
			cardFinderPage = webClient.getPage(request);
			HtmlButtonInput button2 = (HtmlButtonInput) cardFinderPage.getElementByName(ElementIds.SUBMIT);
			synchronized(cardFinderPage = button2.click()) {
				wait(cardFinderPage,5000);
				HtmlTable table = (HtmlTable) cardFinderPage.getElementById(ElementIds.COMMERCIAL_TABLE);
				if(table==null) {
					info.setStatus("Error");
					return info;
				}
				info = getPrivateCellData(table,info,record);
				if(record.getSsn().length()==4) {
					table = (HtmlTable) cardFinderPage.getElementById(ElementIds.MEDICARE_PART_D_TABLE);
					if(table!=null)
						info = getMedicareCellData(table,info,record);
				}
			}
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ElementNotFoundException e) {
			System.out.println(cardFinderPage.getWebResponse().getContentAsString());
			e.printStackTrace();
			info.setStatus("ERROR");
			return info;
		}
		return info;
	}
	private boolean checkRecord(Record r) {
		if(r.getFirstName().length()<=1 || r.getLastName().length()<=1)
			return false;
		else if( r.getZip().length()<=3 || r.getZip().length()>=6 || r.getDob().equalsIgnoreCase(""))
			return false;
		else if (r.getZip().length()==4) 
			r.setZip("0"+r.getZip());
		return true;
		
	}
	private void wait(HtmlPage page,int length) {
		try {
			page.wait(length);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private InsuranceInfo getPrivateCellData(HtmlTable table,InsuranceInfo info,Record record) {
		for(HtmlTableRow row: table.getRows()) {
			System.out.println(row.asText());
			for(HtmlTableCell cell: row.getCells()) {
				String cellData = cell.asText();
				switch(cellData) {
				case FieldNames.COVERAGE_TYPE:
					info.privatePrimary = new Insurance("Primary Private");
					info.setStatus(EmdeonStatus.FOUND);
					break;
				case FieldNames.PAYER_HELP_DESK:
					break;
				case FieldNames.POLICY_ID:
					if(cell.getIndex()==1)
						info.privatePrimary.setPolicyId(cell.getNextElementSibling().asText());
					else 
						info.privatePrimary.setPolicyId(cell.getPreviousElementSibling().asText());
					break;
				case FieldNames.BIN:
					if(cell.getIndex()==1)
						info.privatePrimary.setBin(cell.getNextElementSibling().asText());
					else 
						info.privatePrimary.setBin(cell.getPreviousElementSibling().asText());
					info.privatePrimary.setCarrier(InsuranceFilter.GetPBMFromBin(record));
					break;
				case FieldNames.PCN:
					if(cell.getIndex()==1)
						info.privatePrimary.setPcn(cell.getNextElementSibling().asText());
					else 
						info.privatePrimary.setPcn(cell.getPreviousElementSibling().asText());
					break;
				case FieldNames.GROUP:
					if(cell.getIndex()==1)
						info.privatePrimary.setGrp(cell.getNextElementSibling().asText());
					else 
						info.privatePrimary.setGrp(cell.getPreviousElementSibling().asText());
					break;
				case FieldNames.ADDITIONAL_COVERAGE:
					if(cell.getIndex()==1)
						info.privatePrimary.setInfo(cell.getNextElementSibling().asText());
					else 
						info.privatePrimary.setInfo(cell.getPreviousElementSibling().asText());
					return info;
				case ErrorNames.NO_DATA:
					info.setStatus(ErrorNames.NO_DATA);
					System.out.println(cardFinderPage.asText());
					return info;
				case ErrorNames.PATIENT_NOT_COVERED: 
					info.setStatus(EmdeonStatus.NOT_COVERED);
					break;
				case ErrorNames.WRONG_FIRST_NAME:
					info.setStatus(EmdeonStatus.WRONG_FIRST_NAME);
					return info;
				case ErrorNames.LAST_NAME_TOO_LONG: 
					info.setStatus(EmdeonStatus.LAST_NAME_TOO_LONG);
					break;
				case ErrorNames.FIRST_NAME_TOO_LONG:
					info.setStatus(EmdeonStatus.FIRST_NAME_TOO_LONG);
					break;
				case ErrorNames.INVALID_DOB: 
				case ErrorNames.INVALID_DOB2:
					info.setStatus(EmdeonStatus.INVALID_DOB);
					break;
				case ErrorNames.PATIENT_PRIVATE_NOT_FOUND3:
				case ErrorNames.PATIENT_PRIVATE_NOT_FOUND2:
				case ErrorNames.PATIENT_PRIVATE_NOT_FOUND: 
					info.setStatus(EmdeonStatus.NOT_FOUND);
					break;
				case ErrorNames.PBM_NOT_PARTICIPATE: 
				case ErrorNames.PBM_NOT_PARTICIPATE2:
					info.setStatus(EmdeonStatus.PBM_NOT_PARTICIPATE);
					break;
				case ErrorNames.NOT_ACTIVE: 
					info.setStatus(EmdeonStatus.NOT_ACTIVE);
					break;
				case ErrorNames.TIMED_OUT:
					info.setStatus(EmdeonStatus.TIMED_OUT);
					break;
				case ErrorNames.CONNECTION_ISSUES:
				case ErrorNames.CONNECTION_ISSUES2:
					info.setStatus(EmdeonStatus.CONNECTION_ISSUES);
					break;
				case ErrorNames.EMDEON_DOWN:
					info.setStatus(EmdeonStatus.PAYOR_DOWN);
					break;
				case ErrorNames.REJECT_UNKNOWN:
					info.setStatus("Reject Unknown");
					break;
				}
			}
		}
		return info;
	}
	private InsuranceInfo getMedicreData(HtmlTable table,InsuranceInfo info,Record record) {
		for(HtmlTableRow row: table.getRows()) {
			System.out.println(row.asText());
			for(HtmlTableCell cell: row.getCells()) {
				String cellData = cell.asText();
				switch(cellData) {
				case FieldNames.COVERAGE_TYPE:
					info.privatePrimary = new Insurance("Primary Private");
					info.setStatus(EmdeonStatus.FOUND);
					break;
				case FieldNames.PAYER_HELP_DESK:
					break;
				case FieldNames.POLICY_ID:
					if(cell.getIndex()==1)
						info.privatePrimary.setPolicyId(cell.getNextElementSibling().asText());
					else 
						info.privatePrimary.setPolicyId(cell.getPreviousElementSibling().asText());
					break;
				case FieldNames.BIN:
					if(cell.getIndex()==1)
						info.privatePrimary.setBin(cell.getNextElementSibling().asText());
					else 
						info.privatePrimary.setBin(cell.getPreviousElementSibling().asText());
					info.privatePrimary.setCarrier(InsuranceFilter.GetPBMFromBin(record));
					break;
				case FieldNames.PCN:
					if(cell.getIndex()==1)
						info.privatePrimary.setPcn(cell.getNextElementSibling().asText());
					else 
						info.privatePrimary.setPcn(cell.getPreviousElementSibling().asText());
					break;
				case FieldNames.GROUP:
					if(cell.getIndex()==1)
						info.privatePrimary.setGrp(cell.getNextElementSibling().asText());
					else 
						info.privatePrimary.setGrp(cell.getPreviousElementSibling().asText());
					break;
				case FieldNames.ADDITIONAL_COVERAGE:
					if(cell.getIndex()==1)
						info.privatePrimary.setInfo(cell.getNextElementSibling().asText());
					else 
						info.privatePrimary.setInfo(cell.getPreviousElementSibling().asText());
					return info;
				case ErrorNames.NO_DATA:
					info.setStatus(ErrorNames.NO_DATA);
					System.out.println(cardFinderPage.asText());
					return info;
				case ErrorNames.PATIENT_NOT_COVERED: 
					info.setStatus(EmdeonStatus.NOT_COVERED);
					break;
				case ErrorNames.WRONG_FIRST_NAME:
					info.setStatus(EmdeonStatus.WRONG_FIRST_NAME);
					return info;
				case ErrorNames.LAST_NAME_TOO_LONG: 
					info.setStatus(EmdeonStatus.LAST_NAME_TOO_LONG);
					break;
				case ErrorNames.FIRST_NAME_TOO_LONG:
					info.setStatus(EmdeonStatus.FIRST_NAME_TOO_LONG);
					break;
				case ErrorNames.INVALID_DOB: 
				case ErrorNames.INVALID_DOB2:
					info.setStatus(EmdeonStatus.INVALID_DOB);
					break;
				case ErrorNames.PATIENT_PRIVATE_NOT_FOUND3:
				case ErrorNames.PATIENT_PRIVATE_NOT_FOUND2:
				case ErrorNames.PATIENT_PRIVATE_NOT_FOUND: 
					info.setStatus(EmdeonStatus.NOT_FOUND);
					break;
				case ErrorNames.PBM_NOT_PARTICIPATE: 
				case ErrorNames.PBM_NOT_PARTICIPATE2:
					info.setStatus(EmdeonStatus.PBM_NOT_PARTICIPATE);
					break;
				case ErrorNames.NOT_ACTIVE: 
					info.setStatus(EmdeonStatus.NOT_ACTIVE);
					break;
				case ErrorNames.TIMED_OUT:
					info.setStatus(EmdeonStatus.TIMED_OUT);
					break;
				case ErrorNames.CONNECTION_ISSUES:
				case ErrorNames.CONNECTION_ISSUES2:
					info.setStatus(EmdeonStatus.CONNECTION_ISSUES);
					break;
				case ErrorNames.EMDEON_DOWN:
					info.setStatus(EmdeonStatus.PAYOR_DOWN);
					break;
				case ErrorNames.REJECT_UNKNOWN:
					info.setStatus("Reject Unknown");
					break;
				}
			}
		}
		return info;
	}
	private InsuranceInfo getMedicareCellData(HtmlTable table,InsuranceInfo info,Record record) {
		for(HtmlTableRow row: table.getRows()) {
			for(HtmlTableCell cell: row.getCells()) {
				String cellData = cell.asText();
				System.out.println(cellData);
				switch(cellData) {
				case FieldNames.PLAN_TYPE:
					info.medicarePrimary = new Insurance("Primary Medicare");
					info.setStatus(EmdeonStatus.FOUND);
					break;
				case FieldNames.CONTRACT_NUMBER:
					if(cell.getIndex()==1) {
						info.medicarePrimary.setType(getPlanType(cell.getNextElementSibling().asText()));
						record.setContractId(cell.getNextElementSibling().asText());
					}
					else {
						record.setContractId(cell.getNextElementSibling().asText());
						info.medicarePrimary.setType(getPlanType(cell.getPreviousElementSibling().asText()));
					}
					System.out.println(record.getFirstName()+" "+record.getLastName());
					System.out.println("CONTRACT ID: "+record.getContractId());
					break;
				case FieldNames.BENEFIT_ID:
					if(cell.getIndex()==1)
						record.setBenefitId(cell.getNextElementSibling().asText());
					else 
						record.setBenefitId(cell.getPreviousElementSibling().asText());
					System.out.println("BENEFIT ID:"+record.getBenefitId());
					break;
				case FieldNames.PAYER_HELP_DESK:					
					break;
				case FieldNames.POLICY_ID:
					if(cell.getIndex()==1)
						info.medicarePrimary.setPolicyId(cell.getNextElementSibling().asText());
					else 
						info.medicarePrimary.setPolicyId(cell.getPreviousElementSibling().asText());
					break;
				case FieldNames.BIN:
					if(cell.getIndex()==1)
						info.medicarePrimary.setBin(cell.getNextElementSibling().asText());
					else 
						info.medicarePrimary.setBin(cell.getPreviousElementSibling().asText());
					break;
				case FieldNames.GROUP:
					if(cell.getIndex()==1)
						info.medicarePrimary.setGrp(cell.getNextElementSibling().asText());
					else 
						info.medicarePrimary.setGrp(cell.getPreviousElementSibling().asText());
					break;
				case FieldNames.PCN:
					if(cell.getIndex()==1)
						info.medicarePrimary.setPcn(cell.getNextElementSibling().asText());
					else 
						info.medicarePrimary.setPcn(cell.getPreviousElementSibling().asText());
					break;
				case FieldNames.ADDITIONAL_COVERAGE:
					if(cell.getIndex()==1)
						info.medicarePrimary.setInfo(cell.getNextElementSibling().asText());
					else 
						info.medicarePrimary.setInfo(cell.getPreviousElementSibling().asText());
					return info;
				case ErrorNames.NO_DATA:
					info.setStatus(ErrorNames.NO_DATA);
					return info;
				case ErrorNames.PATIENT_NOT_COVERED: 
					info.setStatus(EmdeonStatus.NOT_COVERED);
					break;
				case ErrorNames.LAST_NAME_TOO_LONG: 
					info.setStatus(EmdeonStatus.LAST_NAME_TOO_LONG);
					break;
				case ErrorNames.FIRST_NAME_TOO_LONG:
					info.setStatus(EmdeonStatus.FIRST_NAME_TOO_LONG);
					break;
				case ErrorNames.INVALID_DOB: 
					info.setStatus(EmdeonStatus.INVALID_DOB);
					break;
				case ErrorNames.PATIENT_MEDICARE_NOT_FOUND:
					//Do nothing.. already set from Private look up
					break;
				case ErrorNames.PBM_NOT_PARTICIPATE: 
				case ErrorNames.PBM_NOT_PARTICIPATE2:
					if(!info.getStatus().equalsIgnoreCase(EmdeonStatus.FOUND))
						info.setStatus(EmdeonStatus.PBM_NOT_PARTICIPATE);
					return info;
				case ErrorNames.NOT_ACTIVE: 
					if(!info.getStatus().equalsIgnoreCase(EmdeonStatus.FOUND))
						info.setStatus(EmdeonStatus.NOT_ACTIVE);
					return info;
				case ErrorNames.TIMED_OUT:
					if(!info.getStatus().equalsIgnoreCase(EmdeonStatus.FOUND))
						info.setStatus(EmdeonStatus.TIMED_OUT);
					return info;
				case ErrorNames.MED_D_NOT_VALID:
					if(!info.getStatus().equalsIgnoreCase(EmdeonStatus.FOUND))
						info.setStatus(EmdeonStatus.PART_D_NOT_ACTIVE);
					return info;
				case ErrorNames.CONNECTION_ISSUES:
				case ErrorNames.CONNECTION_ISSUES2:
					if(!info.getStatus().equalsIgnoreCase(EmdeonStatus.FOUND))
						info.setStatus(EmdeonStatus.CONNECTION_ISSUES);
					return info;
				}
			}
		}
		return info;
	}
	private String getPlanType(String type) {
		if(type==null)
			return "UNDEFINED";
		if(type.length()==5) {
			if(type.startsWith("R"))
				return InsuranceType.MAPD_PPO;
			if(type.startsWith("H"))
				return InsuranceType.MAPD_HMO;
			if(type.startsWith("S"))
				return InsuranceType.PDP;
		} 
		return type;	
	}
	private void setMedicareNumber(HtmlTable table,InsuranceInfo info) {
		for(HtmlTableRow row: table.getRows()) {
			for(HtmlTableCell cell: row.getCells()) {
				String cellData = cell.asText();
				switch(cellData) {
					case FieldNames.MEDICARE_NUMBER:
					if(cell.getIndex()==1)	
						info.medicarePrimary.setInfo(cell.getNextElementSibling().asText());
					else
						info.medicarePrimary.setInfo(cell.getPreviousElementSibling().asText());
					break;
				}
			}
		}
	}
	private static class EmdeonParameters {
		//
		public static final String SERVICE_TYPE = "ctl00$cphMain$pnlCallBack$ASPxRoundPanel1$ddServiceType";
		public static final String NPI = "ctl00$cphMain$pnlCallBack$ASPxRoundPanel1$txtProviderId";
		public static final String FIRST_NAME = "ctl00$cphMain$pnlCallBack$ASPxRoundPanel1$txtNameFirst";
		public static final String LAST_NAME = "ctl00$cphMain$pnlCallBack$ASPxRoundPanel1$txtNameLast";
		public static final String DOB = "ctl00$cphMain$pnlCallBack$ASPxRoundPanel1$txtBirthDate";
		public static final String DOB_STATE = "ctl00$cphMain$pnlCallBack$ASPxRoundPanel1$txtBirthDate$State";
		public static final String SSN = "ctl00$cphMain$pnlCallBack$ASPxRoundPanel1$txtPatientID";
		public static final String ZIP = "ctl00$cphMain$pnlCallBack$ASPxRoundPanel1$txtZipCode";
		public static final String GENDER = "ctl00$cphMain$pnlCallBack$ASPxRoundPanel1$rbGender";
		public static final String SERVICE_TYPE_VI = "ctl00_cphMain_pnlCallBack_ASPxRoundPanel1_ddServiceType_VI";
		public static final String SERVICE_TYPE_DL = "ctl00$cphMain$pnlCallBack$ASPxRoundPanel1$ddServiceType$DDD$L";
		//Gender Values
		public static final int GENDER_MALE = 0;
		public static final int GENDER_FEMALE =1;
		//Service Types
		public static final String COMMERCIAL = "Commercial Only";
		public static final String MEDICARE_PART_D = "Medicare Part D Only";

		//Service Type Values
		public static final String COMMERCIAL_VALUE = "ELIG";
		public static final String COMMERCIAL_PART_D = "TROOPELIG";
		
		public static int getGenderValue(Record record) {
			if(record.getGender()==null)
				return GENDER_MALE;
			if(record.getGender().startsWith("M") || record.getGender().startsWith("m"))
				return GENDER_MALE;
			else if(record.getGender().startsWith("F") || record.getGender().startsWith("f"))
				return GENDER_FEMALE;
			else 
				return GENDER_MALE;
		}
	}
	private static class ElementIds {
		//TextFields
		public static final String USER_NAME = "ctl00$ctl00$cphMain$cphPublicArea$LoginView2$Login1$Login1$UserName";
		public static final String PASSWORD = "ctl00$ctl00$cphMain$cphPublicArea$LoginView2$Login1$Login1$Password";
		public static final String LOGIN = "ctl00$ctl00$cphMain$cphPublicArea$LoginView2$Login1$Login1$LoginButton";
		//Tables
		public static final String COMMERCIAL_TABLE = "ctl00_cphMain_pnlCallBack_ASPxRoundPanel1_pgControl_gvElig_DXMainTable";
		public static final String MEDICARE_TABLE = "ctl00_cphMain_pnlCallBack_ASPxRoundPanel1_pgControl_gvMedicare_DXMainTable";
		public static final String MEDICARE_PART_D_TABLE = "ctl00_cphMain_pnlCallBack_ASPxRoundPanel1_pgControl_gvTroop_DXMainTable";
		
		public static final String SUBMIT = "ctl00$cphMain$pnlCallBack$ASPxRoundPanel1$btnSubmit";
		
	}
	private static class FieldNames {
		//
		public static final String COVERAGE_TYPE = "Coverage Type";
		public static final String PAYER_HELP_DESK = "Payer Help Desk Number";
		public static final String POLICY_ID = "Cardholder ID";
		public static final String BIN = "BIN";
		public static final String GROUP = "Group";
		public static final String PCN = "PCN";
		public static final String ADDITIONAL_COVERAGE = "Additional Coverage Information";
		public static final String PLAN_TYPE = "Plan Type";
		public static final String CONTRACT_NUMBER = "Contract Number";
		public static final String MEDICARE_NUMBER = "Medicare Beneficiary ID";
		public static final String BENEFIT_ID = "Benefit ID";
		//URLS1
		public static final String EMDEON_URL = "https://secure.erxnetwork.com/logon.aspx";
		public static final String CARDFINDER_URL = "https://secure.erxnetwork.com/NS/Cardfinder/CardFinder.aspx";

	}
	public static class ErrorNames {
		public static final String NO_DATA = "No data to display";
		public static final String CONNECTION_ISSUES = "ERX104 Connectivity Issue, Please Resubmit. If persists, call Emdeon Customer Support at (866) 379-6389";
		public static final String CONNECTION_ISSUES2 = "ERX106Connectivity Issue, Please Resubmit. If persists, call Emdeon Customer Support at (866) 379-6389";
		public static final String TIMED_OUT = "ERX119:CardFinder PBM Timeout: Please Resubmit";
		public static final String NOT_ACTIVE = "ERX180Patient Found Coverage Not Active On Submitted Date of Service";
		public static final String PBM_NOT_PARTICIPATE = "ERX120 PATIENT FOUND - PBM NOT PARTICIPATING WITH CARDFINDER SERVICE";
		public static final String PBM_NOT_PARTICIPATE2 = "ERX151 PBM Does Not Participate with CardFinder";
		public static final String PATIENT_PRIVATE_NOT_FOUND = "ERX108Patient Not Found";
		public static final String PATIENT_PRIVATE_NOT_FOUND2 = "ERX108 Patient Not Found";
		public static final String PATIENT_PRIVATE_NOT_FOUND3 = "ERX108 Patient not found";
		public static final String PATIENT_MEDICARE_NOT_FOUND = "MCARE ELIG;NO PATIENT MATCH FOUND";
		public static final String INVALID_DOB = "ERX102 Missing/Invalid Patient Date of Birth";
		public static final String INVALID_DOB2 = "ERX102:M/I Patient Date of Birth";
		public static final String WRONG_FIRST_NAME = "ERX100 Missing/Invalid Patient First Name";
		public static final String FIRST_NAME_TOO_LONG = "ERX124:MAX FIELD LENGTH EXCEEDED - (CA) Patient First Name \"MAX\" 12";
		public static final String LAST_NAME_TOO_LONG = "ERX125:MAX FIELD LENGTH EXCEEDED - (CB) Patient Last Name \"MAX\" 15";
		public static final String PATIENT_NOT_COVERED = "ERX108 Patient Is Not Covered";
		public static final String EMDEON_DOWN = "ERX03 CONNECTION TO PAYOR IS DOWN (M3BC43200)";
		public static final String MED_D_NOT_VALID = "MCARE ELIG;PATIENT FOUND BUT PART D COVERAGE OUTSIDE SUBMITTED DATE OF SERVICE";
		public static final String REJECT_UNKNOWN = "ERX110 AAA Reject Unknown";
	}
}