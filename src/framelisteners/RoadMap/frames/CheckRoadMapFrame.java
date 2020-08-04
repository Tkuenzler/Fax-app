package framelisteners.RoadMap.frames;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Clients.RoadMapClient;

public class CheckRoadMapFrame extends JFrame implements ActionListener {
	JComboBox<String> pbmBox = null;
	JComboBox<String> insuranceTypeBox = null;
	JComboBox<String> statesBox = null;
	JComboBox<String> pharmaciesBox = null;
	JButton submit = new JButton("Submit");
	String[] pbms = null;
	String[] insurance_types = null;
	RoadMapClient client = null;
	public CheckRoadMapFrame() {
		this.setTitle("Check Road Map");
		Box bh = Box.createHorizontalBox();
		client = new RoadMapClient();
		pbms = client.getPbms();
		insurance_types = new String[]{"Commercial","Medicare"};
		String[] states = client.get50States();
		client.close();
		pbmBox = new JComboBox<String>(pbms);
		insuranceTypeBox = new JComboBox<String>(insurance_types);
		statesBox = new JComboBox<String>(states);
		pharmaciesBox = new JComboBox<String>();
		submit.addActionListener(this);
		bh.add(new JLabel("PBM:"));
		bh.add(Box.createRigidArea(new Dimension(10,10)));
		bh.add(pbmBox);
		bh.add(Box.createRigidArea(new Dimension(10,10)));
		bh.add(new JLabel("Insurance Type:"));
		bh.add(Box.createRigidArea(new Dimension(10,10)));
		bh.add(insuranceTypeBox);
		bh.add(Box.createRigidArea(new Dimension(10,10)));
		bh.add(new JLabel("State:"));
		bh.add(Box.createRigidArea(new Dimension(10,10)));
		bh.add(statesBox);
		bh.add(Box.createRigidArea(new Dimension(10,10)));
		bh.add(new JLabel("Pharmacies:"));
		bh.add(Box.createRigidArea(new Dimension(10,10)));
		bh.add(pharmaciesBox);
		bh.add(Box.createRigidArea(new Dimension(10,10)));
		bh.add(submit);
		bh.add(Box.createRigidArea(new Dimension(10,10)));
		Container cp = this.getContentPane();
		cp.add(BorderLayout.SOUTH, bh);
		this.setSize(1000, 100);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		pharmaciesBox.removeAllItems();
		client = new RoadMapClient();
		String pbm = pbmBox.getItemAt(pbmBox.getSelectedIndex());
		String state = statesBox.getItemAt(statesBox.getSelectedIndex());
		String insurance_type = insuranceTypeBox.getItemAt(insuranceTypeBox.getSelectedIndex());
		String[] pharmacies = client.GetPharmacies(pbm, state, insurance_type);
		if(pharmacies.length==0)
			pharmaciesBox.addItem("No Home");
		for(String s: pharmacies)
			pharmaciesBox.addItem(s);
		repaint();
	}
}
