package framelisteners.telmed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import Clients.DatabaseClient;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import subframes.FileChooser;

public class UpdatePharmacyStatus implements ActionListener {
	private int PAID_PHONE = 3;
	private int REJECT_PHONE = 1;
	private int REJECT_STATUS = 2;
	private int REVERSAL_PHONE = 3;
	private int REVERSAL_STATUS = 12;
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub	
		String[] pharmacies = {"Bach","Lake Ida","Mark"};
		String pharmacy = (String) JOptionPane.showInputDialog(new JFrame(), "What pharmacy would you like to load data from?", "Pharmacy:", JOptionPane.QUESTION_MESSAGE, null, pharmacies, pharmacies[0]);
		switch(pharmacy) {
		case "Bach":
			UploadBachReport();
			break;
		case "Lake Ida":
			UploadLakeIda2();
			break;
		case "Mark":
		default:
			break;
		}
	}
	private void UploadLakeIda2() {
		File file = FileChooser.OpenXlsFile("Open Bach Report");
		DatabaseClient client = new DatabaseClient(true);
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(file);
			Sheet sheet = workbook.getSheet(0);
			for(int row = 1;row<sheet.getRows();row++) {
				Cell[] data = sheet.getRow(row);
				String dob = data[1].getContents();
				String name = data[0].getContents();
				String notes = data[2].getContents();
				if(name.split(" ").length==0)
					continue;
				String first = name.split(" ")[0];
				String last = name.split(" ")[1];
				if(client.UpdatePharmacyStatus(first, last,dob,notes)!=1)
					System.out.println(name+" "+dob+" "+notes);
			}
		} catch (IOException | BiffException ex) {
			ex.printStackTrace();
		} finally {
			if (workbook != null) workbook.close();
			if(client.connect!=null) client.close();
			System.out.println("CLOSED");
		}	
	}
	private void UploadLakeIdaReport() {
		HashMap<String, Rx> map = new HashMap<String, Rx>();
		File file = FileChooser.OpenCsvFile("Open Lake Ida Report");
		BufferedReader br = null;
		DatabaseClient client = new DatabaseClient(true);
		try {
			br = new BufferedReader(new FileReader(file));
			String header = br.readLine();
			String line = null;
			while((line=br.readLine())!=null) {
				String[] data = line.split(",");
				String number = data[1].replaceAll("-", "");
				double profit = Double.parseDouble(data[0]);
				if(!map.containsKey(number))
					map.put(number, new Rx(number));
				Rx rx = map.get(number);
				rx.addToProfit(profit);
			}
			Iterator<Entry<String, Rx>> it = map.entrySet().iterator();
			while (it.hasNext()) {
			    Map.Entry<String, Rx> pair = (Map.Entry<String, Rx>)it.next();
				String key = pair.getKey();
				Rx rx = map.get(key);
				System.out.println(rx.getNumber()+" "+rx.getProfit());
				System.out.println(client.UpdatePharmacyStatus(rx.getNumber(), "COVERED"));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
				if(client.connect!=null) client.close();
				System.out.println("CLOSED");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	private void UploadBachReport() {
		File file = FileChooser.OpenXlsFile("Open Bach Report");
		DatabaseClient client = new DatabaseClient(true);
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(file);
			Sheet paidSheet = workbook.getSheet(0);
			Sheet rejectedSheet = workbook.getSheet(1);
			Sheet reversalSheet = workbook.getSheet(2);
			UploadPaidStatus(client,paidSheet);
			UploadRejectedStatus(client,rejectedSheet);
			UploadReversalStatus(client,reversalSheet);
		} catch (IOException | BiffException ex) {
			ex.printStackTrace();
		} finally {
			if (workbook != null) workbook.close();
			if(client.connect!=null) client.close();
			System.out.println("CLOSED");
		}	
	}
	private void UploadPaidStatus(DatabaseClient client,Sheet sheet) {
		ArrayList<String> list = new ArrayList<String>() {
			@Override
			public boolean contains(Object o) {
				String string = (String) o;
				for(String s: this) {
					if(s.equalsIgnoreCase(string))
						return true;
				}
				return false;
			}
		};
		for(int row = 1;row<sheet.getRows();row++) {
			Cell[] data = sheet.getRow(row);
			if(data.length<=12)
				continue;
			String phone = data[PAID_PHONE].getContents().replaceAll("[^A-Za-z0-9\\s]", "").replace(" ", "");
			if(!list.contains(phone))
				list.add(phone);				
		}
		for(String phone: list) {
			System.out.println(client.UpdatePharmacyStatus(phone, "COVERED"));
		}
	}
	private void UploadRejectedStatus(DatabaseClient client,Sheet sheet) {
		for(int row = 1;row<sheet.getRows();row++) {
			Cell[] data = sheet.getRow(row);
			if(data.length<7)
				continue;
			String phone = data[REJECT_PHONE].getContents().replaceAll("[^A-Za-z0-9\\s]", "").replace(" ", "");
			String status = data[REJECT_STATUS].getContents().replaceAll("[^A-Za-z0-9\\s]", "");
			System.out.println("REJECTED: "+phone+" "+status);
			System.out.println(client.UpdatePharmacyStatus(phone, status));
		}
	}
	private void UploadReversalStatus(DatabaseClient client,Sheet sheet) {
		for(int row = 1;row<sheet.getRows();row++) {
			Cell[] data = sheet.getRow(row);
			if(data.length<21)
				continue;
			String phone = data[REVERSAL_PHONE].getContents().replaceAll("[^A-Za-z0-9\\s]", "").replace(" ", "");
			String status = data[REVERSAL_STATUS].getContents();
			System.out.println("REVERSED: "+phone+" "+status);
			System.out.println(client.UpdatePharmacyStatus(phone, status));
		}
	}
	private class Rx {
		public String number;
		public double profit;
		public Rx(String number) {
			this.number = number;
			this.profit = 0.0;
		}
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
		}
		public double getProfit() {
			return profit;
		}
		public void addToProfit(double profit) {
			this.profit += profit;
		}
		
	}
}
