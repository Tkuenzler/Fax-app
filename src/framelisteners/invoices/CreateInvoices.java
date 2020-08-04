package framelisteners.invoices;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

import images.InvoicePDF;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import subframes.FileChooser;

public class CreateInvoices implements ActionListener {

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		@SuppressWarnings("serial")
		ArrayList<Invoice> list = new ArrayList<Invoice>() {
				@Override
				public boolean contains(Object o) {
					String compare = o.toString();
					for(Invoice s: this){
						if(s.getPhone().equalsIgnoreCase(compare))
							return true;
					}
					return false;
				}
		};
		String pdf = FileChooser.OpenPdfFile("Invoice Template");
		if(pdf==null)return;
		String folder = FileChooser.OpenFolder("Save Invoices to: ");
		if(folder==null)return;
		File invoiceFile = FileChooser.OpenXlsFile("Invoice List");
		if(invoiceFile==null)return;
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(invoiceFile);
			Sheet sheet = workbook.getSheet(0);
			for(int row = 1;row<sheet.getRows();row++) {
				Cell[] data = sheet.getRow(row);
				String phone = data[Invoice.Columns.PHONENUMBER].getContents();
				if(!list.contains(phone)) {
					Invoice invoice = new Invoice(data);
					list.add(invoice);
				}
				else {
					for(Invoice invoice: list) {
						if(invoice.getPhone().equalsIgnoreCase(data[Invoice.Columns.PHONENUMBER].getContents())){
							invoice.addDrug(data);
							break;
						}
					}
				}
			}
			for(Invoice invoice: list) {
				if(invoice.getCopayTotal()==0)
					continue;
				InvoicePDF image = new InvoicePDF(pdf);
				image.PopulateScript(invoice, folder);
				image.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (BiffException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if (workbook != null)workbook.close();
			System.out.println("CLOSED");
		}
	}
	public class Invoice {
		String date,phone,patientName,address,city,state,zip;
		double copayTotal;
		ArrayList<Drug> drugs = new ArrayList<Drug>();
		public Invoice(Cell[] data) {
			setPatientName(data[Columns.PATIENT_NAME].getContents());
			setAddress(data[Columns.ADDRESS].getContents());
			setCity(data[Columns.CITY].getContents());
			setState(data[Columns.STATE].getContents());
			setZip(data[Columns.ZIP].getContents());
			setPhone(data[Columns.PHONENUMBER].getContents());
			addDrug(data);
			
		}
		
		public double getCopayTotal() {
			return copayTotal;
		}
		public void addToCopayTotal(double copay) {
			this.copayTotal += copay;
		}
		public void addDrug(Cell[] data) {
			if(Double.parseDouble(data[Columns.COPAY].getContents())>0)
				drugs.add(new Drug(data));
		}
		public ArrayList<Drug> getDrugs() {
			return drugs;
		}
		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = StripDown(city);
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = StripDown(state);
		}

		public String getZip() {
			return zip;
		}

		public void setZip(String zip) {
			this.zip = StripDown(zip);
		}
		
		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getPatientName() {
			return patientName;
		}

		public void setPatientName(String patientName) {
			String[] name = StripDown(patientName).split(",");
			this.patientName = StripDown(name[1]+" "+name[0]);
			System.out.println(this.patientName);
		}
		
		private class Columns {
			public static final int PATIENT_NAME = 2;
			public static final int PHONENUMBER = 3;
			public static final int COPAY = 5;
			public static final int ADDRESS = 6;
			public static final int CITY = 7;
			public static final int STATE = 8;
			public static final int ZIP = 9;
			
		}
		public class Drug {
			String name,copay,rxNumber,dateFilled;
			public Drug(Cell[] data) {
				setName(data[Columns.DRUG].getContents());
				setCopay(data[Columns.COPAY].getContents());
				setRxNumber(data[Columns.RX_NUMBER].getContents());
				setDateFilled(data[Columns.DATE].getContents());
			}
			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = StripDown(name);
			}

			public String getCopay() {
				return copay;
			}

			public void setCopay(String copay) {
				NumberFormat formatter = NumberFormat.getCurrencyInstance();
				this.copay = formatter.format(Double.parseDouble(copay));
				addToCopayTotal(Double.parseDouble(copay));
			}

			public String getRxNumber() {
				return rxNumber;
			}

			public void setRxNumber(String rxNumber) {
				this.rxNumber = StripDown(rxNumber);
			}

			public String getDateFilled() {
				return dateFilled;
			}

			public void setDateFilled(String dateFilled) {
				this.dateFilled = dateFilled;
			}
			private class Columns {
				public static final int DATE = 0;
				public static final int RX_NUMBER = 1;
				public static final int DRUG = 4;
				public static final int COPAY = 5;
			}
			
		}
	}
	public String StripDown(String s) {
		return s.trim().replace("[^A-Za-z0-9]", "");
	}

}
