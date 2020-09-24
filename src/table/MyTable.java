package table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.http.client.utils.URIBuilder;

import Clients.DatabaseClient;
import Clients.EmdeonClient;
import Clients.RoadMapClient;
import Doctor.Doctor;
import Fax.Drug;
import Fax.FaxStatus;
import Fax.MessageStatus;
import Fax.Pharmacy;
import PBM.InsuranceFilter;
import Properties.EmdeonProperties;
import framelisteners.fax.frames.FaxChooserFrame;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import objects.Emdeon;
import objects.Insurance;
import objects.InsuranceInfo;
import objects.PharmacyMap;
import source.CSVFrame;
import source.JSONParser;
import subframes.FileChooser;

@SuppressWarnings("serial")
public class MyTable extends JTable	{
	public MyTable(MyTableModel model) {
		super(model);
		setDefaultRenderer(Object.class, new MyTableCellRenderer());
		JPopupMenu popupMenu = new JPopupMenu();
		JMenu fax = new JMenu("Fax");
		JMenuItem item = null;
		JMenu sendFax = new JMenu("Send Fax");
		item = new JMenuItem("Send Fax");
		item.addActionListener(new SendFax());
		sendFax.add(item);
		fax.add(sendFax);
		item = new JMenuItem("View Message Status");
		item.addActionListener(new ViewMessageStatus());
		fax.add(item);
		item = new JMenuItem("View Fax Status");
		item.addActionListener(new ViewFaxStatus());
		fax.add(item);
		item = new JMenuItem("Reset Message Status");
		item.addActionListener(new ResetMessageStatus());
		fax.add(item);
		popupMenu.add(fax);
		JMenu remedy = new JMenu("Database");
		JMenu update = new JMenu("Update");
		item = new JMenuItem("Update Record");
		item.addActionListener(new UpdateRecord());
		update.add(item);
		item = new JMenuItem("Update Pharmacy");
		item.addActionListener(new UpdatePharmacy());
		update.add(item);
		item = new JMenuItem("Update Emdeon Status");
		item.addActionListener(new UpdateEmdeonStatus());
		update.add(item);
		item = new JMenuItem("Update Last Emdeon Date");
		item.addActionListener(new UpdateEmdeonDate());
		update.add(item);
		item = new JMenuItem("Get Last Updated");
		item.addActionListener(new GetLastUpdated());
		update.add(item);
		remedy.add(update);
		JMenu faxStatus = new JMenu("Set Fax Status");
		String[] faxStatuses = FaxStatus.FAX_STATUSES;
		for(String s: faxStatuses) {
			JMenuItem i = new JMenuItem(s);
			i.addActionListener(new SetFaxStatus());
			faxStatus.add(i);
		}
		remedy.add(faxStatus);
		JMenu messageStatus = new JMenu("Set Message Status");
		String[] messageStatuses = MessageStatus.MESSAGE_STATUS;
		for(String s: messageStatuses) {
			JMenuItem i = new JMenuItem(s);
			i.addActionListener(new SetMessageStatus());
			messageStatus.add(i);
		}
		remedy.add(messageStatus);
		JMenu add = new JMenu("Add Records");
		item = new JMenuItem("Add Set Pharmacy Records");
		item.addActionListener(new AddSetPharmacyRecords());
		add.add(item);
		item = new JMenuItem("Add Records");
		item.addActionListener(new AddRecords());
		add.add(item);
		remedy.add(add);
		item = new JMenuItem("Delete Records");
		item.addActionListener(new DeleteRecords());
		remedy.add(item);
		popupMenu.add(remedy);
		String[] rowColors = {"Green","Blue","Red","Yellow","Black","Orange","White"};
		JMenu setRowColor = new JMenu("Set Row Colors");
		for(String s: rowColors) {
			item = new JMenuItem(s);
			item.addActionListener(new SetRowColor());
			setRowColor.add(item);
		}
		popupMenu.add(setRowColor);
		JMenu export = new JMenu("Export");
		item = new JMenuItem("Export CSV");
		item.addActionListener(new ExportCSV());
		export.add(item);
		item = new JMenuItem("Export XLS");
		item.addActionListener(new ExportXLS());
		export.add(item);
		item = new JMenuItem("Set Pharmacy");
		item.addActionListener(new SetPharmacy());
		popupMenu.add(item);
		popupMenu.add(export);
		item = new JMenuItem("Append to file");
		item.addActionListener(new Append());
		popupMenu.add(item);
		item = new JMenuItem("Removes Rows");
		item.addActionListener(new Remove());
		popupMenu.add(item);
		item = new JMenuItem("Remove everything but");
		item.addActionListener(new Keep());
		popupMenu.add(item);
		item = new JMenuItem("Count");
		item.addActionListener(new Count());
		popupMenu.add(item);
		item = new JMenuItem("Create duplicate");
		item.addActionListener(new Duplicate());
		popupMenu.add(item);
		item = new JMenuItem("Clear Insurance");
		item.addActionListener(new ClearInsurance());
		popupMenu.add(item);
		item = new JMenuItem("Update Doctor");
		item.addActionListener(new UpdateDoctor());
		popupMenu.add(item);
		item = new JMenuItem("Clear Column");
		item.addActionListener(new ClearColumn());
		popupMenu.add(item);
		item = new JMenuItem("Set Column");
		
		item.addActionListener(new SetColumn());
		popupMenu.add(item);
		JMenu confirmDoctor = new JMenu("Confirm Doctor");
		item = new JMenuItem("YES");
		item.addActionListener(new ConfirmDoctor());
		confirmDoctor.add(item);
		item = new JMenuItem("NO");
		item.addActionListener(new ConfirmDoctor());
		confirmDoctor.add(item);
		popupMenu.add(confirmDoctor);
		item = new JMenuItem("Emdeon");
		item.addActionListener(new EmdeonRecord());
		popupMenu.add(item);
		item = new JMenuItem("Check Insurance");
		item.addActionListener(new CheckInsurance());
		popupMenu.add(item);
		item = new JMenuItem("Open In Webform");
		item.addActionListener(new OpenInWebform());
		popupMenu.add(item);
		item = new JMenuItem("Set Covered Items");
		item.addActionListener(new SetCoveredItems());
		popupMenu.add(item);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setComponentPopupMenu(popupMenu);
	}
	@Override
	public void setValueAt(Object data, int row, int column) {
		MyTableModel model = (MyTableModel) this.getModel();
		Record record = model.data.get(row);
		String value = (String) data; 
		switch(column) {
			case MyTableModel.FIRST_NAME: record.setFirstName(value); 
		 		break;
		 	case MyTableModel.LAST_NAME: record.setLastName(value); 
		 		break;
		 	case MyTableModel.ADDRESS: record.setAddress(value); 
		 		break;
		 	case MyTableModel.CITY: record.setCity(value); 
		 		break;
		 	case MyTableModel.STATE: record.setState(value);
		 		break;
		 	case MyTableModel.ZIP: record.setZip(value); 
		 		break;
		 	case MyTableModel.PHONE: record.setPhone(value.replaceAll("[()\\s-]+", "")); 
		 		break;
		 	case MyTableModel.DOB: record.setDob(value); 
		 		break;
		 	case MyTableModel.CARRIER: record.setCarrier(value); 
		 		break;
		 	case MyTableModel.POLICY_ID: record.setPolicyId(value); 
		 		break;
		 	case MyTableModel.BIN: record.setBin(value); 
		 		break;
		 	case MyTableModel.GROUP: record.setGrp(value); 
		 		break;
		 	case MyTableModel.PCN: record.setPcn(value); 
		 		break;
			case MyTableModel.DR_FIRST: record.setDrFirst(value); 
				break;
			case MyTableModel.DR_LAST: record.setDrLast(value); 
				break;
			case MyTableModel.NPI: record.setNpi(value); 
				break;
			case MyTableModel.DR_ADDRESS1: record.setDrAddress1(value); 
				break;
			case MyTableModel.DR_CITY: record.setDrCity(value); 
				break;
			case MyTableModel.DR_STATE: record.setDrState(value);
				break;
			case MyTableModel.DR_ZIP: record.setDrZip(value); 
				break;
			case MyTableModel.DR_PHONE: record.setDrPhone(value.replaceAll("[()\\s-]+", ""));
				break;
			case MyTableModel.DR_FAX: record.setDrFax(value.replaceAll("[()\\s-]+", "")); 
				break;
			case MyTableModel.SSN: record.setSsn(value); 
				break;
			case MyTableModel.PHARMACY: record.setPharmacy(value); 
				break;
			case MyTableModel.GENDER: record.setGender(value);
				break;
			case MyTableModel.EMAIL: record.setEmail(value);
				break;	
			case MyTableModel.STATUS: record.setStatus(value);
				break;
			case MyTableModel.TYPE: record.setType(value);
				break;
			default: return;
		}
	}
	private class OpenInWebform implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			int[] rows = getSelectedRows();
			if(rows.length>1)
				JOptionPane.showMessageDialog(null, "Please select 1 row");
			Record record = CSVFrame.model.getRowAt(rows[0]);
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
				try {
					URIBuilder b = new URIBuilder("https://ltf5469.tam.us.siteprotect.com/WEBFORM.php");
					b.addParameter("phone_number",record.getPhone());
					b.addParameter("first_name", record.getFirstName());
					b.addParameter("last_name", record.getLastName());
					String month = record.getDob().substring(0, 2);
					String day = record.getDob().substring(3,5);
					String year = record.getDob().substring(6,10);
					System.out.println(month+" "+day+" "+year);
					b.addParameter("dob", year+month+day);
					b.addParameter("ssn", record.getSsn());
					b.addParameter("gender", record.getGender());
					b.addParameter("address", record.getAddress());
					b.addParameter("city", record.getCity());
					b.addParameter("state", record.getState());
					b.addParameter("zip", record.getZip());
					b.addParameter("npi", record.getNpi());
					b.addParameter("dr_first", record.getDrFirst());
					b.addParameter("dr_last", record.getDrLast());
					b.addParameter("dr_address", record.getDrAddress1());
					b.addParameter("dr_city", record.getDrCity());
					b.addParameter("dr_state", record.getDrState());
					b.addParameter("dr_zip", record.getDrZip());
					b.addParameter("dr_phone", record.getDrPhone());
					b.addParameter("dr_fax", record.getDrFax());
					Desktop.getDesktop().browse(b.build());
				} catch (IOException | URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	private class SetRowColor implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			int[] rows = getSelectedRows();
			for(int row: rows) {
				Record record = CSVFrame.model.getRowAt(row);
				record.setRowColor(event.getActionCommand());
			}
		}
	}
	private class SetMessageStatus implements ActionListener {
		@Override 
		public void actionPerformed(ActionEvent event) {
			String status = event.getActionCommand();
			DatabaseClient client = new DatabaseClient(false);
			if(client.getDatabaseName()==null || client.getTableName()==null)
				return;
			int[] rows = getSelectedRows();
			int count = 0;
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you to set all records to "+status+" on table "+client.getTableName(),"Update Message Status",JOptionPane.YES_NO_OPTION);
			if(confirm!=JOptionPane.YES_OPTION)
				return;
			for(int row: rows) {
				Record record = CSVFrame.model.getRowAt(row);
				record.setMessageStatus(status);
				count += client.updateMessageStatus(record);	
			}
			client.close();
			JOptionPane.showMessageDialog(null, "Sucessfully updated "+count+" out of "+rows.length);
		}
	}
	private class SetFaxStatus implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			String status = event.getActionCommand();
			int[] rows = getSelectedRows();
			int update = 0;
			DatabaseClient client = new DatabaseClient(false);
			if(client.getDatabaseName()==null || client.getTableName()==null)
				return;
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you to set all records to "+status+" on table "+client.getTableName(),"Update Fax Status",JOptionPane.YES_NO_OPTION);
			if(confirm!=JOptionPane.YES_OPTION)
				return;
			for(int row: rows)  {
				Record record = CSVFrame.model.getRowAt(row);
				record.setFaxStatus(status);
				update += client.updateFaxDisposition(status, record);
			}
			JOptionPane.showMessageDialog(null, "Sucessfully updated "+update+" out of "+rows.length);
			client.close();
		}
	}
	private class SendFax implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int[] rows = getSelectedRows();
			Vector<Record> data = new Vector<Record>();
			MyTableModel model =  (MyTableModel)getModel();
			for(int row: rows) 
				data.add(model.getRowAt(row));
			new FaxChooserFrame(data);
		}
	}
	private class ViewMessageStatus implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			int row = 	getSelectedRow();
			MyTableModel model = (MyTableModel)getModel();
			Record record = model.getRowAt(row);
			JOptionPane.showMessageDialog(null,record.getMessageStatus());
		}	
	}
	private class ViewFaxStatus implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			int row = 	getSelectedRow();
			MyTableModel model = (MyTableModel)getModel();
			Record record = model.getRowAt(row);
			JOptionPane.showMessageDialog(null,record.getFaxStatus());
		}	
	}
	private class ConfirmDoctor implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			String status = event.getActionCommand();
			int confirm = 0;
			if(status.equalsIgnoreCase("YES"))
				confirm = 1;
			else if(status.equalsIgnoreCase("NO"))
				confirm = 0;
			MyTableModel model = (MyTableModel)getModel();
			DatabaseClient client = new DatabaseClient(false);
			int[] rows = getSelectedRows();
			int updated = 0;
			for(int i: rows) {
				Record record = model.data.get(i);
				int update = client.confirmDoctor(record,confirm);
				if(update>0)
					updated++;
			}
			client.close();
			JOptionPane.showMessageDialog(null, "Successfully updated "+updated+" out of "+rows.length);
		}
	}
	private class ResetMessageStatus implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] rows = getSelectedRows();
			MyTableModel model = (MyTableModel)getModel();
			DatabaseClient client = new DatabaseClient(false);
			int fixed = 0;
			for(int i: rows) {
				Record record = model.data.get(i);
				int update = client.updateRecord(record);
				int reset = client.resetMessageStatus(record);
				if(update>0 && reset>0)
					fixed++;
			}
			client.close();
			JOptionPane.showMessageDialog(null, "Successfully updated "+fixed+" out of "+rows.length);
		}
	}
	private class ClearInsurance implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear insurance","Clear Insurance",JOptionPane.YES_NO_OPTION);
			if(confirm!=JOptionPane.YES_OPTION)
				return;
			int[] rows = getSelectedRows(); 
			for(int i: rows) {
				Record r = CSVFrame.model.getRowAt(i);
				r.setStatus("");
				r.setType("");
				r.setCarrier("");
				r.setPolicyId("");
				r.setBin("");
				r.setGrp("");
				r.setPcn("");
			}
			CSVFrame.model.fireTableDataChanged();
		}
	}
	private class Duplicate implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int[] rows = getSelectedRows(); 
			if(rows.length>1) {
				JOptionPane.showMessageDialog(null, "Select one row");
				return;
			}
			int row = rows[0];
			Record record = CSVFrame.model.getRowAt(row);
			try {
				Record dupe = (Record) record.clone();
				CSVFrame.model.addRowAt(row+1, dupe);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private class Remove implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove these rows?","Remove Rows",JOptionPane.YES_NO_OPTION);
			if(confirm!=JOptionPane.YES_OPTION)
				return;
			MyTableModel model = (MyTableModel) getModel();
			int[] rows = getSelectedRows(); 
			for(int i = 0;i<rows.length;i++) {
				System.out.println(rows[i]+" ["+i+"]");
				model.deleteRow(rows[i]-i);
			}			
		}
	}
	private class Count implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int[] rows = getSelectedRows(); 
			JOptionPane.showMessageDialog(new JFrame(), "There are "+rows.length+" rows");
		}
	}
	private class AddRecords implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String AFID = JOptionPane.showInputDialog("AFID?");
			if(AFID==null)
				return;
			DatabaseClient client = new DatabaseClient(false);
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to load records to "+client.getTableName(),"Upload Records",JOptionPane.YES_NO_OPTION);
			if(confirm!=JOptionPane.YES_OPTION)
				return;
			int[] rows = getSelectedRows();
			int add = 0;
			int badRecords = 0;
			for(int row: rows) {
				Record record = CSVFrame.model.getRowAt(row);
				if(!ValidateLead(record)) {
					badRecords++;
					record.setRowColor(Color.ORANGE);
					continue;
				}
				int added = client.addRecord(record,AFID);
				switch(added) {
					case 1: add ++; record.setRowColor(Color.GREEN);
						break;
					case 0: record.setRowColor(Color.MAGENTA); 
						break;
					case 1062: record.setRowColor(Color.RED);
						break;
				}
			}	
			JOptionPane.showMessageDialog(null, "Successfully added "+add+" to  "+client.getTableName());
			client.close();
			if(badRecords>0)
				JOptionPane.showMessageDialog(null, "There were "+badRecords+" Bad Records");
		}
	}
	private class AddSetPharmacyRecords implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			DatabaseClient client = new DatabaseClient(false);
			String AFID = JOptionPane.showInputDialog("AFID?");
			if(AFID==null)
				return;
			RoadMapClient roadmap = new RoadMapClient();
			String[] pharmacies = roadmap.getPharmacies();
			roadmap.close();
			int[] rows = getSelectedRows();
			int add = 0;
			int badRecords = 0;
			for(int row: rows) {
				Record record = CSVFrame.model.getRowAt(row);
				boolean correctPharmacy = false;
				checkName:
				for(String pharmacy: pharmacies) {
					if(record.getType().equalsIgnoreCase(pharmacy)) {
						correctPharmacy = true;
						break checkName;
					}
				}
				if(!ValidateLead(record)) {
					badRecords++;
					record.setRowColor(Color.ORANGE);
					continue;
				}
				if(correctPharmacy) {
					int added = client.addRecord(record,AFID);
					switch(added) {
					//Successful
					case 1: add ++; record.setRowColor(Color.GREEN);
						break;
					//Duplicate
					case -1: record.setRowColor(Color.MAGENTA); 
						break;
					case 0:
					case -2: record.setRowColor(Color.RED);
						break;
					}
				}
			}	
			JOptionPane.showMessageDialog(null, "Successfully added "+add+" to  "+client.getTableName());
			client.close();
			if(badRecords>0)
				JOptionPane.showMessageDialog(null, "There were "+badRecords+" Bad Records");
		}
	}
	private class UpdateEmdeonDate implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			DatabaseClient client = new DatabaseClient(false);
			int[] rows = getSelectedRows();
			int update = 0;
			for(int row: rows) {
				Record record = CSVFrame.model.getRowAt(row);
				if(client.UpdateEmdeonDate(record)>0)
					update++;
				else {
					System.out.println("DID NOT UPDATE: "+record.getFirstName()+" "+record.getLastName());
				}
			}
			client.close();
			JOptionPane.showMessageDialog(null, "UPDATED "+update+" out of "+rows.length);
		}
	}
	private class UpdateEmdeonStatus implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			DatabaseClient client = new DatabaseClient(false);
			int[] rows = getSelectedRows();
			int update = 0;
			for(int row: rows) {
				Record record = CSVFrame.model.getRowAt(row);
				int updated = client.updateRecord(DatabaseClient.Columns.EMDEON_STATUS, record.getStatus(),record);
				if(updated>0) {
					record.setRowColor(Color.GREEN);
					update++;
				}
				else
					record.setRowColor(Color.RED);
			}
			JOptionPane.showMessageDialog(null, "Successfully updated "+update+" out of  "+rows.length);
		}
	}
	private class UpdatePharmacy implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			DatabaseClient client = new DatabaseClient(false);
			if(client.getTableName()==null || client.getDatabaseName()==null)
				return;
			int[] rows = getSelectedRows();
			int add = 0;
			for(int row: rows) {
				Record record = CSVFrame.model.getRowAt(row);
				add += client.UpdatePharmacy(record,record.getPharmacy());
				System.out.println(add);
			}	
			JOptionPane.showMessageDialog(null, "Successfully updated "+add+" to  "+client.getTableName());
		}
	}
	private class Keep implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove everything but these rows?","Remove Rows",JOptionPane.YES_NO_OPTION);
			if(confirm!=JOptionPane.YES_OPTION)
				return;
			int[] rows = getSelectedRows();
			MyTableModel model = (MyTableModel) getModel();
			ArrayList<Record> list = new ArrayList<Record>();
			for(int row: rows) {
				list.add(model.getRowAt(row));
			}			
			model.deleteAllRows();
			for(Record record: list) {
				model.addRow(record);
			}
		}
	}
	private class Append implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			int[] rows = getSelectedRows();
			File file = FileChooser.AppendCsvFile();
			if(file==null)
				return;
			if(checkFile(file)){
				BufferedWriter bw = null;
				try {
					bw = new BufferedWriter(new FileWriter(file,true));
					for(int row: rows) {
						for(int column = 0;column<MyTableModel.COLUMN_HEADERS.length;column++) {
							String value = (String) CSVFrame.table.getValueAt(row, column);
							if(value==null)
								value = "";
							if(column<MyTableModel.COLUMN_HEADERS.length-1)
								bw.write(value+",");
							else
								bw.write(value);
						}
						bw.newLine();
					}
					JOptionPane.showMessageDialog(null, "Sucessfully appended file");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					try {
						bw.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			else {
				JOptionPane.showMessageDialog(new JFrame(), "Invalid file type (headers must match)");
			}
		}
		private boolean checkFile(File file) {
			StringBuilder header = new StringBuilder();
			for(int i = 0;i<MyTableModel.COLUMN_HEADERS.length;i++) {
				if(i<MyTableModel.COLUMN_HEADERS.length-1)
					header.append(MyTableModel.COLUMN_HEADERS[i]+",");
				else
					header.append(MyTableModel.COLUMN_HEADERS[i]+"");
			}
			BufferedReader rw = null;
			boolean value = false;
			try {
				rw = new BufferedReader(new FileReader(file));
				String line = rw.readLine();
				System.out.println(line);
				System.out.println(header.toString());
				if(line.equalsIgnoreCase(header.toString()))
					value = true;
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					rw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return value;
		}
	}
	private class ExportCSV implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//get the rows to export
			int[] rows = getSelectedRows();
			//Get file to save to
			File file = FileChooser.SaveCsvFile();
			if(file==null)
				return;
			BufferedWriter bw = null;
				try {
					//Write the column headers
					bw = new BufferedWriter(new FileWriter(file));
					for(int i = 0;i<MyTableModel.COLUMN_HEADERS.length;i++) {
						if(i<MyTableModel.COLUMN_HEADERS.length-1)
							bw.write(MyTableModel.COLUMN_HEADERS[i]+",");
						else
							bw.write(MyTableModel.COLUMN_HEADERS[i]+"");
					}
					bw.newLine();
					//Writes the data from table
					for(int row: rows) {
						for(int column = 0;column<MyTableModel.COLUMN_HEADERS.length;column++) {
							String value = (String) CSVFrame.table.getValueAt(row, column);
							System.out.println(value);
							if(value==null)
								value = "";
							if(column<MyTableModel.COLUMN_HEADERS.length-1)
								bw.write(value+",");
							else
								bw.write(value);
						}
						bw.newLine();
					}
					bw.close();
					JOptionPane.showMessageDialog(null, "Sucessfully exported file");
				} catch(IOException e) {
					e.printStackTrace();
				}
		}
	}
	private class ExportXLS implements ActionListener {
		WritableWorkbook workBook = null;
		WritableSheet excelSheet = null;
		@Override
		public void actionPerformed(ActionEvent arg0) {
			File file = FileChooser.SaveXlsFile();
			if(file==null)
				return;
			try {
	        	workBook = Workbook.createWorkbook(file);
	        	excelSheet = workBook.createSheet("Leads", 0);
	        	AddHeaders();
	        	int[] rows = getSelectedRows();
	        	int count = 0;
	        	for(int row: rows) {
	        		Record record = CSVFrame.model.getRowAt(row);
	        		for(int column = 0;column<MyTableModel.COLUMN_HEADERS.length;column++) {
	        			AddLabel(record,count,row,column);
	        		}
	        		count++;
	        	}
	        	workBook.write();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (RowsExceededException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
	            if (workBook != null) {
	                try {
	                    workBook.close();
	                    JOptionPane.showMessageDialog(null, "Succesfully exported XLS file");
	                } catch (IOException e) {
	                    e.printStackTrace();
	                } catch (WriteException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
		}
		private void AddHeaders() throws RowsExceededException, WriteException {
			for(int column = 0;column<MyTableModel.COLUMN_HEADERS.length;column++) {
				String header = MyTableModel.COLUMN_HEADERS[column];
				Label label = new Label(column,0,header);
				excelSheet.addCell(label);	
			}
		}
		private void AddLabel(Record record,int count,int row,int column) throws RowsExceededException, WriteException {
			String value = CSVFrame.model.getValueAt(row, column);
			if(value==null)
				value = "";
			Label label = new Label(column,count+1,value.toUpperCase());
			excelSheet.addCell(label);		
		}
	}
	private class DeleteRecords implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			DatabaseClient client = new DatabaseClient(false);
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete these records from "+client.getTableName(),"Delete Records",JOptionPane.YES_NO_OPTION);
			if(confirm!=JOptionPane.YES_OPTION)
				return;
			int count = 0;
			int[] rows = getSelectedRows();
			for(int row: rows) {
				Record record = CSVFrame.model.getRowAt(row);
				count += client.deleteRecord(record);
			}
			JOptionPane.showMessageDialog(null, count+" out of "+rows.length+" records deleted");
			client.close();
		}
	}

	private class GetLastUpdated implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int[] rows = getSelectedRows();
			if(rows.length>1 || rows.length==0) {
				JOptionPane.showMessageDialog(null, "Please select 1 row");
				return;
			}
			Record record = CSVFrame.model.getRowAt(rows[0]);
			DatabaseClient client = new DatabaseClient(false);
			String lastUpdated = client.getColumn(Columns.LAST_UPDATED, record);
			JOptionPane.showMessageDialog(null, lastUpdated);
			client.close();
		}
	}
	private class UpdateRecord implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String column = (String) JOptionPane.showInputDialog(new JFrame(), "Choose a column", "Columns", JOptionPane.QUESTION_MESSAGE, null, DatabaseClient.Columns.HEADERS, DatabaseClient.Columns.HEADERS[0]);
			if(column==null)
				return;
			String value = null;
			int[] rows = getSelectedRows();
			int count = 0;
			DatabaseClient db = new DatabaseClient(false);
			ArrayList<String> errors = new ArrayList<String>();
			for(int row: rows) {
				Record record = CSVFrame.model.getRowAt(row);
				switch(column) {
					case Columns.ALL:
						count += db.updateRecord(record);
						continue;
					case Columns.FIRST_NAME:
						value = record.getFirstName();
						break;
					case Columns.LAST_NAME:
						value = record.getLastName();
						break;
					case Columns.DOB:
						value = record.getDob();
						break;
					case Columns.PHONE_NUMBER:
						value = record.getPhone();
						break;
					case Columns.ADDRESS:
						value = record.getAddress();
						break;
					case Columns.CITY:
						value = record.getCity();
						break;
					case Columns.STATE:
						value = record.getState();
						break;
					case Columns.ZIPCODE:
						value = record.getZip();
						break;
					case Columns.CARRIER:
						value = record.getCarrier();
						break;
					case Columns.POLICY_ID:
						value = record.getPolicyId();
						break;
					case Columns.BIN:
						value = record.getBin();
						break;
					case Columns.GROUP:
						value = record.getGrp();
						break;
					case Columns.PCN:
						value = record.getPcn();
						break;
					case Columns.NPI:
						value = record.getNpi();
						break;
					case Columns.DR_FIRST:
						value = record.getDrFirst();
						break;
					case Columns.DR_LAST:
						value = record.getDrLast();
						break;
					case Columns.DR_ADDRESS1:
						value = record.getDrAddress1();
						break;
					case Columns.DR_CITY:
						value = record.getDrCity();
						break;
					case Columns.DR_STATE:
						value = record.getDrState();
						break;
					case Columns.DR_ZIP:
						value = record.getDrZip();
						break;
					case Columns.DR_PHONE:
						value = record.getDrPhone();
						break;
					case Columns.DR_FAX:
						value = record.getDrFax();
						break;
					case Columns.SSN:
						value = record.getSsn();
						break;
					case Columns.GENDER:
						value = record.getGender();
						break;	
				}
				int updated = db.updateRecord(column, value,record);
				if(updated>0)
					record.setRowColor(Color.GREEN);
				if(updated<=0) {
					System.out.println("Failed: "+updated);
					record.setRowColor(Color.RED);
					errors.add(record.getFirstName()+" "+record.getLastName()+" "+record.getPhone());
				}
				count += updated;
			}
			db.close();
			JOptionPane.showMessageDialog(null, count+" out of "+rows.length+" updated");
			if(!errors.isEmpty()) {
				//JOptionPane.showMessageDialog(null, errors.toArray(new String[errors.size()]));
				String[] error = errors.toArray(new String[errors.size()]);
				JOptionPane.showMessageDialog(null, new JScrollPane(new JList<String>(error)));
			}
		}
	}
	private class SetColumn implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			MyTableModel model = CSVFrame.model;
			String[] columns = MyTableModel.COLUMN_HEADERS;
			String column = (String) JOptionPane.showInputDialog(new JFrame(), "Select a table", "tables:", JOptionPane.QUESTION_MESSAGE, null, columns, columns[0]);
			if(column==null)
				return;
			String value = JOptionPane.showInputDialog("Set "+column+" as:");
			if(value==null)
				return;
			int columnNumber = model.getColumn(column);
			int[] rows = getSelectedRows();
			for(int i: rows) {
				model.updateValue(value, i, columnNumber);
			}
		}
	}
	private class ClearColumn implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String column = (String) JOptionPane.showInputDialog(new JFrame(), "Select a Column", "Columns:", JOptionPane.QUESTION_MESSAGE, null, MyTableModel.COLUMN_HEADERS, MyTableModel.COLUMN_HEADERS[0]);
			if(column==null)
				return;
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear "+column, "Clear Column", JOptionPane.YES_OPTION);
			if(confirm!=JOptionPane.YES_OPTION)
				return;
			int[] rows = getSelectedRows();
			for(int i: rows) {
				CSVFrame.model.updateValue("", i, CSVFrame.model.getColumn(column));
			}
		}
	}
	private class CheckInsurance implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to check insurance on these records?","Check Insurance?",JOptionPane.YES_NO_OPTION);
			if(confirm!=JOptionPane.YES_OPTION)
				return;
			int[] rows = getSelectedRows();
			for(int row: rows) {
				Record record = CSVFrame.model.getRowAt(row);
				record.checkInsurance(InsuranceFilter.Filter(record));
			}
		}
	}
	private class SetPharmacy implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int[] rows = getSelectedRows();
			ArrayList<PharmacyMap> roadMap = new ArrayList<PharmacyMap>();
			RoadMapClient client = new RoadMapClient();
			/*
			 * Get all pharmacy names
			 */
			String[] pharmacies = client.getPharmacies();
			/*
			 * Create and populate all pharmacies
			 */
			for(String pharmacy: pharmacies) {
				PharmacyMap map = client.getPharmacy(pharmacy);
				client.LoadAllStates(map);
				roadMap.add(map);
			}
			client.close();
			for(int row: rows) {
				Record record = CSVFrame.model.getRowAt(row);
				record.setPharmacy(Pharmacy.GetPharmacy(roadMap, record));
			}
		}
	}
	private class SetCoveredItems implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String coveredMeds = Drug.GetDrugNames();
			DatabaseClient client = new DatabaseClient(true);
			int[] rows = getSelectedRows();
			int count = 0;
			for(int i: rows) {
				Record record = CSVFrame.model.getRowAt(i);
				int value = client.setCoveredItems(record,coveredMeds);
				if(value==1)
					count++;
			}
			client.close();
			JOptionPane.showMessageDialog(null, "Succesfully updated "+count+" medications");
		}
	}
	private class EmdeonRecord implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to emdeon these records?","Emdeon Records?",JOptionPane.YES_NO_OPTION);
			if(confirm!=JOptionPane.YES_OPTION)
				return;
			new Thread(new Runnable() {
			    @Override
				public void run() {
			    	EmdeonClient emdeon = new EmdeonClient();
					if(!emdeon.login()) {
						JOptionPane.showMessageDialog(null, "Login Failed");
						return;
					}
					EmdeonProperties properties = new EmdeonProperties();
					Emdeon emd = properties.getEmdeonProperties();
					int[] rows = getSelectedRows();
			    	for(int row: rows) {
						Record record = CSVFrame.model.getRowAt(row);
						InsuranceInfo info = emdeon.fillOutForm(record,emd.getPause()*1000);
						info.setInsuranceType();
						if(info.isCommercial) 
							updateInsurance(info.privatePrimary,row);
						else if(info.isMedicare) 
							updateInsurance(info.medicarePrimary,row);
						else
							CSVFrame.model.updateValue(info.status, row, MyTableModel.STATUS);
					}
			    }
			}).start();	
		}
		private void updateInsurance(Insurance insurance, int i) {
			CSVFrame.model.updateValue("FOUND", i, MyTableModel.STATUS);
			CSVFrame.model.updateValue(insurance.getType(), i, MyTableModel.TYPE);
			CSVFrame.model.updateValue(insurance.getCarrier(), i, MyTableModel.CARRIER);
			CSVFrame.model.updateValue(insurance.getPolicyId(), i, MyTableModel.POLICY_ID);
			CSVFrame.model.updateValue(insurance.getBin(), i, MyTableModel.BIN);
			CSVFrame.model.updateValue(insurance.getGrp(), i, MyTableModel.GROUP);
			CSVFrame.model.updateValue(insurance.getPcn(), i, MyTableModel.PCN);
			CSVFrame.model.updateValue(insurance.getInfo(), i, MyTableModel.EMAIL);
		}
	}
	private class UpdateDoctor implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to update these doctor's info","Delete Records",JOptionPane.YES_NO_OPTION);
			if(confirm!=JOptionPane.YES_OPTION)
				return;
			int[] rows = getSelectedRows();
			for(int row: rows) {
				Record r = CSVFrame.model.getRowAt(row);
				if(r.npi.length()!=10 || r.npi.charAt(0)!= '1')
					continue;
				try {
					HttpURLConnection connection = (HttpURLConnection) new URL("https://npiregistry.cms.hhs.gov/api/?version=2.0&number="+r.getNpi()).openConnection();
					connection.connect();
					BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					Doctor d = JSONParser.CreateDoctor(rd.readLine());
					if(d==null)
						continue;
					r.setDrFirst(d.firstName);
					r.setDrLast(d.lastName);
					r.setDrAddress1(d.practiceAddress1);
					r.setDrAddress2(d.practiceAddress2);
					r.setDrCity(d.practiceCity);
					r.setDrState(d.practiceState);
					r.setDrZip(d.practiceZipeCode);
					r.setDrFax(d.practiceFax);
					r.setDrPhone(d.practicePhone);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}
	}
	public String UpdateRecordForDr(Record record,String table,int used) {
		StringBuilder base = new StringBuilder("UPDATE `"+table+"` SET ");
		for(String s: Columns.DR_HEADERS) {
			switch(s) {
				case Columns.NPI:
					base.append("`"+Columns.NPI+"` = '"+record.getNpi()+"', ");
					break;
				case Columns.DR_FIRST:
					base.append("`"+Columns.DR_FIRST+"` = '"+record.getDrFirst()+"', ");
					break;
				case Columns.DR_LAST:
					base.append("`"+Columns.DR_LAST+"` = '"+record.getDrLast()+"', ");
					break;
				case Columns.DR_ADDRESS1:
					base.append("`"+Columns.DR_ADDRESS1+"` = '', ");
					break;
				case Columns.DR_CITY:
					base.append("`"+Columns.DR_CITY+"` = '"+record.getDrCity()+"', ");
					break;
				case Columns.DR_STATE:
					base.append("`"+Columns.DR_STATE+"` = '"+record.getDrState()+"', ");
					break;
				case Columns.DR_ZIP:
					base.append("`"+Columns.DR_ZIP+"` = '"+record.getDrZip()+"', ");
					break;
				case Columns.DR_PHONE:
					base.append("`"+Columns.DR_PHONE+"` = '"+record.getDrPhone()+"', ");
					break;
				case Columns.DR_FAX:
					base.append("`"+Columns.DR_FAX+"` = '"+record.getDrFax()+"', ");
					break;
				case Columns.FAX_DISPOSITION:
					base.append("`"+Columns.FAX_DISPOSITION+"` = '', ");
					break;
				case Columns.USED:
					base.append("`"+Columns.USED+"` = "+used+", ");
					break;
				case Columns.AGENT:
					base.append("`agent` = '', ");
					break;
				case Columns.CONFIRM_DOCTOR:
					base.append("`"+Columns.CONFIRM_DOCTOR+"` = 0");
					break;
			}
		}
		base.append(" WHERE `"+Columns.ID+"` = '"+record.getId()+"'");
		return base.toString();
		
	}
	private boolean ValidateLead(Record record) {
		if(record.getPhone().equalsIgnoreCase(record.getDrFax()))
			return false;
		if(record.getPhone().equalsIgnoreCase(record.getDrPhone()))
			return false;
		if(record.getAddress().equalsIgnoreCase(record.getDrAddress1()))
			return false;
		String ptName = record.getFirstName()+record.getLastName();
		String drName = record.getDrFirst()+record.getDrLast();
		if(ptName.equalsIgnoreCase(drName))
			return false;
		return true;
	}
	static class MyTableCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		    Record r = CSVFrame.model.getRowAt(row);
		    if(isSelected) {
		    	c.setBackground(table.getSelectionBackground());
		        c.setForeground(table.getSelectionForeground());
		    }
		    else {
		    	c.setBackground(r.getRowColor());
		        c.setForeground(table.getForeground());
		    }
		    return c;
		 }

	 }
	public static class Columns {
		public static final String ALL = "ALL";
		public static final String FIRST_NAME = "first_name";
		public static final String LAST_NAME = "last_name";
		public static final String DOB = "dob";
		public static final String PHONE_NUMBER = "phonenumber";
		public static final String SSN = "ssn";
		public static final String GENDER = "gender";
		public static final String ADDRESS = "address";
		public static final String CITY = "city";
		public static final String STATE = "state";
		public static final String ZIPCODE = "zip";
		public static final String CARRIER = "carrier";
		public static final String POLICY_ID = "policy_id";
		public static final String BIN = "bin";
		public static final String GROUP = "grp";
		public static final String PCN = "pcn";
		public static final String NPI = "npi";
		public static final String DR_FIRST = "dr_first_name";
		public static final String DR_LAST = "dr_last_name";
		public static final String DR_ADDRESS1 = "dr_address1";
		public static final String DR_CITY = "dr_city";
		public static final String DR_STATE = "dr_state";
		public static final String DR_ZIP = "dr_zip";
		public static final String DR_PHONE = "dr_phone";
		public static final String DR_FAX = "dr_fax";
		public static final String ID = "_id";
		public static final String AGENT = "agent";
		public static final String PHARMACY = "PHARMACY";
		public static final String FAX_ATTEMPTS = "FAX_ATTEMPTS";
		public static final String FAXES_SENT = "FAXES_SENT";
		public static final String FAX_DISPOSITION = "FAX_DISPOSITION";
		public static final String MESSAGE_STATUS = "MESSAGE_STATUS";
		public static final String USED = "USED";
		public static final String AFID = "AFID";
		public static final String DATE_ADDED = "DATE_ADDED";
		public static final String LAST_UPDATED = "LAST_UPDATED";
		public static final String MESSAGE_ID = "MESSAGE_ID";
		public static final String CONFIRM_DOCTOR = "CONFIRM_DOCTOR";
		public static final String TELMED_DISPOSITION = "TELMED_DISPOSITION";
		public static final String[] DR_HEADERS = {NPI,DR_FIRST,DR_LAST,DR_ADDRESS1,DR_CITY,DR_STATE,DR_ZIP,DR_PHONE,DR_FAX,FAX_DISPOSITION,USED,AGENT,CONFIRM_DOCTOR};
		public static final String[] DR_HEADERS2 = {NPI,DR_FIRST,DR_LAST,DR_ADDRESS1,DR_CITY,DR_STATE,DR_ZIP,DR_PHONE,DR_FAX,FAX_DISPOSITION,MESSAGE_STATUS,MESSAGE_ID,USED,CONFIRM_DOCTOR};

	}
}