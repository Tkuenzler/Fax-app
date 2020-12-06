package framelisteners.Database.Load;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import Clients.DatabaseClient;
import source.CSVFrame;
import table.Record;

public class CustomSQLQuery implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String[] types = {"DME Leads","Regular Leads"};
		String type = (String) JOptionPane.showInputDialog(new JFrame(), "Select a Lead Type", "Types:", JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
		if(type==null)
			return;
		String sql = JOptionPane.showInputDialog("Query: ");
		if(sql==null)
			return;
		
		DatabaseClient client = new DatabaseClient(true);
		ResultSet set = client.customQuery(sql);
		if(set==null) {
			JOptionPane.showMessageDialog(null, "No results returned");
			return;
		}
		try {
			while(set.next()) {
				if(type.equalsIgnoreCase(types[1]))
					CSVFrame.model.addRow(new Record(set,client.getDatabaseName(),client.getTableName()));
				else {
					Record record = new Record();
					record.setDME(set);
					CSVFrame.model.addRow(record);
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			client.close();
		}
	}
}
