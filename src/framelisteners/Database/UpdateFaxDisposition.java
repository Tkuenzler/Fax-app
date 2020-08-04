package framelisteners.Database;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import framelisteners.Database.frames.FaxDispositionFrame;

public class UpdateFaxDisposition implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		new FaxDispositionFrame();
	}
}
