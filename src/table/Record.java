package table;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import Clients.DatabaseClient;
import Clients.NDCVerifyClient;
import Clients.RingCentralClient;
import Fax.FaxStatus;
import Fax.MessageStatus;
import PBM.InsuranceFilter;
import PBM.InsuranceType;
import source.CSVFrame;

public class Record implements Cloneable{
	boolean hasBeenEmdeoned;
	public int row;
	String id,messageId,age;
	String firstName,lastName,dob,ssn,gender;
	String address,city,state,zip,phone,email;
	String status,type,carrier,policyId,bin,grp,pcn;
	String drFirst,drLast,drAddress1,drAddress2;
	String drCity,drState,drZip,drFax,drPhone,npi;
	String painLocation,painCause,agent,callCenter,pharmacy;
	String notes,messageStatus,faxStatus;
	String database,table;
	String contractId,benefitId;
	String brace_list;
	String coveredMeds;
	String[] products;
	Color color = Color.WHITE;
	public Record() {
	
	}
	public Record(String first,String last,String phonenumber) {
		setPhone(phonenumber);
		setFirstName(first);
		setLastName(last);
	}
	public Record(String[] data) {
		setFirstName(toProperCaseNoStripDown(data[MyTableModel.FIRST_NAME])); 
		setLastName(toProperCaseNoStripDown(data[MyTableModel.LAST_NAME]));
		setAddress(toProperCase(data[MyTableModel.ADDRESS])); 
		setDob(data[MyTableModel.DOB]);
		setCity(toProperCase(data[MyTableModel.CITY]));	
		setState(data[MyTableModel.STATE].toUpperCase());
		setZip(data[MyTableModel.ZIP]);	
		setPhone(data[MyTableModel.PHONE]);
		setCarrier(data[MyTableModel.CARRIER]); 
		setPolicyId(data[MyTableModel.POLICY_ID]);
		setBin(data[MyTableModel.BIN]);	
		setGrp(data[MyTableModel.GROUP]);
		setPcn(data[MyTableModel.PCN]);	
		setNpi(data[MyTableModel.NPI]);
		setDrFirst(toProperCase(data[MyTableModel.DR_FIRST])); 
		setDrLast(toProperCase(data[MyTableModel.DR_LAST]));
		setDrAddress1(toProperCase(data[MyTableModel.DR_ADDRESS1]));	
		setDrCity(toProperCase(data[MyTableModel.DR_CITY]));	
		setDrState(data[MyTableModel.DR_STATE].toUpperCase());
		setDrZip(data[MyTableModel.DR_ZIP]);	
		setDrPhone(data[MyTableModel.DR_PHONE]);
		setDrFax(data[MyTableModel.DR_FAX]);	
		setGender(toProperCase(data[MyTableModel.GENDER])); 
		setEmail(data[MyTableModel.EMAIL]);
		setSsn(data[MyTableModel.SSN]);	setStatus(data[MyTableModel.STATUS]);  
		setType(data[MyTableModel.TYPE]);
		setId(getFirstName()+getLastName()+getPhone());
		setPharmacy(data[MyTableModel.PHARMACY]);
	}
	public Record(ResultSet result,String database,String table) {
		try {
			ResultSetMetaData meta = result.getMetaData();
			setFirstName(toProperCaseNoStripDown(result.getString(DatabaseClient.Columns.FIRST_NAME)));
			setLastName(toProperCaseNoStripDown(result.getString(DatabaseClient.Columns.LAST_NAME)));
			setPhone(result.getString(DatabaseClient.Columns.PHONE_NUMBER).replaceAll("[()\\s-]+", ""));
			setAddress(toProperCase(result.getString(DatabaseClient.Columns.ADDRESS)));
			setCity(toProperCase(result.getString(DatabaseClient.Columns.CITY)));
			setState(result.getString(DatabaseClient.Columns.STATE).toUpperCase());
			setZip(result.getString(DatabaseClient.Columns.ZIPCODE));
			setGender(toProperCase(result.getString(DatabaseClient.Columns.GENDER)));
			setDob(result.getString(DatabaseClient.Columns.DOB));
			setCarrier(result.getString(DatabaseClient.Columns.CARRIER));
			setBin(result.getString(DatabaseClient.Columns.BIN));
			setPcn(result.getString(DatabaseClient.Columns.PCN));
			setGrp(result.getString(DatabaseClient.Columns.GROUP));
			setPolicyId(result.getString(DatabaseClient.Columns.POLICY_ID));	
			setNpi(result.getString(DatabaseClient.Columns.NPI));
			setDrFirst(toProperCase(result.getString(DatabaseClient.Columns.DR_FIRST)));
			setDrLast(toProperCase(result.getString(DatabaseClient.Columns.DR_LAST)));
			setDrAddress1(toProperCase(result.getString(DatabaseClient.Columns.DR_ADDRESS1)));
			setDrCity(toProperCase(result.getString(DatabaseClient.Columns.DR_CITY).replaceAll("\"'","")));
			setDrState(result.getString(DatabaseClient.Columns.DR_STATE).replaceAll("\"'","").toUpperCase());
			setDrZip(result.getString(DatabaseClient.Columns.DR_ZIP).replaceAll("\"'",""));
			setDrPhone(result.getString(DatabaseClient.Columns.DR_PHONE).replaceAll("[()\\-\\s]", ""));
			setDrFax(result.getString(DatabaseClient.Columns.DR_FAX));
			setSsn(result.getString(DatabaseClient.Columns.SSN));
			setPharmacy(result.getString(DatabaseClient.Columns.PHARMACY));
			setId(result.getString(DatabaseClient.Columns.ID)); 
			setFaxStatusNoColor(result.getString(DatabaseClient.Columns.FAX_DISPOSITION));
			setMessageStatusNoColor(result.getString(DatabaseClient.Columns.MESSAGE_STATUS));
			setMessageId(result.getString(DatabaseClient.Columns.MESSAGE_ID));
			if(containsColumnName(meta,DatabaseClient.Columns.NOTES))
				setNotes(result.getString(DatabaseClient.Columns.NOTES));
			setStatus(result.getString(DatabaseClient.Columns.EMDEON_STATUS));
			setType(result.getString(DatabaseClient.Columns.TYPE));
			setAgent(result.getString(DatabaseClient.Columns.AGENT));
			setCallCenter(result.getString(DatabaseClient.Columns.CALL_CENTER));
			setProducts(result.getString(DatabaseClient.Columns.PRODUCTS));
			if(containsColumnName(meta,"COVERED_MEDS"))
				setCoveredMeds(result.getString("COVERED_MEDS"));
			this.table = table;
			this.database = database;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setDME(ResultSet result) throws SQLException {
		setFirstName(toProperCaseNoStripDown(result.getString(DatabaseClient.Columns.FIRST_NAME)));
		setLastName(toProperCaseNoStripDown(result.getString(DatabaseClient.Columns.LAST_NAME)));
		setPhone(result.getString(DatabaseClient.Columns.PHONE_NUMBER).replaceAll("[()\\s-]+", ""));
		setAddress(toProperCase(result.getString(DatabaseClient.Columns.ADDRESS)));
		setCity(toProperCase(result.getString(DatabaseClient.Columns.CITY)));
		setState(result.getString(DatabaseClient.Columns.STATE).toUpperCase());
		setZip(result.getString(DatabaseClient.Columns.ZIPCODE));
		setGender(toProperCase(result.getString(DatabaseClient.Columns.GENDER)));
		setDob(result.getString(DatabaseClient.Columns.DOB));
		setCarrier(toProperCase(result.getString(DatabaseClient.Columns.CARRIER)));
		setPolicyId(result.getString(DatabaseClient.Columns.POLICY_ID));
		setBrace_list(result.getString("BRACES"));
		setId(getFirstName()+getLastName()+getPhone()); 
		setNpi(result.getString(DatabaseClient.Columns.NPI));
		setDrFirst(toProperCase(result.getString(DatabaseClient.Columns.DR_FIRST)));
		setDrLast(toProperCase(result.getString(DatabaseClient.Columns.DR_LAST)));
		setDrAddress1(toProperCase(result.getString(DatabaseClient.Columns.DR_ADDRESS1)));
		setDrCity(toProperCase(result.getString(DatabaseClient.Columns.DR_CITY).replaceAll("\"'","")));
		setDrState(result.getString(DatabaseClient.Columns.DR_STATE).replaceAll("\"'","").toUpperCase());
		setDrZip(result.getString(DatabaseClient.Columns.DR_ZIP).replaceAll("\"'",""));
		setDrPhone(result.getString(DatabaseClient.Columns.DR_PHONE).replaceAll("[()\\-\\s]", ""));
		setDrFax(result.getString(DatabaseClient.Columns.DR_FAX));
	}
	public int getAge() {
		if(this.dob.equalsIgnoreCase("") || this.dob.equalsIgnoreCase("01/01/1900"))
			return 0;
		else  {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			LocalDate birthDate = LocalDate.parse(this.dob,formatter);
			LocalDate currentDate = LocalDate.now();
			return Period.between(birthDate, currentDate).getYears();
		}
			
	}
	public void setCoveredMeds(String meds) {
		System.out.println(meds);
		if(meds==null)
			this.coveredMeds = "";
		else
			this.coveredMeds = meds;
	}
	public String getCoveredMeds() {
		if(coveredMeds==null)
			return "";
		else
			return coveredMeds;
	}
	public void setProducts(String products) {
		if(products==null)
			this.products = new String[] {"Pain"};
		else 
			this.products = products.split(",");
	}
	public String[] getProducts() {
		if(this.products==null)
			return new String[] {"Pain"};
		else
			return products;
	}
	public String getBrace_list() {
		if(this.brace_list == null)
			return "";
		else
			return brace_list;
	}
	public void setBrace_list(String brace_list) {
		if(brace_list == null)
			this.brace_list = "";
		else 
			this.brace_list = brace_list;
	}
	public String getAgent() {
		if(this.agent==null)
			return "";
		else
			return agent;
	}
	
	public String getCallCenter() {
		if(this.callCenter==null)
			return "";
		else
			return callCenter;
	}
	public void setCallCenter(String callCenter) {
		this.callCenter = callCenter;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public void setPainCause(String painCause) {
		if(painCause==null)
			this.painCause = "";
		else
			this.painCause = painCause;
	}
	public void setPainLocation(String painLocation) {
		if(painLocation==null)
			this.painLocation = "";
		else
			this.painLocation = painLocation;
	}
	public String getPainCause() {
		if(painCause==null)
			return "";
		else
			return painCause;
	}
	public String getPainLocation() {
		if(painLocation==null)
			return "";
		else
			return painLocation;
	}
	public String getFirstName() {
		if(firstName==null)
			return "";
		return firstName;
	}
	public void setFirstName(String firstName) {
		if(firstName==null) {
			this.firstName = "";
			return;
		}
		this.firstName = firstName.trim();
	}
	public String getLastName() {
		if(lastName==null)
			return "";
		return lastName;
	}
	public void setLastName(String lastName) {
		if(lastName==null) {
			this.lastName = "";
			return;
		}
		this.lastName = lastName.trim();
	}
	public String getDob() {
		if(dob==null)
			return "01/01/1900";
		return dob;
	}
	public void setDob(String dob) {
		if(dob==null) {
			System.out.println("DOB IS NULL");
			this.dob = "01/01/1900";
		}
		else {
			SimpleDateFormat correctFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date date = null;
			try {
				if(dob.contains("-")) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					date = formatter.parse(dob);
					this.dob = correctFormat.format(date);
				}
				else if(dob.contains("/")) {
					date  = correctFormat.parse(dob);
					this.dob = correctFormat.format(date);
				}	
				else
					System.out.println(dob);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				this.dob = "01/01/1900";
			}
		}
	}
	public String getSsn() {
		if(ssn==null)
			return "";
		return this.ssn;
	}
	public void setSsn(String ssn) {
		if(policyId!=null) {
			if((policyId.endsWith("a") || policyId.endsWith("A")) && policyId.length()==10) {
				this.ssn = policyId.substring(policyId.length()-5, policyId.length()-1);
				return;
			}
		}
		if(ssn!=null)
			if(ssn.equalsIgnoreCase("null") || ssn.equalsIgnoreCase(" "))
				this.ssn = "";
			else if(ssn.length()==3)
				this.ssn = "0"+ssn;
			else if(ssn.length()==2)
				this.ssn = "00"+ssn;
			else if(ssn.length()==1)
				this.ssn = "000"+ssn;
			else
				this.ssn = ssn;
		else 
			this.ssn = "";
	}
	public String getAddress() {
		if(address==null)
			return "";
		return address;
	}
	public void setAddress(String address) {
		if(address==null) {
			this.address = "";
			return;
		}
		this.address = address.trim();
	}
	public String getCity() {
		if(city==null)
			return "";
		return city;
	}
	public void setCity(String city) {
		if(city==null) {
			this.city = "";
			return;
		}
		this.city = city.trim();
	}
	public String getState() {
		if(state==null)
			return "";
		return state;
	}
	public void setState(String state) {
		if(state==null) {
			this.state = "";
			return;
		}
		if(state.trim().length()>2) 
			this.state = getStateInitial(toProperCase(state));
		else
			this.state = state.trim();
	}
	public String getZip() {
		if(zip==null)
			return "";
		return zip;
	}
	public void setZip(String zip) {
		if(zip==null) {
			this.zip = "";
			return;
		}
		if(zip.contains("-")) {
			this.zip = zip.split("-")[0];
			return;
		}
		switch(zip.length()) {
			case 4: this.zip = "0"+zip; return;
			case 3: this.zip = "00"+zip; return;
			default: this.zip = zip;
		}
	}
	public String getPhone() {
		if(phone==null)
			return "";
		return phone;
	}
	public void setPhone(String phone) {
		if(phone==null) {
			this.phone = "";
			return;
		}
		this.phone = phone.replaceAll("[()\\s-]+", "");
	}
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		if(status==null)
			return "";
		return status;
	}
	public void setStatus(String status) {
		if(status==null) {
			this.status = "";
			return;
		}
		this.status = status.trim();
	}
	public String getType() {
		if(type==null)
			return "";
		return type;
	}
	public void setType(String type) {
		if(type==null) {
			this.type = "";
			return;
		}
		this.type = type.trim();
	}
	public String getCarrier() {
		if(carrier==null)
			return "";
		return carrier;
	}
	public void setCarrier(String carrier) {
		if(carrier==null) {
			this.carrier = "";
			return;
		}
		this.carrier = carrier.trim();
	}
	public String getPolicyId() {
		if(policyId==null)
			return "";
		return policyId;
	}
	public void setPolicyId(String policyId) {
		if(policyId==null) {
			this.policyId = "";
			return;
		}
		this.policyId = policyId.replace("\\u0009", "").toUpperCase();
		
	}
	public String getBin() {
		if(bin==null)
			return "";
		return bin;
	}
	public void setBin(String bin) {
		if(bin==null) {
			bin = "";
			return;
		}
		switch(bin.length()) {
			case 1: this.bin = "00000"+bin; break;
			case 2: this.bin = "0000"+bin; break;
			case 3: this.bin = "000"+bin; break;
			case 4: this.bin = "00"+bin; break;
			case 5: this.bin = "0"+bin; break;
			default: this.bin = bin; break;
		}
	}
	public String getGrp() {
		if(grp==null)
			return "";
		return grp;
	}
	public void setGrp(String grp) {
		if(grp==null) {
			this.grp = "";
			return;
		}
		if(grp.equalsIgnoreCase("No Data Returned"))
			this.grp = "No Data Returned";
		else
			this.grp = grp.toUpperCase();
	}
	public String getPcn() {
		if(pcn==null)
			return "";
		return pcn;
	}
	public void setPcn(String pcn) {
		if(pcn==null) {
			this.pcn = "";
			return;
		}
		if(pcn.equalsIgnoreCase("No Data Returned"))
			this.pcn = "No Data Returned";
		else
			this.pcn = pcn.toUpperCase();
	}
	public String getDrFirst() {
		if(drFirst==null)
			return "";
		return drFirst;
	}
	public void setDrFirst(String drFirst) {
		if(drFirst==null) {
			this.drFirst = "";
			return;
		}
		this.drFirst = drFirst.trim();
	}
	public String getDrLast() {
		if(drLast==null)
			return "";
		return drLast;
	}
	public void setDrLast(String drLast) {
		if(drLast==null) {
			this.drLast = "";
			return;
		}
		this.drLast = drLast.trim();
	}
	public String getDrAddress1() {
		if(drAddress1==null)
			return "";
		return drAddress1;
	}
	public void setDrAddress1(String drAddress1) {
		if(drAddress1==null) {
			this.drAddress1 = "";
			return;
		}
		this.drAddress1 = drAddress1.replaceAll("\"", "").trim();
	}
	public String getDrAddress2() {
		if(drAddress2==null)
			return "";
		return drAddress2;
	}
	public void setDrAddress2(String drAddress2) {
		if(drAddress2==null) {
			this.drAddress2 = "";
			return;
		}
		this.drAddress2 = drAddress2.replaceAll("\"", "").trim();
	}
	public String getDrCity() {
		if(drCity==null)
			return "";
		return drCity;
	}
	public void setDrCity(String drCity) {
		if(drCity==null) {
			this.drCity = "";
			return;
		}
		this.drCity = drCity.trim();
	}
	public String getDrState() {
		if(drState==null)
			return "";
		return drState;
	}
	public void setDrState(String drState) {
		if(drState==null) {
			this.drState = "";
			return;
		}
		this.drState = drState.trim();
	}
	public String getDrZip() {
		if(drZip==null)
			return "";
		return drZip;
	}
	public void setDrZip(String drZip) {
		if(drZip==null) {
			this.drZip = "";
			return;
		}
		if(drZip.contains("-")) {
			this.drZip = drZip.split("-")[0];
			return;
		}
		switch(drZip.length()) {
			case 4: this.drZip = "0"+drZip; return;
			case 3: this.drZip = "00"+drZip; return;
			default: this.drZip = drZip;
		}
	}
	public String getDrFax() {
		if(drFax==null)
			return "";
		return drFax;
	}
	public void setDrFax(String drFax) {
		if(drFax==null) {
			this.drFax = "";
			return;
		}
		this.drFax = drFax.replaceAll("[()\\s-]+.", "").trim();
	}
	public String getDrPhone() {
		if(drPhone==null)
			return "";
		return drPhone;
	}
	public void setDrPhone(String drPhone) {
		if(drPhone==null) {
			this.drPhone = "";
			return;
		}
		this.drPhone = drPhone.replaceAll("[()\\s-]+.", "").trim();
	}
	public String getNpi() {
		if(npi==null)
			return "";
		return npi;
	}
	public void setNpi(String npi) {
		if(npi.startsWith("1") && npi.length()==10)
			this.npi = npi.trim();
		else
			this.npi = "";
	}
	public String getGender() {
		if(gender==null)
			return "";
		return gender;
	}
	public void setGender(String gender) {
		if(gender.equalsIgnoreCase("Male")|| gender.equalsIgnoreCase("M"))
			this.gender = "Male";
		else if(gender.equalsIgnoreCase("Female") || gender.equalsIgnoreCase("F"))
			this.gender = "Female";
		else
			this.gender = "";
	}
	public String getNotes() {
		if(notes==null)
			return "";
		else 
			return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public boolean isTheSame(Record record) {
		return  this.getId().equalsIgnoreCase(record.getId());
	}
	public void printRecord() {
		System.out.println(this.getFirstName()+" "+this.getLastName());
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email.trim();
	}
	public String getPharmacy() {
		return pharmacy;
	}
	public void setPharmacy(String pharmacy) {
		this.pharmacy = pharmacy;
	}
	public Object clone()throws CloneNotSupportedException{  
		return (Record)super.clone();  
	}
	public void setRowColor(Color c) {
		this.color = c;
		try {
		CSVFrame.model.fireTableRowsUpdated(row, row);
		} catch(IndexOutOfBoundsException ex) {
			return;
		}
	}
	public void setRowColor(String color) {
		switch(color) {
			case "Blue": setRowColor(Color.BLUE);
				break;
			case "Red": setRowColor(Color.RED);
				break;	
			case "Green": setRowColor(Color.GREEN);
				break;
			case "Yellow": setRowColor(Color.YELLOW);
				break;
			case "Orange": setRowColor(Color.ORANGE);
				break;
			case "Black": setRowColor(Color.BLACK);
				break;
			default: setRowColor(Color.WHITE);
				break;
		}
	}
	public Color getRowColor() {
		if(this.color == null) 
			return Color.WHITE;
		else
			return this.color;
		
	}
	public boolean isSameColor(Color color) {
		return getRowColor().getRGB()==color.getRGB();
	}
	public String getFaxStatus() {
		if(this.faxStatus==null)
			return "";
		else
			return this.faxStatus;
	}
	public String getMessageStatus() {
		if(messageStatus==null)
			return "";
		else 
			return messageStatus;
	}
	public void setFaxStatus(String status) {
		if(status==null) {
			this.faxStatus = "";
			return;
		}
		this.faxStatus = status;
		switch(faxStatus) {
			case FaxStatus.APPROVED: setRowColor(Color.GREEN);
				break;
			case FaxStatus.DENIED: setRowColor(Color.RED);
				break;
			case FaxStatus.WRONG_DOCTOR: setRowColor(Color.RED);
				break;
			case FaxStatus.WRONG_FAX: setRowColor(Color.RED);
				break;
			case FaxStatus.NEEDS_TO_BE_SEEN: setRowColor(Color.GRAY);
				break;
			case FaxStatus.DECEASED: setRowColor(Color.BLACK);
				break;
		}
	}
	public void setFaxStatusNoColor(String status) {
		if(status==null)
			this.faxStatus = "";
		else
			this.faxStatus = status;
	}
	public void setMessageStatus(String status) {
		this.messageStatus = status;
		switch(status) {
			case MessageStatus.QUEUED: setRowColor(Color.YELLOW); break;
			case MessageStatus.SENT: setRowColor(Color.GREEN); break;
			case MessageStatus.SENDING_FAILED: setRowColor(Color.RED); break;
			case MessageStatus.ILLEGAL_ARGUMENT: setRowColor(Color.GRAY); break;
			case MessageStatus.RECEIVED: setRowColor(Color.BLUE); break;
			case MessageStatus.DNF: setRowColor(Color.BLACK); break;
			case MessageStatus.BAD_FAX_NUMBER: setRowColor(Color.MAGENTA); break;
			case MessageStatus.ON_HOLD: setRowColor(Color.BLACK); break;
			case "": setRowColor(Color.WHITE); break;
			//RingCentral Errors
			case RingCentralClient.Errors.INVALID_URL:
			case RingCentralClient.Errors.SERVICE_UNAVAILABLE:
			case RingCentralClient.Errors.TOO_MANY_REQUEST:
			case RingCentralClient.Errors.UNKNOWN_ERROR:				
				setRowColor(Color.GRAY); break;
			default:  setRowColor(Color.RED); break;
		}
	}
	public void setMessageStatusNoColor(String status) {
		if(status==null)
			this.messageStatus = "";
		else
			this.messageStatus = status;
	}
	public int compareStatus(Record r) {
		if(this.getRowColor().getRGB()>r.getRowColor().getRGB())
			return 1;
		else if(this.getRowColor().getRGB()<r.getRowColor().getRGB())
			return -1;
		else 
			return 0;
	}
	public void checkInsurance(String s) {
		switch(s) {
			case InsuranceType.PRIVATE_VERIFIED:
				setRowColor(Color.GREEN);
				break;
			case InsuranceType.PRIVATE_NO_TELMED:
				setRowColor(Color.BLUE);
				break;
			case InsuranceType.PRIVATE_UNKNOWN:
				setRowColor(Color.YELLOW);
				break;
			case InsuranceType.NOT_COVERED:
				setRowColor(Color.PINK);
				break;
			case InsuranceType.MAPD:
			case InsuranceType.PDP:
			case InsuranceType.MEDICAID_MEDICARE:
			case InsuranceType.MEDICARE_TELMED:
				setRowColor(Color.RED);
				break;
			case InsuranceType.MEDICARE_COMMERCIAL:
				setRowColor(Color.RED);
				break;
			case InsuranceType.TRICARE:
				setRowColor(Color.MAGENTA);
				break;
			case InsuranceType.MEDICAID:
			case InsuranceType.MOLINA:
				setRowColor(Color.BLACK);
				break;
			case InsuranceType.UNKNOWN_PBM:
				setRowColor(Color.GRAY);
				break;
		}
		this.type = s;
	}
	public void setBeenEmdeoned(boolean emdeoned) {
		this.hasBeenEmdeoned = emdeoned;
	}
	public boolean hasBeenEmdeoned() {
		return this.hasBeenEmdeoned;
	}
	
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	private boolean containsColumnName(ResultSetMetaData meta,String column) throws SQLException {
		int numberOfColumns = meta.getColumnCount();
		// get the column names; column indexes start from 1
		for (int i = 1; i < numberOfColumns + 1; i++) {
		    String columnName = meta.getColumnName(i);
		    // Get the name of the column's table name
		    if (column.equals(columnName)) {
		        return true;
		    }
		}
		return false;
	}
	
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getBenefitId() {
		return benefitId;
	}
	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}
	public String getStateInitial(String state) {
		Map<String, String> states = new HashMap<String, String>();
		states.put("Alabama","AL");
		states.put("Alaska","AK");
		states.put("Alberta","AB");
		states.put("American Samoa","AS");
		states.put("Arizona","AZ");
		states.put("Arkansas","AR");
		states.put("Armed Forces (AE)","AE");
		states.put("Armed Forces Americas","AA");
		states.put("Armed Forces Pacific","AP");
		states.put("British Columbia","BC");
		states.put("California","CA");
		states.put("Colorado","CO");
		states.put("Connecticut","CT");
		states.put("Delaware","DE");
		states.put("District Of Columbia","DC");
		states.put("Florida","FL");
		states.put("Georgia","GA");
		states.put("Guam","GU");
		states.put("Hawaii","HI");
		states.put("Idaho","ID");
		states.put("Illinois","IL");
		states.put("Indiana","IN");
		states.put("Iowa","IA");
		states.put("Kansas","KS");
		states.put("Kentucky","KY");
		states.put("Louisiana","LA");
		states.put("Maine","ME");
		states.put("Manitoba","MB");
		states.put("Maryland","MD");
		states.put("Massachusetts","MA");
		states.put("Michigan","MI");
		states.put("Minnesota","MN");
		states.put("Mississippi","MS");
		states.put("Missouri","MO");
		states.put("Montana","MT");
		states.put("Nebraska","NE");
		states.put("Nevada","NV");
		states.put("New Brunswick","NB");
		states.put("New Hampshire","NH");
		states.put("New Jersey","NJ");
		states.put("New Mexico","NM");
		states.put("New York","NY");
		states.put("Newfoundland","NF");
		states.put("North Carolina","NC");
		states.put("North Dakota","ND");
		states.put("Northwest Territories","NT");
		states.put("Nova Scotia","NS");
		states.put("Nunavut","NU");
		states.put("Ohio","OH");
		states.put("Oklahoma","OK");
		states.put("Ontario","ON");
		states.put("Oregon","OR");
		states.put("Pennsylvania","PA");
		states.put("Prince Edward Island","PE");
		states.put("Puerto Rico","PR");
		states.put("Quebec","QC");
		states.put("Rhode Island","RI");
		states.put("Saskatchewan","SK");
		states.put("South Carolina","SC");
		states.put("South Dakota","SD");
		states.put("Tennessee","TN");
		states.put("Texas","TX");
		states.put("Utah","UT");
		states.put("Vermont","VT");
		states.put("Virgin Islands","VI");
		states.put("Virginia","VA");
		states.put("Washington","WA");
		states.put("West Virginia","WV");
		states.put("Wisconsin","WI");
		states.put("Wyoming","WY");
		states.put("Yukon Territory","YT");
		String inital = states.get(state); 
		if(inital==null)
			return state;
		else 
			return inital;
	}
	public void SetInsurance(JSONObject obj) throws JSONException {
		setPolicyId(obj.getString(NDCVerifyClient.JSON.POLICY_ID));
		setBin(obj.getString(NDCVerifyClient.JSON.BIN));
		setPcn(obj.getString(NDCVerifyClient.JSON.PCN));
		setGrp(obj.getString(NDCVerifyClient.JSON.GRP));
		setCarrier(InsuranceFilter.GetPBMFromBin(this));
	}

	public String toProperCase(String text) {
	    if (text == null || text.isEmpty()) {
	        return text;
	    }
	    StringBuilder converted = new StringBuilder();
	    boolean convertNext = true;
	    for (char ch : text.toCharArray()) {
	        if (Character.isSpaceChar(ch)) {
	            convertNext = true;
	        } else if (convertNext) {
	            ch = Character.toTitleCase(ch);
	            convertNext = false;
	        } else {
	            ch = Character.toLowerCase(ch);
	        }
	        converted.append(ch);
	    }
	    return StripDown(converted.toString());
	}
	public String toProperCaseNoStripDown(String text) {
	    if (text == null || text.isEmpty()) {
	        return text;
	    }
	    StringBuilder converted = new StringBuilder();
	    boolean convertNext = true;
	    for (char ch : text.toCharArray()) {
	        if (Character.isSpaceChar(ch)) {
	            convertNext = true;
	        } else if (convertNext) {
	            ch = Character.toTitleCase(ch);
	            convertNext = false;
	        } else {
	            ch = Character.toLowerCase(ch);
	        }
	        converted.append(ch);
	    }
	    return converted.toString();
	}
	public String StripDown(String s) {
		return s.trim().replaceAll("[^A-Za-z0-9\\s]", "");
	}
	
}