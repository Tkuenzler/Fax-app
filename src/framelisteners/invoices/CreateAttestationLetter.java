package framelisteners.invoices;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JOptionPane;

import images.Script;
import subframes.FileChooser;

public class CreateAttestationLetter implements ActionListener {
	private final int DATE_ADDED = 0;
	private final int FILL_DATE = 1;
	private final int RX_NUMBER = 2;
	private final int LAST_NAME = 3;
	private final int FIRST_NAME = 4;
	private final int DOB = 5;
	private final int DRUG = 6;
	private final int QUANTITY_WRITTEN = 7;
	private final int QUANTITY_SUPPLIED = 8;
	private final int DAY_SUPPLY = 9;
	private final int DOCTOR_LAST = 10;
	private final int DOCTOR_FIRST = 11;
	private final int DOCTOR_ADDRESS = 12;
	private final int DOCTOR_CITY = 13;
	private final int DOCTOR_STATE = 14;
	private final int DOCTOR_ZIP = 15;
	private final int DOCTOR_PHONE = 16;
	private final int DOCTOR_FAX = 17;
	private final int NPI = 18;
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		HashMap<String, PatientRecord> patients = new HashMap<String, PatientRecord>();
		File file = FileChooser.OpenCsvFile("Patient List");
		if(file==null)
			return;
		File attestationPdf = new File(FileChooser.OpenPdfFile("Open Attestation letter"));
		File saveFolder = new File(FileChooser.OpenFolder("Where do you want to save attestation letters to?"));
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			String headers = line=br.readLine();
			while((line=br.readLine())!=null) {
				String[] data = line.split(",");
				String fill_date =  data[FILL_DATE].trim();
				String rx_number = data[RX_NUMBER].trim();
				String first_name =  data[FIRST_NAME].trim();
				String last_name =  data[LAST_NAME].trim();
				String dob =  data[DOB].trim();
				String drug =  data[DRUG].trim();
				String quantity =  data[QUANTITY_SUPPLIED].trim();
				String day_supply =  data[DAY_SUPPLY].trim();
				String dr_last =  data[DOCTOR_LAST].trim();
				String dr_first =  data[DOCTOR_FIRST].trim();
				String dr_address =  data[DOCTOR_ADDRESS].trim();
				String dr_city =  data[DOCTOR_CITY].trim();
				String dr_state =  data[DOCTOR_STATE].trim();
				String dr_zip =  data[DOCTOR_ZIP].trim();
				String dr_phone =  data[DOCTOR_PHONE].trim();
				String dr_fax =  data[DOCTOR_FAX].trim();
				String npi =  data[NPI].trim();
				PatientRecord record = null;
				if(patients.containsKey(first_name+" "+last_name))
					record = patients.get(first_name+" "+last_name);
				else {
					record = new PatientRecord(first_name,last_name);
					record.setDob(dob);
					record.setDr_first(dr_first);
					record.setDr_last(dr_last);
					record.setDr_addres(dr_address);
					record.setDr_city(dr_city);
					record.setDr_state(dr_state);
					record.setDr_zip(dr_zip);
					record.setDr_phone(dr_phone);
					record.setDr_fax(dr_fax);
					record.setNpi(npi);
					patients.put(record.getFirstName()+" "+record.getLastName(), record);
				}
				Medication medication = null;
				if(record.hasMedication(drug))
					medication = record.getMedication(drug);
				else {
					medication = new Medication(drug);
					medication.setDaySupply(day_supply);
					medication.setQuantity(quantity);
					medication.setRxNumber(rx_number);
					record.addMedication(medication);
				}
				try {
					medication.addFillDate(fill_date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
				System.out.println("CLOSED");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		Set<String> keys = patients.keySet();
		PATIENT_LIST:
		for(String key: keys) {
			PatientRecord record = patients.get(key);
			Script script = new Script(attestationPdf);
			try {
				script.populateAttestationPDF(record);
				int medCount = 1;
				Set<String> medKeys = record.getMedications().keySet();
				for(String medKey: medKeys) {
					if(medCount>6) {
						JOptionPane.showMessageDialog(null, record.getFirstName()+" "+record.getLastName()+" HAS MORE THAN 6 MEDICATIONS");
						continue PATIENT_LIST;
					}
					Medication medication = record.getMedication(medKey);
					script.populateAttestationMedication(medication,medCount);
					medCount++;
				}
				script.saveAttestationPDF(saveFolder+"//"+record.getFirstName()+" "+record.getLastName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public class PatientRecord {
		String first_name,last_name,dob,dr_first,dr_last,dr_addres,dr_city,dr_state,dr_zip,dr_phone,dr_fax,npi;
		HashMap<String, Medication> drugs = new HashMap<String, Medication>();
		public PatientRecord(String first_name,String last_name) {
			this.first_name = first_name;
			this.last_name = last_name;
		}
		public HashMap<String, Medication> getMedications() {
			return this.drugs;
		}
		public String getNpi() {
			return this.npi;
		}
		public void setNpi(String npi) {
			this.npi = npi;
		}
		public int getDrugCount() {
			return drugs.size();
		}
		public void setDob(String dob) {
			this.dob = dob;
		}
		public String getDob() {
			return this.dob;
		}
		public String getDr_first() {
			return dr_first;
		}
		public void setDr_first(String dr_first) {
			this.dr_first = dr_first;
		}
		public String getDr_last() {
			return dr_last;
		}
		public void setDr_last(String dr_last) {
			this.dr_last = dr_last;
		}
		public String getDr_addres() {
			return dr_addres;
		}
		public void setDr_addres(String dr_addres) {
			this.dr_addres = dr_addres;
		}
		public String getDr_city() {
			return dr_city;
		}
		public void setDr_city(String dr_city) {
			this.dr_city = dr_city;
		}
		public String getDr_state() {
			return dr_state;
		}
		public void setDr_state(String dr_state) {
			this.dr_state = dr_state;
		}
		public String getDr_zip() {
			return dr_zip;
		}
		public void setDr_zip(String dr_zip) {
			this.dr_zip = dr_zip;
		}
		public String getDr_phone() {
			return dr_phone;
		}
		public void setDr_phone(String dr_phone) {
			this.dr_phone = dr_phone;
		}
		public String getDr_fax() {
			return dr_fax;
		}
		public void setDr_fax(String dr_fax) {
			this.dr_fax = dr_fax;
		}
		public String getFirstName() {
			return this.first_name;
		}
		public String getLastName() {
			return this.last_name;
		}
		public Medication getMedication(String drug) {
			return drugs.get(drug);
		}
		public void addMedication(Medication medication) {
			drugs.put(medication.getName(), medication);
		}
		public boolean hasMedication(String drug) {
			if(drugs.containsKey(drug))
				return true;
			else
				return false;
		}
	}
	public class Medication {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String name,quantity,daySupply,dateWritten,rxNumber;
		ArrayList<Date> fillDates = new ArrayList<Date>();
		public Medication(String name) {
			this.name = name;
		}
		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}
		public void setDaySupply(String daySupply) {
			this.daySupply = daySupply;
		}
		public void setDateWritten(String dateWritten) {
			this.dateWritten = dateWritten;
		}
		public String getName() {
			return this.name;
		}
		public void addFillDate(String fillDate) throws ParseException {
			Date date = sdf.parse(fillDate);
			fillDates.add(date);
		}
		public void setRxNumber(String rxNumber) {
			this.rxNumber = rxNumber;
		}
		public String getEarlistFillDate() {
			Date earliestDate = null;
			for(int i = 0;i<fillDates.size();i++) {
				if(earliestDate==null)
					earliestDate = fillDates.get(i);
				else
					if(fillDates.get(i).before(earliestDate))
						earliestDate = fillDates.get(i);
			}
			return sdf.format(earliestDate);
		}
		public ArrayList<Date> getFillDates() {
			return this.fillDates;
		}
		public String getRxNumber() {
			return this.rxNumber;
		}
		public String getDaySupply() {
			return this.daySupply;
		}
		public String getQuantity() {
			return this.quantity;
		}
		
	}
}