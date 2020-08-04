package subframes;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import Clients.DatabaseClient;
import table.Record;

public class NotesFrame extends JFrame implements ActionListener {
	JTextArea noteField = new JTextArea("");
	JButton save = new JButton("Save");
	String notes; 
	Record record;
	public NotesFrame(Record record) {
		super(record.getFirstName()+" "+record.getLastName());
		this.notes = record.getNotes();
		this.record = record;
		setUp();
	}
	private void setUp() {
		setSize(300, 200);
		setResizable(true);
		noteField.setLineWrap(true);
		noteField.setWrapStyleWord(true);
		noteField.setText(notes);
		save.addActionListener(this);
		add(noteField,BorderLayout.CENTER);
		add(save,BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		DatabaseClient client = null;
		if(record.getDatabase()==null || record.getTable()==null)
			client = new DatabaseClient(false);
		else 
			client = new DatabaseClient(record.getDatabase(),record.getTable());
		if(client.getTableName()==null || client.getDatabaseName()==null) {
			JOptionPane.showMessageDialog(null,"Must select table and database");
			return;
		}
		int update = client.updateNotes(record, noteField.getText());
		if(update>0) {
			JOptionPane.showMessageDialog(null, "Succesfully updated note");
			record.setNotes(noteField.getText());
		}
		else 
			JOptionPane.showMessageDialog(null, "Failed to update note");
		client.close();
		dispose();
	}
}
