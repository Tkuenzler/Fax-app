package framelisteners.fax.frames;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import Clients.RingCentralClient;
import Fax.MessageStatus;
import Info.FaxNumber;
import source.LoadInfo;
import subframes.FileChooser;

public class FaxApprovalFrame extends JFrame {	
	File[] files = null;
	RingCentralClient client = null;
	JTextArea text = new JTextArea();
	public FaxApprovalFrame() {
		super("Faxing Approvals");
		files = new File(FileChooser.OpenFolder("Open Folder")).listFiles();
		if(!openClient()) {
			JOptionPane.showMessageDialog(null, "Login failed");
			return;
		}
		CreateFrame();
		new FaxFiles().start();
	}
	private class FaxFiles extends Thread {
		@Override
		public void run() {
			for(File file: files) {
				try {
					if(FilenameUtils.getExtension(file.getAbsolutePath()).equalsIgnoreCase("pdf")) {
						String response = client.FaxFile(file, "5029968432");
						String messageId = getMessageId(response);
						System.out.println("MESSAGE ID "+messageId);
						String messageStatus = getMessageStatus(client.GetMessageById(messageId));
						System.out.println(" MESSAGE STATUS "+messageStatus);
						while(messageStatus.equalsIgnoreCase(MessageStatus.QUEUED)) {
							messageStatus = getMessageStatus(client.GetMessageById(messageId));
							System.out.println(messageId+": "+messageStatus);
							sleep(2000);
						}
						text.append(file+" has been "+messageStatus+"\n");
					}
					else {
						text.append(file.getAbsolutePath()+" is not a PDF file "+"\n");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				text.append("COMPLETE");
			}
		}
	}
	private void CreateFrame() {
		this.setSize(300,300);
		this.setVisible(true);
		this.add(text,BorderLayout.CENTER);
	}
	private boolean openClient() {
		HashMap<String,FaxNumber> faxNumbers;
		 try {
				faxNumbers = LoadInfo.LoadFaxNumbers();
		} catch (URISyntaxException | JSONException | IOException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null,e1.getMessage());
				return false;
		}
		String[] LINES = faxNumbers.keySet().toArray(new String[faxNumbers.keySet().size()]);
		String line = (String) JOptionPane.showInputDialog(new JFrame(), "Select a script", "Scripts:", JOptionPane.QUESTION_MESSAGE, null, LINES, LINES[0]);
		if(line==null)
			return false;
		FaxNumber fax = faxNumbers.get(line);
		client = new RingCentralClient(fax);
		return client.login();
	}
	private String getMessageStatus(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray to = obj.getJSONArray("to");
			return to.getJSONObject(0).getString("messageStatus");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private String getMessageId(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return  String.valueOf(obj.get("id"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
