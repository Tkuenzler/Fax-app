package framelisteners.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import source.CSVFrame;
import table.Record;

public class DuplicateRemover implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		ArrayList<Record> keep = new ArrayList<Record>() {
			@Override 
			public boolean contains(Object o) {
				Record r = (Record) o;
				for(Record r2: this) {
					if(r.getPhone().equalsIgnoreCase(r2.getPhone()))
						return true;
				}
				return false;
			}
		};
		int rows = CSVFrame.model.getRowCount();
		for(int i = 0;i<rows;i++) {
			Record record = CSVFrame.model.getRowAt(i);
			double percent = i/rows;
			if(!keep.contains(record)) {
				keep.add(record);
				System.out.println(i+"/"+rows+" :"+percent);
			}
			else
				continue;
		}
		CSVFrame.model.deleteAllRows();
		for(Record r: keep)
			CSVFrame.model.addRow(r);
		JOptionPane.showMessageDialog(null, (rows-keep.size())+" rows deleted");
	}
}