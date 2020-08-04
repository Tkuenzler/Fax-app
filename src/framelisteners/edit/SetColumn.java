package framelisteners.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import source.CSVFrame;
import table.MyTableModel;
import table.Record;

public class SetColumn implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		MyTableModel model = CSVFrame.model;
		String[] columns = MyTableModel.COLUMN_HEADERS;
		String column = (String) JOptionPane.showInputDialog(new JFrame(), "Select a table", "tables:", JOptionPane.QUESTION_MESSAGE, null, columns, columns[0]);
		if(column==null)
			return;
		String value = JOptionPane.showInputDialog("Set "+column+" as:");
		if(value==null)
			return;
		int columnNumber = model.getColumn(column);
		for(int i = 0;i<CSVFrame.model.getRowCount();i++) {
			model.updateValue(value, i, columnNumber);
		}
	}
}
