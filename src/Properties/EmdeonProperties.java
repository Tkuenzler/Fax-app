package Properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Properties;
import objects.Emdeon;
import source.Main;

public class EmdeonProperties {
	//Emdeon Keys
	private final String NPI = "npi";
	private final String EMDEON_USERNAME = "emdeon_username";
	private final String EMDEON_PASSWORD = "emdeon_password";
	private final String PRIVATE_ONLY = "private_only";
	private final String PAUSE = "pause";
	private final String AUTO_EXPORT = "auto_export";
	private String fileName  = null;
	private String FOLDER = null;
	Properties prop = new Properties();
	public EmdeonProperties() {
		try {
			FOLDER = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			fileName = FOLDER+"\\..\\Emdeon.properties";
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public void saveEmdeonProperties(Emdeon emd) {
		OutputStream out = null;
		try {
			File file = new File(fileName);
			if(!file.exists())
				file.createNewFile();
			out = new FileOutputStream(fileName);
			prop.setProperty(NPI, emd.getNpi());
			prop.setProperty(EMDEON_USERNAME,emd.getUsername());
			prop.setProperty(EMDEON_PASSWORD, emd.getPassword());
			prop.setProperty(PAUSE, ""+emd.getPause());
			prop.setProperty(PRIVATE_ONLY, ""+emd.isPrivateOnly());
			prop.setProperty(AUTO_EXPORT, ""+emd.isAutoExport());
			prop.store(out, null);
		}catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public Emdeon getEmdeonProperties() {
		InputStream in = null;
		Emdeon emd = null;
		try {
			// load a properties file
			File file = new File(fileName);
			if(!file.exists())
				file.createNewFile();
			in = new FileInputStream(fileName);
			prop.load(in);
			emd = new Emdeon();
			emd.setNpi(prop.getProperty(NPI));
			emd.setUsername(prop.getProperty(EMDEON_USERNAME));
			emd.setPassword(prop.getProperty(EMDEON_PASSWORD));
			emd.setPause(prop.getProperty(PAUSE));
			emd.setPrivateOnly(prop.getProperty(PRIVATE_ONLY));
			emd.setAutoExport(prop.getProperty(AUTO_EXPORT));
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return emd;
	}
}
