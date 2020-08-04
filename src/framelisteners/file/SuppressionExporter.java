package framelisteners.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import source.CSVFrame;
import subframes.FileChooser;
import table.MyTableModel;
import table.Record;

public class SuppressionExporter implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		File file = FileChooser.SaveCsvFile();
		if(file==null)
			return;
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			System.out.println(file.getAbsolutePath());
			bw.write("Phonenumber");
			bw.newLine();
			for(int i = 0;i<CSVFrame.model.getRowCount();i++) {
				Record r = CSVFrame.model.getRowAt(i);
				System.out.println(r.getPhone());
				bw.write(r.getPhone());
				bw.newLine();
			}
			JOptionPane.showMessageDialog(null, "Finished creating suppression file");
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		finally {
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
