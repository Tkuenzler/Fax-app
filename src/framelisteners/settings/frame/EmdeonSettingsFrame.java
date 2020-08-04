package framelisteners.settings.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import Properties.EmdeonProperties;
import net.miginfocom.swing.MigLayout;
import objects.Emdeon;

public class EmdeonSettingsFrame extends JFrame implements ActionListener {
	JTextField username = new JTextField(15);
	JPasswordField password = new JPasswordField(15);
	JTextField npi = new JTextField(10);
	JCheckBox privateOnly = new JCheckBox();
	JSpinner pause;
	int current;
	Emdeon emd;
	JButton save = new JButton("Save");
	public EmdeonSettingsFrame() {
		setBounds(100, 100, 600, 230);
		setTitle("Emdeon Settings");
		loadSettings();
		setUpPanel();
		save.addActionListener(this);
		setVisible(true);
	}
	private void loadSettings() {
		EmdeonProperties properties  = new EmdeonProperties();
		emd = properties.getEmdeonProperties();
		username.setText(emd.getUsername());
		password.setText(emd.getPassword());
		privateOnly.setSelected(emd.isPrivateOnly());
		npi.setText(emd.getNpi());
	}
	private void setUpPanel() {
	    SpinnerNumberModel spinnerModel;
	    int min = 1;
	    int current = emd.getPause();
	    int max = 20;
	    int increment = 1;
	    spinnerModel = new SpinnerNumberModel(current, min, max, increment);
	    pause = new JSpinner(spinnerModel);
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());
		panel.setBounds(10, 10, 560, 170);
		panel.setBorder(BorderFactory.createLoweredBevelBorder());
		getContentPane().add(panel);
		panel.add(new JLabel("Username"),"gaptop 20px, gapleft 10px");
		panel.add(username,"gaptop 20px, gapleft 10px");
		panel.add(new JLabel("Password"),"gaptop 20px, gapleft 10px");
		panel.add(password,"gaptop 20px, gapleft 10px");
		panel.add(new JLabel("NPI"),"gaptop 20px, gapleft 10px");
		panel.add(npi,"gaptop 20px, gapleft 10px,wrap");
		panel.add(new JLabel("Private Only"),"gaptop 20px, gapleft 10px");
		panel.add(privateOnly,"gaptop 20px, gapleft 10px");
		panel.add(new JLabel("Pause"),"gaptop 20px, gapleft 10px");
		panel.add(pause,"gaptop 20px, gapleft 5px");
		panel.add(save,"gaptop 20px, gapleft 10px");
	}
	private String getPassword() {
		StringBuilder pass = new StringBuilder();
    	for(char c: password.getPassword())
    		pass.append(c);
    	return pass.toString();
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		Emdeon emd = new Emdeon();
		emd.setNpi(npi.getText());
		emd.setUsername(username.getText());
		emd.setPassword(getPassword());
		emd.setPause(pause.getValue().toString());
		emd.setPrivateOnly(""+privateOnly.isSelected());
		EmdeonProperties properties = new EmdeonProperties();
		properties.saveEmdeonProperties(emd);
		this.dispose();
	}

}
