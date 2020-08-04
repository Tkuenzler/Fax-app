package framelisteners.fax;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import framelisteners.settings.frame.FaxSettingsFrame;

public class FaxSettings implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		FaxSettingsFrame frame = new FaxSettingsFrame();
	}

}
