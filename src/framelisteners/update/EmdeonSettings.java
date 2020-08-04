package framelisteners.update;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

import framelisteners.settings.frame.EmdeonSettingsFrame;

public class EmdeonSettings implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	new EmdeonSettingsFrame();
		    }
		});
	}
}
