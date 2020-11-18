package framelisteners.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import Database.Database;
import Database.Columns.AgentsColumns;
import Database.Tables.Tables;
import subframes.FileChooser;

public class AddStationIds implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String password = JOptionPane.showInputDialog("What is the password?");
		if(!password.equalsIgnoreCase("Winston4503"))
			return;
		File file = FileChooser.OpenTxtFile("Station Id File");
		Database client = new Database("Info_Table");
		BufferedReader br = null;
		try {
			if(!client.login())
				JOptionPane.showMessageDialog(null, "LOGIN FAILED");
			br = new BufferedReader(new FileReader(file));
			String line = null;
			while((line=br.readLine())!=null) {
				String[] data = line.split(",");
				for(String id: data) {
					if(id.trim().length()==7) {
						ResultSet set = client.select(Tables.AGENTS, null, AgentsColumns.STATION_ID+" = ?", new String[] {id.trim()});
						if(set.next())
							continue;
						System.out.println(client.insert(Tables.AGENTS, AgentsColumns.ADD_COLUMNS, new Object[] {"","","MT_MARKETING",id.trim(),0,"0000-00-00",0,"0000-00-00"}));
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
				System.out.println("CLOSED");
				if(client!=null)client.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
