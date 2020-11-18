package framelisteners.Scripts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;

import Database.Database;
import Database.Columns.LeadColumns;
import Database.Tables.Tables;
import subframes.FileChooser;

public class AddScripts implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		int count = 0;
		/*
		 * GET FOLDER
		 */
		File folder = new File(FileChooser.OpenFolder("Scripts Folder"));
		File[] files = folder.listFiles();
		
		/*
		 *GET DATABASE AND LOGIN 
		 */
		
		Database client = new Database();
		try {
			if(!client.loginAndChooseDatabase()) {
				JOptionPane.showMessageDialog(null, "Login Failed");
				return;
			}
			/*
			 * GET ALL FILES IN FOLDER THAT ARE PDFS
			 */
			for(File file: files) {
				if(!FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("pdf"))
					continue;
				
				/*
				 * CONVERT PDF TO BYTE STREAM AND UPLOAD
				 */

				byte[] pdfData = new byte[(int) file.length()];
				DataInputStream dis = new DataInputStream(new FileInputStream(file));
				dis.readFully(pdfData);  // read from file into byte[] array
				dis.close();
				
				String phone = file.getName().substring(0, file.getName().length()-4);
				int value = client.update(Tables.LEADS, new String[] {LeadColumns.SCRIPT}, new Object[] {pdfData}, LeadColumns.PHONE_NUMBER+" = '"+phone+"'");
				if(value==1) {
					if(file.delete())
						count++;
				}
				else
					System.out.println(phone+" not uploaded");
				
				/*
				 * DELETE FILE
				 */

			}
			JOptionPane.showMessageDialog(null, count+" out of "+files.length+" uploaded");
		} catch(SQLException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(client!=null)client.close();
		}
	}

}
