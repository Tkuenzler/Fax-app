package framelisteners.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import source.CSVFrame;
import table.MyTableModel;

public class ClearDoctorInfo implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int confirm = JOptionPane.showConfirmDialog(null, "ARE YOU SURE YOU WANT TO CLEAR INSURANCE",null, JOptionPane.YES_NO_OPTION);
		if(confirm!=JOptionPane.YES_OPTION)
			return;
		for(int i = 0;i<CSVFrame.model.getRowCount();i++) {
			CSVFrame.model.updateValue("", i, MyTableModel.NPI);
			CSVFrame.model.updateValue("", i, MyTableModel.DR_FIRST);
			CSVFrame.model.updateValue("", i, MyTableModel.DR_LAST);
			CSVFrame.model.updateValue("", i, MyTableModel.DR_ADDRESS1);
			CSVFrame.model.updateValue("", i, MyTableModel.DR_CITY);
			CSVFrame.model.updateValue("", i, MyTableModel.DR_STATE);
			CSVFrame.model.updateValue("", i, MyTableModel.DR_ZIP);
			CSVFrame.model.updateValue("", i, MyTableModel.DR_FAX);
			CSVFrame.model.updateValue("", i, MyTableModel.DR_PHONE);
		}
	}

}
