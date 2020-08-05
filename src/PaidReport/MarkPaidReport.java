package PaidReport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import Clients.DatabaseClient;
import subframes.FileChooser;

public class MarkPaidReport implements ActionListener {
	public int DATE = 0;
	public int RX_NUMBER = 0;
	public int PAY = 0;
	public int COST = 0;
	public int PAID_MATCH = 0;
	XSSFWorkbook myWorkBook = null;
	FileInputStream fis =null;
	String report = null;
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		ChooseReportType();
		if(report==null)
			return;
		DatabaseClient client = new DatabaseClient(true);
		File file = FileChooser.OpenXlsxFile("Mark Report");
		try {
			fis = new FileInputStream(file);
		    myWorkBook = new XSSFWorkbook (fis);
		    for(int i = 0;i<myWorkBook.getNumberOfSheets();i++) {
				XSSFSheet sheet = myWorkBook.getSheetAt(i);
				System.out.println("NEW SHEET: "+sheet.getSheetName());
				Iterator<Row> rows = sheet.rowIterator();
				boolean foundHeader = false;
				while(rows.hasNext()) {
					Row row = rows.next();
					System.out.println(row.getLastCellNum());
					if(row.getLastCellNum()>15 && !foundHeader) {
						SetHeaders(row);
						foundHeader = true;
						continue;
					}
					else if(row.getLastCellNum()<15)
						continue;
					String date = getCellValue(row.getCell(DATE));
					String rxNumber = getCellValue(row.getCell(RX_NUMBER));
					String paidMatch = getCellValue(row.getCell(PAID_MATCH));
					String p = getCellValue(row.getCell(PAY));
					String c = getCellValue(row.getCell(COST));
					if(p.equalsIgnoreCase(""))
						p = "0";
					if(c.equalsIgnoreCase(""))
						c = "0";
					double pay = Double.parseDouble(p);
					double cost = Double.parseDouble(c);
					System.out.println(pay+" "+cost);
					if((pay-cost)>0 && paidMatch.equalsIgnoreCase("YES"))
						System.out.println(client.SetClaimStatus(date,rxNumber,report));
					else if((pay-cost)>0 && report.equalsIgnoreCase("audited"))
					System.out.println(client.SetClaimStatus(date,rxNumber,report));
					else if(pay==0)
						System.out.println(client.SetClaimStatus(date,rxNumber,"error"));
				}
		    }
		} catch(IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		} finally {
            try {
            	if(fis!=null)fis.close();
            	if(myWorkBook!=null) myWorkBook.close();
            	if(client!=null)client.close();
            } catch(IOException ex) {
            	
            }
            JOptionPane.showMessageDialog(null, "Succesfully Uploaded File");
        }
	}
	private void SetHeaders(Row row) {
		Iterator<Cell> cells = row.cellIterator();
		while(cells.hasNext()) {
			Cell cell = cells.next();
			String value = getCellValue(cell);
			switch(value.toUpperCase()) {
				case "DATE":
					DATE = cell.getColumnIndex();
					break;
				case "RX NUMBER":
					RX_NUMBER = cell.getColumnIndex();
					break;
				case "INS DUE":
					PAY = cell.getColumnIndex();
					break;
				case "INS. DUE":
					PAY = cell.getColumnIndex();
					break;
				case "COST":
					COST = cell.getColumnIndex();
					break;
				case "PAID MATCH":
					PAID_MATCH = cell.getColumnIndex();
					break;
			}
		}
	}
	private String getCellValue(Cell cell) {
		if(cell==null)
			return "";
		if(cell.getCellType()==CellType.NUMERIC && (cell.getColumnIndex()==PAY || cell.getColumnIndex()==COST))
			return ""+cell.getNumericCellValue();
		else if(cell.getCellType()==CellType.NUMERIC)
			return ""+(int)cell.getNumericCellValue();
		else if(cell.getCellType()==CellType.STRING)
			return cell.getStringCellValue().trim();
		else if(cell.getCellType()==CellType.FORMULA) {
			if(cell.getCachedFormulaResultType()==CellType.NUMERIC)
				return ""+cell.getNumericCellValue();
			else
				return cell.getRichStringCellValue().toString();		
		}
		else
			return cell.getStringCellValue();
	}
	private void ChooseReportType() {
		String[] reports = {"paid","audited"};
		this.report = (String) JOptionPane.showInputDialog(new JFrame(), "Select a report", "Reports:", JOptionPane.QUESTION_MESSAGE, null, reports, reports[0]);
		
	}

}
