package framelisteners.Database.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import Fax.FaxStatus;
import Clients.DatabaseClient;
import net.miginfocom.swing.MigLayout;
import source.CSVFrame;
import table.Record;

public class FaxDispositionFrame extends JFrame implements ActionListener {
	String[] statuses = FaxStatus.FAX_STATUSES;
	JTextField firstName = new JTextField(10);
	JTextField lastName = new JTextField(10);
	JTextField phonenumber = new JTextField(10);
	JComboBox<String> statusBox = new JComboBox<String>(statuses);
	JComboBox<String> tablesBox;
	JButton update = new JButton("Update");
	String database;
	public FaxDispositionFrame() {
		setTitle("Disposition Record");
		setSize(500,200);
		setLayout(new MigLayout());
		DatabaseClient db = new DatabaseClient(true);
		String[] tables = db.getTables();
		tablesBox = new JComboBox<String>(tables); 
		database = db.getDatabaseName();
		db.close();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		update.addActionListener(this);
		setUp();
		setVisible(true);
	}
	public void setUp() {
		add(new JLabel("First Name:  "),"gapleft 20px, gaptop 10px");
		add(firstName, "gapleft 10px, gaptop 10px");
		add(new JLabel("Last Name: "),"gapleft 20px, gaptop 10px");
		add(lastName, "gapleft 10px, gaptop 10px, wrap");
		add(new JLabel("Phone: "),"gapleft 20px, gaptop 10px");
		add(phonenumber,"gapleft 10px, gaptop 10px");	
		add(new JLabel("Disposition: "),"gapleft 20px, gaptop 10px");
		add(statusBox,"gapleft 10px, gaptop 10px,wrap");
		add(tablesBox,"gapleft 20px, gaptop 10px,skip");
		add(update,"gapleft 10px, gaptop 10px");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Record record = new Record();
		record.setFirstName(firstName.getText().trim());
		record.setLastName(lastName.getText().trim());
		record.setPhone(phonenumber.getText().trim());
		record.setId(record.getFirstName()+record.getLastName()+record.getPhone());
		String status = statusBox.getItemAt(statusBox.getSelectedIndex());
		String table = tablesBox.getItemAt(tablesBox.getSelectedIndex());
		if(record.getPhone().replaceAll("[()\\s-]+", "").trim().length()!=10) {
			JOptionPane.showMessageDialog(new JFrame(), "Invalid Phone number");
			return;
		}
		else if(status.equalsIgnoreCase(FaxStatus.BLANK)) {
			JOptionPane.showMessageDialog(new JFrame(), "Please choose a disposition");
			return;
		}
		DatabaseClient client = new DatabaseClient(database,table);
		int update = client.updateFaxDisposition(status, record);
		if(update>0){
			JOptionPane.showMessageDialog(null, "Successfully updated "+record.getFirstName()+" "+record.getLastName());
			firstName.setText("");
			lastName.setText("");
			phonenumber.setText("");
			statusBox.setSelectedIndex(0);
		}
		else
			JOptionPane.showMessageDialog(null,"Failed to update "+record.getFirstName()+" "+record.getLastName());
		client.close();
	}
}
