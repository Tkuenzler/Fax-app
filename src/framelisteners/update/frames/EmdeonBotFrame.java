package framelisteners.update.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import Clients.EmdeonClient;
import Clients.InfoDatabase;
import Fax.EmdeonStatus;
import PBM.InsuranceFilter;
import Properties.EmdeonProperties;
import objects.Emdeon;
import objects.Insurance;
import objects.InsuranceInfo;
import source.CSVFrame;
import table.MyTableModel;
import table.Record;

@SuppressWarnings("serial")
public class EmdeonBotFrame extends JFrame {
	
	MyTableModel model = CSVFrame.model;
	JProgressBar progressBar;
	EmdeonProperties properties;
	Emdeon emd;
	int count = 0;
	public EmdeonBotFrame() {
		this.setBounds(50, 50, 300, 75);
		Container content = this.getContentPane();
		progressBar = new JProgressBar(0,CSVFrame.model.getRowCount());
	    progressBar.setStringPainted(true);
	    progressBar.setValue(0);
		Border border = BorderFactory.createTitledBorder("Loading...");
		progressBar.setBorder(border);
		content.add(progressBar, BorderLayout.NORTH);
		this.setVisible(true);
		properties = new EmdeonProperties();
		emd = properties.getEmdeonProperties();
		CheckIfAudited();
		int LoadChecked = JOptionPane.showConfirmDialog(null, "Do you want to load insurance from Checked Database","Checked Records",JOptionPane.YES_NO_OPTION);
		if(LoadChecked==JOptionPane.YES_OPTION)
			loadCheckedInsuranceInfo();
		loadInsuranceInfo();
	}
	private void loadCheckedInsuranceInfo() {
		InfoDatabase info = new InfoDatabase();
		for(Record record: CSVFrame.model.data) {
			info.GetInsuranceInfo(record);
			CSVFrame.model.fireTableDataChanged();
		}
		info.close();
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
	private void loadInsuranceInfo() {
		//updateDOB();
		EmdeonClient emdeon1 = new EmdeonClient();
		if(!emdeon1.login()){ 
			JOptionPane.showMessageDialog(new JFrame(), "Error");
			this.dispose();
			return;
		}
		int counter = 0;
		lookup:
		for(int i = 0;i<model.getRowCount();i++) {
			Record record = CSVFrame.model.getRowAt(i);
			if(!record.getStatus().equalsIgnoreCase("")) {
					continue;
			}
			if(counter==15) {
				emdeon1.logout();
				emdeon1.login();
				System.out.println("========REFRESHED=======");
				counter = 0;
			}
			try {
				loadInsuranceFromEmdeon(emdeon1,record,i);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("NEXT: COUNTER: "+counter);
			counter++;
			count++;
			SwingUtilities.invokeLater(new Runnable() {
	               public void run() {
	            	   progressBar.setValue(count);
	               }
	        });
		}
		emdeon1.close();
		System.out.println(model.data.size());
		this.dispose();
	}
	private void loadInsuranceFromEmdeon(EmdeonClient emdeon,Record record,int i) throws CloneNotSupportedException {
		String status = null;
		String type = null;
		if(record.getSsn().length()==4) {
			status = emdeon.fillOutFormMedicare(record);
			if(status.equalsIgnoreCase("FOUND"))
				type = "Medicare";
			else
				type = "";
			if(status.equalsIgnoreCase(EmdeonStatus.NOT_FOUND)) {
				status = new String(emdeon.fillOutFormPrivate(record));
				if(status.equalsIgnoreCase("FOUND"))
					type = "Commercial";
				else 
					type = "";
			}
		}
		else {
			status = emdeon.fillOutFormPrivate(record);
			if(status.equalsIgnoreCase("FOUND"))
				type = "Commercial";
			else
				type = "";
			
		}
		updateInsurance(record,type,status,i);
	}
	private void updateInsurance(Record record,String type,String status,int i) {
		model.updateValue(status, i, MyTableModel.STATUS);
		model.updateValue(type, i, MyTableModel.TYPE);
		model.updateValue(record.getCarrier(), i, MyTableModel.CARRIER);
		model.updateValue(record.getPolicyId(), i, MyTableModel.POLICY_ID);
		model.updateValue(record.getBin(), i, MyTableModel.BIN);
		model.updateValue(record.getGrp(), i, MyTableModel.GROUP);
		model.updateValue(record.getPcn(), i, MyTableModel.PCN);
		model.updateValue(record.getEmail(), i, MyTableModel.EMAIL);
	}
}