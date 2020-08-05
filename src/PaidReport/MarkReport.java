package PaidReport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Clients.DatabaseClient;
import subframes.FileChooser;

public class MarkReport implements ActionListener {

	public class Rows {
		public static final int DATE = 1;
		public static final int RX_NUMBER = 2;
		public static final int PROFIT_MARGIN = 11;
		public static final int NAME = 3;
		public static final int PHONE = 4;
		public static final int MEDICATION = 6;
		public static final int BIN = 14;
		public static final int GRP = 15;
		public static final int PCN = 16;
		public static final int NDC = 34;
	}
	XSSFWorkbook myWorkBook = null;
	FileInputStream fis =null;
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(true);
		File file = FileChooser.OpenXlsxFile("Mark Report");
		try {
			fis = new FileInputStream(file);
		    myWorkBook = new XSSFWorkbook (fis);
			String pharmacy = null;
			for(int i = 0;i<myWorkBook.getNumberOfSheets();i++) {
				XSSFSheet sheet = myWorkBook.getSheetAt(i);
				System.out.println("NEW SHEET: "+sheet.getSheetName());
				pharmacy = sheet.getSheetName();
				Iterator<Row> rows = sheet.rowIterator();
				boolean skip = false;
				while(rows.hasNext()) {
					Row row = rows.next();
					if(!skip) {
						skip = true;
						continue;
					}
					double profit = row.getCell(Rows.PROFIT_MARGIN).getNumericCellValue();
					if(profit>0)
						System.out.println(client.AddPaidClaim(row,pharmacy));
					else 
						System.out.println("DELETED: "+client.DeletePaidClaim(row.getCell(Rows.PHONE)));
					
				}
			}
		} catch(IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		} finally {
			try {
            	if(fis!=null)fis.close();
            	if(myWorkBook!=null) myWorkBook.close();
            } catch(IOException ex) {
            	
            }
            JOptionPane.showMessageDialog(null, "Succesfully Uploaded File");
        }
	}
	public String getCellValue(Cell cell) {
		CellType type = cell.getCellType();
		if(type==CellType.NUMERIC)
			return ""+cell.getNumericCellValue();
		else if(type==CellType.BOOLEAN)
			return ""+cell.getBooleanCellValue();
		else
			return cell.getStringCellValue();
	}
}
