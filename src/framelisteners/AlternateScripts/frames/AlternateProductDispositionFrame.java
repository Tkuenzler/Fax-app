package framelisteners.AlternateScripts.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import Fax.FaxStatus;
import Fax.ProductScripts;
import Clients.DatabaseClient;
import net.miginfocom.swing.MigLayout;
import table.Record;

public class AlternateProductDispositionFrame extends JFrame implements ActionListener {
	String[] statuses = FaxStatus.FAX_STATUSES;
	JTextField firstName = new JTextField(10);
	JTextField lastName = new JTextField(10);
	JTextField phonenumber = new JTextField(10);
	JComboBox<String> statusBox = new JComboBox<String>(statuses);
	JComboBox<String> productsBox = new JComboBox<String>(ProductScripts.ALL);
	JButton update = new JButton("Update");
	String database;
	
	public AlternateProductDispositionFrame() {
		setTitle("Disposition Record");
		setSize(500,200);
		setLayout(new MigLayout());
		DatabaseClient db = new DatabaseClient(true);
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
		add(productsBox,"gapleft 20px, gaptop 10px,skip");
		add(update,"gapleft 10px, gaptop 10px");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String id = firstName.getText().trim()+lastName.getText().trim()+phonenumber.getText().trim();
		String phone = phonenumber.getText().trim();
		String disposition = statusBox.getItemAt(statusBox.getSelectedIndex());
		String product = productsBox.getItemAt(productsBox.getSelectedIndex());
		if(phone.replaceAll("[()\\s-]+", "").trim().length()!=10) {
			JOptionPane.showMessageDialog(new JFrame(), "Invalid Phone number");
			return;
		}
		else if(disposition.equalsIgnoreCase(FaxStatus.BLANK)) {
			JOptionPane.showMessageDialog(new JFrame(), "Please choose a disposition");
			return;
		}
		String column = ProductScripts.GetProductFaxDispositionColumn(product);
		String columnDate = ProductScripts.GetProductFaxDispositionDateColumn(product);
		System.out.println(column);
		System.out.println(columnDate);
		if(column.equalsIgnoreCase("")) {
			JOptionPane.showConfirmDialog(null, "Invalid Product");
			return;
		}
		DatabaseClient client = new DatabaseClient(database,"Alternate_Scripts");
		
		int update = 0;
		switch(product) {
			case ProductScripts.TOPICAL_SCRIPT:
				if(client.IsFaxedForTopicalScript(id))
					update = client.UpdateProductDisposition(column,columnDate,disposition,id);
				else 
					JOptionPane.showMessageDialog(null, "Patient Not Faxed for Topical Script Yet");
				break;
			default:
				update = client.UpdateProductDisposition(column,columnDate,disposition,id);
				break;
		}
		if(update>0){
			JOptionPane.showMessageDialog(null, "Successfully updated");
			firstName.setText("");
			lastName.setText("");
			phonenumber.setText("");
			statusBox.setSelectedIndex(0);
		}
		else
			JOptionPane.showMessageDialog(null,"Failed to update ");
		client.close();
	}
}
