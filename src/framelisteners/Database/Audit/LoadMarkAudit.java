package framelisteners.Database.Audit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.json.JSONException;
import Clients.InfoDatabase;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import source.LoadInfo;
import subframes.FileChooser;

public class LoadMarkAudit implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		File report = FileChooser.OpenXlsFile("Open Mark Report");
		InfoDatabase info = new InfoDatabase();
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(report);
			for(int i = 0;i<workbook.getNumberOfSheets();i++) {
				Sheet sheet = workbook.getSheet(i);
				System.out.println("NEW SHEET "+sheet.getName() );
				String company = LoadInfo.getAppName();
				for(int row = 1;row<sheet.getRows();row++) {
					Cell[] data = sheet.getRow(row);
					String phone = null;
					try {
						String[] name = data[Columns.PATIENT_NAME].getContents().split(",");
						phone = data[Columns.PHONENUMBER].getContents().replaceAll("[^A-Za-z0-9\\s]", "").replace(" ", "");
						String date = data[Columns.DATE].getContents().trim();
						String first = name[1].replaceAll("[^A-Za-z0-9\\s]", "").replace(" ", "");
						String last = name[0].replaceAll("[^A-Za-z0-9\\s]", "").replace(" ", "");
						System.out.println(info.AddAuditedPatient(first,last,phone,date,company));
					} catch(ArrayIndexOutOfBoundsException ex) {
						System.out.println(ex.getMessage());
						System.out.println(phone);
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		} catch (BiffException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println(e1.getMessage());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (workbook != null)workbook.close();
			if(info != null) info.close();
			System.out.println("CLOSED");
		}
	}
	private class Columns {
		public static final int DATE = 2;
		public static final int PATIENT_NAME = 3;
		public static final int PHONENUMBER = 4;
	}

}
