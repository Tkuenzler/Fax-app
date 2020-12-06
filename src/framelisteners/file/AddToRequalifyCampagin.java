package framelisteners.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import org.json.JSONException;

import Clients.Five9Client;
import Clients.InfoDatabase;
import Clients.RoadMapClient;
import source.CSVFrame;
import source.LoadInfo;
import subframes.FileChooser;
import table.MyTableModel;
import table.Record;

public class AddToRequalifyCampagin implements ActionListener {
	String[] pharmacies = null;
	InfoDatabase info = null;
	String marketingName = null;
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		File file = FileChooser.SaveTxtFile();
		if(file==null)
			return;
		info = new InfoDatabase();
		BufferedWriter bw = null;
		if(!CheckCategories())
			return;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			marketingName = LoadInfo.getAppName();
			RoadMapClient roadMap = new RoadMapClient();
			pharmacies = roadMap.getPharmacies();
			roadMap.close();
			for(Record record: CSVFrame.model.data) {
				if(!validPharmacy(record.getPharmacy())) {
					bw.write(record.getFirstName()+" "+record.getLastName()+": "+record.getPhone()+" IS INVALID PHARMACY");
					bw.newLine();
					continue;
				}
				String response = AddToRequalify(record);
				bw.write(record.getFirstName()+" "+record.getLastName()+": "+record.getPhone()+" "+response);
				bw.newLine();
			}
			bw.close();
			info.close();
		} catch (URISyntaxException | IOException | JSONException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e1.getMessage());
			return;
		}
		JOptionPane.showMessageDialog(null, "FINISHED");
	}
	private boolean CheckCategories() {
		for(Record record: CSVFrame.model.data) {
			String[] categories = record.getEmail().split(",");
			for(String category: categories) {
				switch(category.trim().toUpperCase()) {
						case "PAIN":
						case "CONSTIPATION":
						case "ANTIFUNGAL":
						case "DERMATITIS":
						case "MIGRAINE":
						case "VITAMIN":
							continue;
						default:
							JOptionPane.showMessageDialog(null, record.getFirstName()+" "+record.getLastName()+" "+category+" not valid drug category"); 
							return false;
				}
			}
		}
		return true;
	}
	private boolean validPharmacy(String pharmacy) {
		for(String s: pharmacies) {
			if(s.equalsIgnoreCase(pharmacy))
				return true;
		}
		return false;
	}
	private String AddToRequalify(Record record) {
		int addToDB = info.AddToRequalifyCampaign(record, marketingName);
		if(addToDB>0) {
			Five9Client.addToRequalifyList(record, marketingName);
			return "WAS SUCCESSFULLY ADDED";
		}
		else if(addToDB==-1) {
			return "IS A DUPLICATE";
		}
		else {
			return "UNEXPECTED ERROR";
		}
	}
}
