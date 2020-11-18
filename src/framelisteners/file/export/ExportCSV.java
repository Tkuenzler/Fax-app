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
public class ExportCSV implements ActionListener {
	boolean multiple = false;
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int confirm = JOptionPane.showConfirmDialog(null, "Do you want to export a row for each product?","Export with Multiple Rows",JOptionPane.YES_NO_OPTION);
		if(confirm==JOptionPane.YES_OPTION)
			multiple = true;
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
				if(multiple)
					bw.write(",PRODUCT");
				bw.newLine();
				for(int row = 0;row< CSVFrame.table.getRowCount();row++) {
					Record record = CSVFrame.model.getRowAt(row);
					if(multiple) { 
						if(record.getProducts().length>0) {
							for(String product: record.getProducts()) {
								AddRow(bw,product,row);
							}
						}
						else {
							AddRow(bw,"",row);
						}
					}
					else
						AddRow(bw,"",row);
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
	private void AddRow(BufferedWriter bw,String product,int row) throws IOException {
		for(int column = 0;column<MyTableModel.COLUMN_HEADERS.length;column++) {
			String value = (String)CSVFrame.table.getValueAt(row, column);
			if(value==null)
				value = "";
			value = WordUtils.capitalize(value.trim());
			if(column<MyTableModel.COLUMN_HEADERS.length-1)
				bw.write(value+",");
			else
				bw.write(value);
		}
		if(multiple)
			bw.write(","+product);
		bw.newLine();
	}
}