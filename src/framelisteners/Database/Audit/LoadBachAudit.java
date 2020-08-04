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

public class LoadBachAudit implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		File report = FileChooser.OpenXlsFile("Open Bach Report");
		InfoDatabase info = new InfoDatabase();
		Workbook workbook = null;
		try {
			String company = LoadInfo.getAppName();
			workbook = Workbook.getWorkbook(report);
			Sheet sheet = workbook.getSheet(0);
			for(int row = 1;row<sheet.getRows();row++) {
				Cell[] data = sheet.getRow(row);
				System.out.println("LENGTH: "+data.length);
				if(data.length!=21) {
					System.out.println("LENGTH: "+data.length);
					continue;
				}
				String[] name = data[Columns.PATIENT_NAME].getContents().split(",");
				String phone = data[Columns.PHONENUMBER].getContents().replaceAll("[^A-Za-z0-9\\s]", "").replace(" ", "");
				if(name.length!=2)
					continue;
				String first = name[1].replaceAll("[^A-Za-z0-9\\s]", "").replace(" ", "");
				String last = name[0].replaceAll("[^A-Za-z0-9\\s]", "").replace(" ", "");
				info.AddAuditedPatient(first,last,phone,"",company);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (BiffException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
		public static final int PATIENT_NAME = 2;
		public static final int PHONENUMBER = 3;
	}

}
