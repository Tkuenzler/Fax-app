package framelisteners.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import source.CSVFrame;
import subframes.FileChooser;
import table.Record;

public class RunSuppresionList implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
			int pretotal = CSVFrame.model.getRowCount();
			Vector<Record> remove = new Vector<Record>();
			File file = FileChooser.OpenTxtFile("Load Supression List");
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(file));
				String line = null;
				while((line = br.readLine())!=null) {
					String lined = line.replace("\"","");
					for(int i = 0;i<CSVFrame.model.getRowCount();i++) {
						Record r = CSVFrame.model.getRowAt(i);
						if(r.getPhone().equalsIgnoreCase(lined)) {
							remove.addElement(r);
						}
					}
				}
			} catch(IOException ex) {
				 ex.printStackTrace();
			}
			CSVFrame.model.deleteAllRows();
			for(Record r: remove) {
				CSVFrame.model.addRow(r);
			}
			
	}
}