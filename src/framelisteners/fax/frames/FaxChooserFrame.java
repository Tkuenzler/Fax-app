package framelisteners.fax.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import org.json.JSONException;

import Fax.Drug;
import Info.FaxNumber;
import Properties.FaxProperties;
import images.Script;
import images.Script.ScriptException;
import objects.Fax;
import source.CSVFrame;
import source.LoadInfo;
import table.Record;

public class FaxChooserFrame extends JFrame {
	private class ScriptNames {
		public static final String DR_CHASE = "Dr Chase";
		public static final String CUSTOM_SCRIPT = "Custom Script";
		public static final String PAIN = "Pain";
		public static final String SCAR = "Scar";
		public static final String SKIN = "Skin";
		public static final String ANTI_FUNGAL = "Anti-Fungal";
		public static final String SINGLE_PRODUCT = "Single Product";
		public static final String PBM_SCRIPT = "PBM Script";
		public static final String LIVE_SCRIPT = "Live Script";
		public static final String DME_SCRIPT = "DME Script";
	}
	private HashMap<String,FaxNumber> faxNumbers;
	private JPanel contentPane;
	private boolean singleScript = false;
	JComboBox<String> comboBox = new JComboBox<String>();
	JCheckBox notes = new JCheckBox("With Notes",false);
	//Scripts
	JRadioButton drChase = new JRadioButton(ScriptNames.DR_CHASE,false);
	JRadioButton painScript = new JRadioButton(ScriptNames.PAIN,false);
	JRadioButton scarScript = new JRadioButton(ScriptNames.SCAR,false);
	JRadioButton skinScript = new JRadioButton(ScriptNames.SKIN,false);
	JRadioButton antiFungalScript = new JRadioButton(ScriptNames.ANTI_FUNGAL,false);
	JRadioButton singleProduct = new JRadioButton(ScriptNames.SINGLE_PRODUCT,false);
	JRadioButton pbmScript = new JRadioButton(ScriptNames.PBM_SCRIPT,false);
	JRadioButton liveScript = new JRadioButton(ScriptNames.LIVE_SCRIPT,false);
	JRadioButton customScript = new JRadioButton(ScriptNames.CUSTOM_SCRIPT,false);
	JRadioButton dmeScript = new JRadioButton(ScriptNames.DME_SCRIPT,false);
	
	ButtonGroup scripts = new ButtonGroup();
	//Products
	JCheckBox pain = new JCheckBox("Pain",true);
	JCheckBox derm = new JCheckBox("Derm",true);
	JCheckBox acid = new JCheckBox("Acid",true);
	JCheckBox vitamins = new JCheckBox("Vitamins",true);
	
	JButton btnFax = new JButton("Fax");
	Vector<Record> data;
	/**
	 * Create the frame.
	 */
	public FaxChooserFrame(Vector<Record> data) {
		try {
			faxNumbers = LoadInfo.LoadFaxNumbers();
		} catch (URISyntaxException | JSONException | IOException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e1.getMessage());
			return;
		}
		this.data = data;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 350);
		setVisible(true);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		comboBox.setModel(new DefaultComboBoxModel<String>());
		comboBox.setBounds(75, 10, 150, 20);
		contentPane.add(comboBox);
		
		notes.setBounds(75,30,120,20);
		contentPane.add(notes);
		
		//Scripts
		setScripts();
		JPanel scriptPanel = new JPanel();
		scriptPanel.setLayout(null);
		scriptPanel.setBorder(BorderFactory.createTitledBorder("Scripts"));
		scriptPanel.setBounds(10,50,160,230);
		
		drChase.setBounds(10,20,120,20);
		scriptPanel.add(drChase);
		
		singleProduct.setBounds(10,40,120 ,20);
		scriptPanel.add(singleProduct);
		
		painScript.setBounds(10,60,120 ,20);
		scriptPanel.add(painScript);

		scarScript.setBounds(10,80,120 ,20);
		scriptPanel.add(scarScript);
		
		skinScript.setBounds(10,100,120 ,20);
		scriptPanel.add(skinScript);
		
		antiFungalScript.setBounds(10,120,120 ,20);
		scriptPanel.add(antiFungalScript);
		
		pbmScript.setBounds(10,140,120 ,20);
		scriptPanel.add(pbmScript);
		
		customScript.setBounds(10,160,120 ,20);
		scriptPanel.add(customScript);
		
		liveScript.setBounds(10,180,120 ,20);
		scriptPanel.add(liveScript);
		
		dmeScript.setBounds(10,200,120 ,20);
		scriptPanel.add(dmeScript);
		
		JPanel productPanel = new JPanel();
		productPanel.setLayout(null);
		productPanel.setBorder(BorderFactory.createTitledBorder("Products"));
		productPanel.setBounds(150, 50, 140, 230);
		
		pain.setBounds(10, 20, 120, 20);
		productPanel.add(pain);
		
		derm.setBounds(10, 40, 120, 20);
		productPanel.add(derm);
		
		acid.setBounds(10,60,120,20);
		productPanel.add(acid);
		
		vitamins.setBounds(10,80,120,20);
		productPanel.add(vitamins);
		
		contentPane.add(productPanel);
		contentPane.add(scriptPanel);
		btnFax.setBounds(75, 280, 125, 20);
		for(String s: faxNumbers.keySet()) {
			comboBox.addItem(s);
		}
		getPharmacy();
		btnFax.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Fax fax = new FaxProperties().getFaxProperties();
				String faxLine = comboBox.getItemAt(comboBox.getSelectedIndex());
				String pharmacy = getPharmacy();
				if(pharmacy==null) {
					JOptionPane.showMessageDialog(null, "No Pharmacy Selected");
					return;
				}
				int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to fax on "+faxLine+" on "+pharmacy+" script?", pharmacy, JOptionPane.YES_NO_OPTION);
				if(confirm==JOptionPane.NO_OPTION)
					return;
				fax.setPain(pain.isSelected());
				fax.setDerm(derm.isSelected());
				fax.setAcid(acid.isSelected());
				fax.setVitamins(vitamins.isSelected());
				try {						
					Script script;
					switch(pharmacy) {
						case ScriptNames.DR_CHASE:
							fax.setPharmacy(fax.getDrChaseScript());
							script = new Script(fax,true);
							break;
						case ScriptNames.CUSTOM_SCRIPT:
							fax.setPharmacy(fax.getCustomScript());
							script = new Script(fax,true);
							script.SetDrugs(Drug.GetDrug(Drug.GetADrug("What is the first medication?")),Drug.GetDrug(Drug.GetADrug("What is the second medication?")));
							break;
						case ScriptNames.PAIN:
							fax.setPharmacy(fax.getPainScript());
							script = new Script(fax,true);
							break;
						case ScriptNames.SCAR:
							fax.setPharmacy(fax.getScarScript());
							script = new Script(fax,true);
							break;
						case ScriptNames.SKIN:
							fax.setPharmacy(fax.getSkinScript());
							script = new Script(fax,true);
							break;
						case ScriptNames.ANTI_FUNGAL:
							fax.setPharmacy(fax.getAntiFungalScript());
							script = new Script(fax,true);
							break;
						case ScriptNames.SINGLE_PRODUCT:
							fax.setPharmacy(fax.getSingleProductScript());
							singleScript = true;
							script = new Script(fax,true);
							break;
						case ScriptNames.PBM_SCRIPT:
							fax.setPharmacy(fax.getPbmScript());
							script = new Script(fax,false);
							break;
						case ScriptNames.LIVE_SCRIPT:
							fax.setPharmacy(fax.getLiveScript());
							script = new Script(fax,true);
							break;
						case ScriptNames.DME_SCRIPT:
							fax.setPharmacy(fax.getDMEScript());
							script = new Script(fax,true);
							break;
						default:
							JOptionPane.showMessageDialog(null, "Unknown pharmacy");
							return;
					}
					new FaxProperties().saveFaxProperties(fax);
					FaxNumber faxNumber = faxNumbers.get(comboBox.getSelectedItem());
					fax.setLogin(faxNumber.getNumber());
					fax.setPassword(faxNumber.getPassword());
					if(notes.isSelected())
						fax.setNotes(JOptionPane.showInputDialog("Notes:"));
					else
						fax.setNotes("");
					new FaxClientFrame(fax,data,faxNumber,script,singleScript); 
					CSVFrame.rename("FAXING WITH "+faxLine+" ON "+pharmacy+" SCRIPT");
				} catch (ScriptException | IOException | JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				dispose();
			}
		});
		contentPane.add(btnFax);
	}
	private void setScripts() {
		drChase.addActionListener(new ScriptListener());
		painScript.addActionListener(new ScriptListener());
		scarScript.addActionListener(new ScriptListener());
		skinScript.addActionListener(new ScriptListener());		
		antiFungalScript.addActionListener(new ScriptListener());
		singleProduct.addActionListener(new ScriptListener());
		pbmScript.addActionListener(new ScriptListener());
		customScript.addActionListener(new ScriptListener());
		liveScript.addActionListener(new ScriptListener());
		dmeScript.addActionListener(new ScriptListener());
		scripts.add(drChase);
		scripts.add(painScript);
		scripts.add(scarScript);
		scripts.add(skinScript);
		scripts.add(singleProduct);
		scripts.add(pbmScript);
		scripts.add(customScript);
		scripts.add(liveScript);
		scripts.add(antiFungalScript);
		scripts.add(dmeScript);
	}
	private String getPharmacy() {
		Enumeration<AbstractButton> buttons = scripts.getElements();
		while(buttons.hasMoreElements()) {
			JRadioButton button = (JRadioButton) buttons.nextElement();
			if(button.isSelected())
				return button.getText();
		}
		return null;
	}
	private class ScriptListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(drChase.isSelected() || liveScript.isSelected()) {
				derm.setSelected(true);
				pain.setSelected(true);
				acid.setSelected(true);
				vitamins.setSelected(true);
			}
			else {
				derm.setSelected(false);
				pain.setSelected(false);
				acid.setSelected(false);
				vitamins.setSelected(false);
			}
		}
	}
}
