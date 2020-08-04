package framelisteners.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import framelisteners.edit.frames.FindFrame;

public class Find implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	new FindFrame();
		    }
		});
	}
}
