package framelisteners.edit.pharmacy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import org.json.JSONException;

import Fax.Drug;
import PBM.InsuranceFilter;
import PBM.InsuranceType;
import PivotTable.LoadData;
import source.CSVFrame;
import table.Record;

public class CheckTopDrugs implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		LoadData bachData = new LoadData();
		LoadData lakeIdaData = new LoadData();
		try {
			bachData.GetData(LoadData.BACH_LIST);
			lakeIdaData.GetData(LoadData.LAKE_IDA_LIST);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Record record: CSVFrame.model.data) {
			int type = InsuranceFilter.GetInsuranceType(record);
			Drug[] drugs = null;
			if(type==InsuranceType.Type.PRIVATE_INSURANCE) {
				drugs = bachData.GetDrugs(record);
			}
			else if(type==InsuranceType.Type.MEDICARE_INSURANCE) {
				drugs = lakeIdaData.GetDrugs(record);
			}
			record.printRecord();
			if(drugs!=null)
			for(Drug drug: drugs) {
				if(drug==null)
					continue;
				else
					System.out.println(drug.getName());
			}
			System.out.println("===================================");
			
		}
	}

}
