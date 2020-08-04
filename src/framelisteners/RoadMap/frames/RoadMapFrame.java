package framelisteners.RoadMap.frames;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import Clients.RoadMapClient;

public class RoadMapFrame extends JFrame {
	RoadMapClient client;
	JComboBox<String> pharmacies = new JComboBox<String>();
	JButton save = new JButton("Save");
	public RoadMapFrame() {
		super("Road Map ");
		client = new RoadMapClient();
		String[] pharmacies = client.getPharmacies();
		this.pharmacies.setModel(new DefaultComboBoxModel<String>(pharmacies));
		client.close();
		this.setBounds(100, 100, 600, 600);
		this.add(this.pharmacies);
		setVisible(true);
		setResizable(false);
	}
}
