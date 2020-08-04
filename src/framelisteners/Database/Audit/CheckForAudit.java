package framelisteners.Database.Audit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import Clients.InfoDatabase;

public class CheckForAudit implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		InfoDatabase info = null;
		String phone = null;
		do {
			phone = JOptionPane.showInputDialog("Phonenumber?");
			if(phone==null)
				return;
			info = new InfoDatabase();
			if(info.CheckIfAudited(phone))
				JOptionPane.showMessageDialog(null, "PATIENT HAS BEEN AUDITED!!!!!");
			else
				JOptionPane.showMessageDialog(null, "PATIENT IS GOOD TO SUBMIT");
		} while(phone!=null);
	}

}
