package framelisteners.settings.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Properties.DatabaseProperties;
import net.miginfocom.swing.MigLayout;
import objects.Database;

public class DatabaseSettingsFrame extends JFrame implements ActionListener {

	private JTextField localHost;
	private JTextField database;
	private JTextField port;
	private JTextField username;
	private JTextField password;
	private JButton save = new JButton("Save");
	/**
	 * Create the frame.
	 */
	public DatabaseSettingsFrame() {
		localHost = new JTextField(25);
		database  = new JTextField(20);
		port = new JTextField(5);
		username = new JTextField(20);
		password = new JTextField(20);
		save.addActionListener(this);
		setBounds(100, 100, 700, 230);
		setTitle("Database settings");
		getContentPane().setLayout(null);
		loadSettings();
		setUpPanel();
		setResizable(false);
		setVisible(true);
	}
	private void loadSettings() {
		DatabaseProperties properties  = new DatabaseProperties();
		Database db = properties.getDatabaseProperties();
		if(db==null)
			return;
		database.setText(db.getDatabase());
		port.setText(db.getPort());
		localHost.setText(db.getLocalHost());
		username.setText(db.getUsername());
		password.setText(db.getPassword());
	}
	private void setUpPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());
		panel.setBounds(10, 10, 660, 170);
		panel.setBorder(BorderFactory.createLoweredBevelBorder());
		getContentPane().add(panel);
		
		panel.add(new JLabel("Local Host"),"gaptop 20px, gapleft 10px");
		panel.add(localHost,"gaptop 20px, gapleft 10px");
		panel.add(new JLabel("Port"),"gaptop 20px, gapleft 10px");
		panel.add(port,"gaptop 20px, gapleft 10px");
		panel.add(new JLabel("Database"),"gaptop 20px, gapleft 10px");
		panel.add(database,"gaptop 20px, gapleft 10px, wrap");
		panel.add(new JLabel("Username"),"gaptop 20px, gapleft 10px");
		panel.add(username,"gaptop 20px, gapleft 10px");
		panel.add(new JLabel("Password"),"gaptop 20px, gapleft 10px");
		panel.add(password,"gaptop 20px, gapleft 10px, wrap");
		panel.add(save,"cell 2 3, gaptop 20px, gapleft 10px");
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		Database db = new Database();
		db.setDatabase(database.getText());
		db.setLocalHost(localHost.getText());
		db.setPort(port.getText());
		db.setUsername(username.getText());
		db.setPassword(password.getText());
		DatabaseProperties properties = new DatabaseProperties();
		properties.saveDatabaseProperties(db);
		this.dispose();
	}
	
}
