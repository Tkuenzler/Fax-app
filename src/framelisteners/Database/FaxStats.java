package framelisteners.Database;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import framelisteners.Database.frames.FaxStatsFrame;

public class FaxStats implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		new FaxStatsFrame();
	}
}
