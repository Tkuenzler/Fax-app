package framelisteners.telmed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import Clients.DatabaseClient;
import subframes.FileChooser;
import table.Record;

public class AddTelmedByStatus implements ActionListener {
	private int PHONE = 10;
	private int STATUS = 8;
	private int ID = 46;
	private int DATE_MODIFIED = 3;
	private int PCN = 43;
	private int BIN = 42;
	private int GRP = 44;
	private int FIRST_NAME = 4;
	private int LAST_NAME = 5;
	private int STATE = 6;
	private int ZIP = 7;
	private int PHARMACY = 15;
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		File file = FileChooser.OpenCsvFile("Telmed Records");
		BufferedReader br = null;
		DatabaseClient client = new DatabaseClient(true);
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			br.readLine();
			while((line=br.readLine())!=null) {
				String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				
				String status = data[0].replaceAll("\"", "");
				String phone = data[1].replaceAll("\"", "").replaceAll("-", "");
				String id = data[2].replaceAll("\"", "");
				/*
				String date = data[DATE_MODIFIED].split(" ")[0].replaceAll("\"", "");
				if(date.equalsIgnoreCase(""))
					date = "0000-00-00";
				String pcn = data[PCN].replaceAll("\"", "");
				String bin = data[BIN].replaceAll("\"", "");
				String grp = data[GRP].replaceAll("\"", "");
				String first = data[FIRST_NAME].replaceAll("\"", "");
				String last = data[LAST_NAME].replaceAll("\"", "");
				String state = data[STATE].replaceAll("\"", "");
				String zip = data[ZIP].replaceAll("\"", "");
				String pharmacy = data[PHARMACY].replaceAll("\"", "");
				Record record = new Record();
				record.setFirstName(first);
				record.setLastName(last);
				record.setPhone(phone);
				record.setBin(bin);
				record.setGrp(grp);
				record.setPcn(pcn);
				record.setZip(zip);
				record.setState(state);
				System.out.println(first+" "+last);
				*/
				int add = client.UpdateTelmedStatus(phone, id, status);
				//int add = client.AddFullTelmedRecord(record,pharmacy,status,id);
				if(add==1)
					System.out.println("UPDATED "+phone+" "+add);
				else
					System.out.println("DID NOT UPDATE "+phone+" "+add);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
				System.out.println("CLOSED");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
