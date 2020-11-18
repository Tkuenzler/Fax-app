package DME;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Database.Database;
import Database.Columns.DMEColumns;
import Database.Tables.Tables;
import Fax.FaxStatus;
import source.CSVFrame;
import table.Record;

public class LoadFaxDispositionDME implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String[] statuses = FaxStatus.FAX_STATUSES;
		String status = (String) JOptionPane.showInputDialog(new JFrame(), "Disposition", "Disposition", JOptionPane.INFORMATION_MESSAGE, null, statuses, statuses[0]);
		if(status==null)
			return;
		Database client = new Database();
		try {
			if(!client.loginAndChooseDatabase()) {
				JOptionPane.showMessageDialog(null, "FAILED TO LOG IN");
				return;
			}
			ResultSet set = client.select(Tables.DME, null, DMEColumns.FAX_DISPOSITION+" = ?", new String[] {status});
			while(set.next()) {
				Record record = new Record();
				record.setDME(set);
				CSVFrame.model.addRow(record);
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			if(client!=null) client.close();
		}
	}
}
