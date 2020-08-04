package framelisteners.telmed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import Clients.DatabaseClient;

public class CheckTotalProfit implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(true);
		JOptionPane.showMessageDialog(null, client.TotalProfit());
		client.close();
	}
}
