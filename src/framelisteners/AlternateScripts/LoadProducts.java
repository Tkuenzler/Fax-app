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
			case ProductScripts.ANTI_FUNGAL_SCRIPT:
				LoadAntiFungalScript();
				break;
			case ProductScripts.CONSTIPATION_SCRIPT:
				LoadConstipationScript();
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
	private void LoadAntiFungalScript() {
		DatabaseClient client = new DatabaseClient(true);
		client.LoadAntiFungalScript(pharmacy);
		client.close();
	}
	private void LoadConstipationScript() {
		DatabaseClient client = new DatabaseClient(true);
		client.LoadConstipationScript(pharmacy);
		client.close();
	}
}
