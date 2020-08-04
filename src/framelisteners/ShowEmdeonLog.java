package framelisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Log.*;

@SuppressWarnings("unused")
public class ShowEmdeonLog implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		LogFrame frame = new LogFrame(Log.EmdeonLog);
	}

}
