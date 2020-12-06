package source;


import java.awt.HeadlessException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;


public class Main {
	
	public static void main(String[] args) throws ClientProtocolException, IOException, JSONException, SQLException {			
		//FCCClient.GetComplaintsFromNumbers();
		//EZScriptRxClient rxClient = new EZScriptRxClient();
		//rxClient.Login("allfamilyrheem", "@11Fam1lyPh@rm%");
		try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {					
					try {
						UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						if(Checker.check(LoadInfo.getAppName())) {
							
							new CSVFrame();
						}
						else {
							JOptionPane.showMessageDialog(new JFrame(), "Error 4503 has occured.");
							System.exit(0);
						}
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException| UnsupportedLookAndFeelException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(new JFrame(), e.getMessage());				
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, e.getMessage());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, e.getMessage());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
			    	
			    }
			});		
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}	
}