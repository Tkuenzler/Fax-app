package framelisteners.Database.frames;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import Clients.DatabaseClient;
import Clients.DatabaseClient.Columns;
import net.miginfocom.swing.MigLayout;
import source.CSVFrame;
import table.Record;

public class SearchByColumnFrame extends JFrame implements ActionListener {
	private  JTextField valueField = new JTextField();
	private JComboBox<String> columns = null;
	private JComboBox<String> tables;
	private JComboBox<String> operands = new JComboBox<String>(new String[] {"=",">","<",">=","<=","LIKE"});
	String database;
	DatabaseClient db;
	/**
	 * Create the frame.
	 */
	public SearchByColumnFrame() {
		db  = new DatabaseClient(true);
		if(db.getDatabaseName()==null)
			return;
		tables = new JComboBox<String>(db.getTables());
		database = db.getDatabaseName();
		columns = new JComboBox<String>(db.GetColumns("Leads"));
		db.close();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 110);
		getContentPane().setLayout(new MigLayout());
		valueField.setSize(90, 20);
		valueField.setColumns(10);
		getContentPane().add(columns, "gaptop 20px, gapleft 10px");
		getContentPane().add(operands,"gaptop 20px, gapleft 10px");
		getContentPane().add(valueField, "gaptop 5px, gapleft 15px");
		JButton search = new JButton("Search");
		search.addActionListener(this);
		getContentPane().add(tables, "gaptop 5px, gapleft 20px");
		getContentPane().add(search,"gaptop 5px, gapleft 20px");
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String column = columns.getItemAt(columns.getSelectedIndex());
		String table = tables.getItemAt(tables.getSelectedIndex());
		String operand = operands.getItemAt(operands.getSelectedIndex());
		String value = null;
		if(operand.equalsIgnoreCase("LIKE"))
			value = "%"+valueField.getText()+"%";
		else 
			value = valueField.getText();
		DatabaseClient client = new DatabaseClient(database,table);
		String query = "SELECT * FROM `"+table+"` WHERE `"+column+"` "+operand+" '"+value+"'";
		ResultSet set = client.customQuery(query);
		try {
			while(set.next()) {
				Record record = null;
				if(table.contains("DME")) {
					record = new Record();
					record.setDME(set);
				}
				else 
					record = new Record(set,client.getDatabaseName(),client.getTableName());
				CSVFrame.model.addRow(record);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
		client.close();
	}
}
