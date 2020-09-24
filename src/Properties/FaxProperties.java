package Properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import objects.Fax;
import source.Main;

public class FaxProperties {
	private final String DR_CHASE_SCRRIPT = "dr_chase_script";
	private final String SINGLE_PRODUCT_SCRIPT = "single_product_script"; 
	private final String CUSTOM_SCRIPT = "custom_script";
	private final String PAIN_SCRIPT = "pain_script";
	private final String SKIN_SCRIPT = "skin_script";
	private final String SCAR_SCRIPT = "scar_script";
	private final String ANTI_FUNGAL_SCRIPT = "anti_fungal_script";
	private final String PBM_SCRIPT = "pbm_script";
	private final String DME_SCRIPT = "dme_script";
	private final String COVERED_SCRIPT = "covered_script";
	private final String SAVE_LOCATION = "save";
	private final String PAIN = "pain";
	private final String DERM = "derm";
	private final String PHARMACY = "pharmacy";
	private String fileName = null;
	public FaxProperties() {
		try {
			String FOLDER = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			fileName = FOLDER+"\\..\\Fax.properties";
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(new JFrame(),e.getMessage());
		}
	}
	public void saveFaxProperties(Fax fax) {
		Properties prop = new Properties();
		OutputStream out = null;
		try {
			File file = new File(fileName);
			if(!file.exists())
				file.createNewFile();
			out = new FileOutputStream(fileName);
			
			if(prop.containsKey(COVERED_SCRIPT))
				prop.setProperty(COVERED_SCRIPT,fax.getCoveredScript());
			else
				prop.put(COVERED_SCRIPT, fax.getCoveredScript());
			
			if(prop.containsKey(DR_CHASE_SCRRIPT))
				prop.setProperty(DR_CHASE_SCRRIPT,fax.getDrChaseScript());
			else
				prop.put(DR_CHASE_SCRRIPT, fax.getDrChaseScript());
			
			if(prop.containsKey(CUSTOM_SCRIPT))
				prop.setProperty(CUSTOM_SCRIPT,fax.getCustomScript());
			else
				prop.put(CUSTOM_SCRIPT, fax.getCustomScript());
			
			//
			if(prop.containsKey(DME_SCRIPT))
				prop.setProperty(DME_SCRIPT,fax.getDMEScript());
			else
				prop.put(DME_SCRIPT, fax.getDMEScript());
			
			if(prop.containsKey(PAIN_SCRIPT))
				prop.setProperty(PAIN_SCRIPT,fax.getPainScript());
			else
				prop.put(PAIN_SCRIPT, fax.getPainScript());
			
			if(prop.containsKey(SKIN_SCRIPT))
				prop.setProperty(SKIN_SCRIPT,fax.getSkinScript());
			else
				prop.put(SKIN_SCRIPT, fax.getSkinScript());
			
			if(prop.containsKey(SCAR_SCRIPT))
				prop.setProperty(SCAR_SCRIPT,fax.getScarScript());
			else
				prop.put(SCAR_SCRIPT, fax.getScarScript());
			
			if(prop.containsKey(ANTI_FUNGAL_SCRIPT))
				prop.setProperty(ANTI_FUNGAL_SCRIPT,fax.getAntiFungalScript());
			else
				prop.put(ANTI_FUNGAL_SCRIPT, fax.getAntiFungalScript());
			
			
			if(prop.containsKey(SINGLE_PRODUCT_SCRIPT))
				prop.setProperty(SINGLE_PRODUCT_SCRIPT,fax.getSingleProductScript());
			else
				prop.put(SINGLE_PRODUCT_SCRIPT,fax.getSingleProductScript());
			
			if(prop.containsKey(PBM_SCRIPT))
				prop.setProperty(PBM_SCRIPT,fax.getPbmScript());
			else
				prop.put(PBM_SCRIPT,fax.getPbmScript());
			
			if(prop.containsKey(SAVE_LOCATION))
				prop.setProperty(SAVE_LOCATION,fax.getSaveLocation());
			else
				prop.put(SAVE_LOCATION, fax.getSaveLocation());
			
			if(prop.containsKey(PAIN))
				prop.setProperty(PAIN, ""+fax.isPain());
			else
				prop.put(PAIN, ""+fax.isPain());
			
			if(prop.containsKey(DERM))
				prop.setProperty(DERM, ""+fax.isDerm());
			else
				prop.put(DERM,""+fax.isDerm());
			
			if(prop.containsKey(PHARMACY))
				prop.setProperty(PHARMACY, ""+fax.getPharmacy());
			else
				prop.put(PHARMACY, fax.getPharmacy());
			
			prop.store(out, null);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(new JFrame(),e.getMessage());
				}
			}
		}
	}
	public Fax getFaxProperties() {
		Properties prop = new Properties();
		InputStream in = null;
		Fax fax = null;
		try {
			File file = new File(fileName);
			if(!file.exists())
				file.createNewFile();
			in = new FileInputStream(fileName);
			// load a properties file
			prop.load(in);
			fax = new Fax();
			fax.setDrChaseScript(prop.getProperty(DR_CHASE_SCRRIPT));
			fax.setCustomScript(prop.getProperty(CUSTOM_SCRIPT));
			fax.setDMEScript(prop.getProperty(DME_SCRIPT));
			fax.setCoveredScript(prop.getProperty(COVERED_SCRIPT));
			fax.setPainScript(prop.getProperty(PAIN_SCRIPT));
			fax.setSkinScript(prop.getProperty(SKIN_SCRIPT));
			fax.setScarScript(prop.getProperty(SCAR_SCRIPT));
			fax.setAntiFungalScript(prop.getProperty(ANTI_FUNGAL_SCRIPT));
			fax.setSaveLocation(prop.getProperty(SAVE_LOCATION));
			fax.setSingleProductScript(prop.getProperty(SINGLE_PRODUCT_SCRIPT));
			fax.setPbmScript(prop.getProperty(PBM_SCRIPT));
			fax.setPain(Boolean.parseBoolean(prop.getProperty(PAIN)));
			fax.setDerm(Boolean.parseBoolean(prop.getProperty(DERM)));
			fax.setPharmacy((prop.getProperty(PHARMACY)));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(),e.getMessage());
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(new JFrame(),e.getMessage());
				}
			}
		}
		return fax;
	}
}