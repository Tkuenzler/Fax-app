package framelisteners.edit.Doctor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import source.CSVFrame;
import table.Record;

public class CheckDrFax implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		for(Record record: CSVFrame.model.data) {
			if(record.getDrFax().equalsIgnoreCase(record.getDrPhone()) ||
				record.getDrFax().length()!=10)
				record.setRowColor(Color.MAGENTA);
		}
	}
}
