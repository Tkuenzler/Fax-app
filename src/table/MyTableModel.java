package table;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class MyTableModel extends AbstractTableModel implements TableModelListener  {

	public static final int FIRST_NAME = 0;
	public static final int LAST_NAME = 1;
	public static final int ADDRESS = 2;
	public static final int CITY = 3;
	public static final int STATE = 4;
	public static final int ZIP = 5;
	public static final int PHONE = 6;
	public static final int DOB = 7;
	public static final int SSN = 8;
	public static final int PHARMACY = 9;
	public static final int STATUS = 10;
	public static final int TYPE = 11;
	public static final int CARRIER = 12;
	public static final int POLICY_ID = 13;
	public static final int BIN = 14; 
	public static final int GROUP = 15;
	public static final int PCN = 16;
	public static final int DR_FIRST = 17;
	public static final int DR_LAST = 18;
	public static final int NPI = 19;
	public static final int DR_ADDRESS1 = 20;
	public static final int DR_CITY = 21;
	public static final int DR_STATE = 22;
	public static final int DR_ZIP = 23;
	public static final int DR_FAX = 24;
	public static final int DR_PHONE = 25;
	public static final int GENDER = 26;
	public static final int EMAIL = 27;
	public static final String[] COLUMN_HEADERS = {"First Name","Last Name","Address","City","State","Zip","Phone",
			"DOB","SSN#","Pharmacy","Status","Type","Insurance Carrier","Policy ID","Bin","Group","PCN","Dr First Name","Dr Last Name","NPI",
			"Dr Address1","Dr City","Dr State","Dr Zip","Dr Fax","Dr Phone","Gender","Email"};
	public Vector<Record> data;
	public static boolean cellsEditable = true;
	List<Color> rowColours = Arrays.asList(
		        Color.RED,
		        Color.GREEN,
		        Color.CYAN
		    );
	public MyTableModel() {
		super();
		data = new Vector<Record>() {
			 @Override
			 public boolean contains(Object o) {
				 Record record = (Record) o;
				 for(Record r: this) {
					if(r.isTheSame(record))
						return true;
				 }
				 return false;
			 }
		 };
		//this.addTableModelListener(this);
	}
	@Override 
	public boolean isCellEditable(int row,int col) {
		return cellsEditable;
	}
	@Override
	public int getColumnCount() {
		return COLUMN_HEADERS.length;
	}
	public String getColumnName(int i) {
		return COLUMN_HEADERS[i];
	}
	@Override
	public int getRowCount() {
		return data.size();
	}
	@Override
	public String getValueAt(int row, int column) {
		Record record = (Record) data.get(row);
		switch(column){
			case FIRST_NAME: return record.getFirstName();
			case LAST_NAME: return record.getLastName();
			case ADDRESS: return record.getAddress();
			case CITY: return record.getCity();
			case STATE: return record.getState();
			case ZIP: return record.getZip();
			case PHONE: return record.getPhone();
			case DOB: return record.getDob();
			case CARRIER: return record.getCarrier();
			case POLICY_ID: return record.getPolicyId();
			case BIN: return record.getBin();
			case GROUP: return record.getGrp();
			case PCN: return record.getPcn();
			case DR_FIRST: return record.getDrFirst();
			case DR_LAST: return record.getDrLast();
			case NPI: return record.getNpi();
			case DR_ADDRESS1: return record.getDrAddress1();
			case DR_CITY: return record.getDrCity();
			case DR_STATE: return record.getDrState();
			case DR_ZIP: return record.getDrZip();
			case DR_PHONE: return record.getDrPhone();
			case DR_FAX: return record.getDrFax();
			case SSN: return record.getSsn();
			case PHARMACY: return record.getPharmacy();
			case GENDER: return record.getGender();
			case EMAIL: return record.getEmail();
			case STATUS: return record.getStatus();
			case TYPE: return record.getType();
			default: return null;
		}
	}
	public void updateRecordAt(int row,Record record) {
		Record recordAt = data.get(row);
		recordAt.setCarrier(record.getCarrier());
		recordAt.setPolicyId(record.getPolicyId());
		recordAt.setBin(record.getBin());
		recordAt.setPcn(record.getPcn());
		recordAt.setGrp(record.getGrp());
		recordAt.setType(record.getType());
		//recordAt.setNpi(record.getNpi());
	}
	public int getColumn(String column) {
		switch(column) {
			case "First Name": return 0;
			case "Last Name": return 1;
			case "Address": return 2;
			case "City": return 3;
			case "State": return 4;
			case "Zip": return 5;
			case "Phone": return 6;
			case "DON": return 7;
			case "SSN#": return 8;
			case "Pharmacy": return 9;
			case "Status": return 10;
			case "Type": return 11;
			case "Insurance Carrier": return 12;
			case "Policy ID": return 13;
			case "Bin": return 14;
			case "Group": return 15;
			case "PCN": return 16;
			case "Dr First Name": return 17;
			case "Dr Last Name": return 18;
			case "NPI": return 19;
			case "Dr Addreess1": return 20;
			case "Dr City": return 21;
			case "Dr State": return 22;
			case "Dr Zip": return 23;
			case "Dr Fax": return 24;
			case "Dr Phone": return 25;
			case "Gender": return 26;
			case "Email": return 27;
			default: return -1;
		}
	}
	public void updateValue(String value,int row, int column) {
		 Record record = data.get(row);
		 switch(column){
		 	case FIRST_NAME: record.setFirstName(value);
		 		break;
		 	case LAST_NAME: record.setLastName(value);
		 		break;
		 	case ADDRESS: record.setAddress(value);
		 		break;
		 	case CITY: record.setCity(value);
		 		break;
		 	case STATE: record.setState(value);
		 		break;
		 	case ZIP: record.setZip(value);
		 		break;
		 	case PHONE: record.setPhone(value.replaceAll("[()\\s-]+", ""));
		 		break;
		 	case DOB: record.setDob(value);
		 		break;
		 	case CARRIER: record.setCarrier(value);
		 		break;
		 	case POLICY_ID: record.setPolicyId(value);
		 		break;
		 	case BIN: record.setBin(value);
		 		break;
		 	case GROUP: record.setGrp(value);
		 		break;
		 	case PCN: record.setPcn(value);
		 		break;
			case DR_FIRST: record.setDrFirst(value);
				break;
			case DR_LAST: record.setDrLast(value);
				break;
			case NPI: record.setNpi(value); System.out.println(value);
				break;
			case DR_ADDRESS1: record.setDrAddress1(value);
				break;
			case DR_CITY: record.setDrCity(value);
				break;
			case DR_STATE: record.setDrState(value);
				break;
			case DR_ZIP: record.setDrZip(value);
				break;
			case DR_PHONE: record.setDrPhone(value.replaceAll("[()\\s-]+", ""));
				break;
			case DR_FAX: record.setDrFax(value.replaceAll("[()\\s-]+", ""));
				break;
			case SSN : record.setSsn(value);
				break;
			case PHARMACY: record.setPharmacy(value);
				break;
			case GENDER: record.setGender(value);
				break;
			case EMAIL: record.setEmail(value);
				break;
			case STATUS: record.setStatus(value);
				break;
			case TYPE: record.setType(value);
				break;
			default: return;
		}
		fireTableCellUpdated(row, column);
	}
	public Record getRowAt(int row) {
	     return (Record)data.get(row);
	}
	public void addRow(Record r) {
		data.add(r);
		fireTableDataChanged();
	}
	public void addRowAt(int row,Record record) {
		data.add(row, record);
		fireTableDataChanged();
	}
	public void deleteRow(int row) {
		data.remove(row);
		fireTableDataChanged();
	}
	public void deleteAllRows() {
		data.removeAllElements();
		fireTableDataChanged();
	}
	@Override
	public void tableChanged(TableModelEvent ev) {
		 int row = ev.getFirstRow();
	     int column = ev.getColumn();
	     MyTableModel model = (MyTableModel)ev.getSource(); 
	     String data = model.getValueAt(row, column);
	     System.out.println(data);
	}
}
