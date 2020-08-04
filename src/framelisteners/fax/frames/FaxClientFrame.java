package framelisteners.fax.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.mysql.jdbc.CommunicationsException;
import Clients.DatabaseClient;
import Clients.InfoDatabase;
import Clients.RingCentralClient;
import Fax.Drug;
import Fax.MessageStatus;
import Fax.ProductScripts;
import Info.FaxNumber;
import Log.Logger;
import PBM.InsuranceFilter;
import PBM.InsuranceType;
import PivotTable.LoadData;
import Log.Log;
import images.Script;
import images.Script.ScriptException;
import objects.Fax;
import source.CSVFrame;
import table.Record;

public class FaxClientFrame extends JFrame {
	Fax fax;
	RingCentralClient client;
	Vector<Record> data;
	int finalStatus, max;
	JProgressBar bar;
	String database, table;
	DatabaseClient db;
	int updateDb,recordToGetStatus;
	Timer timer;
	Script script;
	boolean singleProductFax;
	String product,statusColumn,dateColumn;
	LoadData insuranceData;
	Logger log = new Logger(Log.FaxLog);
	public FaxClientFrame(Fax fax,Vector<Record> data, FaxNumber faxNumber,Script script,boolean isSingleScript) throws IOException, JSONException {
		super("Faxing");
		this.script = script;
		this.fax = fax;
		client = new RingCentralClient(faxNumber);
		if (!client.login()) {
			JOptionPane.showMessageDialog(null, "Login Failed");
			return;
		}
		this.data = data;
		this.max = this.data.size();
		setUp();
		if(fax.getPharmacy().equalsIgnoreCase(fax.getLiveScript())) {
			insuranceData = new LoadData();
			insuranceData.GetData(insuranceData.getList());
		}
		if(isSingleScript) {
			insuranceData = new LoadData();
			insuranceData.GetData(insuranceData.getList());
			String[] products = ProductScripts.ALL;
			product = (String) JOptionPane.showInputDialog(new JFrame(), "Select which product", "Products:", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);
			if(product==null)
				return;
			db = new DatabaseClient(true);
			database = db.getDatabaseName();
			db.close();
			statusColumn = ProductScripts.GetProductMessageStatus(product);
			dateColumn = ProductScripts.GetProductFaxDate(product);
			singleProductFax = true;
			new ClearMessageStatus().start();
		}
		else {
			updateDb = JOptionPane.showConfirmDialog(null, "Do you want to update the database?", "Update",
					JOptionPane.YES_NO_OPTION);
			if (updateDb == JOptionPane.YES_OPTION) {
				db = new DatabaseClient(false);
				table = db.getTableName();
				database = db.getDatabaseName();
				db.close();
				new ClearMessageStatus().start();
			}
			else
				new CheckDNF().start();
		}		
	}
	private void setUp() {
		bar = new JProgressBar(0, max);
		bar.setStringPainted(true);
		setBounds(50, 50, 300, 75);
		Container pane = getContentPane();
		pane.add(bar, BorderLayout.NORTH);
		setResizable(false);
		setVisible(true);
	}
	private class ClearMessageStatus extends Thread {
		public ClearMessageStatus() {
			System.out.println("CLEAR MESSAGE STATUS THREAD STARTED");
		}
		@Override
		public void run() {
			DatabaseClient dbClient = new DatabaseClient(database,table);
			for(Record record: data) {
				if(singleProductFax)
					dbClient.ClearProdutMessageStatus(statusColumn, record);
				else
					dbClient.clearMessageStatus(record);
			}
			dbClient.close();
			System.out.println("CLEAR MESSAGE STATUS THREAD COMPLETE");
			new CheckDNF().start();
		}
	}
	private class CheckDNF extends Thread {
		public CheckDNF() {
			System.out.println("CHECK DNF THREAD STARTED");
		}
		@Override
		public void run() {
			InfoDatabase db = new InfoDatabase();
			if (db.isClosed()) {
				new SendFaxes().start();
				return;
			}
			for (Record record : data) {
				if (db.IsInDNF(record.getDrFax())) {
					record.setMessageStatus(MessageStatus.DNF);
					incrementProgressBar();
					recordToGetStatus++;
				}
				else if(record.getState().equalsIgnoreCase("OR")) {
					record.setMessageStatus(MessageStatus.ON_HOLD);
					incrementProgressBar();
					recordToGetStatus++;
				}
			}
			db.close();
			System.out.println("CHECK DNF THREAD COMPLETE");
			new SendFaxes().start();
		}
	}

	private class SendFaxes extends Thread {
		public SendFaxes() {

		}
		@Override
		public void run() {
			for (int i = 0; i < data.size(); i++) {
				Record record = data.get(i);
				record.row = i;
				String response = null;
				try {
					if (!record.isSameColor(Color.WHITE))
						continue;
					if ((record.getDrFax().length() != 10) || record.getDrFax().startsWith("0") || record.getDrFax().startsWith("1")) {
						record.setMessageStatus(MessageStatus.BAD_FAX_NUMBER);
						incrementProgressBar();
						recordToGetStatus++;
						continue;
					}
					SetScript(record);
					response = client.SendFax(record,script);
					switch (response) {
						case RingCentralClient.Errors.INVALID_URL:
						case RingCentralClient.Errors.SERVICE_UNAVAILABLE:
						case RingCentralClient.Errors.TOO_MANY_REQUEST:
						case RingCentralClient.Errors.UNKNOWN_ERROR:
							record.setMessageStatus(response);
							recordToGetStatus++;
							continue;
					default:
						parseJSON(response, record);
						break;
					}
					fallAsleep(6000, this);
				} catch (ScriptException e) {
					// TODO Auto-generated catch block
					record.setMessageStatus(e.getMessage());
					recordToGetStatus++;
					incrementProgressBar();
					e.printStackTrace();
					log.log("Script Exception", e.getMessage());
				} catch(FileNotFoundException e) {
					record.setMessageStatus("FILE NOT FOUND");
					recordToGetStatus++;
					record.setRowColor(Color.GRAY);
					e.printStackTrace();
					log.log("FileNotFoundException", e.getMessage());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					record.setMessageStatus("JSON ERROR");
					record.setRowColor(Color.GRAY);
					e.printStackTrace();
					recordToGetStatus++;
					log.log("JSONException", e.getMessage());
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					record.setMessageStatus("CONNECTION ERROR");
					record.setRowColor(Color.GRAY);
					ConnectionDisruptedFrame frame = new ConnectionDisruptedFrame();
					while (!frame.isConnected()) {
						fallAsleep(3000, this);
					}
					frame.close();
					recordToGetStatus++;
					log.log("IOException", e.getMessage());
				}  catch(Exception e) {
					e.printStackTrace();
					record.setMessageStatus(e.getMessage());
					record.setRowColor(Color.GRAY);
					log.log("Exception", e.getMessage());
					recordToGetStatus++;
				} 
			}
			fallAsleep(1000, this);
			script.close();
			System.out.println("SEND FAXES THREAD COMPLETE");
			new GetMessages().start();
		}
	}
	private class GetMessages extends Thread {
		@Override
		public void run() {
			System.out.println("GetMessage Thread Opened");
			while (recordToGetStatus < data.size()) {
				loop: for (int i = 0; i < data.size(); i++) {
					Record record = data.get(i);
					try {				
						if (!record.isSameColor(Color.YELLOW))
							continue;
						String response = client.GetMessageById(record.getMessageId());
						switch (response) {
							case RingCentralClient.Errors.INVALID_URL:
							case RingCentralClient.Errors.SERVICE_UNAVAILABLE:
							case RingCentralClient.Errors.TOO_MANY_REQUEST:
							case RingCentralClient.Errors.UNKNOWN_ERROR:
								if (record.getMessageStatus().equalsIgnoreCase(MessageStatus.BLANK)) {
									recordToGetStatus++;
									record.setMessageStatus(response);
								}
								continue;
						default:
							parseJSON(response, record);
							if (!isQueued(record)) {
								System.out.println(record.getMessageStatus());
								recordToGetStatus++;
								incrementProgressBar();
							}
							break;
						}
						fallAsleep(1000, this);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						log.log("IOException", e.getMessage());
						ConnectionDisruptedFrame frame = new ConnectionDisruptedFrame();
						while (!frame.isConnected()) {
							fallAsleep(3000, this);
						}
						frame.close();
						break loop;
					} catch (JSONException e) {
						log.log("JSONException", e.getMessage());
						// TODO Auto-generated catch block
						e.printStackTrace();
						break loop;
					} catch(Exception e) {
						log.log("Exception", e.getMessage());
						e.printStackTrace();
						record.setMessageStatus(e.getMessage());
						record.setRowColor(Color.GRAY);
						break loop;
					}
				}
				fallAsleep(2000, this);
			}
			if (updateDb == JOptionPane.NO_OPTION)
				dispose();
			else if (updateDb == JOptionPane.YES_OPTION)
				new UpdateMessageStatus().start();
			System.out.println("GET MESSAGE STATUS THREAD COMPLETE");
		}
	}
	private class UpdateMessageStatus extends Thread {
		private UpdateMessageStatus() {
			System.out.println("UPDATE MESSAGE STATUS THREAD STARTED");
		}
		@Override
		public void run() {
			DatabaseClient dbClient = new DatabaseClient(database,table);
			for(int i = 0;i<data.size();i++) {
				Record record = data.get(i);
				if(MessageStatus.IsMessageStatus(record.getMessageStatus())) {
					if(singleProductFax)
						System.out.println("UPDATED MESSAGE STATUS: "+dbClient.UpdateProductMessageStatus(statusColumn, dateColumn, record));
					else
						System.out.println("UPDATED MESSAGE STATUS: "+dbClient.updateMessageStatus(record));
				}
			}
			dbClient.close();
			System.out.println("UPDATE MESSAGE STATUS THREAD COMPLETE");
			dispose(); 
		}
	}
	private void fallAsleep(int wait, Thread thead) {
		try {
			Thread.sleep(wait);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void incrementProgressBar() {
		finalStatus++;
		new Thread() {
			@Override
			public void run() {
				bar.setValue(finalStatus);
			}
		}.start();
	}
	private void parseJSON(String json, Record record) throws JSONException, CommunicationsException {
		JSONObject obj = new JSONObject(json);
		JSONArray to = obj.getJSONArray("to");
		String messageId = String.valueOf(obj.get("id"));
		String status = to.getJSONObject(0).getString("messageStatus");
		record.setMessageStatus(status);
		record.setMessageId(messageId);
	}

	private boolean isQueued(Record record) {
		return record.getMessageStatus().equalsIgnoreCase(MessageStatus.QUEUED);
	}
	private Drug[] SwapManufactorersRecommendation(Drug[] drugs) {
		Drug[] newDrugs = new Drug[drugs.length];
		int count = 0;
		for(Drug drug: drugs) {
			if(drug==null)
				continue;
			if(drug.IsSameDrug(Drug.Diflorasone360)) {
				newDrugs[count] = Drug.Diflorasone180;
				count++;
			}
			else if(drug.IsSameDrug(Drug.Desoximetasone360)) {
				newDrugs[count] = Drug.Desoximetasone120;
				count++;
			}
			else if(drug.IsSameDrug(Drug.Calcipotrene360)) {
				newDrugs[count] = Drug.Calcipotrene240;
				count++;
			}
			else if(drug.IsSameDrug(Drug.Lidocaine300)) {
				newDrugs[count] = Drug.Lidocaine250;
				count++;
			}
			else if(drug.IsSameDrug(Drug.Clobetasol360)) {
				newDrugs[count] = Drug.Clobetasol180;
				count++;
			}
			else if(drug.IsSameDrug(Drug.LidoPrilo360)) {
				newDrugs[count] = Drug.LidoPrilo240;
				count++;
			}
			else {
				newDrugs[count] = drug;
				count++;
			}
		}
		return newDrugs;
	}
	private Drug[] SwapToHigher(Drug[] drugs) {
		Drug[] newDrugs = new Drug[drugs.length];
		int count = 0;
		for(Drug drug: drugs) {
			if(drug==null)
				continue;
			if(drug.IsSameDrug(Drug.Diflorasone360)) {
				newDrugs[count] = Drug.Diflorasone360;
				count++;
			}
			else if(drug.IsSameDrug(Drug.Desoximetasone360)) {
				newDrugs[count] = Drug.Desoximetasone360;
				count++;
			}
			else if(drug.IsSameDrug(Drug.Calcipotrene360)) {
				newDrugs[count] = Drug.Calcipotrene360;
				count++;
			}
			else if(drug.IsSameDrug(Drug.Lidocaine300)) {
				newDrugs[count] = Drug.Lidocaine300;
				count++;
			}
			else if(drug.IsSameDrug(Drug.Clobetasol360)) {
				newDrugs[count] = Drug.Clobetasol360;
				count++;
			}
			else if(drug.IsSameDrug(Drug.LidoPrilo360)) {
				newDrugs[count] = Drug.LidoPrilo360;
				count++;
			} else {
				newDrugs[count] = drug;
				count++;
			}
		}
		return newDrugs;
	}
	private static Drug[] SwapDrugs(Record record,Drug[] drugs) {
		switch(record.getBin()) {
			case "610502":
			case "004336":
			case "020115":
			case "610591":
			case "020099":
			case "610239":
				break;
			default:
				return drugs;
		}
		Drug[] newDrugs = new Drug[drugs.length];
		int count = 0;
		for(Drug drug: drugs) {
			if(drug!=null) {
				if(drug.IsSameDrug(Drug.Chlorzoxazone250)) {
					newDrugs[count] = Drug.Chlorzoxazone375;
					count++;
				}
				else {
					newDrugs[count] = drug;
					count++;
				}
	
			}
		}
		return newDrugs;
	}
	private void SetScript(Record record) throws InvalidPasswordException, IOException, ScriptException, JSONException {
		//DME
		if(fax.getPharmacy().equalsIgnoreCase(fax.getDMEScript())) {
			script.PopulateDME(record);
		}
		//Single Product Script
		else if(fax.getPharmacy().equalsIgnoreCase(fax.getSingleProductScript())) {
			script.setCategory(product);
			Drug drug = null;
			switch(product) {
				case ProductScripts.TOPICAL_SCRIPT:
					drug = Drug.GetTopicalScript(record);
					break;
				case ProductScripts.ORAL_SCRIPT:
					drug = Drug.GetOralScript(record);
					break;
				case ProductScripts.ANTI_FUNGAL_SCRIPT:
					script.LoadNewScript(fax.getAntiFungalScript());
					script.PopulateScript(record);
					break;
				case ProductScripts.CONSTIPATION_SCRIPT:
					drug = Drug.Lactulose;
					break;
			}
			script.SetDrug(drug);
			script.PopulateScript(record);
		}
		//PBM
		else if(fax.getPharmacy().equalsIgnoreCase(fax.getPbmScript())) {
			script.LoadNewScript(GetPBMScript(fax,record));
			script.PopulateScript(record);
		}
		//Anti fungal
		else if(fax.getPharmacy().equalsIgnoreCase(fax.getAntiFungalScript())) {
			script.LoadNewScript(fax.getAntiFungalScript());
			script.PopulateScript(record);
		}
		//Live Script
		else if(fax.getPharmacy().equalsIgnoreCase(fax.getLiveScript())) {
			int type = InsuranceFilter.GetInsuranceType(record);
			if(type==InsuranceType.Type.PRIVATE_INSURANCE) {
				script.LoadNewScript(fax.getDrChaseScript());
				script.PopulateScript(record);
				return;
			}
			else  {
				switch(record.getBin()) {
					case "004336":
					case "610502":
					case "610239":
					case "610591":
					case "020115":
					case "015581":
					case "015599":
					case "610097":
						script.LoadNewScript(GetPBMScript(fax,record));
						script.PopulateScript(record);
						return;
				}
				LoadData data = new LoadData();
				data.GetData(LoadData.LAKE_IDA_LIST);
				Drug[] drugs = data.GetDrugs(record);
				if(drugs!=null) {
					script.LoadNewScript(fax.getLiveScript());
					script.SetDrugs(drugs);
					script.PopulateScript(record);
					return;
				}
				else {
					script.LoadNewScript(fax.getDrChaseScript());
					script.PopulateScript(record);
					return;
				}
			}
		}
		else  {
			script.LoadNewScript(fax.getPharmacy());
			script.PopulateScript(record);
		}
	}
	
	private String GetPBMScript(Fax fax,Record record) {
		String folder = fax.getPbmScript();
		if(record.getBin()==null)
			return folder+"\\"+PBMScript.CASCADE;
		switch(record.getBin()) {
			case "004336":
			case "610239":
			case "610591":
				return folder+"\\"+PBMScript.CAREMARK;
			case "610502":
				return folder+"\\"+PBMScript.AETNA;
			case "020099":
			case "020115":
				return folder+"\\"+PBMScript.INGENIO_RX;
			case "015581":
			case "015599":
			case "610649":
				return folder+"\\"+PBMScript.HUMANA;
			case "610097":
				return folder+"\\"+PBMScript.OPTUM_RX;
			default:
				return null;
		}															
	}
	private class PBMScript {
		public static final String HUMANA = "Humana.pdf";
		public static final String CAREMARK = "Caremark.pdf";
		public static final String AETNA = "Aetna.pdf";
		public static final String INGENIO_RX = "IngenioRx.pdf";
		public static final String OPTUM_RX = "OptumRx.pdf";
		public static final String CASCADE = "Cascade.pdf";
	}
	public class ConnectionDisruptedFrame extends JFrame {
		public ConnectionDisruptedFrame() {
			setup();
		}

		private void setup() {
			setUndecorated(true);
			getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.BLACK));
			URL imageUrl = this.getClass().getClassLoader().getResource("images/loader.gif");
			ImageIcon loading = new ImageIcon(imageUrl);
			add(new JLabel("Reconnecting... ", loading, JLabel.CENTER));
			setBounds(100, 100, 400, 250);
			setResizable(false);
			setVisible(true);

		}

		private boolean isConnected() {
			if (client.checkConnection())
				return true;
			else
				return false;
		}

		private void close() {
			this.dispose();
		}
		
	}
}
