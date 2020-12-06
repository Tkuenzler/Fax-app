package images; 

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import Fax.Drug;
import framelisteners.invoices.CreateAttestationLetter.Medication;
import framelisteners.invoices.CreateAttestationLetter.PatientRecord;
import objects.Fax;
import table.Record;

public class Script {
	String src,category;
	PDDocument pdfDocument;
	PDFMergerUtility pdfMerger;
	public String fileName;
	Record record;
	Fax fax;
	PDDocumentCatalog docCatalog = null;
	PDAcroForm acroForm = null;
	Drug drug1 = null, drug2 = null;
	Drug[] drugs;
	public Script(Fax fax,boolean load) throws ScriptException, IOException {
		this.fax = fax;
		this.src = fax.getPharmacy();
	}
	public Script(String source) {
		try {
			LoadNewScript(source);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Script(File file) {
		try {
			LoadNewScript(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setCategory(String category) {
		if(category==null)
			this.category = "";
		else
			this.category = category;
	}
	public String getCategory() {
		if(category==null)
			return "";
		else
			return this.category;
	}
	public void SetDrug(Drug drug) {
		drug1 = drug;
	}
	public void SetDrugs(Drug drug1,Drug drug2) {
		this.drug1 = drug1;
		this.drug2 = drug2;
	}
	public void SetDrugs(Drug[] drugs) {
		this.drugs = drugs;
	}
	public void PopulateDrugs(List<PDField> fields) throws IOException {
		for(PDField field: fields) {
			switch(field.getPartialName()) {
			case "Drug Therapy 1":
				if(drug1!=null)
					acroForm.getField("Drug Therapy 1").setValue(drug1.getTherapy()); 
				break;
			case "Drug 1":
				if(drug1!=null)
					acroForm.getField("Drug 1").setValue("Medication:  "+drug1.getName()); 
				break;
			case "Drug Qty 1": 
				if(drug1!=null)
					acroForm.getField("Drug Qty 1").setValue("Dispense:  "+drug1.getQty()); 
				break;
			case "Drug Sig 1":
				if(drug1!=null)
					acroForm.getField("Drug Sig 1").setValue("Sig:  "+drug1.getSig()); 
				break;
			case "Drug Therapy 2":
				if(drug2!=null)
					acroForm.getField("Drug Therapy 2").setValue(drug2.getTherapy()); 
				break;
			case "Drug 2":
				if(drug2!=null)
					acroForm.getField("Drug 2").setValue("Medication:  "+drug2.getName()); 
				break;
			case "Drug Qty 2": 
				if(drug2!=null)
					acroForm.getField("Drug Qty 2").setValue("Dispense:  "+drug2.getQty()); 
				break;
			case "Drug Sig 2":
				if(drug2!=null)
					acroForm.getField("Drug Sig 2").setValue("Sig:  "+drug2.getSig()); 
				break;
			}
		}
	}
	public void PopulateDrugs(List<PDField> fields,Drug drug1,Drug drug2,String notes) throws IOException {
		for(PDField field: fields) {
			switch(field.getPartialName()) {
			case "Drug Therapy 1":
				if(drug1!=null)
					acroForm.getField("Drug Therapy 1").setValue(drug1.getTherapy()); 
				break;
			case "Drug 1":
				if(drug1!=null)
					acroForm.getField("Drug 1").setValue("Medication:  "+drug1.getName()); 
				break;
			case "Drug Qty 1": 
				if(drug1!=null)
					acroForm.getField("Drug Qty 1").setValue("Dispense:  "+drug1.getQty()); 
				break;
			case "Drug Sig 1":
				if(drug1!=null)
					acroForm.getField("Drug Sig 1").setValue("Sig:  "+drug1.getSig()); 
				break;
			case "Drug Therapy 2":
				if(drug2!=null)
					acroForm.getField("Drug Therapy 2").setValue(drug2.getTherapy()); 
				break;
			case "Drug 2":
				if(drug2!=null)
					acroForm.getField("Drug 2").setValue("Medication:  "+drug2.getName()); 
				break;
			case "Drug Qty 2": 
				if(drug2!=null)
					acroForm.getField("Drug Qty 2").setValue("Dispense:  "+drug2.getQty()); 
				break;
			case "Drug Sig 2":
				if(drug2!=null)
					acroForm.getField("Drug Sig 2").setValue("Sig:  "+drug2.getSig()); 
				break;
			case "Notes":
				acroForm.getField("Notes").setValue(notes);
				break;
			}
		}
	}
	public void PopulateDrugArray(List<PDField> fields) throws IOException {
		for(PDField field: fields) {
			switch(field.getPartialName()) {
				case "Drug Therapy 1":
					if(drugs[0]!=null)
						acroForm.getField(field.getPartialName()).setValue(drugs[0].getTherapy()); 
					break;
				case "Drug 1": 
					if(drugs[0]!=null)
						acroForm.getField(field.getPartialName()).setValue("Medication: "+drugs[0].getName()); 
					break;
				case "Drug Qty 1":
					if(drugs[0]!=null)
						acroForm.getField(field.getPartialName()).setValue("Dispense: "+drugs[0].getQty()); 
					break;
				case "Drug Sig 1":
					if(drugs[0]!=null)
						acroForm.getField(field.getPartialName()).setValue("Sig:  "+drugs[0].getSig()); 
					break;
				case "Drug Therapy 2":
					if(drugs[1]!=null)
						acroForm.getField(field.getPartialName()).setValue(drugs[1].getTherapy()); 
					break;
				case "Drug 2": 
					if(drugs[1]!=null)
						acroForm.getField(field.getPartialName()).setValue("Medication: "+drugs[1].getName()); 
					break;
				case "Drug Qty 2":
					if(drugs[1]!=null)
						acroForm.getField(field.getPartialName()).setValue("Dispense: "+drugs[1].getQty()); 
					break;
				case "Drug Sig 2":
					if(drugs[1]!=null)
						acroForm.getField(field.getPartialName()).setValue("Sig: "+drugs[1].getSig()); 
					break;
				case "Drug Therapy 3":
					if(drugs[2]!=null)
						acroForm.getField(field.getPartialName()).setValue(drugs[2].getTherapy()); 
					break;
				case "Drug 3": 
					System.out.println("DRUG 3");
					if(drugs[2]!=null)
						acroForm.getField(field.getPartialName()).setValue("Medication: "+drugs[2].getName()); 
					break;
				case "Drug Qty 3":
					if(drugs[2]!=null)
						acroForm.getField(field.getPartialName()).setValue("Dispense: "+drugs[2].getQty()); 
					break;
				case "Drug Sig 3":
					if(drugs[2]!=null)
						acroForm.getField(field.getPartialName()).setValue("Sig:  "+drugs[2].getSig()); 
					break;
			}
		}
	}
	public void setFax(Fax fax) {
		this.fax = fax;
	}
	public void LoadNewScript(String src) throws InvalidPasswordException, IOException {
		System.out.println(src);
		System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
		pdfDocument = PDDocument.load(new File(src));
		docCatalog = pdfDocument.getDocumentCatalog();
		acroForm = docCatalog.getAcroForm();
	}
	public void LoadNewScript(File file) throws InvalidPasswordException, IOException {
		System.out.println(src);
		System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
		pdfDocument = PDDocument.load(file);
		docCatalog = pdfDocument.getDocumentCatalog();
		acroForm = docCatalog.getAcroForm();
	}
	public void AddScript(String src) throws InvalidPasswordException, IOException, ScriptException {
		System.out.println("ADDING: "+src);
		pdfMerger = new PDFMergerUtility();
		pdfDocument = PDDocument.load(new File(src));
		docCatalog = pdfDocument.getDocumentCatalog();
		acroForm = docCatalog.getAcroForm();

		try {
			//COVER PAGE
			List<PDField> fields = acroForm.getFields();
			populateFields(fields,record,0);
			File file1 = new File(fileName);
			File file2 = new File(fax.getSaveLocation()+"\\"+fax.getLogin()+"2.pdf");
			pdfDocument.save(file2);
	        pdfMerger.addSource(file1);
	        pdfMerger.addSource(file2);
	        pdfMerger.setDestinationFileName(fileName);
	        pdfMerger.mergeDocuments(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
		}  catch (IllegalArgumentException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			throw new ScriptException("Illegal Argument");
		}  finally {
			
		}
	}
	public void PopulateDME(Record record)  throws ScriptException, IOException {
		System.out.println("FROM DME SOURCE: "+src);
		List<PDField> fields = acroForm.getFields();
		clearFields(fields);
		populateFields(fields,record,0);
		if(record.getBrace_list().equalsIgnoreCase(""))
			throw new ScriptException("NO BRACE LISTED");
		uncheckDME(fields);
		checkOffDME(fields,record);
		fileName = fax.getSaveLocation()+"\\"+fax.getLogin()+".pdf";
		pdfDocument.save(new File(fileName));
	}
	public void uncheckDME(List<PDField> field) throws IOException {
		((PDCheckBox) acroForm.getField("Back")).unCheck();
		((PDCheckBox) acroForm.getField("Ankle")).unCheck();
		((PDCheckBox) acroForm.getField("Wrist")).unCheck();
		((PDCheckBox) acroForm.getField("Knee")).unCheck();
		((PDCheckBox) acroForm.getField("Shoulder")).unCheck();
		((PDCheckBox) acroForm.getField("Elbow")).unCheck();
		((PDCheckBox) acroForm.getField("Hip")).unCheck();
		
	}
	public void checkOffDME(List<PDField> fields,Record record) throws IOException {
		String[] brace_list = record.getBrace_list().split(",");
		for(String brace: brace_list) {
			System.out.println("BRACE FOUND: "+brace); 
			switch(brace.trim()) {
				case "Back":
					((PDCheckBox) acroForm.getField("Back")).check();
					break;
				//Ankle
				case "Ankle":
					((PDCheckBox) acroForm.getField("Ankle")).check();	
					break;
				case "Right Ankle":
					((PDCheckBox) acroForm.getField("Ankle")).check();	
					acroForm.getField("Right Ankle").setValue("RT");
					break;
				case "Left Ankle":
					((PDCheckBox) acroForm.getField("Ankle")).check();	
					acroForm.getField("Left Ankle").setValue("LT");
					break;
				//Wrist
				case "Wrist":
					((PDCheckBox) acroForm.getField("Wrist")).check();
					break;
				case "Right Wrist":
					((PDCheckBox) acroForm.getField("Wrist")).check();
					acroForm.getField("Right Wrist").setValue("RT");
					break;
				case "Left Wrist":
					((PDCheckBox) acroForm.getField("Wrist")).check();
					acroForm.getField("Left Wrist").setValue("LT");
					break;
				//Knees
				case "Knee":
				case "Knees":
					((PDCheckBox) acroForm.getField("Knee")).check();
					break;
				case "Right Knee":
					((PDCheckBox) acroForm.getField("Knee")).check();
					acroForm.getField("Right Knee").setValue("RT");
					break;
				case "Left Knee":
					((PDCheckBox) acroForm.getField("Knee")).check();
					acroForm.getField("Left Knee").setValue("LT");
					break;
				//SHOULDER
				case "Shoulder":
					((PDCheckBox) acroForm.getField("Shoulder")).check();
					break;
				case "Right Shoulder":
					((PDCheckBox) acroForm.getField("Shoulder")).check();
					acroForm.getField("Right Shoulder").setValue("RT");
					break;
				case "Left Shoulder":
					((PDCheckBox) acroForm.getField("Shoulder")).check();
					acroForm.getField("Left Shoulder").setValue("LT");
					break;
				//ELBOW
				case "Elbow":
					((PDCheckBox) acroForm.getField("Elbow")).check();
					break;
				case "Right Elbow":
					((PDCheckBox) acroForm.getField("Elbow")).check();
					acroForm.getField("Right Elbow").setValue("RT");
					break;
				case "Left Elbow":
					((PDCheckBox) acroForm.getField("Elbow")).check();
					acroForm.getField("Left Elbow").setValue("LT");
					break;
				//HIP
				case "Hip":
				case "Hips":
					((PDCheckBox) acroForm.getField("Hip")).check();
					break;
			}
		}
	}
	public void AddScript(Drug drug1, Drug drug2,String notes) throws InvalidPasswordException, IOException, ScriptException {
		System.out.println("ADDING: "+drug1.getName());
		pdfMerger = new PDFMergerUtility();
		pdfDocument = PDDocument.load(new File(fax.getCustomScript()));
		docCatalog = pdfDocument.getDocumentCatalog();
		acroForm = docCatalog.getAcroForm();
		try {
			//COVER PAGE
			List<PDField> fields = acroForm.getFields();
			populateFields(fields,record,0);
			if(drug1!=null || drug2!=null)
				PopulateDrugs(fields,drug1,drug2,notes);
			File file1 = new File(fileName);
			File file2 = new File(fax.getSaveLocation()+"\\"+fax.getLogin()+"2.pdf");
			pdfDocument.save(file2);
	        pdfMerger.addSource(file1);
	        pdfMerger.addSource(file2);
	        pdfMerger.setDestinationFileName(fileName);
	        pdfMerger.mergeDocuments(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
		}  catch (IllegalArgumentException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			throw new ScriptException("Illegal Argument");
		}  finally {
			
		}
	}
	public void PopulateScript(Record record,int pages) throws ScriptException {
		System.out.println("FAX LINE: "+fax.getLogin());
		this.record = record;
		try {
			//COVER PAGE
			List<PDField> fields = acroForm.getFields();
			//clearFields(fields);
			populateFields(fields,record,pages);
			if(drugs!=null)
				PopulateDrugArray(fields);
			if(drug1!=null || drug2!=null)
				PopulateDrugs(fields);
			this.fileName = fax.getSaveLocation()+"\\"+fax.getLogin()+".pdf";
			pdfDocument.save(new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
		}  catch (IllegalArgumentException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			throw new ScriptException("Illegal Argument");
		}  finally {
			
		}
	}
	private void populateFields(List<PDField> fields,Record record,int pages) throws IOException {
		String number = null;
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		String name = record.getFirstName()+" "+record.getLastName();
		String dr_name = record.getDrFirst()+" "+record.getDrLast();
		for(PDField field: fields) {
			switch(field.getPartialName()) {
			case "Cover 1":
				
				acroForm.getField("Cover 1").setValue("Your Patient "+name+" has requested Home Delivery services for their medications. Patient "+name+" "
						+ "has identified you, Dr "+dr_name+" as their treating physician and requested we contact you you regarding approval for their "
						+ "Home Delivery services. Please review the accompanying request represented in this document.");
				break;
			case "Cover 2":
				acroForm.getField("Cover 2").setValue("Your patient, "+name+" is waiting for their order.");
				break;
			case "Pages":
				acroForm.getField("Pages").setValue(""+pages); break;
			case "Date":
				acroForm.getField("Date").setValue(dateFormat.format(date).toUpperCase()); break;
			case "Notes":
				acroForm.getField("Notes").setValue(fax.getNotes()); break;
			case "Patient First Name":
				acroForm.getField("Patient First Name").setValue(record.getFirstName()); break;
			case "Patient Last Name":
				acroForm.getField("Patient Last Name").setValue(record.getLastName()); break;
			case "city":
				acroForm.getField("city").setValue(record.getCity()); break;
			case "state":
				acroForm.getField("state").setValue(record.getState()); break;
			case "zip":
				acroForm.getField("zip").setValue(record.getZip()); break;
			case "gender":
				acroForm.getField("gender").setValue(record.getGender()); break;
			case "Patient Name":
				acroForm.getField("Patient Name").setValue(record.getFirstName().toUpperCase()+" "+record.getLastName().toUpperCase()); break;
			case "Patient Phone":
				number = record.getPhone();
				if(number.length()==10)
					number = "("+number.substring(0,3)+") "+number.substring(3,6)+"-"+number.substring(6,10);
				else
					number = record.getPhone();
				acroForm.getField("Patient Phone").setValue(number); break;
			case "DOB":
				acroForm.getField("DOB").setValue(record.getDob().toUpperCase()); break;
			case "Patient Address":
				acroForm.getField("Patient Address").setValue(record.getAddress().toUpperCase()); break;
			case "City/State/Zip":
				acroForm.getField("City/State/Zip").setValue(record.getCity().toUpperCase()+"/"+record.getState().toUpperCase()+"/"+record.getZip().toUpperCase()); break;
			case "Carrier":
				acroForm.getField("Carrier").setValue(record.getCarrier()); break;
			case "Policy Id":
				acroForm.getField("Policy Id").setValue(record.getPolicyId()); break;
			case "BIN":
				acroForm.getField("BIN").setValue(record.getBin()); break;
			case "GROUP":
				acroForm.getField("GROUP").setValue(record.getGrp()); break;
			case "PCN":
				acroForm.getField("PCN").setValue(record.getPcn()); break;
			case "Doctor City/State/Zip":
				acroForm.getField("Doctor City/State/Zip").setValue(record.getDrCity().toUpperCase()+"/"+record.getDrState().toUpperCase()+"/"+record.getDrZip().toUpperCase()); break;
			case "Doctor Name":
				acroForm.getField("Doctor Name").setValue(record.getDrFirst().toUpperCase()+" "+record.getDrLast().toUpperCase()); break;
			case "Doctor Phone":
				number = record.getDrPhone();
				if(number.length()==10)
					number = "("+number.substring(0,3)+") "+number.substring(3,6)+"-"+number.substring(6,10);
				else
					number = record.getDrPhone();
				acroForm.getField("Doctor Phone").setValue(number); break;
			case "Doctor Fax":
				number = record.getDrFax();
				if(number.length()==10)
					number = "("+number.substring(0,3)+") "+number.substring(3,6)+"-"+number.substring(6,10);
				else
					number = record.getDrFax();
				acroForm.getField("Doctor Fax").setValue(number); break;
			case "NPI":
				acroForm.getField("NPI").setValue(record.getNpi().toUpperCase()); break;
			case "Doctor Address":
				acroForm.getField("Doctor Address").setValue(record.getDrAddress1().toUpperCase()); break;
			case "Fax":
				number = fax.getLogin().substring(1, 11);
				if(number.length()==10)
					number = "("+number.substring(0,3)+") "+number.substring(3,6)+"-"+number.substring(6,10);
				else
					number =fax.getLogin();
				acroForm.getField("Fax").setValue(number); 
				break;
			case "Category":
				acroForm.getField("Category").setValue(getCategory());
				break;
			}
		}
	}
	public void populateAttestationMedication(Medication medication,int count) throws IOException {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		acroForm.getField("Medication "+count).setValue("Prescription: "+medication.getName());
		acroForm.getField("Rx Number "+count).setValue("Rx Number: "+medication.getRxNumber());
		acroForm.getField("Day Supply "+count).setValue("Day Supply: "+medication.getDaySupply());
		acroForm.getField("Quantity "+count).setValue("Quantity: "+medication.getQuantity());
		acroForm.getField("Date").setValue(dateFormat.format(date).toUpperCase());
		String firstFill = medication.getEarlistFillDate();
		acroForm.getField("First Fill "+count).setValue("First Fill Date: "+firstFill);
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		for(Date refill: medication.getFillDates()) {
			String fill = sdf.format(refill);
			if(fill.equalsIgnoreCase(firstFill))
				continue;
			sb.append(fill+",");
		}
		if(sb.length()>0)
			sb.setLength(sb.length() - 1);
		acroForm.getField("Refill Dates "+count).setValue("Refill Dates: "+sb.toString());
	}
	public void populateAttestationPDF(PatientRecord record) throws IOException {
		List<PDField> fields = acroForm.getFields();
		for(PDField field: fields) {
			switch(field.getPartialName()) {
				case "Doctor Name":
					acroForm.getField(field.getPartialName()).setValue(record.getDr_first()+" "+record.getDr_last());
					break;
				case "Patient Name":
					acroForm.getField(field.getPartialName()).setValue(record.getFirstName()+" "+record.getLastName());
					break;
				case "DOB":
					acroForm.getField(field.getPartialName()).setValue(record.getDob());
					break;
				case "Doctor Address":
					acroForm.getField(field.getPartialName()).setValue(record.getDr_addres());
					break;
				case "Doctor City/State/Zip":
					acroForm.getField(field.getPartialName()).setValue(record.getDr_city()+" "+record.getDr_state()+" "+record.getDr_zip());
					break;
				case "Doctor Phone":
					acroForm.getField(field.getPartialName()).setValue(record.getDr_phone());
					break;
				case "Doctor Fax":
					acroForm.getField(field.getPartialName()).setValue(record.getDr_fax());
					break;
				case "NPI":
					acroForm.getField(field.getPartialName()).setValue(record.getNpi());
			}
		}
	}
	public void saveAttestationPDF(String name) {
		this.fileName = name+".pdf";
		try {
			pdfDocument.save(new File(fileName));
			close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public void saveScript(String folder) {
		this.fileName = folder+"\\"+record.getFirstName()+" "+record.getLastName()+".pdf";
		try {
			pdfDocument.save(new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public void clearFields(List<PDField> fields)  {
		String f = null;
		try {
			for(PDField field: fields) {
				f = field.getPartialName();
				if(field.getFieldType().equalsIgnoreCase("Tx")) { 
					acroForm.getField(field.getPartialName()).setValue("");
				}
			}
		} catch(IOException ex) {
			System.out.println("ERROR ON: "+f);
			ex.printStackTrace();
		}
	}
	public void close() {
		try {
			pdfDocument.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public class  ScriptException extends Exception {
		public ScriptException(String message){
			super(message);
		}
	}
}