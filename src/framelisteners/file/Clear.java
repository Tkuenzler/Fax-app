package framelisteners.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import source.CSVFrame;

public class Clear implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear all the rows?","Delete All Rows",JOptionPane.YES_NO_OPTION);
		if(confirm==JOptionPane.NO_OPTION)
			return;
		CSVFrame.model.deleteAllRows();
		CSVFrame.table.removeAll();
	}
}