package framelisteners.update;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import Clients.GenderClient;
import source.CSVFrame;
import table.MyTableModel;
import table.Record;

public class GuessGender implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Vector<Record> data = CSVFrame.model.data;
		new Thread() {
			@Override
			public void run() {
				for(int i = 0;i<data.size();i++) {
					String gender = null;
					Record record = CSVFrame.model.getRowAt(i);
					if(record.getGender().length()==0) {
						gender = GenderClient.getGender(record.getFirstName());
						try {
							this.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(gender!=null) {
						record.setGender(gender); 
						CSVFrame.model.updateValue(gender, i, MyTableModel.GENDER);
					}
					
				}
			}
		}.start();
	}

}
