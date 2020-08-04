package framelisteners.Database.Audit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;

import Clients.InfoDatabase;
import source.CSVFrame;
import source.LoadInfo;
import table.Record;

public class LoadAudit implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String company = null;
		try {
			company = LoadInfo.getAppName();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InfoDatabase info = new InfoDatabase();
		for(Record record: CSVFrame.model.data) {
			System.out.println(info.AddAuditedPatient(record.getFirstName(),record.getLastName(),record.getPhone(),"",company));
		}
		info.close();
	}
}
