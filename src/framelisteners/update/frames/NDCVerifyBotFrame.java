package framelisteners.update.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import Clients.InfoDatabase;
import Clients.NDCVerifyClient;
import Info.NDCVerify;
import objects.Insurance;
import source.CSVFrame;
import source.LoadInfo;
import table.MyTableModel;
import table.Record;

@SuppressWarnings("serial")
public class NDCVerifyBotFrame extends JFrame {
	
	MyTableModel model = CSVFrame.model;
	JProgressBar progressBar;
	NDCVerifyClient client;
	int count = 0;
	public NDCVerifyBotFrame() {
		if(!login()) {
			JOptionPane.showMessageDialog(null, "Login Failed");
			return;
		}
		this.setBounds(50, 50, 300, 75);
		Container content = this.getContentPane();
		progressBar = new JProgressBar(0,CSVFrame.model.getRowCount());
	    progressBar.setStringPainted(true);
	    progressBar.setValue(0);
		Border border = BorderFactory.createTitledBorder("Loading...");
		progressBar.setBorder(border);
		content.add(progressBar, BorderLayout.NORTH);
		this.setVisible(true);
		CheckIfAudited();
		LoadInsurance();
	}
	private boolean login() {
		client = new NDCVerifyClient("mikekuenzler88@gmail.com","Mtkmarketing321!");
		client.setNpi("1700128972");
		//NDCVerify verify = LoadInfo.LoadNDCVerify();
		//client = new NDCVerifyClient(verify);
		try {
			if(client.login())
				return true;
			else
				return true;
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	private void CheckIfAudited() {
		InfoDatabase db = new InfoDatabase();
		for(Record record: CSVFrame.model.data) {
			if(db.IsAudtied(record.getPhone())) {
				record.setStatus("PATIENT HAS BEEN AUDITED DELETE!!!!");
				record.setRowColor(Color.BLACK);
				CSVFrame.model.fireTableDataChanged();
			}
		}
		db.close();
	}
	private void LoadInsurance() {
		for(Record record: CSVFrame.model.data) {
			if(!record.getStatus().equalsIgnoreCase("")) {
				count++;
				continue;
			}
			try {
				client.CardVerify(record,"1700128972");
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				record.setStatus(e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				record.setStatus(e.getMessage());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				record.setStatus(e.getMessage());
			}
			count++;
			incrementCounter();
			CSVFrame.model.fireTableDataChanged();
		}
		this.dispose();
	}
	private void incrementCounter() {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
         	   progressBar.setValue(count);
            }
     });
	}
}