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

public class ExportRoadMap implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		RoadMapClient roadmap = new RoadMapClient();
		String pharmacy = roadmap.getPharmacy();
		if(pharmacy==null)
			return;
		File file = FileChooser.SaveCsvFile();
		if(file==null)
			return;
		String map = roadmap.GetRoadMap(pharmacy);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(map);
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

}
