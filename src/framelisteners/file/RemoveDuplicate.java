package framelisteners.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Clients.DatabaseClient;
import subframes.FileChooser;

public class RemoveDuplicate implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		ArrayList<String> list = new ArrayList<String>() {
			@Override
			public boolean contains(Object o) {
				String compare = o.toString();
				for(String s: this){
					if(s.equalsIgnoreCase(compare))
						return true;
				}
				return false;
			}
		};
		File load = FileChooser.OpenCsvFile("Single Column File");	
		if(load==null)
			return;
		File save = FileChooser.SaveCsvFile();
		if(save==null)
			return;
		BufferedWriter bw = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(load));
			String line = null;
			while((line=br.readLine())!=null) {
				if(!list.contains(line))
					list.add(line);
			}
			br.close();
			bw = new BufferedWriter(new FileWriter(save));
			for(String s: list) {
				bw.write(s);
				bw.newLine();
			}
			bw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
