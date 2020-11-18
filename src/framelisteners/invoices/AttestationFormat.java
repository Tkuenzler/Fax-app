package framelisteners.invoices;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import subframes.FileChooser;

public class AttestationFormat implements ActionListener {
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
		File file = FileChooser.SaveCsvFile();
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			for(int i = 0;i<=DOCTOR_FAX;i++) {
				switch(i) {
				case DATE_ADDED:
					bw.append("Date Added,");
					break;
				case FILL_DATE:
					bw.append("Fill Date,");
					break;
				case RX_NUMBER:
					bw.append("Rx Number,");
					break;
				case LAST_NAME:
					bw.append("Last Name,");
					break;
					
				case FIRST_NAME:
					bw.append("First Name,");
					break;
				case DOB:
					bw.append("Date Of Birth,");
					break;
				case DRUG:
					bw.append("Medication Name,");
					break;
				case QUANTITY_WRITTEN:
					bw.append("Quantity Written,");
					break;
				case QUANTITY_SUPPLIED:
					bw.append("Quantity Supplied,");
					break;
				case DAY_SUPPLY:
					bw.append("Day Supply,");
					break;
				case DOCTOR_LAST:
					bw.append("Dr Last Name,");
					break;
				case DOCTOR_FIRST:
					bw.append("Dr First Name ,");
					break;
				case DOCTOR_ADDRESS:
					bw.append("Dr Address,");
					break;
				case DOCTOR_CITY:
					bw.append("Dr City,");
					break;
				case DOCTOR_STATE:
					bw.append("Dr State,");
					break;
				case DOCTOR_ZIP:
					bw.append("Dr Zip,");
					break;
				case DOCTOR_PHONE:
					bw.append("Dr Phone,");
					break;
				case DOCTOR_FAX:
					bw.append("Dr Fax, ");
					break;
				case NPI:
					bw.append("NPI");
					break;
				}
			}
			JOptionPane.showMessageDialog(null,"Sucessfully Saved");
		}  catch(IOException e) {
			JOptionPane.showMessageDialog(new JFrame(),e.getMessage());
		}
		finally {
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
