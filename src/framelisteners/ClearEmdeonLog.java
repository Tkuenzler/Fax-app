package framelisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Log.*;
public class ClearEmdeonLog implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Logger log = new Logger(Log.EmdeonLog);
		log.clearLogFile();
	}
}
