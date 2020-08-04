package framelisteners.Database.Delete;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import Clients.InfoDatabase;

public class DeleteChecked implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String phone = JOptionPane.showInputDialog("What phone number").trim().replace("[^A-Za-z0-9]", "");
		if(phone==null)
			return;
		InfoDatabase client = new InfoDatabase();
		int delete = client.DeleteCheckedRecord(phone);
		JOptionPane.showMessageDialog(null, "Deleted "+delete+" records");
		client.close();
	}

}
