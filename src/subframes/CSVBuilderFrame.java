package subframes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import source.CSVFrame;
import source.StringSimilarity;
import table.MyTableModel;
import table.Record;

@SuppressWarnings("serial")
public class CSVBuilderFrame extends JFrame {
	
	private String fileName,file;
	private JPanel contentPane;
	private JTextField fileNameTextField;
	private JCheckBox multipleFiles,combineRows;
	private File[] files;
	//ROW1
	JComboBox<String> firstNameBox,lastNameBox,addressBox,dobBox;
	//ROW2
	JComboBox<String> cityBox,stateBox,zipBox,phoneBox;
	//Row3
	JComboBox<String> carrierBox,policyBox,binBox,grpBox;
	//Row4
	JComboBox<String> pcnBox,npiBox,drFirstNameBox,drLastNameBox;
	//Row5
	JComboBox<String> drAddress1Box,drCityBox,drStateBox;
	//Row6
	JComboBox<String> drZipBox, drFaxBox, drPhoneBox,ssnBox;
	//Row 7
	JComboBox<String> genderBox,emailBox, typeBox, statusBox;
	JComboBox<String> pharmacyBox,productsBox;
	
	String[] blank = {"                           "};
	StringBuilder errors = new StringBuilder();
	int rowsAdded;
	/**
	 * Create the frame.
	 */
	public CSVBuilderFrame() {
		super("Build CSV File");
		setResizable(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1250, 400);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel fileSelectPanel = new JPanel();
		fileSelectPanel.setBounds(10, 10, 800, 60);
		fileSelectPanel.setLayout(new MigLayout());

		JButton browse = new JButton("Browse");
		browse.addActionListener(new ChooseFile());
		fileSelectPanel.add(browse,"gapright 5px");		
		
		fileNameTextField = new JTextField();
		fileNameTextField.setEditable(false);
		fileNameTextField.setColumns(40);
		fileSelectPanel.add(fileNameTextField,"gapright 5px");
		
		multipleFiles = new JCheckBox("Multiple Files");
		multipleFiles.setSelected(false);
		fileSelectPanel.add(multipleFiles);
		
		combineRows = new JCheckBox("Combine Patients with different products");
		combineRows.setSelected(false);
		fileSelectPanel.add(combineRows);
		
		contentPane.add(fileSelectPanel);
		setUpHeaderPanel();
		setVisible(true);
	}
	private void setUpHeaderPanel() {
		//35
		JPanel headerPanel = new JPanel();
		headerPanel.setBounds(10, 80, 1200, 360);
		headerPanel.setLayout(new MigLayout());
			
			//Row1
		//First Name 
		JLabel patientFirstName = new JLabel("First Name");
		headerPanel.add(patientFirstName,"gapright 5px");
		firstNameBox = new JComboBox<String>(blank);
		headerPanel.add(firstNameBox,"gapright 5px");
		
		//Last Name
		JLabel patientLastName = new JLabel("Last Name");
		headerPanel.add(patientLastName,"gapright 5px");
		lastNameBox = new JComboBox<String>(blank);
		headerPanel.add(lastNameBox,"gapright 5px");

		//Address
		JLabel patientAddress = new JLabel("Address");
		headerPanel.add(patientAddress,"gapright 5px");
		addressBox = new JComboBox<String>(blank);
		headerPanel.add(addressBox,"gapright 5px");
		
		//DOB
		JLabel dob = new JLabel("DOB");
		headerPanel.add(dob,"gapright 5px");
		dobBox = new JComboBox<String>(blank);
		headerPanel.add(dobBox,"wrap");
		
			//ROW2
		//City
		JLabel patientCity = new JLabel("City");
		headerPanel.add(patientCity,"gapright 5px, gaptop 10px");
		cityBox = new JComboBox<String>(blank);
		headerPanel.add(cityBox,"gapright 5px, gaptop 10px");
		
		
		//State
		JLabel patientState = new JLabel("State");
		headerPanel.add(patientState,"gapright 5px, gaptop 10px");
		stateBox = new JComboBox<String>(blank);
		headerPanel.add(stateBox,"gapright 5px, gaptop 10px");
		
		//Zip
		JLabel zip = new JLabel("Zip");
		headerPanel.add(zip,"gapright 5px, gaptop 10px");
		zipBox = new JComboBox<String>(blank);
		headerPanel.add(zipBox,"gapright 5px, gaptop 10px");
		
		//PHONE
		JLabel phone = new JLabel("Phone");
		headerPanel.add(phone,"gapright 5px, gaptop 10px");
		phoneBox = new JComboBox<String>(blank);
		headerPanel.add(phoneBox,"wrap");
			
			//ROW 3
		//CARRIER
		JLabel carrier = new JLabel("Insurance");
		headerPanel.add(carrier,"gapright 5px, gaptop 10px");
		carrierBox = new JComboBox<String>(blank);
		headerPanel.add(carrierBox,"gapright 5px, gaptop 10px");
		
		//Policy Id
		JLabel policyId = new JLabel("Policy Id");
		headerPanel.add(policyId,"gapright 5px, gaptop 10px");
		policyBox = new JComboBox<String>(blank);
		headerPanel.add(policyBox,"gapright 5px, gaptop 10px");
		
		//Rx Bin
		JLabel bin = new JLabel("Rx Bin");
		headerPanel.add(bin,"gapright 5px, gaptop 10px");
		binBox = new JComboBox<String>(blank);
		headerPanel.add(binBox,"gapright 5px, gaptop 10px");
	
		//Rx Group
		JLabel grp = new JLabel("Rx Grp");
		headerPanel.add(grp,"gapright 5px, gaptop 10px");
		grpBox = new JComboBox<String>(blank);
		headerPanel.add(grpBox,"wrap");
		
			//ROW 4
		//Rx PCN
		JLabel pcn = new JLabel("PCN");
		headerPanel.add(pcn,"gapright 5px, gaptop 10px");
		pcnBox = new JComboBox<String>(blank);
		headerPanel.add(pcnBox,"gapright 5px, gaptop 10px");
		
		//NPI
		JLabel npi = new JLabel("NPI");
		headerPanel.add(npi,"gapright 5px, gaptop 10px");
		npiBox = new JComboBox<String>(blank);
		headerPanel.add(npiBox,"gapright 5px, gaptop 10px");
		
		//Dr First Name
		JLabel drFirstName = new JLabel("Dr First Name");
		headerPanel.add(drFirstName,"gapright 5px, gaptop 10px");
		drFirstNameBox = new JComboBox<String>(blank);
		headerPanel.add(drFirstNameBox,"gapright 5px, gaptop 10px");
		
		//Dr Last Name
		JLabel drLastName = new JLabel("Dr Last Name");
		headerPanel.add(drLastName,"gapright 5px, gaptop 10px");
		drLastNameBox = new JComboBox<String>(blank);
		headerPanel.add(drLastNameBox,"wrap");

			//Row5
		//Dr Address 1
		JLabel drAddress1 = new JLabel("Dr Address 1");
		headerPanel.add(drAddress1,"gapright 5px, gaptop 10px");
		drAddress1Box = new JComboBox<String>(blank);
		headerPanel.add(drAddress1Box,"gapright 5px, gaptop 10px");
		
		//Pharmacy 
		JLabel drAddress2 = new JLabel("Pharmacy");
		headerPanel.add(drAddress2,"gapright 5px, gaptop 10px");
		pharmacyBox = new JComboBox<String>(blank);
		headerPanel.add(pharmacyBox,"gapright 5px, gaptop 10px");
		
		//Dr City
		JLabel drCity = new JLabel("Dr City");
		headerPanel.add(drCity,"gapright 5px, gaptop 10px");
		drCityBox = new JComboBox<String>(blank);
		headerPanel.add(drCityBox,"gapright 5px, gaptop 10px");
		
		//Dr State
		JLabel drState = new JLabel("Dr State");
		headerPanel.add(drState,"gapright 5px, gaptop 10px");
		drStateBox = new JComboBox<String>(blank);
		headerPanel.add(drStateBox,"wrap");
		
			//Row6
		//Dr Zip
		JLabel drZip = new JLabel("Dr Zip");
		headerPanel.add(drZip,"gapright 5px, gaptop 10px");
		drZipBox = new JComboBox<String>(blank);
		headerPanel.add(drZipBox,"gapright 5px, gaptop 10px");
		
		//Dr Fax
		JLabel drFax = new JLabel("Dr Fax");
		headerPanel.add(drFax,"gapright 5px, gaptop 10px");
		drFaxBox = new JComboBox<String>(blank);
		headerPanel.add(drFaxBox,"gapright 5px, gaptop 10px");
	
		//Dr Phone
		JLabel drPhone = new JLabel("Dr Phone");
		headerPanel.add(drPhone);
		drPhoneBox = new JComboBox<String>(blank);
		headerPanel.add(drPhoneBox,"gapright 5px, gaptop 10px");
		
		JLabel ssn = new JLabel("SSN#");
		headerPanel.add(ssn,"gapright 5px, gaptop 10px");
		ssnBox = new JComboBox<String>(blank);
		headerPanel.add(ssnBox,"wrap");

		//Gender
		headerPanel.add(new JLabel("Gender"),"gapright 5px, gaptop 10px");
		genderBox = new JComboBox<String>(blank);
		headerPanel.add(genderBox,"gapright 5px, gaptop 10px");
		
		//Email
		headerPanel.add(new JLabel("Email"),"gapright 5px, gaptop 10px");
		emailBox = new JComboBox<String>(blank);
		headerPanel.add(emailBox,"gapright 5px, gaptop 10px");
		
		headerPanel.add(new JLabel("Type"),"gapright 5px, gaptop 10px");
		typeBox = new JComboBox<String>(blank);
		headerPanel.add(typeBox,"gapright 5px, gaptop 10px");
		
		
		//Status
		headerPanel.add(new JLabel("Status"),"gapright 5px, gaptop 10px");
		statusBox = new JComboBox<String>(blank);
		headerPanel.add(statusBox,"wrap");

		//Status
		headerPanel.add(new JLabel("Products Box"),"gapright 5px, gaptop 10px");
		productsBox = new JComboBox<String>(blank);
		headerPanel.add(productsBox,"wrap");
		
		//Submit
		JButton submit = new JButton("Submit");
		submit.addActionListener(new Submit());
		headerPanel.add(submit,"cell 5 7, gaptop 20px");
		
		//Smart Mapping
		JButton map = new JButton("Mapping");
		map.addActionListener(new SmartMapping());
		headerPanel.add(map,"gaptop 20px");
		contentPane.add(headerPanel);
	}
	private void setUp() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String[] headers = br.readLine().concat(",  ").split(",");
			for(int i = 0;i<headers.length;i++) {
				firstNameBox.addItem(headers[i]);
				lastNameBox.addItem(headers[i]);
				addressBox.addItem(headers[i]);
				cityBox.addItem(headers[i]);
				stateBox.addItem(headers[i]);
				zipBox.addItem(headers[i]);
				dobBox.addItem(headers[i]);
				phoneBox.addItem(headers[i]);
				carrierBox.addItem(headers[i]);
				policyBox.addItem(headers[i]);
				binBox.addItem(headers[i]);
				grpBox.addItem(headers[i]);
				pcnBox.addItem(headers[i]);
				npiBox.addItem(headers[i]);
				drFirstNameBox.addItem(headers[i]);
				drLastNameBox.addItem(headers[i]);
				drAddress1Box.addItem(headers[i]);
				pharmacyBox.addItem(headers[i]);
				drCityBox.addItem(headers[i]);
				drStateBox.addItem(headers[i]);
				drZipBox.addItem(headers[i]);
				drFaxBox.addItem(headers[i]);
				drPhoneBox.addItem(headers[i]);
				ssnBox.addItem(headers[i]);
				genderBox.addItem(headers[i]);
				emailBox.addItem(headers[i]);
				typeBox.addItem(headers[i]);
				statusBox.addItem(headers[i]);
				productsBox.addItem(headers[i]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void removeAllItemsFromComboBoxes() {
		firstNameBox.removeAllItems();
		lastNameBox.removeAllItems();
		addressBox.removeAllItems();
		dobBox.removeAllItems();
		cityBox.removeAllItems();
		stateBox.removeAllItems();
		zipBox.removeAllItems();
		phoneBox.removeAllItems();
		carrierBox.removeAllItems();
		policyBox.removeAllItems();
		binBox.removeAllItems();
		grpBox.removeAllItems();
		pcnBox.removeAllItems();
		npiBox.removeAllItems();
		drFirstNameBox.removeAllItems();
		drLastNameBox.removeAllItems();
		drAddress1Box.removeAllItems();
		pharmacyBox.removeAllItems();
		drCityBox.removeAllItems();
		drStateBox.removeAllItems();
		drZipBox.removeAllItems();
		drFaxBox.removeAllItems();
		drPhoneBox.removeAllItems();
		ssnBox.removeAllItems();
		genderBox.removeAllItems();
		emailBox.removeAllItems();
		typeBox.removeAllItems();
		statusBox.removeAllItems();
		productsBox.removeAllItems();
	}
	private void loadInfo(int rowToStartAt,String fileToWorkOn) {
		int FIRST_NAME = firstNameBox.getSelectedIndex();
		int LAST_NAME = lastNameBox.getSelectedIndex();
		int ADDRESS = addressBox.getSelectedIndex();
		int CITY = cityBox.getSelectedIndex();
		int STATE = stateBox.getSelectedIndex();
		int ZIP = zipBox.getSelectedIndex();
		int DOB = dobBox.getSelectedIndex();
		int PHONE = phoneBox.getSelectedIndex();
		int INSURANCE = carrierBox.getSelectedIndex();
		int POLICY_ID = policyBox.getSelectedIndex();
		int BIN = binBox.getSelectedIndex();
		int GRP = grpBox.getSelectedIndex();
		int PCN = pcnBox.getSelectedIndex();
		int NPI = npiBox.getSelectedIndex();
		int DR_FIRST_NAME = drFirstNameBox.getSelectedIndex();
		int DR_LAST_NAME = drLastNameBox.getSelectedIndex();
		int DR_ADDRESS1 = drAddress1Box.getSelectedIndex();
		int PHARMACY = pharmacyBox.getSelectedIndex();
		int DR_CITY = drCityBox.getSelectedIndex();
		int DR_STATE = drStateBox.getSelectedIndex();
		int DR_ZIP = drZipBox.getSelectedIndex();
		int DR_FAX = drFaxBox.getSelectedIndex();
		int DR_PHONE = drPhoneBox.getSelectedIndex();
		int SSN = ssnBox.getSelectedIndex();
		int GENDER = genderBox.getSelectedIndex();
		int EMAIL = emailBox.getSelectedIndex();
		int STATUS = statusBox.getSelectedIndex();
		int TYPE = typeBox.getSelectedIndex();
		int PRODUCTS = productsBox.getSelectedIndex();
		LineNumberReader lr = null;
		String currentFile = null;
		HashMap<String, Record> map = new HashMap<String, Record>();
		for(int i = 0;i<files.length;i++) {
			File file = files[i];
			int rowCount = 0;
			String line = null;
			try {
				if(!fileToWorkOn.equalsIgnoreCase(file.getAbsolutePath()) && !fileToWorkOn.equals("")) {
					continue;
				}
				else
					currentFile = file.getAbsolutePath();
				lr = new LineNumberReader(new FileReader(currentFile));
				System.out.println(currentFile);
				String[] columnData = new String[MyTableModel.COLUMN_HEADERS.length];
				while((line=lr.readLine())!=null) {
					if((rowToStartAt>=rowCount && rowToStartAt!=0) || rowCount==0 ) {
						rowCount++;
						System.out.println("SKIPPING");
						continue;
					}
					String[] data = line.concat(", ").split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
					columnData[MyTableModel.FIRST_NAME] = data[FIRST_NAME].trim();
					columnData[MyTableModel.LAST_NAME] = data[LAST_NAME].trim();
					columnData[MyTableModel.ADDRESS] = data[ADDRESS].trim();
					columnData[MyTableModel.DOB] = data[DOB].trim();
					columnData[MyTableModel.CITY] = data[CITY].trim();
					columnData[MyTableModel.STATE] = data[STATE].trim();
					columnData[MyTableModel.ZIP] = data[ZIP].trim();
					columnData[MyTableModel.PHONE] = data[PHONE].trim().replaceAll("^\"|\"$", "");
					columnData[MyTableModel.CARRIER] = data[INSURANCE];
					columnData[MyTableModel.POLICY_ID] = data[POLICY_ID];
					columnData[MyTableModel.BIN] = editRxBin(data[BIN]);
					columnData[MyTableModel.GROUP] = data[GRP].replaceAll("^\"|\"$", "");
					columnData[MyTableModel.PCN] = data[PCN].replaceAll("^\"|\"$", "");
					columnData[MyTableModel.NPI] = data[NPI];
					columnData[MyTableModel.DR_FIRST] = data[DR_FIRST_NAME];
					columnData[MyTableModel.DR_LAST] = data[DR_LAST_NAME];
					columnData[MyTableModel.DR_ADDRESS1] = data[DR_ADDRESS1];
					columnData[MyTableModel.PHARMACY] = data[PHARMACY];
					columnData[MyTableModel.DR_CITY] = data[DR_CITY];
					columnData[MyTableModel.DR_STATE] = data[DR_STATE];
					columnData[MyTableModel.DR_ZIP] = data[DR_ZIP];
					columnData[MyTableModel.DR_FAX] = data[DR_FAX].replaceAll("[()\\s-]+", "");
					columnData[MyTableModel.DR_PHONE] = data[DR_PHONE].replaceAll("[()\\s-]+", "");
					columnData[MyTableModel.SSN] = data[SSN];
					columnData[MyTableModel.GENDER] = data[GENDER];
					columnData[MyTableModel.EMAIL] = data[EMAIL];
					columnData[MyTableModel.STATUS] = data[STATUS];
					columnData[MyTableModel.TYPE] = data[TYPE];
					Record record = new Record(columnData);
					if(combineRows.isSelected()) {
						if(map.containsKey(record.getId())) {
							map.get(record.getId()).addProduct(data[PRODUCTS]);
						}
						else  {
							record.addProduct(data[PRODUCTS]);
							map.put(record.getId(), record);
							CSVFrame.model.addRow(record);
							rowCount++;
							rowsAdded++;
						}
					}
					else {
						record.addProduct(data[PRODUCTS]);
						CSVFrame.model.addRow(record);
						rowCount++;
						rowsAdded++;
					}
				}			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(ArrayIndexOutOfBoundsException e) {
				errors.append("<"+rowCount+"> "+line+"<"+currentFile+">");
				loadInfo(rowCount,currentFile);
			}finally{
				try {
					lr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
		    }
		}
		for(Record record: CSVFrame.model.data) {
			record.printRecord();
			for(String p: record.getProducts())
				System.out.println(p);
		}
		System.out.println(errors.toString());
	}
	private class Submit implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if(fileName==null || fileName.equalsIgnoreCase("")) {
				JOptionPane.showMessageDialog(new JFrame(), "You must pick a file");
				return;
			}
			loadInfo(0, "");
			dispose();
			CSVFrame.getFrames()[0].setTitle(fileNameTextField.getText());
			CSVFrame.title = new StringBuilder(fileNameTextField.getText());
			JOptionPane.showMessageDialog(new JFrame(), "You have added "+rowsAdded+" rows");
		}
	}
	private String editRxBin(String bin) {
		String RxBin = null;
		if(bin.length()==3)
			RxBin = "000"+bin;
		else if(bin.length()==4)
			RxBin = "00"+bin;
		else if(bin.length()==5)
			RxBin = "0"+bin;
		else if(bin.length()==6)
			RxBin = bin;
		return RxBin;
	}
	private class ChooseFile implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if(multipleFiles.isSelected()) {
				files = FileChooser.OpenMultipleCsvFiles();
				if(files==null)
				    return;
				fileName = files[0].getAbsolutePath();
				fileNameTextField.setText(fileName);
				removeAllItemsFromComboBoxes();
				setUp();
			}
			else {
			    files = new File[1];
			    files[0] = FileChooser.OpenCsvFile("Load Data File");
			    if(files[0]==null)
			    	return;
			    file = files[0].getName();
			    fileName = files[0].getAbsolutePath();
			    fileNameTextField.setText(fileName);
			    removeAllItemsFromComboBoxes();
			    setUp();
			}
		}
	}
	private class SmartMapping implements ActionListener {
		private SmartMapping(){			
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(fileName==null)
				return;
			else if(!fileName.equalsIgnoreCase("")) {
				mapComboBox(firstNameBox);
				mapComboBox(lastNameBox);
				mapComboBox(addressBox);
				mapComboBox(cityBox);
				mapComboBox(stateBox);
				mapComboBox(zipBox);
				mapComboBox(phoneBox);
				mapComboBox(dobBox);
				mapComboBox(carrierBox);
				mapComboBox(policyBox);
				mapComboBox(binBox);
				mapComboBox(pcnBox);
				mapComboBox(grpBox);
				mapComboBox(npiBox);
				mapComboBox(drFirstNameBox);
				mapComboBox(drLastNameBox);
				mapComboBox(drCityBox);
				mapComboBox(drStateBox);
				mapComboBox(drZipBox);
				mapComboBox(drAddress1Box);
				mapComboBox(pharmacyBox);
				mapComboBox(drFaxBox);
				mapComboBox(drPhoneBox);
				mapComboBox(ssnBox);
				mapComboBox(genderBox);
				mapComboBox(emailBox);
				mapComboBox(typeBox);
				mapComboBox(statusBox);
			}
		}
		private void mapComboBox(JComboBox<String> box) {
			double high = 0;
			int column = 0;
			String header = null;
			if(box==firstNameBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.FIRST_NAME];
			else if(box==lastNameBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.LAST_NAME];
			else if(box==addressBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.ADDRESS];
			else if(box==cityBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.CITY];
			else if(box==stateBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.STATE];
			else if(box==zipBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.ZIP];
			else if(box==phoneBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.PHONE];
			else if(box==dobBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.DOB];
			else if(box==carrierBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.CARRIER];
			else if(box==policyBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.POLICY_ID];
			else if(box==grpBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.GROUP];
			else if(box==binBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.BIN];
			else if(box==pcnBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.PCN];
			else if(box==drFirstNameBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.DR_FIRST];
			else if(box==drLastNameBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.DR_LAST];
			else if(box==drCityBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.DR_CITY];
			else if(box==drStateBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.DR_STATE];
			else if(box==drZipBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.DR_ZIP];
			else if(box==drAddress1Box)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.DR_ADDRESS1];
			else if(box==pharmacyBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.PHARMACY];
			else if(box==drFirstNameBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.DR_FIRST];
			else if(box==drLastNameBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.DR_LAST];
			else if(box==drFaxBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.DR_FAX];
			else if(box==drPhoneBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.DR_PHONE];
			else if(box==npiBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.NPI];
			else if(box==ssnBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.SSN];
			else if (box==genderBox) 
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.GENDER];
			else if(box==emailBox) 
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.EMAIL];
			else if(box==typeBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.TYPE];
			else if(box==statusBox)
				header = MyTableModel.COLUMN_HEADERS[MyTableModel.STATUS];
			if(box==typeBox || box==statusBox) 
				column = box.getItemCount()-1;
			for(int i = 0;i<box.getModel().getSize();i++) {
				String value = box.getItemAt(i);
				double actual;
				if(value.contains("Physician") && header.contains("Dr")) 
					actual = StringSimilarity.similarity(header.replace("Dr", "Physician"), value);
				else if(value.contains("MD") && header.contains("Dr")) 
					actual = StringSimilarity.similarity(header.replace("Dr", "MD"), value);
				else if(value.contains("Doctor") && header.contains("Dr")) 
					actual = StringSimilarity.similarity(header.replace("Dr", "Doctor"), value);
				else if(header.contains("Insurance") && value.contains("Insurance")) 
					actual = .99;
				else if(header.contains("Policy") && value.contains("Policy"))
					actual = .99;
				else if(header.contains("Phone") && value.contains("number1")) 
					actual = .99;
				else if(header.equalsIgnoreCase("Address") && value.contains("street"))
					actual = 1.00;
				else if(header.equalsIgnoreCase("NPI") && value.contains("NPI"))
					actual = 1.00;
				else if(header.equalsIgnoreCase("DOB") && value.equalsIgnoreCase("Birthdate"))
					actual = .99;
				else if(header.contains("fax") && value.contains("fax"))
					actual = .99;
				else if(header.equalsIgnoreCase("SSN#") && value.equalsIgnoreCase("Medicare #"))
					actual = .99;
				else
					actual = StringSimilarity.similarity(header, value);
				//double actual = StringSimilarity.similarity(header, value);
				if(box==typeBox || box==statusBox) {
					if(Double.compare(actual, .95)>=0)
						column = i;
				}
				else if(actual>high) {
					high = actual;
					column = i;
				}
			}
			box.setSelectedIndex(column);
		}
	}
}
