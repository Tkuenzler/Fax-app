package framelisteners.Database.Audit;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Clients.InfoDatabase;
import source.CSVFrame;
import table.Record;

public class CheckAllAudit implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		InfoDatabase info = new InfoDatabase();
		for(Record record: CSVFrame.model.data) {
			if(info.CheckIfAudited(record.getPhone())) {
				record.setStatus("PATIENT HAS BEEN AUDITEDD!!!!!");
				record.setRowColor(Color.BLACK);
			}
			else 
				record.setRowColor(Color.GREEN);	
		}
		info.close();
	}
}
