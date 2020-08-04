package framelisteners.Database;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import Clients.DatabaseClient;

public class RowCount implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(false);
		if(client.getTableName()==null) {
			client.close();
			return;
		}
		int rows = client.rowCount();
		client.close();
		JOptionPane.showMessageDialog(null, "THERE ARE A TOTAL OF "+rows+" ROWS");
	}
}
