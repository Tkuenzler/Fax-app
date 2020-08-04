package framelisteners.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import source.CSVFrame;

public class RowCounter implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JOptionPane.showMessageDialog(new JFrame(), "There are "+CSVFrame.table.getRowCount()+" rows");
		
	}
}