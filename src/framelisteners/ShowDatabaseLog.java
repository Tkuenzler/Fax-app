package framelisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import Log.*;

public class ShowDatabaseLog implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	new LogFrame(Log.SQLLog);
		    }
		});
		
	}

}
