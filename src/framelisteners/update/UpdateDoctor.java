package framelisteners.update;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import framelisteners.update.frames.UpdateDoctorFrame;
import source.CSVFrame;

@SuppressWarnings("unused")
public class UpdateDoctor implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(CSVFrame.table.getRowCount()==0) {
			JOptionPane.showMessageDialog(new JFrame(), "Load records first");
			return;
		}
		new Thread(new Runnable() {
		    @Override
			public void run() {
		    	UpdateDoctorFrame update = new UpdateDoctorFrame();
		    }
		}).start();
	}
}