package framelisteners.edit.pharmacy;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Clients.RoadMapClient;
import PBM.InsuranceFilter;
import PBM.InsuranceType;
import source.CSVFrame;
import table.Record;

public class CheckPharmacy implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		RoadMapClient client = new RoadMapClient();
		for(Record record: CSVFrame.model.data) {
			boolean medicare = client.CanTakeMedicare(record.getPharmacy());
			int value = InsuranceFilter.GetInsuranceType(record);
			switch(value) {
				case InsuranceType.Type.PRIVATE_INSURANCE: 
				{
					if(client.isInRoadMap(record, record.getPharmacy())>0)
						record.setRowColor(Color.GREEN);
					else
						record.setRowColor(Color.RED);
				}
					break;
				case InsuranceType.Type.MEDICARE_INSURANCE:
				{
					if(medicare) {
						if(client.isInRoadMap(record, record.getPharmacy())>0)
							record.setRowColor(Color.GREEN);
						else
							record.setRowColor(Color.RED);
					}
					else
						record.setRowColor(Color.RED);
				}
					break;
				default:
					record.setRowColor(Color.BLACK);
			}
		}
		client.close();
	}
}
