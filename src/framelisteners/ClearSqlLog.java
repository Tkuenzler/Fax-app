package framelisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Log.*;

public class ClearSqlLog implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		Logger log = new Logger(Log.SQLLog);
		log.clearLogFile();
	}

}
