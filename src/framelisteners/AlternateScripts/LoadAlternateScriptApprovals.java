package framelisteners.AlternateScripts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import Clients.DatabaseClient;
import Clients.RoadMapClient;
import Fax.ProductScripts;

public class LoadAlternateScriptApprovals implements ActionListener {
		String pharmacy;
		@Override
		public void actionPerformed(ActionEvent event) {
			int daysBack = 0;
			try {
				daysBack = Integer.parseInt(JOptionPane.showInputDialog(null, "How many days back?"));
			} catch(NumberFormatException ne) {
				JOptionPane.showMessageDialog(null, "Not a valid number");
				return;
			}
			String product = event.getActionCommand();
			String column = ProductScripts.GetProductFaxDispositionColumn(product);
			String columnDate = ProductScripts.GetProductFaxDispositionDateColumn(product);
			RoadMapClient roadmap = new RoadMapClient();
			pharmacy = roadmap.getPharmacy();
			roadmap.close();
			if(pharmacy==null)
				return;
			DatabaseClient client = new DatabaseClient(true);
			client.AlternateScriptsLoadApprovals(column,columnDate,pharmacy,daysBack);
			client.close();
		}
}
