package framelisteners.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import Clients.InfoDatabase;

public class AddToDNF implements ActionListener  {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub		
		String fax = JOptionPane.showInputDialog("ADD TO DNF: ");
		if(fax.length()!=10) {
			JOptionPane.showMessageDialog(null, "Must be 10 numbrs only");
			return;
		}
		InfoDatabase db = new InfoDatabase();
		if(db.isClosed()) {
			JOptionPane.showMessageDialog(null, "Failed to connect to server");
			return;
		}
		int add = db.addToDNF(fax);
		if(add>0)
			JOptionPane.showMessageDialog(null, "Successfully added");
		else
			JOptionPane.showMessageDialog(null, "Failed to add");
		db.close();
	}
	
}
