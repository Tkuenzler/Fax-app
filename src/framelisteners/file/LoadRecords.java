package framelisteners.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import subframes.CSVBuilderFrame;

@SuppressWarnings("unused")
public class LoadRecords implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	CSVBuilderFrame setup = new CSVBuilderFrame();
		    }
		});	
	}
}