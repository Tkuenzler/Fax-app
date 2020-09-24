package framelisteners.AlternateScripts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Clients.DatabaseClient;
import Clients.RoadMapClient;
import Fax.ProductScripts;

public class LoadProducts implements ActionListener {
	String pharmacy;
	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		RoadMapClient roadmap = new RoadMapClient();
		pharmacy = roadmap.getPharmacy();
		roadmap.close();
		if(pharmacy==null)
			return;
		String product = event.getActionCommand();
		switch(product) {
			case ProductScripts.TOPICAL_SCRIPT:
				LoadAlternateScript();
				break;
			case ProductScripts.ORAL_SCRIPT:
				LoadOralScript();
				break;	
			case ProductScripts.COVERED_MEDS:
				LoadCoveredItems();
				break;
			default:
				LoadProduct(product);
				break;
			
		}
	}
	private void LoadAlternateScript() {
		DatabaseClient client = new DatabaseClient(true);
		client.LoadAlternateScripts(pharmacy);
		client.close();
	}
	private void LoadOralScript() {
		DatabaseClient client = new DatabaseClient(true);
		client.LoadOralScripts(pharmacy);
		client.close();
	}
	private void LoadProduct(String product) {
		DatabaseClient client = new DatabaseClient(true);
		client.LoadProduct(pharmacy,product);
		client.close();
	}
	private void LoadCoveredItems() {
		DatabaseClient client = new DatabaseClient(true);
		client.LoadCoveredItems();
		client.close();
	}
}
