package framelisteners.RoadMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import framelisteners.RoadMap.frames.CheckRoadMapFrame;

public class LoadCheckRoadMapFrame implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		new CheckRoadMapFrame();
	}

}
