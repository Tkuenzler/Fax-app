package DME;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Database.Database;

public class SetDMEFaxDisposition implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Database client = new Database("MT_MARKETING");
		try {
			if(!client.login()) {
				JOptionPane.showMessageDialog(null, "FAILED TO LOG IN");
				return;
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			if(client!=null) client.close();
		}
	}
}
