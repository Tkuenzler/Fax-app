package framelisteners.telmed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import Clients.DatabaseClient;
import subframes.FileChooser;

public class UpdateTelmedCost implements ActionListener {	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub	
		File file = FileChooser.OpenTxtFile("INVOICE SHEET");
		BufferedReader br = null;
		DatabaseClient client = new DatabaseClient(true);
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			while((line=br.readLine())!=null) {
				String[] data = line.split("-");
				if(data.length<2)
					continue;
				String id = data[0].trim();
				int amount = (int) Double.parseDouble(data[1].trim());
				if(amount>=0)
					continue;
				amount = amount * -1;
				if(client.UpdateTelmedCost(id, ""+amount)!=1)
					System.out.println(id+" "+amount);
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
