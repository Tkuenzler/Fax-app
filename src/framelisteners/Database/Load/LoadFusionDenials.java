package framelisteners.Database.Load;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import Clients.DatabaseClient;
import Fax.FaxStatus;
import source.CSVFrame;
import table.Record;

public class LoadFusionDenials implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String[] zipcodes = {"33761","33763","34695","34677","34697","33759","34684","34698","33765","34682","34660",
				"34683","33755","34685","34681","33757","33758","33766","33635","33769","33626","33767","33756","33764",
				"34688","33615","34689","33771","33760","33779","33770","33775","33762","33786","33625","33773","33729",
				"33607","33634","33556","34692","34690","33778","33782","33716","33774","34691","33624","33777","34680","33780",
				"33785","33781","34655","33614","33702","33609","33772","33776","33618","33688","34652","33709","33663","33664",
				"34656","33714","34653","33629","33744","33558","33708","33703","33603","33611","33616","33710","33548","33747",
				"33738","34673","33612","33604","33713","33602","33646","33606","33704","33601","33622","33623","33630","33631",
				"33633","33650","33655","33660","33661","33662","33672","33673","33674","33675","33677","33679","33680","33681",
				"33682","33684","33685","33686","33687","33689","33694","33613","33608","33731","33732","33733","33734","33736",
				"33740","33742","33743","33784","33730","33549","33621","33701","34668","33707","34638","33620","33605","34654",
				"33617","33741","33705","33706","33712","33610","33559","33637","33619","33711","34674","33647","34637","34669",
				"34639","34667","33534","33550","33584","33715","33578","33510","33572","33583","33544","33543","33570","33508",
				"33509","33511","33571","34610","34679","33575","33592","33568","33586","33569","33595","33594"};
		DatabaseClient client = new DatabaseClient(false);
		ResultSet set = client.customQuery("SELECT * FROM `Leads` WHERE `state` = 'FL' AND "+FaxStatus.SQL_DENIED);
		try {
			while(set.next()) {
				String zip = set.getString("zip");
				for(String zipcode: zipcodes) {
					if(zip.equalsIgnoreCase(zipcode))
						CSVFrame.model.addRow(new Record(set,client.getDatabaseName(),client.getTableName()));
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
