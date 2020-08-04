package framelisteners.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import table.MyTableModel;

public class CellsEditable implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(MyTableModel.cellsEditable) 
			MyTableModel.cellsEditable = false;
		else
			MyTableModel.cellsEditable = true;
		
	}

}
