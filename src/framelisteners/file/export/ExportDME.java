package framelisteners.file.export;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.text.WordUtils;

import source.CSVFrame;
import subframes.FileChooser;
import table.MyTableModel;
import table.Record;
public class ExportDME implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		File file = FileChooser.SaveCsvFile();
		if(file==null)
			return;
		BufferedWriter bw = null;
			try {
				
				bw = new BufferedWriter(new FileWriter(file));
				for(int row = 0;row< CSVFrame.table.getRowCount();row++) {
					Record record = CSVFrame.model.getRowAt(row);
					String name = record.getFirstName()+" "+record.getLastName();
					String dr_name = record.getDrFirst()+" "+record.getDrLast();
					bw.append(",,,,"+name+","+record.getDob()+","+record.getPhone()+",,"+dr_name+","+record.getAddress()+","+record.getCity()+","+record.getState()+","+record.getZip()+",,,"+record.getCarrier()+","+record.getPolicyId());		
					bw.newLine();
				}
			} catch(IOException e) {
				JOptionPane.showMessageDialog(new JFrame(),e.getMessage());
			}
			finally {
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(null,"Sucessfully Saved");
	}
}