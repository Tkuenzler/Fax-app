package framelisteners.Database;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import Clients.InfoDatabase;

public class GetLookUps implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String afid = JOptionPane.showInputDialog("What AFID?");
		InfoDatabase client = new InfoDatabase();
		client.GetLookUps(afid);
		client.close();
		
	}
	
}
