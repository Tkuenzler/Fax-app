package framelisteners.fax;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

import framelisteners.fax.frames.FaxChooserFrame;
import framelisteners.fax.frames.FaxClientFrame;
import source.CSVFrame;

@SuppressWarnings("unused")
public class SendFax implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	new FaxChooserFrame(CSVFrame.model.data); 	
		    }
		});
	}
}	