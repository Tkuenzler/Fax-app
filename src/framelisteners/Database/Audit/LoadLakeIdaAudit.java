package framelisteners.Database.Audit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import org.json.JSONException;
import Clients.InfoDatabase;
import source.LoadInfo;
import subframes.FileChooser;

public class LoadLakeIdaAudit implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		File file = FileChooser.OpenCsvFile("Lake Ida Report");
		if(file==null)
			return;
		InfoDatabase info = new InfoDatabase();
		BufferedReader br = null;
		try {
			String company = LoadInfo.getAppName();
			br = new BufferedReader(new FileReader(file));
			String header = br.readLine();
			String line = null;
			while((line=br.readLine())!=null) {
				String[] data = br.readLine().split(",");
				String first = data[Columns.FIRST_NAME];
				String last = data[Columns.LAST_NAME];
				String phone = data[Columns.PHONENUMBER].replace("-", "");
				String date = data[Columns.DATE];
				if(info.IsAudtied(phone)) {
					if(info.IsFilledAfter(phone, date)) {
						info.UpdateFillDate(phone,date);
					}
				}
				else {
					info.AddAuditedPatient(first, last, phone, date, company);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
				if(info != null) info.close();
				System.out.println("CLOSED");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private class Columns {
		public static final int FIRST_NAME = 1;
		public static final int LAST_NAME = 2;
		public static final int PHONENUMBER = 3;
		public static final int DATE = 0;
	}

}
