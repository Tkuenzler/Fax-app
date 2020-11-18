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
		public static final String RXPLUS = "Rx Plus";
		public static final String RXPLUS2 = "Rx Plus 2";
		public static final String RXPLUS_CAREMARK = "Rx Plus Caremark";
		public static final String ANTI_FUNGAL = "Anti-Fungal";
		public static final String SINGLE_PRODUCT = "Single Product";
		public static final String PBM_SCRIPT = "PBM Script";
		public static final String COVERED_SCRIPT = "Covered Scripts";
		public static final String DME_SCRIPT = "DME Script";
	}
	private HashMap<String,FaxNumber> faxNumbers;
	private JPanel contentPane;
	private boolean singleScript = false;
	JComboBox<String> comboBox = new JComboBox<String>();
	JCheckBox notes = new JCheckBox("With Notes",false);
	//Scripts
	JRadioButton drChase = new JRadioButton(ScriptNames.DR_CHASE,false);
	JRadioButton rxPlusScript = new JRadioButton(ScriptNames.RXPLUS,false);
	JRadioButton rxPlus2Script = new JRadioButton(ScriptNames.RXPLUS2,false);
	JRadioButton rxPlusCaremarkScript = new JRadioButton(ScriptNames.RXPLUS_CAREMARK,false);
	JRadioButton antiFungalScript = new JRadioButton(ScriptNames.ANTI_FUNGAL,false);
	JRadioButton singleProduct = new JRadioButton(ScriptNames.SINGLE_PRODUCT,false);
	JRadioButton pbmScript = new JRadioButton(ScriptNames.PBM_SCRIPT,false);
	JRadioButton coveredScript = new JRadioButton(ScriptNames.COVERED_SCRIPT,false);
	JRadioButton customScript = new JRadioButton(ScriptNames.CUSTOM_SCRIPT,false);
	JRadioButton dmeScript = new JRadioButton(ScriptNames.DME_SCRIPT,false);
	
	ButtonGroup scripts = new ButtonGroup();
	
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
		
		rxPlusScript.setBounds(10,60,120 ,20);
		scriptPanel.add(rxPlusScript);
		
		rxPlus2Script.setBounds(10,80,120 ,20);
		scriptPanel.add(rxPlus2Script);
		
		rxPlusCaremarkScript.setBounds(10,100,120 ,20);
		scriptPanel.add(rxPlusCaremarkScript);

		antiFungalScript.setBounds(10,120,120 ,20);
		scriptPanel.add(antiFungalScript);
		
		pbmScript.setBounds(10,140,100 ,20);
		scriptPanel.add(pbmScript);
		
		customScript.setBounds(10,160,120 ,20);
		scriptPanel.add(customScript);
		
		dmeScript.setBounds(10,180,120 ,20);
		scriptPanel.add(dmeScript);
		
		coveredScript.setBounds(10,200,120 ,20);
		scriptPanel.add(coveredScript);
		
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
						case ScriptNames.RXPLUS:
							fax.setPharmacy(fax.getRxPlusScript());
							script = new Script(fax,true);
							break;
						case ScriptNames.RXPLUS2:
							fax.setPharmacy(fax.getRxPlusScript2());
							script = new Script(fax,true);
							break;
						case ScriptNames.RXPLUS_CAREMARK:
							fax.setPharmacy(fax.getRxPlusCaremark());
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
						case ScriptNames.DME_SCRIPT:
							fax.setPharmacy(fax.getDMEScript());
							script = new Script(fax,true);
							break;
						case ScriptNames.COVERED_SCRIPT:
							fax.setPharmacy(fax.getCoveredScript());
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
		scripts.add(drChase);
		scripts.add(rxPlusScript);
		scripts.add(rxPlus2Script);
		scripts.add(rxPlusCaremarkScript);
		scripts.add(singleProduct);
		scripts.add(pbmScript);
		scripts.add(customScript);
		scripts.add(antiFungalScript);
		scripts.add(dmeScript);
		scripts.add(coveredScript);
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
}
