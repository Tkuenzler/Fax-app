package images;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import framelisteners.invoices.CreateInvoices.Invoice;
import framelisteners.invoices.CreateInvoices.Invoice.Drug;
import objects.Fax;
import table.Record;

public class InvoicePDF {
	String src;
	PDDocument pdfDocument;
	Record record;
	PDDocumentCatalog docCatalog = null;
	PDAcroForm acroForm = null;
	public InvoicePDF(String src) throws IOException {	
		System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
		pdfDocument = PDDocument.load(new File(src));
		docCatalog = pdfDocument.getDocumentCatalog();
		acroForm = docCatalog.getAcroForm();
	}
	public void PopulateScript(Invoice invoice,String folder) {
		try {
			//COVER PAGE
			List<PDField> fields = acroForm.getFields();
			StringBuilder fillDate = new StringBuilder();
			StringBuilder product = new StringBuilder();
			StringBuilder rx = new StringBuilder();
			StringBuilder copay = new StringBuilder();
			StringBuilder rxList = new StringBuilder();
			for(int i = 0;i<invoice.getDrugs().size();i++) {
				Drug drug = invoice.getDrugs().get(i);
				fillDate.append(drug.getDateFilled()+"\r\n"); 
				product.append(drug.getName()+"\r\n"); 
				rx.append(drug.getRxNumber()+"\r\n"); 
				copay.append(drug.getCopay()+"\r\n"); 
				if(i<invoice.getDrugs().size()-1)
					rxList.append(drug.getRxNumber()+", ");
				else
					rxList.append(drug.getRxNumber()+"");
			}
			for(PDField field: fields) {
				switch(field.getPartialName()) {
					case "Patient Name":
						acroForm.getField("Patient Name").setValue(invoice.getPatientName()); 
						break;
					case "Patient Address":
						acroForm.getField("Patient Address").setValue(invoice.getAddress()); 
						break;
					case "City/State/Zip":
						acroForm.getField("City/State/Zip").setValue(invoice.getCity()+"/"+invoice.getState()+"/"+invoice.getZip()); 
						break;
					case "Patient Phone":
						acroForm.getField("Patient Phone").setValue(invoice.getPhone()); 
						break;
					case "Fill Date":
						acroForm.getField("Fill Date").setValue(fillDate.toString());
						break;
					case "RX":
						acroForm.getField("RX").setValue(rx.toString());
						break;
					case "Product":
						acroForm.getField("Product").setValue(product.toString());
						break;
					case "Copay":
						acroForm.getField("Copay").setValue(copay.toString());
						break;
					case "RX List":
						acroForm.getField("RX List").setValue(rxList.toString());
						break;
					case "Date":
						acroForm.getField("Date").setValue(getCurrentDate("MM/dd/yyyy"));
						break;
						
				}
			}
			pdfDocument.save(new File(folder+"\\"+invoice.getPatientName()+".pdf"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
		}  catch (IllegalArgumentException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
		} 
	}
	private String getCurrentDate(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format); 
		Date date = new Date(); 
		return formatter.format(date);
	}
	public void close() {
		try {
			pdfDocument.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}