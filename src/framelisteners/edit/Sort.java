package framelisteners.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import framelisteners.edit.frames.FindFrame;
import framelisteners.edit.frames.SortFrame;

public class Sort implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		    	new SortFrame();
	}
}