package framelisteners.telmed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import Clients.DatabaseClient;

public class CheckProfitByAgent implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String agent = JOptionPane.showInputDialog(null, "Agent Name");
		DatabaseClient client = new DatabaseClient(true);
		JOptionPane.showMessageDialog(null, client.CheckProfitForAgent(agent));
		client.close();
	}
}
