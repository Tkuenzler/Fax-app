package framelisteners.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import source.CSVFrame;
import subframes.FileChooser;
import table.MyTableModel;
import table.Record;

public class Supression implements ActionListener {
	private List<String> supressionList;
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int rowsBefore = CSVFrame.model.getRowCount();
		supressionList = new ArrayList<String>() {
			@Override
			public boolean contains(Object o) {
				String compare = o.toString();
				for(String s: this){
					if(s.equalsIgnoreCase(compare))
						return true;
				}
				return false;
			}
		};
		File supressionFile = FileChooser.OpenCsvFile("Load Supression List");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(supressionFile));
			String line = null;
			while((line=br.readLine())!=null) {
				supressionList.add(line);
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
		ArrayList<Record> records = new ArrayList<Record>();
		for(Record record: CSVFrame.model.data) {
			if(supressionList.contains(record.getPhone())) {
				System.out.println("DUPE FOUND");
				record.printRecord();
			}
			else 
				records.add(record);		
		}
		CSVFrame.model.deleteAllRows();
		for(Record record: records) {
			CSVFrame.model.addRow(record);
		}
		int keep = records.size();
		JOptionPane.showMessageDialog(null, (rowsBefore-keep)+" rows removed");
	}
}
