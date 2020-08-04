package framelisteners.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import PivotTable.LoadData;

public class CreateInsuranceJSON implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		LoadData data = new LoadData();
		data.LoadAjudicationData();
		data.SaveData();
	}

}
