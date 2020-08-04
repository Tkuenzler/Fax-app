package framelisteners.Database.DoctorChase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Clients.DatabaseClient;

public class LoadNoAnswerDoctor implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		DatabaseClient client = new DatabaseClient(true);
		if(client.getDatabaseName()==null)		
			return;
		client.LoadNoAnswer();
		client.close();
	}

}
