package framelisteners.update;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import framelisteners.update.frames.EmdeonBotFrame;

@SuppressWarnings("unused")
public class UpdateInsuranceEmdeon implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
		    @Override
			public void run() {
		    	EmdeonBotFrame frame = new EmdeonBotFrame();
		    }
		}).start();
	}
}
