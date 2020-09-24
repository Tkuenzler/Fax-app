package framelisteners.settings.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Properties.FaxProperties;
import net.miginfocom.swing.MigLayout;
import objects.Fax;
import subframes.FileChooser;

public class FaxSettingsFrame extends JFrame  {
	JTextField drChaseLocation = new JTextField(50);
	JTextField painScriptLocation = new JTextField(50);
	JTextField scarScriptLocation = new JTextField(50);
	JTextField skinScriptLocation = new JTextField(50);
	JTextField antiFungalScriptLocation = new JTextField(50);
	JTextField singleProductScriptLocation = new JTextField(50);
	JTextField pbmScriptLocation = new JTextField(50);
	JTextField customScriptLocation = new JTextField(50);
	JTextField coveredScriptLocation = new JTextField(50);
	JTextField dmeScriptLocation = new JTextField(50);
	
	//maintenance
	JTextField savedLocation = new JTextField(50);
	JTextField company = new JTextField(50);
	JButton browseDrChaseLocation = new JButton("Load Dr Chase Script");
	JButton browsePainLocation = new JButton("Load Pain Script");
	JButton browseScarLocation = new JButton("Load Scar Script");
	JButton browseSkinLocation = new JButton("Load Skin Script");
	JButton browseAntiFungalLocation = new JButton("Load Anti-Fungal Script");
	JButton browseSingleProductScriptLocation = new JButton("Load Sinlge Product Script");
	JButton browsePbmScriptLocation = new JButton("Load PBM Script");
	JButton browseCoveredScriptLocation = new JButton("Load Covered Script");
	JButton browseCustomScriptLocation = new JButton("Load Custom Script");
	JButton browseDMEScriptLocation = new JButton("Load DME Script");
	
	
	JButton browseSavedLocation = new JButton("Save Script");
	JButton save = new JButton("Save");
	Fax fax;
	public FaxSettingsFrame() {
		setBounds(100, 100, 800, 550);
		setTitle("Fax Settings");
		drChaseLocation.setEditable(false);
		singleProductScriptLocation.setEditable(false);
		painScriptLocation.setEditable(false);
		scarScriptLocation.setEditable(false);
		skinScriptLocation.setEditable(false);
		antiFungalScriptLocation.setEditable(false);
		pbmScriptLocation.setEditable(false);
		customScriptLocation.setEditable(false);
		dmeScriptLocation.setEditable(false);
		coveredScriptLocation.setEditable(false);
		loadSettings();
		setUpPanel();
		browseSingleProductScriptLocation.addActionListener(new BrowseSingleProductScript());
		browseDrChaseLocation.addActionListener(new BrowseDrChase());
		browseSavedLocation.addActionListener(new BrowseSaved());
		browsePbmScriptLocation.addActionListener(new BrowsePBMScript());
		browsePainLocation.addActionListener(new BrowsePain());
		browseSkinLocation.addActionListener(new BrowseSkin());
		browseScarLocation.addActionListener(new BrowseScar());
		browseAntiFungalLocation.addActionListener(new BrowseAntiFungal());
		browseCustomScriptLocation.addActionListener(new BrowseCustomScript());
		browseDMEScriptLocation.addActionListener(new BrowseDMEScript());
		browseCoveredScriptLocation.addActionListener(new BrowseCoveredScript());
		save.addActionListener(new Save());
		setVisible(true);
	}
	private void loadSettings() {
		FaxProperties properties  = new FaxProperties();
		fax = properties.getFaxProperties();
		drChaseLocation.setText(fax.getDrChaseScript());
		painScriptLocation.setText(fax.getPainScript());
		scarScriptLocation.setText(fax.getScarScript());
		skinScriptLocation.setText(fax.getSkinScript());
		coveredScriptLocation.setText(fax.getCoveredScript());
		antiFungalScriptLocation.setText(fax.getAntiFungalScript());
		pbmScriptLocation.setText(fax.getPbmScript());
		savedLocation.setText(fax.getSaveLocation());
		singleProductScriptLocation.setText(fax.getSingleProductScript());
		customScriptLocation.setText(fax.getCustomScript());
		dmeScriptLocation.setText(fax.getDMEScript());
		company.setText(fax.getCompany());
	}
	private void setUpPanel() {
		JPanel panel = new JPanel();
		panel.setBounds(10, 10, 560, 170);
		panel.setBorder(BorderFactory.createLoweredBevelBorder());
		getContentPane().add(panel);
	    JPanel panel2 = new JPanel();
	    JPanel panel3 = new JPanel();
	    panel2.setLayout(new MigLayout());
	    panel3.setLayout(new MigLayout());
	    panel2.setBounds(10, 10, 540, 50);
	    panel3.setBounds(10, 60, 540, 75);
	    
	    panel2.add(browseDrChaseLocation,"gaptop 10px, gapleft 10px");
	    panel2.add(drChaseLocation,"gaptop 10px, gapleft 5px, wrap");
	    
	    panel2.add(browseSingleProductScriptLocation,"gaptop 10px, gapleft 10px");
	    panel2.add(singleProductScriptLocation,"gaptop 10px, gapleft 5px, wrap");
	    
	    panel2.add(browsePainLocation,"gaptop 10px, gapleft 10px");
	    panel2.add(painScriptLocation,"gaptop 10px, gapleft 5px, wrap");
	    
	    panel2.add(browseScarLocation,"gaptop 10px, gapleft 10px");
	    panel2.add(scarScriptLocation,"gaptop 10px, gapleft 5px, wrap");
	    
	    panel2.add(browseSkinLocation,"gaptop 10px, gapleft 10px");
	    panel2.add(skinScriptLocation,"gaptop 10px, gapleft 5px, wrap");
	    
	    panel2.add(browseAntiFungalLocation,"gaptop 10px, gapleft 10px");
	    panel2.add(antiFungalScriptLocation,"gaptop 10px, gapleft 5px, wrap");
	    
	    panel2.add(browsePbmScriptLocation,"gaptop 10px, gapleft 10px");
	    panel2.add(pbmScriptLocation,"gaptop 10px, gapleft 5px, wrap");
	    
	    panel2.add(browseCustomScriptLocation,"gaptop 10px, gapleft 10px");
	    panel2.add(customScriptLocation,"gaptop 10px, gapleft 5px, wrap");
	    
	    panel2.add(browseSavedLocation,"gaptop 10px, gapleft 10px");
	    panel2.add(savedLocation,"gaptop 10px, gapleft 5px, wrap");
	    
	    panel2.add(browseDMEScriptLocation,"gaptop 10px, gapleft 10px");
	    panel2.add(dmeScriptLocation,"gaptop 10px, gapleft 5px, wrap");
	    
	    panel2.add(browseCoveredScriptLocation,"gaptop 10px, gapleft 10px");
	    panel2.add(coveredScriptLocation,"gaptop 10px, gapleft 5px, wrap");
	
		panel2.add(new JLabel("Company: "), "gaptop 10px, gapleft 10px");
		panel2.add(company, "gaptop 10px, gapleft 5px");
		
		panel3.add(save,"cell 4 1, gaptop 10px");
		panel.add(panel2);
		panel.add(panel3);

	}
	private class Save implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			Fax fax = new Fax();
			fax.setDrChaseScript(drChaseLocation.getText());
			fax.setPainScript(painScriptLocation.getText());
			fax.setScarScript(scarScriptLocation.getText());
			fax.setSkinScript(skinScriptLocation.getText());
			fax.setAntiFungalScript(antiFungalScriptLocation.getText());
			fax.setSingleProductScript(singleProductScriptLocation.getText());
			fax.setPbmScript(pbmScriptLocation.getText());
			fax.setCustomScript(customScriptLocation.getText());
			fax.setSaveLocation(savedLocation.getText());		
			fax.setDMEScript(dmeScriptLocation.getText());
			fax.setCoveredScript(coveredScriptLocation.getText());
			FaxProperties properties = new FaxProperties();
			properties.saveFaxProperties(fax);
			dispose();
		} 
	}
	private class BrowseCoveredScript implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String file = FileChooser.OpenPdfFile("Open Covered Script");
			if(file!=null) 
				coveredScriptLocation.setText(file);	
		}
	}
	private class BrowseDMEScript implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String folder = FileChooser.OpenPdfFile("Open DME Script");
			if(folder!=null) 
				dmeScriptLocation.setText(folder);	
		}
	}
	private class BrowseCustomScript implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String pdf = FileChooser.OpenPdfFile("Open Custom Script");
			if(pdf!=null) 
				customScriptLocation.setText(pdf);	
		}
	}
	private class BrowseSingleProductScript implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String folder = FileChooser.OpenPdfFile("Open Sinlge Product Script");
			if(folder!=null) 
				singleProductScriptLocation.setText(folder);	
		}
	}
	private class BrowsePBMScript implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String folder = FileChooser.OpenFolder("Open PBM Script");
			if(folder!=null) 
				pbmScriptLocation.setText(folder);	
		}
	}
	private class BrowseDrChase implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String pdf = FileChooser.OpenPdfFile("Open Tristar Script");
			if(pdf!=null) 
				drChaseLocation.setText(pdf);	
		}
	}
	private class BrowsePain implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String pdf = FileChooser.OpenPdfFile("Open Pain Script");
			if(pdf!=null) 
				painScriptLocation.setText(pdf);	
		}
	}
	private class BrowseScar implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String pdf = FileChooser.OpenPdfFile("Open Scar Script");
			if(pdf!=null) 
				scarScriptLocation.setText(pdf);	
		}
	}
	private class BrowseSkin implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String pdf = FileChooser.OpenPdfFile("Open Skin Script");
			if(pdf!=null) 
				skinScriptLocation.setText(pdf);	
		}
	}
	private class BrowseAntiFungal implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String pdf = FileChooser.OpenPdfFile("Open Anti-Fungal Script");
			if(pdf!=null) 
				antiFungalScriptLocation.setText(pdf);	
		}
	}
	private class BrowseSaved implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String folder = FileChooser.OpenFolder("Open Saved Location");
			if(folder!=null)
				savedLocation.setText(folder);
		}
	}
	
}