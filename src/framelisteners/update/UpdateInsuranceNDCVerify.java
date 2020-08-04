package framelisteners.update;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import framelisteners.update.frames.NDCVerifyBotFrame;

@SuppressWarnings("unused")
public class UpdateInsuranceNDCVerify implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want run NDC Verify?","NDCVerify Updata Insurance",JOptionPane.YES_NO_OPTION);
		if(confirm!=JOptionPane.YES_OPTION)
			return;
		new Thread(new Runnable() {
		    @Override
			public void run() {
		    	NDCVerifyBotFrame frame = new NDCVerifyBotFrame();
		    }
		}).start();
	}
}
