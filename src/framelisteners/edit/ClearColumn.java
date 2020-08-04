package framelisteners.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import source.CSVFrame;
import table.MyTableModel;

public class ClearColumn implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String column = (String)JOptionPane.showInputDialog(null, "What column do you want to clear?", "Clear Column", JOptionPane.QUESTION_MESSAGE, null, MyTableModel.COLUMN_HEADERS, MyTableModel.COLUMN_HEADERS[0]);
		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear "+column, "Clear Column", JOptionPane.YES_OPTION);
		if(confirm!=JOptionPane.YES_OPTION)
			return;
		for(int i = 0;i<CSVFrame.model.getRowCount();i++) {
			CSVFrame.model.updateValue("", i, CSVFrame.model.getColumn(column));
		}
	}
}
