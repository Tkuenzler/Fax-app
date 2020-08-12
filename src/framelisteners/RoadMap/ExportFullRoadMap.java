package framelisteners.RoadMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Clients.RoadMapClient;
import subframes.FileChooser;

public class ExportFullRoadMap implements ActionListener {
	RoadMapClient roadmap = null;
	@Override
	public void actionPerformed(ActionEvent arg0) {
		File file = FileChooser.SaveCsvFile();
		roadmap = new RoadMapClient();
		String[] pharmacies = roadmap.getPharmacies();
		String[] pbms = roadmap.getPbms();
		String[] states = roadmap.get50States();
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			StringBuilder header = new StringBuilder();
			StringBuilder map = new StringBuilder();
			header.append("State,");
			for(int i = 0;i<pbms.length;i++) {
				String pbm = pbms[i];
				if(i==pbms.length-1)
					header.append(pbm);
				else
					header.append(pbm+",");	
			}
			bw.write(header.toString());
			bw.newLine();
			for(String state: states) {
				bw.write(state+",");
				for(int i = 0;i<pbms.length;i++) {
					String pbm = pbms[i];
					if(isInPharmacy(pharmacies,state,pbm))
						bw.write("1");
					else
						bw.write("0");
					if(i<pbms.length-1)
						bw.write(",");
				}
				bw.newLine();
			}
		} catch(IOException e) {
			JOptionPane.showMessageDialog(new JFrame(),e.getMessage());
		}
		finally {
			roadmap.close();
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JOptionPane.showMessageDialog(null, "Complete");
			
	}
	private boolean isInPharmacy(String[] pharmacies, String state, String pbm) {
		for(String pharmacy: pharmacies) {
			if(roadmap.isInRoadMap(pharmacy, pbm, state)==1)
				return true;
		}
		return false;
	}

}
