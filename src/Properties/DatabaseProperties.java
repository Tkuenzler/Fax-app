package Properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Properties;

import objects.Database;
import objects.Emdeon;
import source.Main;

public class DatabaseProperties {
	private final String LOCAL_HOST = "local_host";
	private final String PORT = "port";
	private final String DATABASE = "database";
	private final String USERNAME = "username";
	private final String PASSWORD = "password";
	private String fileName = null;
	public DatabaseProperties() {
		try {
			String FOLDER = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			fileName = FOLDER+"\\..\\Database.properties";
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public void saveDatabaseProperties(Database db) {
		Properties prop = new Properties();
		OutputStream out = null;
		try {
			File file = new File(fileName);
			if(!file.exists())
				file.createNewFile();
			out = new FileOutputStream(fileName);
			prop.setProperty(LOCAL_HOST,db.getLocalHost());
			prop.setProperty(PORT, db.getPort());
			prop.setProperty(DATABASE, db.getDatabase());
			prop.setProperty(USERNAME, db.getUsername());
			prop.setProperty(PASSWORD, db.getPassword());
			prop.store(out, null);
		} catch (IOException io) {
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
	public Database getDatabaseProperties() {
		Properties prop = new Properties();
		InputStream in = null;
		Database db = null;
		try {
			File file = new File(fileName);
			if(!file.exists())
				file.createNewFile();
			in = new FileInputStream(fileName);
			// load a properties file
			prop.load(in);
			db = new Database();
			db.setDatabase(prop.getProperty(DATABASE));
			db.setPort(prop.getProperty(PORT));
			db.setLocalHost(prop.getProperty(LOCAL_HOST));
			db.setUsername(prop.getProperty(USERNAME));
			db.setPassword(prop.getProperty(PASSWORD));
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
		return db;
	}
}
