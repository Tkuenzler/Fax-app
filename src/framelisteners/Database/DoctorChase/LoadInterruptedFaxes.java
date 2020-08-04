package framelisteners.Database.DoctorChase;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Clients.DatabaseClient;
import Clients.RingCentralClient;
import Clients.RoadMapClient;
import Fax.MessageStatus;
import Info.FaxNumber;
import source.CSVFrame;
import source.LoadInfo;
import table.Record;

public class LoadInterruptedFaxes implements ActionListener {
	int MESSAGE_RECIEVED = 0;
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		DatabaseClient dbClient = new DatabaseClient(false);
		RoadMapClient map = new RoadMapClient();
		String pharmacy = map.getPharmacy();
		map.close();
		ResultSet set = dbClient.loadInterupptedFaxes(pharmacy);
		try {
			while(set.next()) {
				CSVFrame.model.addRow(new Record(set,dbClient.getDatabaseName(),dbClient.getTableName()));
			}
			set.close();
			dbClient.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new LoadInterruptedRingCentralFaxes().start();
	}
	private class LoadInterruptedRingCentralFaxes extends Thread {
		@Override
		public void run() {
			 HashMap<String,FaxNumber> faxNumbers;
			 try {
					faxNumbers = LoadInfo.LoadFaxNumbers();
			} catch (URISyntaxException | JSONException | IOException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e1.getMessage());
					return;
			}
			String[] LINES = faxNumbers.keySet().toArray(new String[faxNumbers.keySet().size()]);
			String line = (String) JOptionPane.showInputDialog(new JFrame(), "Select a fax line", "Fax Lines::", JOptionPane.QUESTION_MESSAGE, null, LINES, LINES[0]);
			if(line==null)
				return;
			FaxNumber fax = faxNumbers.get(line);
			RingCentralClient client = new RingCentralClient(fax);
			if(!client.login())
				return;
			while(MESSAGE_RECIEVED<CSVFrame.model.getRowCount()) {
				for(Record record: CSVFrame.model.data) {
					if(!record.isSameColor(Color.GREEN) || record.isSameColor(Color.RED)) {
						try {
							String json = client.GetMessageById(record.getMessageId());
							parseJSON(json,record);
							this.sleep(1000);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						switch(record.getMessageStatus()) {
							case MessageStatus.QUEUED:
								break;
							default:
								updateRecord(record);
								MESSAGE_RECIEVED++;
						}
					}
					
				}
			}
			JOptionPane.showMessageDialog(null, "COMPLETED");
		}
	}
	private boolean updateRecord(Record record) {
		DatabaseClient db = new DatabaseClient(record.getDatabase(),record.getTable());
		int update = db.updateMessageStatus(record);
		db.close();
		if(update==1)
			return true;
		else
			return false;
	}
	private void parseJSON(String json, Record record) {
		record.printRecord();
		System.out.println(json);
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray to = obj.getJSONArray("to");
			String messageId = String.valueOf(obj.get("id"));
			String status = to.getJSONObject(0).getString("messageStatus");
			record.setMessageStatus(status);
			record.setMessageId(messageId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
