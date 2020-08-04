package framelisteners.NotFound;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Clients.DatabaseClient;
import Clients.RoadMapClient;

public class LoadInvalidInfo implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(false);
		client.LoadNotFoundWrongInfo();
		client.close();
	}
}
