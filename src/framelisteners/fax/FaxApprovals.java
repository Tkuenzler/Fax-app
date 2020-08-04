package framelisteners.fax;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import framelisteners.fax.frames.FaxApprovalFrame;

public class FaxApprovals implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		new FaxApprovalFrame();
	}

}
