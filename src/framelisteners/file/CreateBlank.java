package framelisteners.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import source.CSVFrame;
import table.Record;

public class CreateBlank implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		int rows = 0;
		try {
			 rows = Integer.parseInt(JOptionPane.showInputDialog(null, "How many rows?"));
		} catch(NumberFormatException ne) {
			JOptionPane.showMessageDialog(null, "Not a valid number");
			return;
		}
		for(int i = 0;i<rows;i++)
			CSVFrame.model.addRow(new Record());
	}

}
