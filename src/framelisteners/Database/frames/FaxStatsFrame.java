package framelisteners.Database.frames;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Clients.DatabaseClient;
import Clients.DatabaseClient.Columns;
import Fax.FaxStatus;
import Fax.MessageStatus;

public class FaxStatsFrame extends JFrame implements ActionListener {
	private JTextField afidName = new JTextField(15);
	private JButton search = new JButton("Search");
	private JLabel wrongFaxDisplay = new JLabel("");
	private JLabel wrongDoctorDisplay = new JLabel("");
	private JLabel deniedDisplay = new JLabel("");
	private JLabel approvedDisplay = new JLabel("");
	private JLabel needsToBeSeenDisplay = new JLabel("");
	private JLabel deceasedDisplay = new JLabel("");
	private JLabel totalDisplay = new JLabel("");
	private JLabel sentDisplay = new JLabel("");
	JLabel blankDisplay = new JLabel("");
	String table, database;
	private JPanel contentPane;
	public FaxStatsFrame() {
		super("Fax Disposition Stats");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 350, 311);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		search.addActionListener(this);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 36, 300, 230);
		panel.setLayout(new GridLayout(10, 2, 0, 0));
		contentPane.add(panel);
		
		afidName.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		afidName.setHorizontalAlignment(JLabel.CENTER);
		panel.add(afidName);
		panel.add(search);
		
		JLabel sentLabel = new JLabel("Sent");
		sentLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		sentLabel.setVerticalAlignment(JLabel.CENTER);
		panel.add(sentLabel);
		panel.add(sentDisplay);
		
		JLabel blankLabel = new JLabel("Blank");
		blankLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		blankLabel.setVerticalAlignment(JLabel.CENTER);
		panel.add(blankLabel);
		panel.add(blankDisplay);
		
		JLabel wrongFaxLabel = new JLabel("Wrong Fax");
		wrongFaxLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		wrongFaxLabel.setVerticalAlignment(JLabel.CENTER);
		panel.add(wrongFaxLabel);
		panel.add(wrongFaxDisplay);
		
		JLabel wrongDoctorLabel = new JLabel("Wrong Doctor");
		wrongDoctorLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		wrongDoctorLabel.setVerticalAlignment(JLabel.CENTER);
		panel.add(wrongDoctorLabel);
		panel.add(wrongDoctorDisplay);
		
		JLabel approvedLabel = new JLabel("Approved");
		approvedLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		approvedLabel.setVerticalAlignment(JLabel.CENTER);
		panel.add(approvedLabel);
		panel.add(approvedDisplay);
		
		JLabel deniedLabel = new JLabel("Denied");
		deniedLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		deniedLabel.setVerticalAlignment(JLabel.CENTER);
		panel.add(deniedLabel);
		panel.add(deniedDisplay);
		
		JLabel needsToBeSeenLabel = new JLabel("Needs To be seen");
		needsToBeSeenLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		needsToBeSeenLabel.setVerticalAlignment(JLabel.CENTER);
		panel.add(needsToBeSeenLabel);
		panel.add(needsToBeSeenDisplay);
				
		JLabel deceasedLabel = new JLabel("Deceased");
		deceasedLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		deceasedLabel.setVerticalAlignment(JLabel.CENTER);
		panel.add(deceasedLabel);
		panel.add(deceasedDisplay);
			
		JLabel totalLabel = new JLabel("Total");
		totalLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		totalLabel.setVerticalAlignment(JLabel.CENTER);
		panel.add(totalLabel);
		panel.add(totalDisplay);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(99, 0, 100, 36);
		contentPane.add(panel_1);
		setAmounts();
		setVisible(true);
	}
	private void setAmounts() {
		NumberFormat df = NumberFormat.getPercentInstance();
		df.setMinimumFractionDigits(3);
		DatabaseClient client = new DatabaseClient(false);
		if(client.getTableName()==null) {
			client.close();
			return;
		}
		this.database = client.getDatabaseName();
		this.table = client.getTableName();
		int blank = client.getCountByColumn(Columns.FAX_DISPOSITION,FaxStatus.BLANK);
		System.out.println(blank);
		int wrongFax = client.getCountByColumn(Columns.FAX_DISPOSITION,FaxStatus.WRONG_FAX);
		System.out.println(wrongFax);
		int wrongDoctor = client.getCountByColumn(Columns.FAX_DISPOSITION,FaxStatus.WRONG_DOCTOR);
		System.out.println(wrongDoctor);
		int denied = client.getCountByColumn(Columns.FAX_DISPOSITION,FaxStatus.DENIED);
		System.out.println(denied);
		int approved = client.getCountByColumn(Columns.FAX_DISPOSITION,FaxStatus.APPROVED);
		System.out.println(approved);
		int needsToBeSeen = client.getCountByColumn(Columns.FAX_DISPOSITION,FaxStatus.NEEDS_TO_BE_SEEN);
		System.out.println(needsToBeSeen);
		int deceased = client.getCountByColumn(Columns.FAX_DISPOSITION,FaxStatus.DECEASED);
		System.out.println(deceased);
		int sent = client.getCountByColumn(Columns.MESSAGE_STATUS, MessageStatus.SENT);
		System.out.println(sent);
		int total = wrongFax+wrongDoctor+denied+approved+needsToBeSeen+blank+deceased;
		sentDisplay.setText(""+sent);
		blankDisplay.setText(""+df.format((double)blank/total));
		wrongFaxDisplay.setText(""+df.format((double)wrongFax/total));
		wrongDoctorDisplay.setText(""+df.format((double)wrongDoctor/total));
		approvedDisplay.setText(""+df.format((double)approved/total));
		deniedDisplay.setText(""+df.format((double)denied/total));
		needsToBeSeenDisplay.setText(""+df.format((double)needsToBeSeen/total));
		deceasedDisplay.setText(""+df.format((double)deceased/total));
		totalDisplay.setText(""+total);
		client.close();
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		NumberFormat df = NumberFormat.getPercentInstance();
		df.setMinimumFractionDigits(3);
		DatabaseClient client = new DatabaseClient(database,table);
		int blank = client.getCountByColumnByAfid(Columns.FAX_DISPOSITION,FaxStatus.BLANK,afidName.getText());
		System.out.println(blank);
		int wrongFax = client.getCountByColumnByAfid(Columns.FAX_DISPOSITION,FaxStatus.WRONG_FAX,afidName.getText());
		System.out.println(wrongFax);
		int wrongDoctor = client.getCountByColumnByAfid(Columns.FAX_DISPOSITION,FaxStatus.WRONG_DOCTOR,afidName.getText());
		System.out.println(wrongDoctor);
		int denied = client.getCountByColumnByAfid(Columns.FAX_DISPOSITION,FaxStatus.DENIED,afidName.getText());
		System.out.println(denied);
		int approved = client.getCountByColumnByAfid(Columns.FAX_DISPOSITION,FaxStatus.APPROVED,afidName.getText());
		System.out.println(approved);
		int needsToBeSeen = client.getCountByColumnByAfid(Columns.FAX_DISPOSITION,FaxStatus.NEEDS_TO_BE_SEEN,afidName.getText());
		System.out.println(needsToBeSeen);
		int deceased = client.getCountByColumnByAfid(Columns.FAX_DISPOSITION,FaxStatus.DECEASED,afidName.getText());
		System.out.println(deceased);
		int sent = client.getCountByColumnByAfid(Columns.MESSAGE_STATUS, MessageStatus.SENT,afidName.getText());
		System.out.println(sent);
		int total = wrongFax+wrongDoctor+denied+approved+needsToBeSeen+blank+deceased;
		sentDisplay.setText(""+sent);
		blankDisplay.setText(""+df.format((double)blank/total));
		wrongFaxDisplay.setText(""+df.format((double)wrongFax/total));
		wrongDoctorDisplay.setText(""+df.format((double)wrongDoctor/total));
		approvedDisplay.setText(""+df.format((double)approved/total));
		deniedDisplay.setText(""+df.format((double)denied/total));
		needsToBeSeenDisplay.setText(""+df.format((double)needsToBeSeen/total));
		deceasedDisplay.setText(""+df.format((double)deceased/total));
		totalDisplay.setText(""+total);
		client.close();
		
		
	}
}
