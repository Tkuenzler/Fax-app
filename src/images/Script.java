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
import objects.Fax;
import table.Record;

public class Script {
	String src,category;
	PDDocument pdfDocument;
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
		System.out.println("FROM CON SOURCE: "+src);
		if(load)
			LoadNewScript(src);
	}
	public Script(String source) {
		try {
			LoadNewScript(source);
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
					acroForm.getField("Drug 1").setValue(drug1.getName()); 
				break;
			case "Drug Qty 1": 
				if(drug1!=null)
					acroForm.getField("Drug Qty 1").setValue("Medication:  "+drug1.getQty()); 
				break;
			case "Drug Sig 1":
				if(drug1!=null)
					acroForm.getField("Drug Sig 1").setValue("Dispense:  "+drug1.getSig()); 
				break;
			case "Drug Therapy 2":
				if(drug2!=null)
					acroForm.getField("Drug Therapy 2").setValue("Sig:  "+drug2.getTherapy()); 
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
		System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
		pdfDocument = PDDocument.load(new File(src));
		docCatalog = pdfDocument.getDocumentCatalog();
		acroForm = docCatalog.getAcroForm();
	}
	public void PopulateDME(Record record)  throws ScriptException, IOException {
		System.out.println("FROM DME SOURCE: "+src);
		List<PDField> fields = acroForm.getFields();
		clearFields(fields);
		populateFields(fields,record);
		if(record.getBrace_list().equalsIgnoreCase(""))
			throw new ScriptException("NO BRACE LISTED");
		checkOffDME(fields,record);
		fileName = fax.getSaveLocation()+"\\"+fax.getLogin()+".pdf";
		pdfDocument.save(new File(fileName));
	}
	public void checkOffDME(List<PDField> fields,Record record) throws IOException {
		String[] brace_list = record.getBrace_list().split(",");
		for(String brace: brace_list) {
			switch(brace) {
				case "Back Pain":
					((PDCheckBox) acroForm.getField("Back")).check();
					break;
				case "Left Ankle Pain":
					((PDCheckBox) acroForm.getField("Ankle")).check();
					break;
				case "Right Ankle Pain":
					((PDCheckBox) acroForm.getField("Ankle")).check();
					break;
				case "Left Wrist Pain":
					((PDCheckBox) acroForm.getField("Wrist")).check();
					break;
				case "Right Wrist Pain":
					((PDCheckBox) acroForm.getField("Wrist")).check();
					break;
				case "Left Knee Pain":
					((PDCheckBox) acroForm.getField("Knee")).check();
					break;
				case "Right Knee Pain":
					((PDCheckBox) acroForm.getField("Knee")).check();
					break;
				case "Left Shoulder Pain":
					((PDCheckBox) acroForm.getField("Shoulder")).check();
					break;
				case "Right Shoulder Pain":
					((PDCheckBox) acroForm.getField("Shoulder")).check();
					break;
				case "Left Elbow Pain":
					((PDCheckBox) acroForm.getField("Elbow")).check();
					break;
				case "Right Elbow Pain":
					((PDCheckBox) acroForm.getField("Elbow")).check();
					break;
				/*
				case "Neck Pain":
					((PDCheckBox) acroForm.getField("Neck")).check();
					break;
				case "Hip Pain":
					((PDCheckBox) acroForm.getField("Hip")).check();
					break;
				*/
			}
		}
	}
	public void PopulateScript(Record record) throws ScriptException {
		System.out.println("FAX LINE: "+fax.getLogin());
		this.record = record;
		try {
			//COVER PAGE
			List<PDField> fields = acroForm.getFields();
			clearFields(fields);
			populateFields(fields,record);
			if(drugs!=null)
				PopulateDrugArray(fields);
			if(drug1!=null || drug2!=null)
				PopulateDrugs(fields);
			PDCheckBox pain = (PDCheckBox) acroForm.getField("Pain");
			PDCheckBox derm = (PDCheckBox) acroForm.getField("Dermatitis");
			PDCheckBox vitamins = (PDCheckBox) acroForm.getField("Vitamins");
			PDCheckBox acid = (PDCheckBox) acroForm.getField("Acid");
			if(pain!=null)
				if(fax.isPain())
					pain.check();
			if(derm!=null)
				if(fax.isDerm())
					derm.check();
			if(vitamins!=null)
				if(fax.isVitamins())
					vitamins.check();
			if(acid!=null)
				if(fax.isAcid())
					acid.check();
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
	private void populateFields(List<PDField> fields,Record record) throws IOException {
		String number = null;
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		for(PDField field: fields) {
			switch(field.getPartialName()) {
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
	public void saveScript(String folder) {
		this.fileName = folder+"\\"+record.getFirstName()+" "+record.getLastName()+".pdf";
		try {
			pdfDocument.save(new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public void clearFields(List<PDField> fields) throws IOException {
		for(PDField field: fields) {
			if(field.getFieldType().equalsIgnoreCase("Tx"))
				acroForm.getField(field.getPartialName()).setValue("");
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