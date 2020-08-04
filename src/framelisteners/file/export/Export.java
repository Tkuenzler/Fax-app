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

import com.sun.xml.internal.ws.util.StringUtils;

import source.CSVFrame;
import subframes.FileChooser;
import table.MyTableModel;
public class Export implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		File file = FileChooser.SaveCsvFile();
		if(file==null)
			return;
		BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(file));
				for(int i = 0;i<MyTableModel.COLUMN_HEADERS.length;i++) {
					if(i<MyTableModel.COLUMN_HEADERS.length-1)
						bw.write(MyTableModel.COLUMN_HEADERS[i]+",");
					else
						bw.write(MyTableModel.COLUMN_HEADERS[i]+"");
				}
				bw.newLine();
				for(int row = 0;row< CSVFrame.table.getRowCount();row++) {
					for(int column = 0;column<MyTableModel.COLUMN_HEADERS.length;column++) {
						String value = (String)CSVFrame.table.getValueAt(row, column);
						if(value==null)
							value = "";
						value = WordUtils.capitalize(StringUtils.decapitalize(value));
						if(column<MyTableModel.COLUMN_HEADERS.length-1)
							bw.write(value+",");
						else
							bw.write(value);
					}
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