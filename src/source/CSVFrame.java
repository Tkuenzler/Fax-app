
package source;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

import PaidReport.MarkPaidReport;
import PaidReport.MarkReport;
import framelisteners.AlternateScripts.LoadAlternateScriptApprovals;
import framelisteners.AlternateScripts.LoadAlternateScriptsFaxDisposition;
import framelisteners.AlternateScripts.LoadOldDenials;
import framelisteners.AlternateScripts.LoadProducts;
import framelisteners.AlternateScripts.LoadWrongDoctor;
import framelisteners.AlternateScripts.UpdateAlternateScriptDisposition;
import framelisteners.Database.CreateDBSuppression;
import framelisteners.Database.CreateTableSuppression;
import framelisteners.Database.DeleteFromTelmed;
import framelisteners.Database.DeleteRows;
import framelisteners.Database.FaxStats;
import framelisteners.Database.GetAverageIncome;
import framelisteners.Database.GetFaxDispositions;
import framelisteners.Database.GetLookUps;
import framelisteners.Database.IsData;
import framelisteners.Database.IsInDatabase;
import framelisteners.Database.IsInTable;
import framelisteners.Database.IsViciLead;
import framelisteners.Database.RowCount;
import framelisteners.Database.SetLookUps;
import framelisteners.Database.UpdateFaxDisposition;
import framelisteners.Database.Audit.CheckAllAudit;
import framelisteners.Database.Audit.CheckForAudit;
import framelisteners.Database.Audit.LoadAudit;
import framelisteners.Database.Audit.LoadAuditRecords;
import framelisteners.Database.Audit.LoadBachAudit;
import framelisteners.Database.Audit.LoadLakeIdaAudit;
import framelisteners.Database.Audit.LoadMarkAudit;
import framelisteners.Database.Delete.DeleteAllRecordsChecked;
import framelisteners.Database.Delete.DeleteChecked;
import framelisteners.Database.DoctorChase.GetByFaxDisposition;
import framelisteners.Database.DoctorChase.GetByMessageStatus;
import framelisteners.Database.DoctorChase.LoadApprovals;
import framelisteners.Database.DoctorChase.LoadBadFaxNumbers;
import framelisteners.Database.DoctorChase.LoadDMEFaxable;
import framelisteners.Database.DoctorChase.LoadFaxables;
import framelisteners.Database.DoctorChase.LoadInterruptedFaxes;
import framelisteners.Database.DoctorChase.LoadLiveFaxables;
import framelisteners.Database.DoctorChase.LoadNeedsNewScript;
import framelisteners.Database.DoctorChase.LoadNoAnswerDoctor;
import framelisteners.Database.DoctorChase.LoadTelmedLeads;
import framelisteners.Database.Load.CustomSQLQuery;
import framelisteners.Database.Load.GetAllLeads;
import framelisteners.Database.Load.GetLeadsFromDaysBack;
import framelisteners.Database.Load.LoadAFID;
import framelisteners.Database.Load.LoadColumn;
import framelisteners.Database.Load.LoadFusionDenials;
import framelisteners.Database.Load.LoadOldPharmacies;
import framelisteners.Database.Load.LoadPaddlePointTelmed;
import framelisteners.Database.Load.LoadVendorId;
import framelisteners.Database.Load.LoadbyPhone;
import framelisteners.Database.Load.SearchByColumn;
import framelisteners.NotFound.LoadInvalidInfo;
import framelisteners.NotFound.LoadNotFounds;
import framelisteners.RoadMap.ExportFullRoadMap;
import framelisteners.RoadMap.ExportRoadMap;
import framelisteners.RoadMap.LoadCheckRoadMapFrame;
import framelisteners.RoadMap.LoadRoadMap;
import framelisteners.edit.CellsEditable;
import framelisteners.edit.ClearColumn;
import framelisteners.edit.ClearDoctorInfo;
import framelisteners.edit.ClearInsurance;
import framelisteners.edit.DuplicateRemover;
import framelisteners.edit.Find;
import framelisteners.edit.InsuranceChecker;
import framelisteners.edit.RowCounter;
import framelisteners.edit.SetColumn;
import framelisteners.edit.Sort;
import framelisteners.edit.Doctor.CheckDrFax;
import framelisteners.edit.Doctor.VerifyDrType;
import framelisteners.edit.pharmacy.CheckPharmacy;
import framelisteners.edit.pharmacy.CheckState;
import framelisteners.edit.pharmacy.CheckTopDrugs;
import framelisteners.edit.pharmacy.SetPharmacy;
import framelisteners.fax.FaxSettings;
import framelisteners.fax.SendFax;
import framelisteners.file.AddToDNF;
import framelisteners.file.AddToRequalifyCampagin;
import framelisteners.file.Clear;
import framelisteners.file.CreateBlank;
import framelisteners.file.CreateInsuranceJSON;
import framelisteners.file.LoadRecords;
import framelisteners.file.RemoveDuplicate;
import framelisteners.file.SuppressionExporter;
import framelisteners.file.Supression;
import framelisteners.file.Test;
import framelisteners.file.export.Export;
import framelisteners.file.export.ExportExcel;
import framelisteners.invoices.CreateInvoices;
import framelisteners.settings.AddRoadMap;
import framelisteners.settings.AddToVici;
import framelisteners.settings.RunSuppresionList;
import framelisteners.telmed.AddTelmedByStatus;
import framelisteners.telmed.CheckBadCost;
import framelisteners.telmed.CheckGoodCost;
import framelisteners.telmed.CheckProfitByAgent;
import framelisteners.telmed.CheckTotalProfit;
import framelisteners.telmed.IsInTelmed;
import framelisteners.telmed.LoadTemedInfo;
import framelisteners.telmed.LoadUnSentTelmed;
import framelisteners.telmed.UpdatePharmacyStatus;
import framelisteners.telmed.UpdateTelmedCost;
import framelisteners.telmed.UpdateTelmedStatus;
import framelisteners.telmed.check.GetStatus;
import framelisteners.update.EmdeonSettings;
import framelisteners.update.GuessGender;
import framelisteners.update.UpdateDoctor;
import framelisteners.update.UpdateInsuranceEmdeon;
import framelisteners.update.UpdateInsuranceNDCVerify;
import framelisteners.update.UpdateLastChecked;
import table.MyTable;
import table.MyTableModel;

@SuppressWarnings("serial")
public class CSVFrame extends JFrame  {
	public static MyTable table;
	public static MyTableModel model;
	public static TableRowSorter<MyTableModel>  sorter;
	private static JScrollPane scrollPane;
	public static boolean connected = false;
	public static JMenuBar bar;
	public static StringBuilder title = new StringBuilder();
	public CSVFrame() {
		super("CSV File");
		model = new MyTableModel();
		sorter = new TableRowSorter<MyTableModel>(model);
		for(int i = 0;i<model.getColumnCount();i++)
			sorter.setSortable(i, false);
		table = new MyTable(model);
		table.setOpaque(false);
		table.setRowSorter(sorter);
		table.addMouseListener(new MouseAdapter() {
			  public void mouseClicked(MouseEvent e) {
				    if (e.getClickCount() == 2) {
				      JTable target = (JTable)e.getSource();
				      int row = target.getSelectedRow();
				      int column = target.getSelectedColumn();
				    }
				  }
				});
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setOpaque(false);
		getContentPane().add(scrollPane,BorderLayout.CENTER);
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\TommyK\\workspace\\CSVMaker\\Images\\csv-image64x64.png"));
		bar = setToolbar();
		setJMenuBar(bar);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			  @Override
			  public void windowClosing(WindowEvent we)
			  { 
			    int PromptResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit", "Exit", JOptionPane.YES_NO_OPTION);
			    if(PromptResult==JOptionPane.YES_OPTION)
			      System.exit(0);          
			    else
			    	return;
			  }
			});
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
		setVisible(true);
	}
	public static void rename(String title) {
		getFrames()[0].setTitle(title);
	}
	private JMenuBar setToolbar() {
		JMenuBar bar = new JMenuBar();
		//File Menu
		JMenu file = new JMenu("File");
		JMenuItem item = new JMenuItem("Load Records");
		item.addActionListener(new LoadRecords());
		file.add(item);
		JMenu export = new JMenu("Export");
		item = new JMenuItem("Export CSV");
		item.addActionListener(new Export());
		export.add(item);
		item = new JMenuItem("Export XLS");
		item.addActionListener(new ExportExcel());
		export.add(item);
		item = new JMenuItem("Export as suppression list");
		item.addActionListener(new SuppressionExporter());
		export.add(item);
		item = new JMenuItem("Remove duplicates from file");
		item.addActionListener(new RemoveDuplicate());
		file.add(item);
		file.add(export);
		item = new JMenuItem("Suppression List");
		item.addActionListener(new Supression());
		file.add(item);
		item = new JMenuItem("Add To DNF");
		item.addActionListener(new AddToDNF());
		file.add(item);
		file.addSeparator();
		item = new JMenuItem("Create Blank");
		item.addActionListener(new CreateBlank());
		file.add(item);
		item = new JMenuItem("Clear");
		item.addActionListener(new Clear());
		file.add(item);
		item = new JMenuItem("Add to Requalify Campaign");
		item.addActionListener(new AddToRequalifyCampagin());
		file.add(item);
		item = new JMenuItem("Create Insurance JSON");
		item.addActionListener(new CreateInsuranceJSON());
		file.add(item);
		file.addSeparator();
		item = new JMenuItem("TEST");
		item.addActionListener(new Test());
		file.add(item);
		bar.add(file);
		
		//Edit Menu
		JMenu edit = new JMenu("Edit");
			JMenu doctorMenu = new JMenu("Doctor Menu");
			item = new JMenuItem("Check Dr Fax Number");
			item.addActionListener(new CheckDrFax());
			doctorMenu.add(item);
			item = new JMenuItem("Check Dr Type");
			item.addActionListener(new VerifyDrType());
			doctorMenu.add(item);
			edit.add(doctorMenu);
			JMenu checkForPharmacy = new JMenu("Check for Pharmacy");
			item = new JMenuItem("Set Pharmacy");
			item.addActionListener(new SetPharmacy());
			checkForPharmacy.add(item);
			item = new JMenuItem("Check Pharmacy");
			item.addActionListener(new CheckPharmacy());
			checkForPharmacy.add(item);
			item = new JMenuItem("Check State");
			item.addActionListener(new CheckState());
			checkForPharmacy.add(item);
			item = new JMenuItem("Check Top Drugs");
			item.addActionListener(new CheckTopDrugs());
			checkForPharmacy.add(item);
			edit.add(checkForPharmacy);
		item  = new JMenuItem("Sort");
		item.addActionListener(new Sort());
		edit.add(item);
		item = new JMenuItem("Row Count");
		item.addActionListener(new RowCounter());
		edit.add(item);
		item = new JMenuItem("Lock/Unlock Cells");
		item.addActionListener(new CellsEditable());
		edit.add(item);
		item = new JMenuItem("Find");
		item.addActionListener(new Find());
		edit.add(item);
		item = new JMenuItem("Remove duplicates");
		item.addActionListener(new DuplicateRemover());
		edit.add(item);
		item = new JMenuItem("Clear Column");
		item.addActionListener(new ClearColumn());
		edit.add(item);
		item = new JMenuItem("Set Column");
		item.addActionListener(new SetColumn());
		edit.add(item);
		item = new JMenuItem("Clear Insurance Info");
		item.addActionListener(new ClearInsurance());
		edit.add(item);
		item = new JMenuItem("Clear Doctor Info");
		item.addActionListener(new ClearDoctorInfo());
		edit.add(item);
		item = new JMenuItem("Check Insurance");
		item.addActionListener(new InsuranceChecker());
		edit.add(item);
		bar.add(edit);
		
		//Settings
		JMenu settings = new JMenu("Settings");
		item = new JMenuItem("Run Suppression");
		item.addActionListener(new RunSuppresionList());
		settings.add(item);
		bar.add(settings);
		item = new JMenuItem("Add To Vici");
		item.addActionListener(new AddToVici());
		settings.add(item);
		item = new JMenuItem("Road Maps");
		item.addActionListener(new AddRoadMap());
		settings.add(item);

		
		//Update menu
		JMenu update = new JMenu("Update");
		item = new JMenuItem("Update Doctor");
		item.addActionListener(new UpdateDoctor());
		update.add(item);
		item = new JMenuItem("Guess Gender");
		item.addActionListener(new GuessGender());
		update.add(item);
		update.addSeparator();
		item = new JMenuItem("Emdeon Bot");
		item.addActionListener(new UpdateInsuranceEmdeon());
		update.add(item);
		item = new JMenuItem("NDCVerify Bot");
		item.addActionListener(new UpdateInsuranceNDCVerify());
		update.add(item);
		item = new JMenuItem("Emdeon settings");
		item.addActionListener(new EmdeonSettings());
		update.add(item);
		item = new JMenuItem("Set Emdeon Date Checked");
		item.addActionListener(new UpdateLastChecked());
		update.add(item);
		bar.add(update);
		
		//FAX
		JMenu fax = new JMenu("Fax");
		item = new JMenuItem("Send Fax From Ring Central");
		item.addActionListener(new SendFax());
		fax.add(item);
		fax.addSeparator();
		item = new JMenuItem("Fax Settings");
		item.addActionListener(new FaxSettings());
		fax.add(item);
		bar.add(fax);
		
		//Database
		JMenu database = new JMenu("Database");
		
		JMenu doctorChase = new JMenu("Doctor Chase");
		item = new JMenuItem("Load Faxable Leads");
		item.addActionListener(new LoadFaxables());
		doctorChase.add(item);
		item = new JMenuItem("Load Live Faxable Leads");
		item.addActionListener(new LoadLiveFaxables());
		doctorChase.add(item);
		item = new JMenuItem("Load DME Faxable Leads");
		item.addActionListener(new LoadDMEFaxable());
		doctorChase.add(item);
		item = new JMenuItem("Load Approvals");
		item.addActionListener(new LoadApprovals());
		doctorChase.add(item);
		item = new JMenuItem("Load Needs New Scripts");
		item.addActionListener(new LoadNeedsNewScript());
		doctorChase.add(item);
		item = new JMenuItem("Get Lead By Fax Disposition");
		item.addActionListener(new GetByFaxDisposition());
		doctorChase.add(item);
		item = new JMenuItem("Get Lead By Message Status");
		item.addActionListener(new GetByMessageStatus());
		doctorChase.add(item);
		item = new JMenuItem("Load Doctor No Answer");
		item.addActionListener(new LoadNoAnswerDoctor());
		doctorChase.add(item);
		item = new JMenuItem("Load Bad Fax numbers");
		item.addActionListener(new LoadBadFaxNumbers());
		doctorChase.add(item);
		item = new JMenuItem("Load Telmed Leads");
		item.addActionListener(new LoadTelmedLeads());
		doctorChase.add(item);
		doctorChase.addSeparator();
		item = new JMenuItem("Load Interrupted Faxes");
		item.addActionListener(new LoadInterruptedFaxes());
		doctorChase.add(item);
		item = new JMenuItem("Has Vici Recording");
		item.addActionListener(new IsViciLead());
		doctorChase.add(item);
		database.add(doctorChase);
		
		JMenu loadFrom = new JMenu("Load from database");
		item = new JMenuItem("Load Column");
		item.addActionListener(new LoadColumn());
		loadFrom.add(item);
		item = new JMenuItem("Search By Column");
		item.addActionListener(new SearchByColumn());
		loadFrom.add(item);
		item = new JMenuItem("Get Fax Disposition");
		item.addActionListener(new GetFaxDispositions());
		loadFrom.add(item);
		item = new JMenuItem("Get Custom SQL Query");
		item.addActionListener(new CustomSQLQuery());
		loadFrom.add(item);
		item = new JMenuItem("Get Fax Stats");
		item.addActionListener(new FaxStats());
		loadFrom.add(item);
		item = new JMenuItem("Load Vendor ID");
		item.addActionListener(new LoadVendorId());
		loadFrom.add(item);
		item = new JMenuItem("Load by Phone in File");
		item.addActionListener(new LoadbyPhone());
		loadFrom.add(item);
		item = new JMenuItem("Load AFID");
		item.addActionListener(new LoadAFID());
		loadFrom.add(item);
		item = new JMenuItem("Load All Old Pharmacies");
		item.addActionListener(new LoadOldPharmacies());
		loadFrom.add(item);
		loadFrom.addSeparator();
		item = new JMenuItem("Get Leads By Days Back");
		item.addActionListener(new GetLeadsFromDaysBack());
		loadFrom.add(item);
		item = new JMenuItem("Get All Leads From Table");
		item.addActionListener(new GetAllLeads());
		loadFrom.add(item);
		item = new JMenuItem("Get PaddlePoint Telmeds");
		item.addActionListener(new LoadPaddlePointTelmed());
		loadFrom.add(item);
		item = new JMenuItem("Get Lookups");
		item.addActionListener(new GetLookUps());
		loadFrom.add(item);
		item = new JMenuItem("Set Lookups");
		item.addActionListener(new SetLookUps());
		loadFrom.add(item);
		item = new JMenuItem("Load Fusion Denials");
		item.addActionListener(new LoadFusionDenials());
		loadFrom.add(item);
		database.add(loadFrom);
		
		JMenu delete = new JMenu("Delete From Database");
		JMenu checkRecords = new JMenu("Delete From Checked Insurance");
		item = new JMenuItem("Delete Researched Patient");
		item.addActionListener(new DeleteChecked());
		checkRecords.add(item);
		item = new JMenuItem("Delete Researched Records in file");
		item.addActionListener(new DeleteAllRecordsChecked());
		checkRecords.add(item);
		delete.add(checkRecords);
		item = new JMenuItem("Delete Rows");
		item.addActionListener(new DeleteRows());
		delete.add(item);
		item = new JMenuItem("Delete Telmed Records");
		item.addActionListener(new DeleteFromTelmed());
		delete.add(item);
		database.add(delete);
		
		JMenu audit = new JMenu("Audit");
		item = new  JMenuItem("Check phonenumber Audtied");
		item.addActionListener(new CheckForAudit());
		audit.add(item);
		item = new JMenuItem("Check Records for Audits");
		item.addActionListener(new CheckAllAudit());
		audit.add(item);
		item = new JMenuItem("Load Records");
		item.addActionListener(new LoadAudit());
		audit.add(item);
		item = new JMenuItem("Load Audit Records");
		item.addActionListener(new LoadAuditRecords());
		audit.add(item);
		item = new JMenuItem("Load Lake Ida Records");
		item.addActionListener(new LoadLakeIdaAudit());
		audit.add(item);
		item = new JMenuItem("Load Bach Records");
		item.addActionListener(new LoadBachAudit());
		audit.add(item);
		item = new JMenuItem("Load Mark Records");
		item.addActionListener(new LoadMarkAudit());
		audit.add(item);
		database.add(audit);
		
		item = new JMenuItem("Row Count");
		item.addActionListener(new RowCount());
		database.add(item);
		item = new JMenuItem("Set Fax Dispotion");
		item.addActionListener(new UpdateFaxDisposition());
		database.add(item);
		item = new JMenuItem("Is in database");
		item.addActionListener(new IsInDatabase());
		database.add(item);
		item = new JMenuItem("Is in Table");
		item.addActionListener(new IsInTable());
		database.add(item);
		database.addSeparator();
		item = new JMenuItem("Get Average Income");
		item.addActionListener(new GetAverageIncome());
		database.add(item);
		item = new JMenuItem("Create Table Suppression");
		item.addActionListener(new CreateTableSuppression());
		database.add(item);
		item = new JMenuItem("Create Database Suppression");
		item.addActionListener(new CreateDBSuppression());
		database.add(item);
		item = new JMenuItem("Is Live Data");
		item.addActionListener(new IsData());
		database.add(item);
		bar.add(database);
		
		JMenu telmed = new JMenu("Telmed");
		JMenu check = new JMenu("Check Telmed");
		item = new JMenuItem("Check Telmed");
		item.addActionListener(new GetStatus());
		check.add(item);
		item = new JMenuItem("Is In Telmed");
		item.addActionListener(new IsInTelmed());
		check.add(item);
		telmed.add(check);
		item = new JMenuItem("Add Telmed");
		item.addActionListener(new AddTelmedByStatus());
		telmed.add(item);
				
		telmed.addSeparator();
		item = new JMenuItem("Check Good Cost");
		item.addActionListener(new CheckGoodCost());
		telmed.add(item);
		item = new JMenuItem("Check Bad Cost");
		item.addActionListener(new CheckBadCost());
		telmed.add(item);
		item = new JMenuItem("Check Profit By Agent");
		item.addActionListener(new CheckProfitByAgent());
		telmed.add(item);
		telmed.addSeparator();
		item = new JMenuItem("Total Profit");
		item.addActionListener(new CheckTotalProfit());
		telmed.add(item);
		item = new JMenuItem("Update Telmed  Status");
		item.addActionListener(new UpdateTelmedStatus());
		telmed.add(item);
		item = new JMenuItem("Update Telmed Pharmacy Status");
		item.addActionListener(new UpdatePharmacyStatus());
		telmed.add(item);
		item = new JMenuItem("Update Telmed Cost");
		item.addActionListener(new UpdateTelmedCost());
		telmed.add(item);
		item = new JMenuItem("Load Un Sent Telmeds");
		item.addActionListener(new LoadUnSentTelmed());
		telmed.add(item);
		item = new JMenuItem("Load Check Data");
		item.addActionListener(new LoadTemedInfo());
		telmed.add(item);
		bar.add(telmed);
		
		JMenu alternateProducts = new JMenu("Alternate Scripts");
		JMenu products = new JMenu("Load Products");
		for(String script: Fax.ProductScripts.ALL) {
			JMenuItem i = new JMenuItem(script);
			i.addActionListener(new LoadProducts());
			products.add(i);
		}
		alternateProducts.add(products);
		item = new JMenuItem("Set Fax Disposition");
		item.addActionListener(new UpdateAlternateScriptDisposition());
		alternateProducts.add(item);
		item = new JMenuItem("Load Wrong Doctors");
		item.addActionListener(new LoadWrongDoctor());
		alternateProducts.add(item);
		JMenu alternateProductApprovals = new JMenu("Load Approvals");
		for(String script: Fax.ProductScripts.ALL) {
			JMenuItem i = new JMenuItem(script);
			i.addActionListener(new LoadAlternateScriptApprovals());
			alternateProductApprovals.add(i);
		}
		alternateProducts.add(alternateProductApprovals);
		item = new JMenuItem("Load Denials");
		item.addActionListener(new LoadOldDenials());
		alternateProducts.add(item);
		item = new JMenuItem("Load Fax Dispositions");
		item.addActionListener(new LoadAlternateScriptsFaxDisposition());
		alternateProducts.add(item);
		bar.add(alternateProducts);
		
		JMenu roadmap = new JMenu("RoadMaps");
		item = new JMenuItem("Check Road Map");
		item.addActionListener(new LoadCheckRoadMapFrame());
		roadmap.add(item);
		item = new JMenuItem("Load Road Map");
		item.addActionListener(new LoadRoadMap());
		roadmap.add(item);
		item = new JMenuItem("Export Full RoadMap");
		item.addActionListener(new ExportFullRoadMap());
		roadmap.add(item);
		item = new JMenuItem("Export Pharmacy RoadMap");
		item.addActionListener(new ExportRoadMap());
		roadmap.add(item);
		bar.add(roadmap);
		
		JMenu invoice = new JMenu("Invoices");
		item = new JMenuItem("Create Invoices");
		item.addActionListener(new CreateInvoices());
		invoice.add(item);
		bar.add(invoice);
		
		JMenu notFound = new JMenu("Not Found");
		item = new JMenuItem("Load Not Founds");
		item.addActionListener(new LoadNotFounds());
		notFound.add(item);
		item = new JMenuItem("Load Invalid Info");
		item.addActionListener(new LoadInvalidInfo());
		notFound.add(item);
		bar.add(notFound);
		
		JMenu invoiceReports = new JMenu("Invoicing Reports");
		JMenu report = new JMenu("Reports");
		item = new JMenuItem("Mark Report");
		item.addActionListener(new MarkReport());
		report.add(item);
		invoiceReports.add(report);
		
		JMenu paidReport = new JMenu("Paid Reports");
		item = new JMenuItem("Mark Paid Report");
		item.addActionListener(new MarkPaidReport());
		paidReport.add(item);
		invoiceReports.add(paidReport);
		bar.add(invoiceReports);
		
		return bar;
	}
}