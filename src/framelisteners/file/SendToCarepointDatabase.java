package framelisteners.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;

import Database.Database;
import Database.Columns.CarepointColumns;
import Database.Columns.LeadColumns;
import Database.Tables.Tables;
import source.LoadInfo;
import subframes.FileChooser;
import table.Record;

public class SendToCarepointDatabase implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		int count = 0;
		String password = JOptionPane.showInputDialog("What is the password?");
		if(!password.equalsIgnoreCase("Carepoint123"))
			return;
		String folderName = FileChooser.OpenFolder("Open Folder with PDFS for Carepoint");
		if(folderName==null)
			return;
		File folder = new File(folderName);
		int totalCount = folder.listFiles().length;
		Database client = new Database("MT_MARKETING");
		try {
			if(!client.login()) 
				return;
			for(File file: folder.listFiles()) {
				if(!FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("PDF"))
					continue;
				System.out.println(file.getName());
				String phone = file.getName().replaceAll(".pdf", "");
				if(phone.length()!=10)
					continue;
				ResultSet set = client.select(Tables.LEADS, null, LeadColumns.PHONE_NUMBER+" = ?", new String[] {phone});
				if(set.next()) {
					if(client.select(Tables.CAREPOINT, null, CarepointColumns.PHONE_NUMBER+" = ?", new String[] {phone}).next()) 
						continue;
					byte[] pdfData = new byte[(int) file.length()];
					DataInputStream dis = new DataInputStream(new FileInputStream(file));
					dis.readFully(pdfData);  // read from file into byte[] array
					dis.close();
					Record record = new Record(set,"","");
					int insert = client.insert(Tables.CAREPOINT, CarepointColumns.ADD_TO_DATABASE, CarepointColumns.CreateArray(record, pdfData, LoadInfo.getAppName()));
					if(insert==1) {
						if(file.delete()) {
							count++;
							System.out.println("ADDED AND DELETED");
						}
					}
				}
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JOptionPane.showMessageDialog(null, count+" out of "+totalCount+" added");
			if(client!=null)client.close();
		}
	}

}
