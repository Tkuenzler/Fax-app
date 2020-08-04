package framelisteners.edit;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import PBM.InsuranceFilter;
import PBM.InsuranceType;
import source.CSVFrame;
import table.Record;

public class InsuranceChecker implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		for(Record record: CSVFrame.model.data) {
			record.checkInsurance(InsuranceFilter.Filter(record));
		}
	}	

}
