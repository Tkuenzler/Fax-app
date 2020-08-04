package framelisteners.edit.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import source.CSVFrame;
import table.MyTable;
import table.MyTableModel;

@SuppressWarnings("serial")
public class FindFrame extends JFrame {

	JComboBox<String> headers;
	JPanel contentPane = new JPanel();
	ButtonGroup group = new ButtonGroup();
	JRadioButton up = new JRadioButton("Up");
	JRadioButton down = new JRadioButton("Down");
	JButton find = new JButton("Find");
	JTextField text = new JTextField(10);
	public FindFrame() {
		setTitle("Find..");
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout());
		headers = new JComboBox<String>(MyTableModel.COLUMN_HEADERS);
		up.setSelected(true);
		group.add(up);
		group.add(down);
		contentPane.add(headers,"gapleft 10px, gapright 10px");
		contentPane.add(text,"gapright 10px");
		find.addActionListener(new Find());
		contentPane.add(find,"gapright 10px,wrap");
		contentPane.add(up, "skip");
		contentPane.add(down, "cell 1 1");
		setBounds(10,10,400,100);
		setVisible(true);
	}
	private class Find implements ActionListener {
		String textToFind;
		int row;
		MyTable table;
		@Override
		public void actionPerformed(ActionEvent arg0) {
			textToFind = text.getText();
			table = CSVFrame.table;

			row = table.getSelectedRow();
			if(up.isSelected()) 
				searchUp();
			else if(down.isSelected())
				searchDown();
		}
		private void searchDown() {
			System.out.println("row starting: "+row);
			if(row==-1)
				row=0;
			else
			for(int i = row;i<table.getRowCount();i++) {
				String value = (String) table.getValueAt(i, headers.getSelectedIndex());
				if(value.equalsIgnoreCase(textToFind)) {
					table.changeSelection(i, headers.getSelectedIndex(), false, false);
					break;
				}
			}
		}
		private void searchUp() {
			
		}
	}
}
