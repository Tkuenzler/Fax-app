package Log;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

@SuppressWarnings("serial")
public class LogFrame extends JFrame {
	
	Logger logger;
	JTextPane pane;
	JScrollPane scroll;
	
	public LogFrame(Log log) {
		setTitle("Log");
		logger = new Logger(log);
		pane = new JTextPane();
		pane.setEditable(false);
		readFile();
		scroll = new JScrollPane(pane,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scroll,BorderLayout.CENTER);
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setVisible(true);
	}
	private void readFile() {
		StringBuilder content = logger.readFile();
		pane.setText(content.toString());
	}
}