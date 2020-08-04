package framelisteners.edit.Doctor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import Clients.InfoDatabase;
import Doctor.Doctor;
import source.CSVFrame;
import source.JSONParser;
import table.Record;

public class VerifyDrType implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		InfoDatabase db = new InfoDatabase();
		loop:
		for(Record record: CSVFrame.model.data) {
			try {
				HttpURLConnection connection = (HttpURLConnection) new URL("https://npiregistry.cms.hhs.gov/api/?version=2.0&number="+record.getNpi()).openConnection();
				connection.connect();
				BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while((line=rd.readLine())!=null)
					sb.append(line);
				rd.close();
				connection.disconnect();
				Doctor d = JSONParser.CreateDoctor(sb.toString());
				for(String code: d.getCode()) {
					if(db.CheckDrType(code)) {
						record.setRowColor(Color.GREEN);
						continue loop;
					}		
				}
				record.setRowColor(Color.RED);
			} catch (IOException e) {
			// TODO Auto-generated catch block
				record.setRowColor(Color.GRAY);
				continue loop;
			} catch(Exception e) {
				record.setRowColor(Color.GRAY);
				continue loop;
			}
			db.close();
		}
	}
}
