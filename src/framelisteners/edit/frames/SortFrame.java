package framelisteners.edit.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import source.CSVFrame;
import table.MyTableModel;
import table.Record;

public class SortFrame extends JFrame implements ActionListener {
	private JComboBox<String> headers, sortBy;
	private JButton sort;
	private JPanel contentPane;
	/**
	 * Create the frame.
	 */
	public SortFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 251, 132);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		headers = new JComboBox<String>(MyTableModel.COLUMN_HEADERS);
		headers.addItem("Message Status");
		headers.addItem("<RANDOM>");
		headers.setBounds(10, 29, 80, 20);
		contentPane.add(headers);
		
		JLabel lblNewLabel = new JLabel("by");
		lblNewLabel.setBounds(107, 32, 12, 14);
		contentPane.add(lblNewLabel);
		
		sortBy = new JComboBox<String>(new String[]{"Ascending","Descending"});
		sortBy.setBounds(145, 29, 80, 20);
		contentPane.add(sortBy);
		
		sort = new JButton("Sort");
		sort.setBounds(74, 60, 89, 23);
		sort.addActionListener(this);
		contentPane.add(sort);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Vector<Record> records = CSVFrame.model.data;
		records.sort(new Sorter());
		CSVFrame.model.fireTableStructureChanged();
		for(int i = 0;i<CSVFrame.model.getColumnCount();i++)
			CSVFrame.sorter.setSortable(i, false);
		this.dispose();
	}
	private class Sorter implements Comparator<Record> {
		@Override
		public int compare(Record o1, Record o2) {
			// TODO Auto-generated method stub
			String header = headers.getItemAt(headers.getSelectedIndex());
			String sort = sortBy.getItemAt(sortBy.getSelectedIndex());
			switch(header) {
				case "<RANDOM>":
					sortRandom();
					break;
				case "First Name":
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
					else 
						return o2.getFirstName().compareToIgnoreCase(o1.getFirstName());
				case "Last Name":
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getLastName().compareToIgnoreCase(o2.getLastName());
					else 
						return o2.getLastName().compareToIgnoreCase(o1.getLastName());
				case "Address":
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getAddress().compareToIgnoreCase(o2.getAddress());
					else 
						return o2.getAddress().compareToIgnoreCase(o1.getAddress());
				case "City":
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getCity().compareToIgnoreCase(o2.getCity());
					else 
						return o2.getCity().compareToIgnoreCase(o1.getCity());
				case "State":
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getState().compareToIgnoreCase(o2.getState());
					else 
						return o2.getState().compareToIgnoreCase(o1.getState());
				case "SSN#":
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getSsn().compareToIgnoreCase(o2.getSsn());
					else 
						return o2.getSsn().compareToIgnoreCase(o1.getSsn());
				case "Pharmacy":
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getPharmacy().compareToIgnoreCase(o2.getPharmacy());
					else 
						return o2.getPharmacy().compareToIgnoreCase(o1.getPharmacy());
				case "Insurance Carrier":
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getCarrier().compareToIgnoreCase(o2.getCarrier());
					else 
						return o2.getCarrier().compareToIgnoreCase(o1.getCarrier());
				case "NPI":
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getNpi().compareToIgnoreCase(o2.getNpi());
					else 
						return o2.getNpi().compareToIgnoreCase(o1.getNpi());
				case "Phone":
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getPhone().compareToIgnoreCase(o2.getPhone());
					else 
						return o2.getPhone().compareToIgnoreCase(o1.getPhone());
				case "DOB":
					if(sort.equalsIgnoreCase("Ascending"))
						return convertDOB(o1.getDob()).compareTo(convertDOB(o2.getDob()));
					else 
						return convertDOB(o2.getDob()).compareTo(convertDOB(o1.getDob()));
				case "Policy ID":	
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getPolicyId().compareToIgnoreCase(o2.getPolicyId());
					else 
						return o2.getPolicyId().compareToIgnoreCase(o1.getPolicyId());
				case "Bin":	
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getBin().compareToIgnoreCase(o2.getBin());
					else 
						return o2.getBin().compareToIgnoreCase(o1.getBin());
				case "Group":	
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getGrp().compareToIgnoreCase(o2.getGrp());
					else 
						return o2.getGrp().compareToIgnoreCase(o1.getGrp());
				case "PCN":	
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getPcn().compareToIgnoreCase(o2.getPcn());
					else 
						return o2.getPcn().compareToIgnoreCase(o1.getPcn());
				case "Dr Fax":	
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getDrFax().compareToIgnoreCase(o2.getDrFax());
					else 
						return o2.getDrFax().compareToIgnoreCase(o1.getDrFax());
				case "Status":
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getStatus().compareToIgnoreCase(o2.getStatus());
					else
						return o2.getStatus().compareToIgnoreCase(o1.getStatus());
				case "Dr First Name":
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getDrFirst().compareToIgnoreCase(o2.getDrFirst());
					else
						return o2.getDrFirst().compareToIgnoreCase(o1.getDrFirst());
				case "Dr Last Name":
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getDrLast().compareToIgnoreCase(o2.getDrLast());
					else
						return o2.getDrLast().compareToIgnoreCase(o1.getDrLast());
				case "Dr Address1":
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getDrAddress1().compareToIgnoreCase(o2.getDrAddress1());
					else
						return o2.getDrAddress1().compareToIgnoreCase(o1.getDrAddress1());
				case "Message Status":
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.compareStatus(o2);
					else
						return o2.compareStatus(o1);
				case "Type":
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getType().compareToIgnoreCase(o2.getType());
					else
						return o2.getType().compareToIgnoreCase(o1.getType());
				case "Email":
					if(sort.equalsIgnoreCase("Ascending"))
						return o1.getEmail().compareToIgnoreCase(o2.getEmail());
					else
						return o2.getEmail().compareToIgnoreCase(o1.getEmail());
			}
			return 0;			
		} 
		private Date convertDOB(String dob) {		
			SimpleDateFormat sdf  = new SimpleDateFormat("MM/dd/YYY");
			try {
				if(dob.equalsIgnoreCase(""))
					return  sdf.parse("01/01/1900");
				else
					return sdf.parse(dob);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				try {
					return  sdf.parse("01/01/1900");
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			return null;
		}
		private void sortRandom() {
			 Collections.shuffle(CSVFrame.model.data);
		}
	}
}
