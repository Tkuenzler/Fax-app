package framelisteners.AlternateScripts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Clients.DatabaseClient;
import Fax.ProductScripts;
import source.CSVFrame;
import table.Record;

public class LoadAlternateScriptsFaxDisposition implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String[] products = ProductScripts.ALL;
		String product = (String) JOptionPane.showInputDialog(new JFrame(), "Select a product", "Product:", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);
		String column = ProductScripts.GetProductFaxDispositionColumn(product);
		DatabaseClient client = new DatabaseClient(true);
		for(Record record: CSVFrame.model.data) {
			String dispo = client.loadAlternateScriptFaxDisposition(column,record);
			if(dispo==null)
				continue;
			else
				record.setFaxStatus(dispo);
		}
	}

}
