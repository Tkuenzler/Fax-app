package framelisteners.AlternateScripts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import framelisteners.AlternateScripts.frames.AlternateProductDispositionFrame;
import framelisteners.Database.frames.FaxDispositionFrame;

public class UpdateAlternateScriptDisposition implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		new AlternateProductDispositionFrame();
	}

}
