package framelisteners.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import source.CSVFrame;
import table.MyTableModel;

public class ClearInsurance implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int confirm = JOptionPane.showConfirmDialog(null, "ARE YOU SURE YOU WANT TO CLEAR INSURANCE",null, JOptionPane.YES_NO_OPTION);
		if(confirm!=JOptionPane.YES_OPTION)
			return;
		for(int i = 0;i<CSVFrame.model.getRowCount();i++) {
			CSVFrame.model.updateValue("", i, MyTableModel.STATUS);
			CSVFrame.model.updateValue("", i, MyTableModel.TYPE);
			CSVFrame.model.updateValue("", i, MyTableModel.CARRIER);
			CSVFrame.model.updateValue("", i, MyTableModel.POLICY_ID);
			CSVFrame.model.updateValue("", i, MyTableModel.BIN);
			CSVFrame.model.updateValue("", i, MyTableModel.GROUP);
			CSVFrame.model.updateValue("", i, MyTableModel.PCN);
			
		}
	}

}
