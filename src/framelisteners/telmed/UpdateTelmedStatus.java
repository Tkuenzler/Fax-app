package framelisteners.telmed;

import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Clients.DatabaseClient;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import subframes.FileChooser;

public class UpdateTelmedStatus implements ActionListener {
	private int STATUS = 0;
	private int ID = 2;
	private int PHONE = 1;
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub	
		File file = FileChooser.OpenCsvFile("RDS Status");
		BufferedReader br = null;
		DatabaseClient client = new DatabaseClient(true);
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			String headers = br.readLine();
			while((line=br.readLine())!=null) {
				String[] data = line.split(",");
				String status = data[STATUS];
				String id = data[ID];
				String phone = data[PHONE].replace("-", "");
				if(client.UpdateDMETelmedStatus(phone, id, status)!=1)
					System.out.println(phone+" "+status+" "+id);
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
