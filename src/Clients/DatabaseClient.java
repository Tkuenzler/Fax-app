package Clients;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONException;
import com.mysql.jdbc.CommunicationsException;

import Database.Columns.LeadColumns;
import Fax.EmdeonStatus;
import Fax.FaxStatus;
import Fax.MessageStatus;
import Fax.ProductScripts;
import Fax.TelmedStatus;
import Info.DatabaseUser;
import PBM.InsuranceFilter;
import PBM.InsuranceType;
import PaidReport.MarkReport.Rows;
import source.CSVFrame;
import source.LoadInfo;
import table.Record;

public class DatabaseClient {
	String database,table;
	DatabaseUser db;
	public Connection connect = null;
	public DatabaseClient(boolean dbOnly) {
		try {
			db = LoadInfo.LoadDatabaseUser();
			if(db.isBlank()) {
				JOptionPane.showMessageDialog(null, "No Database info entered");
				return;
			}
			//This will load the MySQL driver, each DB has its own driver
			 Class.forName("com.mysql.jdbc.Driver"); 
			 //Connect to database
			connect = DriverManager
					      .getConnection("jdbc:mysql://"+db.getLocalHost()+":"+db.getPort()+"/", db.getUser(),db.getPassword());
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		String[] databases = getDatabases();
		this.database = (String) JOptionPane.showInputDialog(new JFrame(), "Select a database", "Database:", JOptionPane.QUESTION_MESSAGE, null, databases, databases[0]);
		if(!dbOnly) {
			String[] tables = getTables();
			this.table = (String) JOptionPane.showInputDialog(new JFrame(), "Select a table", "tables:", JOptionPane.QUESTION_MESSAGE, null, tables, tables[0]);
		}
		closeConnection();
		reconnect();
	}
	public DatabaseClient(String database,String table) {
		if(table==null)
			table = "";
		try {
			db = LoadInfo.LoadDatabaseUser();
			if(db.isBlank()) {
				JOptionPane.showMessageDialog(null, "No Database info entered");
				return;
			}
			//This will load the MySQL driver, each DB has its own driver
			 Class.forName("com.mysql.jdbc.Driver"); 
			 //Connect to database
			connect = DriverManager
					      .getConnection("jdbc:mysql://"+db.getLocalHost()+":"+db.getPort()+"/"+database, db.getUser(),db.getPassword());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		this.database = database;
		this.table = table;
	}
	public int updateContractId(Record record) {
		String sql = "UPDATE `Leads` SET `contract_id` = '"+record.getContractId()+"', `benefit_id` = '"+record.getBenefitId()+"' WHERE `_id` = '"+record.getId()+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			ex.printStackTrace();
			return ex.getErrorCode();
		} finally {
			try {
				if(stmt!=null)stmt.close();
			} catch(SQLException ex) {
				
			}
		}
	}
	public String[] GetColumns(String table)  {
		try {
			Statement stmt = connect.createStatement();
			String sql = "SHOW COLUMNS FROM `"+table+"`";
			ResultSet columns = stmt.executeQuery(sql);
			List<String> list = new ArrayList<String>();
			while(columns.next()) {
				String column = columns.getString("Field");
				list.add(column);
			}
			return list.toArray(new String[list.size()]);
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}
	public int UpdatePharmacyStatus(String phone,String status) {
		String sql = "UPDATE `TELMED` SET `PHARMACY_STATUS` = '"+status+"' WHERE `phonenumber` = '"+phone+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			ex.printStackTrace();
			return ex.getErrorCode();
		} finally {
			try {
				if(stmt!=null)stmt.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	public int UpdatePharmacyStatus(String first,String last,String dob,String status) {
		String sql = "UPDATE `TELMED` SET `PHARMACY_STATUS` = '"+status+"' WHERE `first_name` = '"+first+"' AND `last_name` = '"+last+"' AND `dob` = '"+dob+"' AND "+TelmedStatus.GetApprovedPaidStatusQuery();
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			ex.printStackTrace();
			return ex.getErrorCode();
		} finally {
			try {
				if(stmt!=null)stmt.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	public int updateCallCenter(String agent, String callCenter) {
		String update = "UPDATE `TELMED` SET `CALL_CENTER` = '"+callCenter+"' WHERE `agent` = '"+agent+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(update);
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
		return 0;
	}
	private String GetPharmacyQuery(String pharmacy) {
		switch(pharmacy) {
		case "All":
			return " `PHARMACY` <> '' ";
		case "All But No Home":
			return " `PHARMACY` <> 'No Home' ";
		default:
			return " `PHARMACY` = '"+pharmacy+"' ";
		}
		
	}
	public List<Record> GetLeadsByDaysBack(int daysBack) {
		String sql = "SELECT * FROM `Leads` WHERE `DATE_ADDED` >= DATE_ADD(CURDATE(), INTERVAL -"+daysBack+" DAY)";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			List<Record> list = new ArrayList<Record>();
			while(set.next()) {
				list.add(new Record(set,database,table));
			}
			return list;
		} catch(SQLException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				if(stmt!=null) stmt.close();
				if(set!=null)	set.close();
			} catch(SQLException ex) {

			}
		}
		
	}
	public int deleteTEMEDS(String phone) {
		String delete = "DELETE FROM `TELMED` WHERE `phonenumber` = '"+phone+"'";
		try {
			Statement stmt = connect.createStatement();
			int value = stmt.executeUpdate(delete);
			stmt.close();
			return value;
		} catch(SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
	}
	public String[] getDatabases() {
		try {
			Statement stmt = connect.createStatement();
			String sql = "SHOW DATABASES";
			ResultSet databases = stmt.executeQuery(sql);
			List<String> list = new ArrayList<String>();
			while(databases.next()) {
				String database = databases.getString("Database");
				if(database.equalsIgnoreCase("information_schema"))
					continue;
				else 
					list.add(database);
			}
			return list.toArray(new String[list.size()]);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String[] getTables() {
		try {
			Statement stmt = connect.createStatement();
			String sql = "SHOW TABLES IN "+this.database;
			ResultSet tables = stmt.executeQuery(sql);
			List<String> list = new ArrayList<String>();
			while(tables.next()) {
				if(tables.getString("Tables_in_"+database).equalsIgnoreCase("Alternate_Scripts") ||
						tables.getString("Tables_in_"+database).equalsIgnoreCase("FAXED") ||
						tables.getString("Tables_in_"+database).equalsIgnoreCase("Recordings") || 
						tables.getString("Tables_in_"+database).equalsIgnoreCase("NOT_FOUND"))
					continue;
				else
					list.add(tables.getString("Tables_in_"+database));
			}
			return list.toArray(new String[list.size()]);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void reconnect() {
		try {
			//This will load the MySQL driver, each DB has its own driver
			 Class.forName("com.mysql.jdbc.Driver"); 
			 //Connect to database
			connect = DriverManager
					      .getConnection("jdbc:mysql://"+db.getLocalHost()+":"+db.getPort()+"/"+database+"?autoReconnect=true&useUnicode=yes", db.getUser(),db.getPassword());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}
	public void connect() throws CommunicationsException {
		try {
			//This will load the MySQL driver, each DB has its own driver
			 Class.forName("com.mysql.jdbc.Driver"); 
			 //Connect to database
			connect = DriverManager
					      .getConnection("jdbc:mysql://"+db.getLocalHost()+":"+db.getPort()+"/"+database, db.getUser(),db.getPassword());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}
	public void closeConnection()  {
		try {
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean isClosed() {
		try {
			return connect.isClosed();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return true;
		}
	}
	public String getTableName() {
		return this.table;
	}
	public String getDatabaseName() {
		return this.database;
	}
	public void GetLeadsByPharmacyQuery(String query) {
		String sql = "SELECT * FROM `"+table+"` WHERE "+query;
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next()) {
				CSVFrame.model.addRow(new Record(set,database,table));
			}
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch(SQLException ex) {
				
			}
		}
	}
	public int deleteRecord(Record record) {
		String sql = "DELETE FROM `"+table+"` WHERE `"+Columns.ID+"` = '"+record.getId()+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			int delete = stmt.executeUpdate(sql);
			if(delete>0)
				deleteFromAlternateScript(record);
			return delete;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return 0;
		} finally {
			try {
				if(stmt!=null) stmt.close();
			} catch(SQLException ex) {
				
			}
		}
	}
	public int deleteFromAlternateScript(Record record) {
		String sql = "DELETE FROM `Alternate_Scripts` WHERE `"+Columns.ID+"` = '"+record.getId()+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return 0;
		} finally {
			try {
				if(stmt!=null) stmt.close();
			} catch(SQLException ex) {
				
			}
		}
	}
	public int addRecord(Record record,String AFID) {
		PreparedStatement stmt = null;
		try {
			stmt = connect.prepareStatement(buildAddStatement());
			for(int i = 0;i<Columns.HEADERS.length;i++) {
				String column = Columns.HEADERS[i];
				switch(column) {
				case Columns.FIRST_NAME:
					stmt.setString(i, record.getFirstName());
					break;
				case Columns.LAST_NAME:
					stmt.setString(i, record.getLastName());
					break;
				case Columns.LAST_EMDEON_DATE:
					stmt.setString(i, getCurrentDate("yyyy-MM-dd"));
					break;
				case Columns.DOB:
					stmt.setString(i, record.getDob());
					break;
				case Columns.PHONE_NUMBER:
					stmt.setString(i, record.getPhone());
					break;
				case Columns.ADDRESS:
					stmt.setString(i, record.getAddress());
					break;
				case Columns.CITY:
					stmt.setString(i, record.getCity());
					break;
				case Columns.STATE:
					stmt.setString(i, record.getState());
					break;
				case Columns.ZIPCODE:
					stmt.setString(i, record.getZip());
					break;
				case Columns.CARRIER:
					stmt.setString(i, record.getCarrier());
					break;
				case Columns.POLICY_ID:
					stmt.setString(i, record.getPolicyId());
					break;
				case Columns.BIN:
					stmt.setString(i, record.getBin());
					break;
				case Columns.GROUP:
					stmt.setString(i, record.getGrp());
					break;
				case Columns.PCN:
					stmt.setString(i, record.getPcn());
					break;
				case Columns.NPI:
					stmt.setString(i, record.getNpi());
					break;
				case Columns.DR_FIRST:
					stmt.setString(i, record.getDrFirst());
					break;
				case Columns.DR_LAST:
					stmt.setString(i, record.getDrLast());
					break;
				case Columns.DR_ADDRESS1:
					stmt.setString(i, record.getDrAddress1());
					break;
				case Columns.DR_CITY:
					stmt.setString(i, record.getDrCity());
					break;
				case Columns.DR_STATE:
					stmt.setString(i, record.getDrState());
					break;
				case Columns.DR_ZIP:
					stmt.setString(i, record.getDrZip());
					break;
				case Columns.DR_PHONE:
					stmt.setString(i, record.getDrPhone());
					break;
				case Columns.DR_FAX:
					stmt.setString(i, record.getDrFax());
					break;
				case Columns.SSN:
					stmt.setString(i, record.getSsn());
					break;
				case Columns.ID:
					stmt.setString(i, record.getFirstName()+record.getLastName()+record.getPhone());
					break;
				case Columns.GENDER:
					stmt.setString(i, record.getGender());
					break;		
				case Columns.FAX_DISPOSITION:
					stmt.setString(i, "");
					break;
				case Columns.MESSAGE_STATUS:
					stmt.setString(i,"");
					break;
				case Columns.AFID:
					stmt.setString(i, AFID);
					break;
				case Columns.PHARMACY:
					stmt.setString(i, record.getPharmacy());
					break;
				case Columns.MESSAGE_ID:
					stmt.setString(i, "");
					break;
				case Columns.NOTES:
					stmt.setString(i, "");
					break;
				case Columns.CALL_CENTER:
					stmt.setString(i, record.getCallCenter());
					break;
				case Columns.EMDEON_STATUS:
					stmt.setString(i, record.getStatus());
					break;
				case Columns.AGENT:
					stmt.setString(i, record.getAgent());
					break;
				case Columns.DATE_ADDED:
					stmt.setString(i, getCurrentDate("yyyy-MM-dd"));
					break;
				}				
			} 
			int add = stmt.executeUpdate();
			if(add==1 && table.equalsIgnoreCase("Leads"))
				AddToAlternateScript(record);
			return add;
		} catch(SQLException ex) {
			if(ex.getErrorCode()==DatabaseErrorCodes.MYSQL_DUPLICATE_PK) {
				System.out.println("DUPLICATE RECORD "+record.getFirstName()+record.getLastName());
				return ex.getErrorCode();
			}
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			return 0;
		} finally {
			try {
				if(stmt!=null)stmt.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	private int AddToAlternateScript(Record record) {
		String sql = "INSERT INTO `Alternate_Scripts` (`_id`) VALUES ('"+record.getId()+"')";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			ex.printStackTrace();
			return ex.getErrorCode();
		} 
		finally {
			try {
				if(stmt!=null)stmt.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
		
	}
	public int updateRecord(Record record) {
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			int update =  stmt.executeUpdate(buildUpdateStatement(record));
			if(update==1) {
				UpdateAlternateScriptId(record);
				record.setId(record.getFirstName()+record.getLastName()+record.getPhone());
			}
			return update;
		} catch(SQLException ex) {
			ex.printStackTrace();
			System.out.print(ex.getMessage());
			return 0;
		} finally {
			try {
				if(stmt!=null) stmt.close();
			} catch(SQLException ex) {
				
			}
		}
	}
	public int UpdateAlternateScriptId(Record record) {
		String sql = "UPDATE `Alternate_Scripts` SET `_id` = '"+record.getFirstName()+record.getLastName()+record.getPhone()+"' WHERE `_id` = '"+record.getId()+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			System.out.print(ex.getMessage());
			return 0;
		} finally {
			try {
				if(stmt!=null) stmt.close();
			} catch(SQLException ex) {
				
			}
		}
	}
	public int updateNotes(Record record,String notes) {
		try {
			String sql = "UPDATE "+table+" SET `"+Columns.NOTES+"` = '"+notes+"' WHERE `"+Columns.ID+
					"` = '"+record.getId()+"'";
			Statement stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			System.out.print(ex.getMessage());
			return 0;
		}
	}
	public String getNotes(Record record) {
		String sql = "SELECT `"+Columns.NOTES+"` FROM "+getTableName()+" WHERE `"+Columns.ID+"` = '"+record.getId()+"'";
		try {
			Statement stmt = connect.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			while(set.next()) {
				return set.getString(Columns.NOTES);
			}
			return null;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
	public ResultSet customQuery(String sql) {
		try {
			Statement stmt = connect.createStatement();
			return stmt.executeQuery(sql);
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
	public int customUpdate(String sql) {
		try {
			Statement stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return 0;
		}
	}
	public void getAllRecords(String pharmacy) {
		String pharmacyQuery = GetPharmacyQuery(pharmacy);
		String sql = "SELECT * FROM `"+getTableName()+"` WHERE "+pharmacyQuery;
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next()) {
				CSVFrame.model.addRow(new Record(set,this.database,this.table));
			} 
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				if(stmt!=null) stmt.close();
				if(set!=null) set.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public String getFaxDisposition(Record record) {
		String sql = "SELECT `"+Columns.FAX_DISPOSITION+"` FROM "+table+" WHERE `"+Columns.ID+"` = '"+record.getId()+"'";
		try {
			Statement stmt = connect.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			if(set.next())
				return set.getString(Columns.FAX_DISPOSITION);
			else 
				return "";
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
	public String getColumn(String column,Record record) {
		String sql = "SELECT `"+column+"` FROM `"+table+"` WHERE `"+Columns.ID+"` = '"+record.getId()+"'";
		try {
			Statement stmt = connect.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			if (set.next())
				return set.getString(column);
			else 
				return "";
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return "";
		}
	}
	public Record getRecord(String phone) {
		String sql = "SELECT * FROM `Leads` WHERE `"+Columns.PHONE_NUMBER+"` = '"+phone+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next())
				return new Record(set,"","");
			else
				return null;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
	public ResultSet getRecord(String first,String last,String phone) {
		String sql = "SELECT * FROM "+table+" WHERE `"+Columns.ID+"` = '"+first+last+phone+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeQuery(sql);
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
	public ResultSet getRecordByName(String first,String last,String state) {
		String sql = "SELECT * FROM "+table+" WHERE `"+Columns.FIRST_NAME+"` = '"+first+"' AND "
				+"`"+Columns.LAST_NAME+"` = '"+last+"' AND `"+Columns.STATE+"` = '"+state+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeQuery(sql);
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
	public ResultSet getByFaxDisposition(String disposition,String pharmacy) {
		String pharmacyQuery = GetPharmacyQuery(pharmacy);
		String sql = "SELECT * FROM "+table+" WHERE `"+Columns.FAX_DISPOSITION+"` = '"+disposition+"' AND"+pharmacyQuery+"ORDER BY `"+Columns.LAST_UPDATED+"` DESC";
		try {
			Statement stmt = connect.createStatement();
			return stmt.executeQuery(sql);
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
	public ResultSet getByMessageStatus(String status,String pharmacy) {
		String pharmacyQuery = GetPharmacyQuery(pharmacy);
		String sql = "SELECT * FROM "+table+" WHERE `"+Columns.MESSAGE_STATUS+"` = '"+status+"' AND"+pharmacyQuery+"ORDER BY `"+Columns.LAST_UPDATED+"` DESC";
		try {
			Statement stmt = connect.createStatement();
			return stmt.executeQuery(sql);
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
	public int getCountByColumn(String column,String disposition) {
		String sql = "SELECT COUNT(*) FROM "+table+" WHERE `"+column+"` = '"+disposition+"'";
		try {
			Statement stmt = connect.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			while (set.next()) {
				 return set.getInt(1);
			}
			return 0;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return 0;
		}
	}
	public int getCountByColumnByAfid(String column,String disposition,String afid) {
		String sql = "SELECT COUNT(*) FROM "+table+" WHERE `"+column+"` = '"+disposition+"' AND `"+Columns.AFID+"` = '"+afid+"'";
		try {
			Statement stmt = connect.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			int value = 0;
			while (set.next()) {
				 value = set.getInt(1);
			}
			set.close();
			stmt.close();
			return value;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return 0;
		}
	}
	public int updateRecord(String column,String value,Record record) {
		String sql = "UPDATE "+table+" SET "+column+" = '"+value+"' WHERE `phonenumber` =  '"
				+record.getPhone()+"'";
		System.out.println(sql);
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "ERROR "+e.getMessage());
			return 0;
		}
	}
	public int updateRecord(String column,String value,Record record,String table) {
		String sql = "UPDATE "+table+" SET "+column+" = '"+value+"' WHERE `phonenumber` =  '"
				+record.getPhone()+"'";
		System.out.println(sql);
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "ERROR "+e.getMessage());
			return 0;
		}
	}
	public int updateFaxDisposition(String disposition,Record record) {
		String confirmed = null;
		if(disposition.equalsIgnoreCase(FaxStatus.APPROVED))
			confirmed = "`"+Columns.CONFIRM_DOCTOR+"` = 1, ";
		else
			confirmed = "";
		String sql = "UPDATE "+table+" SET `"+Columns.FAX_DISPOSITION_DATE+"` = '"+getCurrentDate("yyyy-MM-dd")+"', `"+Columns.FAX_DISPOSITION+"` = '"+disposition+"', "+confirmed+"`"
				+Columns.MESSAGE_STATUS+"` = 'Sent' "+"WHERE `_id` =  '"+record.getId()+"'";
		System.out.println(sql);
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	private int incrementFaxSent(Record record) {
		String sql = "UPDATE "+table+" SET `"+Columns.FAXES_SENT+"` = `"+Columns.FAXES_SENT+"` + 1, `"+Columns.FAX_SENT_DATE+"` = '"+getCurrentDate("yyyy-MM-dd")+"' WHERE "
				+ "`"+Columns.ID+"` = '"+record.getId()+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			int increment = stmt.executeUpdate(sql);
			resetFaxAttempts(record);
			stmt.close();
			return increment;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "ERROR "+e.getMessage());
			return 0;
		}
	}
	private int incrementFaxAttempt(Record record) {
		String sql = "UPDATE "+table+" SET `"+Columns.FAX_ATTEMPTS+"` = `"+Columns.FAX_ATTEMPTS+"` + 1 WHERE "
				+ "`"+Columns.ID+"` = '"+record.getId()+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			int increment = stmt.executeUpdate(sql);
			stmt.close();
			return increment;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "ERROR "+e.getMessage());
			return 0;
		}
	}
 	private int resetFaxAttempts(Record record) {
 		String sql = "UPDATE "+table+" SET `"+Columns.FAX_ATTEMPTS+"` = 0 WHERE "
				+ "`"+Columns.ID+"` = '"+record.getId()+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "ERROR "+e.getMessage());
			return 0;
		}
 	}
	public void close() {
		try {
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int UpdateEmdeonDate(Record record) {
		String date = getCurrentDate("yyyy-MM-dd");
		System.out.println(date);
		String sql = "UPDATE `Leads` SET `LAST_EMDEON_DATE` = '"+date+"' WHERE `_id` = '"+record.getId()+"'";
		Statement stmt  = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			return e.getErrorCode();
		} finally {
			try {
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				
			}
		}
	}
	public String loadColumn(Record record,String column) {
		String sql = "SELECT `"+column+"` FROM `"+getTableName()+"` WHERE `"+Columns.ID+"` = '"+record.getId()+"'";
		System.out.println(sql);
		try {
			Statement statement = connect.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if(resultSet.next())
				return resultSet.getString(column);
			else
				return null;
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	public int rowCount() {
		try {
			Statement statement = connect.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM "+table);
			while (resultSet.next()) {
				 return resultSet.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	private String buildAddStatement() {
		StringBuilder base = new StringBuilder("INSERT into "+table+" (");
		for(String s: Columns.HEADERS) {
			if(s.equalsIgnoreCase(Columns.ALL))
				continue;
			else if(s.equalsIgnoreCase(Columns.DATE_ADDED)) 
				base.append(s+")");
			else 
				base.append(s+",");

		}
		base.append(" values(");
		for(int i = 0;i<Columns.HEADERS.length-1;i++) {
			if(i==Columns.HEADERS.length-2) 
				base.append("?)");
			else 
				base.append("?,");
		}
		return base.toString();
	}
	private String buildUpdateStatement(Record record) {
		StringBuilder base = new StringBuilder("UPDATE `"+table+"` SET ");
		for(String s: Columns.HEADERS) {
			switch(s) {
			case Columns.FIRST_NAME:
				base.append("`"+Columns.FIRST_NAME+"` = '"+record.getFirstName()+"', ");
				break;
			case Columns.LAST_NAME:
				base.append("`"+Columns.LAST_NAME+"` = '"+record.getLastName()+"', ");
				break;
			case Columns.DOB:
				base.append("`"+Columns.DOB+"` = '"+record.getDob()+"', ");
				break;
			case Columns.PHONE_NUMBER:
				base.append("`"+Columns.PHONE_NUMBER+"` = '"+record.getPhone()+"', ");
				break;
			case Columns.ADDRESS:
				base.append("`"+Columns.ADDRESS+"` = '"+record.getAddress()+"', ");
				break;
			case Columns.CITY:
				base.append("`"+Columns.CITY+"` = '"+record.getCity()+"', ");
				break;
			case Columns.STATE:
				base.append("`"+Columns.STATE+"` = '"+record.getState()+"', ");
				break;
			case Columns.PHARMACY:
				base.append("`"+Columns.PHARMACY+"` = '"+record.getPharmacy()+"'");
				break;
			case Columns.ZIPCODE:
				base.append("`"+Columns.ZIPCODE+"` = '"+record.getZip()+"', ");
				break;
			case Columns.EMDEON_STATUS:
				if(EmdeonStatus.IsValidStatus(record.getStatus()))
					base.append("`"+Columns.EMDEON_STATUS+"` = '"+record.getStatus()+"', ");
				break;
			case Columns.LAST_EMDEON_DATE:
				if(record.hasBeenEmdeoned())
					base.append("`"+Columns.LAST_EMDEON_DATE+"` = '"+getCurrentDate("yyyy-MM-dd")+"', ");
				break;
			case Columns.CARRIER:
				base.append("`"+Columns.CARRIER+"` = '"+record.getCarrier()+"', ");
				break;
			case Columns.POLICY_ID:
				base.append("`"+Columns.POLICY_ID+"` = '"+record.getPolicyId()+"', ");
				break;
			case Columns.BIN:
				base.append("`"+Columns.BIN+"` = '"+record.getBin()+"', ");
				break;
			case Columns.GROUP:
				base.append("`"+Columns.GROUP+"` = '"+record.getGrp()+"', ");
				break;
			case Columns.PCN:
				base.append("`"+Columns.PCN+"` = '"+record.getPcn()+"', ");
				break;
			case Columns.NPI:
				base.append("`"+Columns.NPI+"` = '"+record.getNpi()+"', ");
				break;
			case Columns.DR_FIRST:
				base.append("`"+Columns.DR_FIRST+"` = '"+record.getDrFirst()+"', ");
				break;
			case Columns.DR_LAST:
				base.append("`"+Columns.DR_LAST+"` = '"+record.getDrLast()+"', ");
				break;
			case Columns.DR_ADDRESS1:
				base.append("`"+Columns.DR_ADDRESS1+"` = '"+record.getDrAddress1()+"', ");
				break;
			case Columns.DR_CITY:
				base.append("`"+Columns.DR_CITY+"` = '"+record.getDrCity()+"', ");
				break;
			case Columns.DR_STATE:
				base.append("`"+Columns.DR_STATE+"` = '"+record.getDrState()+"', ");
				break;
			case Columns.DR_ZIP:
				base.append("`"+Columns.DR_ZIP+"` = '"+record.getDrZip()+"', ");
				break;
			case Columns.DR_PHONE:
				base.append("`"+Columns.DR_PHONE+"` = '"+record.getDrPhone()+"', ");
				break;
			case Columns.DR_FAX:
				base.append("`"+Columns.DR_FAX+"` = '"+record.getDrFax()+"', ");
				break;
			case Columns.SSN:
				base.append("`"+Columns.SSN+"` = '"+record.getSsn()+"', ");
				break;
			case Columns.ID:
				base.append("`"+Columns.ID+"` = '"+record.getFirstName()+record.getLastName()+record.getPhone()+"', ");
				break;
			case Columns.GENDER:
				base.append("`"+Columns.GENDER+"` = '"+record.getGender()+"', ");
				break;
			
			}		
		}
		base.append(" WHERE `"+Columns.ID+"` = '"+record.getId()+"'");
		return base.toString();
	}
	public boolean tester2(int back) {
		String sql = "SELECT DAYOFWEEK(DATE_ADD(CURDATE(), INTERVAL -"+back+" DAY)) AS `DAY` FROM `Leads`";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next()) {
				int day = set.getInt("DAY");
				if(day==1 || day==7)
					return false;
				else
					return true;
			}
			return false;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return false;
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch(SQLException ex) {
				
			}
		}
	}
	public int tester(String agent,int back) {
		String sql = "SELECT COUNT(*) AS `COUNT` FROM `Leads` WHERE `"+Columns.AGENT+"` = '"+agent+"' AND `DATE_ADDED` = DATE_ADD(CURDATE(), INTERVAL -"+back+" DAY)";
		int telmedCount = telmedtest(agent,back);
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next())
				return set.getInt("COUNT")+telmedCount;
			else 
				return -1;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return 0;
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch(SQLException ex) {
				
			}
		}
	}
	public int telmedtest(String agent,int back) {
		String sql = "SELECT COUNT(*) AS `COUNT` FROM `TELMED` WHERE `"+Columns.AGENT+"` = '"+agent+"' AND `DATE_ADDED` = DATE_ADD(CURDATE(), INTERVAL -"+back+" DAY)";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next())
				return set.getInt("COUNT");
			else 
				return -1;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return 0;
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch(SQLException ex) {
				
			}
		}
	}
	public String GetTelmedStatus(Record record) {
		String sql = "SELECT * FROM `TELMED` WHERE `"+Columns.PHONE_NUMBER+"` = '"+record.getPhone()+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next())
				return set.getString("TELMED_STATUS");
			else
				return "NOT IN TELMED DATABASE";
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return ex.getMessage();
		} finally {
			try {
				if(stmt!=null) stmt.close();
				if(set!=null) set.close();
			} catch(SQLException ex) {
				
			}
		}
	}
	public String GetTelmedCallCenter(Record record) {
		String sql = "SELECT * FROM `TELMED` WHERE `"+Columns.PHONE_NUMBER+"` = '"+record.getPhone()+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next())
				return set.getString("CALL_CENTER");
			else
				return "NOT IN TELMED DATABASE";
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return ex.getMessage();
		} finally {
			try {
				if(stmt!=null) stmt.close();
				if(set!=null) set.close();
			} catch(SQLException ex) {
				
			}
		}
	}
	public String getVendorId(String phone) {
		String sql = "SELECT * FROM `"+getTableName()+"` WHERE `"+Columns.PHONE_NUMBER+"` = '"+phone+"'";
		try {
			Statement stmt = connect.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			String vendor = null;
			if(set.next()) 
				vendor = set.getString("VENDOR_ID");
			else 
				vendor = "";
			set.close();
			stmt.close();
			return vendor;
		} catch(SQLException ex) {
			return ex.getMessage();
		}
	}
	public String getAfid(String id) {
		String sql = "SELECT * FROM `"+getTableName()+"` WHERE `"+Columns.ID+"` = '"+id+"'";
		try {
			Statement stmt = connect.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			String vendor = null;
			if(set.next()) 
				vendor = set.getString(Columns.AFID);
			else 
				vendor = "";
			set.close();
			stmt.close();
			return vendor;
		} catch(SQLException ex) {
			return ex.getMessage();
		}
	}
	public int confirmDoctor(Record record, int confirm) {
		String sql = "UPDATE `"+table+"` SET `"+Columns.CONFIRM_DOCTOR+"` = "+confirm+" WHERE `"+Columns.ID+"` = '"+record.getId()+"'";
		try {
			Statement stmt = connect.createStatement();
			int update = stmt.executeUpdate(sql);
			stmt.close();
			return update;
		} catch(SQLException ex) {
			return -1;
		}
	}
	public String isInDatabase(Record record) {
		String[] tables = getTables();
		try {
			Statement stmt = connect.createStatement();
			for(String tableName: tables) {
				if(tableName.equalsIgnoreCase("TELMED") || tableName.equalsIgnoreCase("OLD_TELMED"))
					continue;
				String sql = "SELECT * FROM `"+tableName+"` WHERE `"+Columns.ID+"` = '"+record.getId()+"'";
				ResultSet set = stmt.executeQuery(sql);
				if(set.next()) {
					set.close();
					stmt.close();
					return tableName;
				}
			}
			return "NOT IN DB";
		} catch(SQLException ex) {
			return "ERROR";
		}
	}
	public int isInTable(Record record,String table) {
		String sql = "SELECT * FROM `"+getTableName()+"` WHERE `"+Columns.PHONE_NUMBER+"` = '"+record.getPhone()+"'";// AND `FAX_DISPOSITION` = 'APPROVED'
		int result;
		try {
			Statement stmt = connect.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			if(set.next()) 
				result = 1;
			else 
				result = 0;
			set.close();
			stmt.close();
			return result;
		} catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return -1;
		}
	}
	//Doctor Chase Metods
	public int updateMessageStatus(Record record) {
		String sql = "UPDATE `"+getTableName()+"` SET "+Columns.MESSAGE_STATUS+" = '"+record.getMessageStatus()+"'  WHERE `_id` =  '"+record.getId()+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			if(record.getMessageStatus().equalsIgnoreCase(MessageStatus.SENT)) 
				incrementFaxSent(record);
			else if(record.getMessageStatus().equalsIgnoreCase(MessageStatus.SENDING_FAILED))
				incrementFaxAttempt(record);
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "ERROR "+e.getMessage());
			return e.getErrorCode();
		}
	}
	public ResultSet getPhoneNumbers(String table) { 
		String sql = "SELECT `phonenumber` FROM `"+table+"`";
		try {
			Statement stmt = connect.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			return set;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			return null;
		}
	}
	public int clearMessageStatus(Record record) {
		String sql = "UPDATE `"+getTableName()+"` SET `"+Columns.MESSAGE_STATUS+"` = '',  `"+Columns.MESSAGE_ID+"` = '' WHERE `"+Columns.ID+"` = '"+record.getId()+"'";
		try {
			Statement stmt = connect.createStatement();
			int update = stmt.executeUpdate(sql);
			stmt.close();
			return update;
		} catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return -1;
		}	
	}
	public int resetMessageStatus(Record record) {
		String sql = "UPDATE `"+getTableName()+"` SET `"+Columns.MESSAGE_STATUS+"` = '', `"+Columns.MESSAGE_ID+"` = '', `"+Columns.FAX_DISPOSITION+"` = '', `"+Columns.FAX_ATTEMPTS+"` = 0, `"+Columns.FAXES_SENT+"` = 0, `"+Columns.DR_CHASE_AGENT+"` = '', "
				+ "`"+Columns.CHASE_COUNT+"` = 0,  `"+Columns.CONFIRM_DOCTOR+"` = 0, `"+Columns.LAST_CHASE_DATE+"` = '0000-00-00', `"+Columns.FAX_DISPOSITION_DATE+"` =  '0000-00-00', `"+Columns.FAX_SENT_DATE+"` = '0000-00-00' WHERE `"+Columns.ID+"` = '"+record.getId()+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return ex.getErrorCode();
		} finally {
			try {
				if(stmt!=null)stmt.close();
			} catch(SQLException ex) {
				
			}
		}
	}	
	public int setMessageId(Record record)  {
		String sql = "UPDATE `"+getTableName()+"` SET `"+Columns.MESSAGE_ID+"` = '"+record.getMessageId()+"' WHERE `"+Columns.ID+"` = '"+record.getId()+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			return ex.getErrorCode();
		} finally {
			try {
				if(stmt!=null)stmt.close();
			} catch(SQLException ex) {
				
			}
		}
	}
	public ResultSet loadBadFaxNumbers(String pharmacy) {
		String pharmacyQuery = GetPharmacyQuery(pharmacy);
		String SENDING_FAILED = "((`MESSAGE_STATUS` = '"+MessageStatus.SENDING_FAILED+"' AND `FAX_DISPOSITION` = '' AND `FAX_ATTEMPTS` >= 4) OR CHAR_LENGTH(`dr_fax`) != 10) AND"+pharmacyQuery;
		String sql = "SELECT * FROM `"+getTableName()+"` WHERE "+SENDING_FAILED;
		try {
			Statement stmt = connect.createStatement();
			return stmt.executeQuery(sql);
		} catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return null;
		}
	}
	public ResultSet LoadDMEFaxables() {
		String FAX_SENT = "(`FAXES_SENT` < 12)";
		String NEW_LEADS = "(`FAX_DISPOSITION` = '' AND `MESSAGE_STATUS` = '' AND `MESSAGE_ID` = '')";
		String SEVEN_DAYS_OLD = "(`FAX_DISPOSITION` = '' AND `MESSAGE_STATUS` <> 'SendingFailed' AND (`FAX_SENT_DATE` < DATE_ADD(CURDATE(), INTERVAL -5 DAY) OR `FAX_SENT_DATE` = '0000-00-00'))";
		String SENDING_FAILED = "(`FAX_DISPOSITION` = '' AND `MESSAGE_STATUS` = 'SendingFailed' AND `FAX_ATTEMPTS` <= 3)";
		String sql = "SELECT * FROM `DME_Leads` WHERE ("+NEW_LEADS+" OR "+SEVEN_DAYS_OLD+" OR "+SENDING_FAILED+") ORDER BY `FAX_SENT_DATE` ASC";
		System.out.println(sql);
		try {
			Statement stmt = connect.createStatement();
			return stmt.executeQuery(sql);
		} catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return null;
		}
	}
	public ResultSet loadFaxableLeads(String pharmacy) {
		String pharmacyQuery = GetPharmacyQuery(pharmacy);
		String FAX_SENT = "(`FAXES_SENT` < 12)";
		String NEW_LEADS = "(`FAX_DISPOSITION` = '' AND `MESSAGE_STATUS` = '' AND `MESSAGE_ID` = '')";
		String SEVEN_DAYS_OLD = "(`FAX_DISPOSITION` = '' AND `MESSAGE_STATUS` <> 'SendingFailed' AND (`FAX_SENT_DATE` < DATE_ADD(CURDATE(), INTERVAL -5 DAY) OR `FAX_SENT_DATE` = '0000-00-00'))";
		String SENDING_FAILED = "(`FAX_DISPOSITION` = '' AND `MESSAGE_STATUS` = 'SendingFailed' AND `FAX_ATTEMPTS` <= 3)";
		String sql = "SELECT * FROM `"+getTableName()+"` WHERE ("+NEW_LEADS+" OR "+SEVEN_DAYS_OLD+" OR "+SENDING_FAILED+") AND"+pharmacyQuery+"ORDER BY `FAX_SENT_DATE` ASC";
		System.out.println(sql);
		try {
			Statement stmt = connect.createStatement();
			return stmt.executeQuery(sql);
		} catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return null;
		}
	}
	public void LoadNoAnswer() {
		RoadMapClient map = new RoadMapClient();
		String pharmacies = map.getPharmacyQueryForDrChase();
		String sql = "SELECT * FROM `Leads` WHERE `FAX_DISPOSITION` = '' AND `CHASE_COUNT` >= 20 AND `CONFIRM_DOCTOR` <> 1 AND"+pharmacies;
		System.out.println(sql);
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next()) { 
				CSVFrame.model.addRow(new Record(set,database, "Leads"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
				if(map!=null)map.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public ResultSet loadLiveFaxableLeads(String pharmacy) {
		String pharmacyQuery = GetPharmacyQuery(pharmacy);
		String LIVE_LEADS = "(`DATE_ADDED` > DATE_ADD(CURDATE(), INTERVAL -4 MONTH) AND `"+Columns.CONFIRM_DOCTOR+"` = 1)";
		String NEW_LEADS = "(`FAX_DISPOSITION` = '' AND `MESSAGE_STATUS` = '' AND `MESSAGE_ID` = '')";
		String SEVEN_DAYS_OLD = "(`FAX_DISPOSITION` = '' AND `MESSAGE_STATUS` <> 'SendingFailed' AND (`FAX_SENT_DATE` < DATE_ADD(CURDATE(), INTERVAL -5 DAY) OR `FAX_SENT_DATE` = '0000-00-00'))";
		String SENDING_FAILED = "(`FAX_DISPOSITION` = '' AND `MESSAGE_STATUS` = 'SendingFailed' AND `FAX_ATTEMPTS` <= 3)";
		String sql = "SELECT * FROM `"+getTableName()+"` WHERE "+LIVE_LEADS+" AND ("+NEW_LEADS+" OR "+SEVEN_DAYS_OLD+" OR "+SENDING_FAILED+") AND "+pharmacyQuery+" ORDER BY `FAX_SENT_DATE` ASC";
		System.out.println(sql);
		try {
			Statement stmt = connect.createStatement();
			return stmt.executeQuery(sql);
		} catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return null;
		}
	}
	public ResultSet loadTelmedLeads() {
		String FAILED_DISPOSITION = "(`FAX_DISPOSITION` <> 'APPROVED' AND `FAX_DISPOSITION` <> '' AND `FAX_DISPOSITION` <> 'DECEASED')";
		String sql = "SELECT * FROM `"+getTableName()+"` WHERE "+FAILED_DISPOSITION;
		try {
			Statement stmt = connect.createStatement();
			return stmt.executeQuery(sql);
		} catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return null;
		} catch(NullPointerException ex) {
			return null;
		}
	}
	public ResultSet loadNeedsNewScript() {
		String sql = "SELECT * FROM `"+getTableName()+"` WHERE `"+Columns.FAX_DISPOSITION+"` = '"+FaxStatus.NEEDS_NEW_SCRIPT+"'";
		try {
			Statement stmt = connect.createStatement();
			return stmt.executeQuery(sql);
			 
		} catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return null;
		}
	}
	public ResultSet loadApprovals(int daysBack,String pharmacy) {
		String pharmacyQuery = GetPharmacyQuery(pharmacy);
		String sql = "SELECT * FROM `"+table+"` WHERE `"+Columns.FAX_DISPOSITION+"` = 'APPROVED' AND `"+Columns.FAX_DISPOSITION_DATE+"` >= DATE_ADD(CURDATE(), INTERVAL - "+daysBack+" DAY) AND"+pharmacyQuery;
		System.out.println(sql);
		try {
			Statement stmt = connect.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			return set;
		} catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return null;
		}
	}
	public ResultSet loadInterupptedFaxes(String pharmacy) {
		String pharmacyQuery = GetPharmacyQuery(pharmacy);
		String sql ="SELECT * FROM `"+table+"` WHERE `"+Columns.MESSAGE_ID+"` <> '' AND `"+Columns.MESSAGE_STATUS+"` = '' AND"+pharmacyQuery;
		try {
			Statement stmt = connect.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			return set;
		} catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return null;
		}
	}
	public boolean IsInTelmed(String phone) {
		String sql = "SELECT * FROM `TELMED` WHERE `phonenumber` = '"+phone+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next())
				return true;
			else
				return false;
		} catch(SQLException ex) {
			return false;
		} finally {
			try {
				if(stmt!=null) stmt.close();
				if(set!=null)set.close();
			} catch(SQLException ex) {
				ex.getErrorCode();
			} 
		}
	}
	public int UpdatePharmacy(Record record, String pharmacy) {
		String sql = "UPDATE `"+table+"` SET `PHARMACY` = '"+pharmacy+"' WHERE `"+Columns.ID+"` = '"+record.getId()+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return -1;
		} finally {
			try {
				if(stmt!=null) stmt.close();
			} catch(SQLException ex) {
				ex.getErrorCode();
			} 
		}
	}
	public int UpdateTelmedStatus(String phone,String id, String status) {
		String sql = "UPDATE `TELMED` SET `TELMED_STATUS` = '"+status+"' WHERE `TELMED_ID` = '"+id+"'";
		try {
			Statement stmt = connect.createStatement();
			int update = stmt.executeUpdate(sql);
			stmt.close();
			return update;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return -1;
		}
	}
	public int UpdateDMETelmedStatus(String phone,String id, String status) {
		String sql = "UPDATE `DME_TELMED` SET `TELMED_STATUS` = '"+status+"', `TELMED_ID` = '"+id+"' WHERE `phonenumber` = '"+phone+"'";
		try {
			Statement stmt = connect.createStatement();
			int update = stmt.executeUpdate(sql);
			stmt.close();
			return update;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return -1;
		}
	}
	public int TEST_TELMED(String phone,String type) {
		String sql = "UPDATE `TELMED` SET `INSURANCE_TYPE` = '"+type+"'"
				+ " WHERE `phonenumber` = '"+phone+"'";
		try {
			Statement stmt = connect.createStatement();
			int update = stmt.executeUpdate(sql);
			stmt.close();
			return update;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return -1;
		}
	}
	public int UpdateTelmedPharmacy(String phone,String pharmacy) {
		String pharm = hasPharmacy(phone);
		if(pharm!=null)
			if(!pharm.equalsIgnoreCase(""))
				return -2;
		String sql = "UPDATE `TELMED` SET `PHARMACY` = '"+pharmacy+"' WHERE `phonenumber` = '"+phone+"'";
		try {
			Statement stmt = connect.createStatement();
			int update = stmt.executeUpdate(sql);
			stmt.close();
			return update;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return -1;
		}
	}
	public int ClearProfitTelmed(String phone) {
		String sql = "UPDATE `TELMED` SET `PROFIT` = 0 WHERE `phonenumber` = '"+phone+"'";
		try {
			Statement stmt = connect.createStatement();
			int update = stmt.executeUpdate(sql);
			stmt.close();
			return update;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return -1;
		}
	}
	public int SetLastFilled(String phone,String date) {
		String sql = "UPDATE `TELMED` SET `LAST_FILL` = '"+date+"' WHERE `phonenumber` = '"+phone+"'";
		try {
			Statement stmt = connect.createStatement();
			int update = stmt.executeUpdate(sql);
			stmt.close();
			return update;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return -1;
		}
	}
	public int incrementProfit(String phone,double profit) {
		String sql = "UPDATE `TELMED` SET `PROFIT` = `PROFIT` + "+profit+" WHERE `phonenumber` = '"+phone+"'";
		try {
			Statement stmt = connect.createStatement();
			int update = stmt.executeUpdate(sql);
			stmt.close();
			return update;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return -1;
		}
	}
	private String hasPharmacy(String phone) {
		String sql = "SELECT `PHARMACY` FROM `TELMED` WHERE `phonenumber` = '"+phone+"'";
		try {
			Statement stmt = connect.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			String pharmacy = null;
			if(set.next())
				pharmacy = set.getString("PHARMACY");
			set.close();
			stmt.close();
			return pharmacy;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
	public int UpdateTelmedCost(String id,String cost) {
		String sql = "UPDATE `TELMED` SET `COST` = '"+cost+"' WHERE `TELMED_ID` = '"+id+"'";
		try {
			Statement stmt = connect.createStatement();
			int update = stmt.executeUpdate(sql);
			stmt.close();
			return update;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return -1;
		}
	}
	public String BadPay() {
		String sql = "SELECT * FROM `TELMED` WHERE `COST` < 0 AND "+TelmedStatus.NGetPaidStatusQuery();
		System.out.println(sql);
		String sum = "SELECT SUM(`COST`) FROM `TELMED` WHERE `COST` < 0 AND "+TelmedStatus.NGetPaidStatusQuery();
		try {
			Statement stmt = connect.createStatement();
			ResultSet list = stmt.executeQuery(sql);
			while(list.next()) {
				System.out.println(list.getString("TELMED_ID")+" "+list.getInt("COST"));
			}
			list.close();
			ResultSet total = stmt.executeQuery(sum);
			int cost = 0;
			if(total.next())
				cost = total.getInt("SUM(`COST`)");
			total.close();
			stmt.close();
			return "$"+cost;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return ex.getMessage();
		}
	}
	public String GoodPay() {
		String sql = "SELECT SUM(`COST`) FROM `TELMED` WHERE `COST` < 0 AND "+TelmedStatus.GetPaidStatusQuery();
		System.out.println(sql);
		try {
			Statement stmt = connect.createStatement();
			ResultSet total = stmt.executeQuery(sql);
			int cost = 0;
			if(total.next())
				cost = total.getInt("SUM(`COST`)");
			total.close();
			stmt.close();
			return "$"+cost;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return ex.getMessage();
		}
	}
	public String TotalProfit() {
		String sql = "SELECT ((SUM(`PROFIT`)/2)-SUM(`COST`)) AS PROFIT FROM `TELMED`";
		try {
			Statement stmt = connect.createStatement();
			ResultSet total = stmt.executeQuery(sql);
			int cost = 0;
			if(total.next())
				cost = total.getInt("PROFIT");
			total.close();
			stmt.close();
			return "$"+cost;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return ex.getMessage();
		}
	}
	public String CheckProfitForAgent(String agent) {
		String sql = "SELECT ((SUM(`PROFIT`)/2) - SUM(`COST`)) AS PROFIT FROM `TELMED` WHERE `agent` = '"+agent+"'";
		try {
			Statement stmt = connect.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			double profit = 0;
			if(set.next())
				profit = set.getDouble("PROFIT");
			set.close();
			stmt.close();
			return "$"+profit;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return ex.getMessage();
		}
	}
	public int AddFullTelmedRecord(Record record,String pharmacy,String status,String id) {
		String sql = "INSERT INTO `TELMED` (`TELMED_ID`,`first_name`,`last_name`,`dob`,`address`,`city`,`state`,`zip`,`phonenumber`,`gender`,`ssn`,`agent`,`TELMED_STATUS`,`ip`,`carrier`,`insurance_name`,`bin`,`grp`,`pcn`,`DATE_MODIFIED`,`DATE_ADDED`,`PHARMACY`,`CALL_CENTER`,`SOURCE`,`NOTES`) "
				+ "VALUES ('"+id+"','"+record.getFirstName()+"','"+record.getLastName()+"','"+record.getDob()+"','"+record.getAddress()+"','"+record.getCity()+"','"+record.getState()+"','"+record.getZip()+"','"+record.getPhone()+"','"+record.getGender()+"','"+record.getSsn()+"','"+record.getAgent()+"',"
				+ "'"+status+"','', '"+record.getCarrier()+"', '','"+record.getBin()+"','"+record.getGrp()+"','"+record.getPcn()+"','"+getCurrentDate("yyyy-MM-dd")+"','"+getCurrentDate("yyyy-MM-dd")+"','"+pharmacy+"','','','')";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			return ex.getErrorCode();
		}  finally {
			try {
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public int UpdateType(String phone,String type) {
		String sql = "UPDATE `TELMED` SET `PBM` = '"+type+"' WHERE `phonenumber` = '"+phone+"'";
		try {
			Statement stmt = connect.createStatement();
			int update = stmt.executeUpdate(sql);
			stmt.close();
			return update;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			if(ex.getErrorCode()==DatabaseErrorCodes.MYSQL_DUPLICATE_PK)
				return -1;
			else
				return 0;
		}
	}
	public String GetTelmedStatus(String phone) {
		String sql = "SELECT `TELMED_STATUS` FROM `TELMED` WHERE `phonenumber` = '"+phone+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next())
				return set.getString("TELMED_STATUS");
			else
				return "NOT FOUND";
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return null;
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}
	public boolean HasRecording(String phone) {
		String sql = "SELECT * FROM `Recordings` WHERE `PHONE` = '"+phone+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next())
				return true;
			else
				return false;
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return false;
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}
	public int AddToTelmed(Record record,String recordId,String ip) {
		String sql = "INSERT INTO `TELMED` (`first_name`,`last_name`,`phonenumber`,`agent`,`TELMED_STATUS`,`ip`,`PBM`,`bin`,`grp`,`pcn`,`DATE_MODIFIED`,`DATE_ADDED`,`State`) "
				+ "VALUES ('"+record.getFirstName()+"','"+record.getLastName()+"','"+record.getPhone()+"','','New Patient','"+ip+"', '"+record.getCarrier()+",'"
						+ "'"+record.getBin()+"','"+record.getGrp()+"','"+record.getPcn()+"','"+getCurrentDate("yyyy-MM-dd")+"','"+getCurrentDate("yyyy-MM-dd")+"','"+record.getState()+"')";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			return ex.getErrorCode();
		}  finally {
			try {
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/*
	 * NOT FOUND FUNCTIONS
	 */
	public void LoadNotFounds(String pharmacy,String insuranceTypeQuery,String state) {
		String sql = "SELECT * FROM `Leads` WHERE "+EmdeonStatus.GetNotFoundStatus()+" AND "+insuranceTypeQuery+" AND `LAST_EMDEON_DATE` < DATE_ADD(CURDATE(), INTERVAL - 3 MONTH) AND `state` = '"+state+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next()) 
				CSVFrame.model.addRow(new Record(set,database,"Leads"));
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
		}  finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	} 
	public void LoadNotFoundWrongInfo() {
		System.out.println(EmdeonStatus.GetInvalidInfo());
		String sql = "SELECT * FROM `Leads` WHERE "+EmdeonStatus.GetInvalidInfo();
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next()) {
				CSVFrame.model.addRow(new Record(set,database,"Leads"));
			}
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
		}  finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	} 
	/*
	 * ALTERNATE SCRIPT FUNCTIONS
	 */
	public int setCoveredItems(Record record,String meds) {
		String sql = "UPDATE `Alternate_Scripts` SET `COVERED_MEDS` = '"+meds+"' WHERE `_id` = '"+record.getId()+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			ex.printStackTrace();
			return ex.getErrorCode();
		} finally {
			try {
				if(stmt!=null)stmt.close();
			} catch(SQLException ex) {
				
			}
		}
	}
	public void LoadCoveredItems() {
		String sql = "SELECT * FROM `Leads` INNER JOIN `Alternate_Scripts` ON `Leads`.`_id` = `Alternate_Scripts`.`_id` WHERE `COVERED_MEDS` <> ''"
				+" AND (`Alternate_Scripts`.`TOPICAL_SCRIPT_MESSAGE_STATUS` = 'SendingFailed' OR `Alternate_Scripts`.`TOPICAL_SCRIPT_FAX_SENT_DATE` = '0000-00-00' "
				+"OR `Alternate_Scripts`.`TOPICAL_SCRIPT_FAX_SENT_DATE` < DATE_ADD(CURDATE(), INTERVAL - 5 DAY))";
		System.out.println(sql);
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next())
				CSVFrame.model.addRow(new Record(set,database,"Leads"));
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}
	public void LoadAlternateScripts(String pharmacy) {
		String pharmacyQuery = GetPharmacyQuery(pharmacy);
		String sql = "SELECT * FROM `Leads` INNER JOIN `Alternate_Scripts` ON `Leads`.`_id` = `Alternate_Scripts`.`_id` WHERE"
				+FaxStatus.SQL_DENIED+" AND"+pharmacyQuery+" AND "
				+"(`"+Columns.FAX_DISPOSITION_DATE+"` < DATE_ADD(CURDATE(), INTERVAL - 5 DAY) AND `"+Columns.FAX_SENT_DATE+"` < DATE_ADD(CURDATE(), INTERVAL - 5 DAY)) AND "
				+"(`Alternate_Scripts`.`TOPICAL_SCRIPT_FAX_DISPOSITION` = '' "
				+"AND (`Alternate_Scripts`.`TOPICAL_SCRIPT_MESSAGE_STATUS` = 'SendingFailed' OR `Alternate_Scripts`.`TOPICAL_SCRIPT_FAX_SENT_DATE` = '0000-00-00' "
				+"OR `Alternate_Scripts`.`TOPICAL_SCRIPT_FAX_SENT_DATE` < DATE_ADD(CURDATE(), INTERVAL - 5 DAY)))";
		System.out.println(sql);
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next())
				CSVFrame.model.addRow(new Record(set,database,"Leads"));
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}
	public void LoadOralScripts(String pharmacy) {
		String pharmacyQuery = GetPharmacyQuery(pharmacy);
		String sql = "SELECT * FROM `Leads` INNER JOIN `Alternate_Scripts` ON `Leads`.`_id` = `Alternate_Scripts`.`_id` WHERE `Alternate_Scripts`.`TOPICAL_SCRIPT_FAX_DISPOSITION` = 'APPROVED' AND"+pharmacyQuery+" AND "
				+"`Alternate_Scripts`.`TOPICAL_SCRIPT_FAX_DISPOSITION` < DATE_ADD(CURDATE(), INTERVAL - 5 DAY) AND "
				+ "(`Alternate_Scripts`.`ORAL_SCRIPT_FAX_DISPOSITION` = '' "
				+ "AND (`Alternate_Scripts`.`ORAL_SCRIPT_MESSAGE_STATUS` = 'SendingFailed' OR `Alternate_Scripts`.`ORAL_SCRIPT_FAX_SENT_DATE` = '0000-00-00' "
				+ "OR `Alternate_Scripts`.`ORAL_SCRIPT_FAX_SENT_DATE` < DATE_ADD(CURDATE(), INTERVAL - 5 DAY)))";
		System.out.println(sql);
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next())
				CSVFrame.model.addRow(new Record(set,database,"Leads"));
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}
	public void LoadProduct(String pharmacy,String product) {
		String pharmacyQuery = GetPharmacyQuery(pharmacy);
		String message_status = ProductScripts.GetProductMessageStatus(product);
		String fax_sent_date = ProductScripts.GetProductFaxDate(product);
		String fax_disposition = ProductScripts.GetProductFaxDispositionColumn(product);
		String sql = "SELECT * FROM `Leads` INNER JOIN `Alternate_Scripts` ON `Leads`.`_id` = `Alternate_Scripts`.`_id` WHERE `"+LeadColumns.PRODUCTS+"` LIKE '%"+product.replace(" Script", "")+"%' AND "
				+ "(`FAX_DISPOSITION` <> '"+FaxStatus.WRONG_DOCTOR+"' AND `FAX_DISPOSITION` <> '"+FaxStatus.ESCRIBE+"' AND `FAX_DISPOSITION` <> '"+FaxStatus.DECEASED+"' AND `FAX_DISPOSITION` <> '"+FaxStatus.NOT_INTERESTED+"') AND"+pharmacyQuery+" AND "
				+"`"+Columns.FAX_DISPOSITION_DATE+"` < DATE_ADD(CURDATE(), INTERVAL - 5 DAY) AND "
				+"(`Alternate_Scripts`.`"+fax_disposition+"` = '' "
				+"AND (`Alternate_Scripts`.`"+message_status+"` = 'SendingFailed' OR `Alternate_Scripts`.`"+fax_sent_date+"` = '0000-00-00' "
				+"OR `Alternate_Scripts`.`"+fax_sent_date+"` < DATE_ADD(CURDATE(), INTERVAL - 5 DAY)))";
		System.out.println(sql);
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next()) {
				Record record = new Record(set,database,"Leads");
				if(record.getGrp().equalsIgnoreCase("788257") || record.getGrp().equalsIgnoreCase("RXCVSD"))
					continue;
				CSVFrame.model.addRow(record);
			}
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}
	public void LoadConstipationScript(String pharmacy) {
		String pharmacyQuery = GetPharmacyQuery(pharmacy);
		String sql = "SELECT * FROM `Leads` INNER JOIN `Alternate_Scripts` ON `Leads`.`_id` = `Alternate_Scripts`.`_id` WHERE "
				+ "(`FAX_DISPOSITION` <> '"+FaxStatus.ESCRIBE+"' AND `FAX_DISPOSITION` <> '"+FaxStatus.DECEASED+"' AND `FAX_DISPOSITION` <> '"+FaxStatus.NOT_INTERESTED+"' AND `FAX_DISPOSITION` <> '"+FaxStatus.WRONG_DOCTOR+"') AND"+pharmacyQuery+" AND "
				+"`"+Columns.FAX_DISPOSITION_DATE+"` < DATE_ADD(CURDATE(), INTERVAL - 5 DAY) AND "
				+"(`Alternate_Scripts`.`CONSTIPATION_FAX_DISPOSITION` = '' "
				+"AND (`Alternate_Scripts`.`CONSTIPATION_MESSAGE_STATUS` = 'SendingFailed' OR `Alternate_Scripts`.`CONSTIPATION_FAX_SENT_DATE` = '0000-00-00' "
				+"OR `Alternate_Scripts`.`CONSTIPATION_FAX_SENT_DATE` < DATE_ADD(CURDATE(), INTERVAL - 5 DAY)))";
		System.out.println(sql);
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next()) {
				Record record = new Record(set,database,"Leads");
				int type = InsuranceFilter.GetInsuranceType(record);
				if(type==InsuranceType.Type.PRIVATE_INSURANCE)
					CSVFrame.model.addRow(record);
			}
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}
	public void LoadWrongDoctor(String pharmacy) {
		String pharmacyQuery = GetPharmacyQuery(pharmacy);
		String sql = "SELECT * FROM `Leads` INNER JOIN `Alternate_Scripts`ON `Leads`.`_id` = `Alternate_Scripts`.`_id` WHERE "
				+"(`Alternate_Scripts`.`TOPICAL_SCRIPT_FAX_DISPOSITION` = 'WRONG DOCTOR' OR "
				+"`Alternate_Scripts`.`TOPICAL_SCRIPT_FAX_DISPOSITION` = 'NEED PCP' OR "
				+"`Alternate_Scripts`.`TOPICAL_SCRIPT_FAX_DISPOSITION` = 'NEEDS TO BE SEEN') AND"+pharmacyQuery;
		System.out.println(sql);
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next())
				CSVFrame.model.addRow(new Record(set,database,"Leads"));
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}
	public void LoadDenials(String pharmacy,int back) {
		String pharmacyQuery = GetPharmacyQuery(pharmacy);
		String sql = "SELECT * FROM `Leads` INNER JOIN `Alternate_Scripts`ON `Leads`.`_id` = `Alternate_Scripts`.`_id` WHERE "
				+"((`Alternate_Scripts`.`TOPICAL_SCRIPT_FAX_DISPOSITION` = 'DENIED' AND `Leads`.`DATE_ADDED` < DATE_ADD(CURDATE(), INTERVAL - "+back+" MONTH)) OR "
						+ "`Alternate_Scripts`.`TOPICAL_SCRIPT_FAX_DISPOSITION` = 'NOT INTERESTED') AND"+pharmacyQuery;
		System.out.println(sql);
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next())
				CSVFrame.model.addRow(new Record(set,database,"Leads"));
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}
	public String loadAlternateScriptFaxDisposition(String column,Record record) {
		String sql = "SELECT * FROM `Alternate_Scripts` WHERE `_id` = '"+record.getId()+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next()) 
				return set.getString(column);
			else 
				return null;
		} catch(SQLException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null)stmt.close();
			} catch(SQLException ex) {
				
			} 
		}
	}
	public void AlternateScriptsLoadApprovals(String column,String columnDate,String pharmacy,int daysBack) {
		String pharmacyQuery = GetPharmacyQuery(pharmacy);
		String sql = "SELECT * FROM `Leads` INNER JOIN `Alternate_Scripts`ON `Leads`.`_id` = `Alternate_Scripts`.`_id` WHERE "
				+"(`Alternate_Scripts`.`"+column+"` = 'APPROVED' AND `Alternate_Scripts`.`"+columnDate+"` >= DATE_ADD(CURDATE(), INTERVAL - "+daysBack+" DAY)) AND"+pharmacyQuery;
		System.out.println(sql);
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next())
				CSVFrame.model.addRow(new Record(set,database,"Leads"));
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}
	public boolean IsFaxedForTopicalScript(String id) {
		String sql = "SELECT * FROM `Leads` WHERE "+FaxStatus.SQL_DENIED+" AND `"+Columns.ID+"` = '"+id+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next())
				return true;
			else
				return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}
	public boolean IsFaxedForOralScript(String id) {
		String sql = "SELECT * FROM `Alternate_Scripts` WHERE `TOPICAL_SCRIPT_FAX_DISPOSITION` = 'APPROVED' AND `"+Columns.ID+"` = '"+id+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next())
				return true;
			else
				return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}
	public int UpdateProductDisposition(String column,String columnDate,String disposition,String id) {
		String sql = "UPDATE `Alternate_Scripts` SET `"+column+"` = '"+disposition+"', `"+columnDate+"` = '"+getCurrentDate("yyyy-MM-dd")+"' WHERE `_id` = '"+id+"'";
		System.out.println(sql);
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			System.out.println(ex.getMessage());
			return ex.getErrorCode();
		} finally {
			try {
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}
	public int UpdateProductMessageStatus(String statusColumn,String dateColumn,Record record) {
		String sql = "UPDATE `Alternate_Scripts` SET `"+statusColumn+"` = '"+record.getMessageStatus()+"', `"+dateColumn+"` = '"+getCurrentDate("yyyy-MM-dd")+"' WHERE `_id` =  '"
				+record.getId()+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "ERROR "+e.getMessage());
			return 0;
		}
	}
	public int ClearProdutMessageStatus(String statusColumn,Record record) {
		String sql = "UPDATE `Alternate_Scripts` SET `"+statusColumn+"` = '' WHERE `"+Columns.ID+"` = '"+record.getId()+"'";
		try {
			Statement stmt = connect.createStatement();
			int update = stmt.executeUpdate(sql);
			stmt.close();
			return update;
		} catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return -1;
		}	
	}
	private String getCurrentDate(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format); 
		Date date = new Date(); 
		return formatter.format(date);
	}
	private String convertDate(String fromFormatString,String toFormatString,String date) {
		SimpleDateFormat toFormat = new SimpleDateFormat(toFormatString); 
		SimpleDateFormat fromFormat = new SimpleDateFormat(fromFormatString); 
		Date fromDate = null;
		try {
			fromDate = fromFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toFormat.format(fromDate);
	}
	public int AddDMELead(Record record) {
		String sql = "INSERT INTO `DME_Leads` (`first_name`,`last_name`,`phonenumber`,`dob`,`address`,`city`,`state`,`zip`,`ssn`,`carrier`,`policy_id`,`_id`) VALUES "
				+ "('"+record.getFirstName()+"','"+record.getLastName()+"','"+record.getPhone()+"','"+record.getDob()+"','"+record.getAddress()+"',"
						+ "'"+record.getCity()+"','"+record.getState()+"','"+record.getZip()+"','"+record.getSsn()+"','"+record.getCarrier()+"','"+record.getPolicyId()+"','"+record.getId()+"')";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			ex.printStackTrace();
			return ex.getErrorCode();
		} finally {
			try {
				if(stmt!=null) stmt.clearBatch();
			} catch(SQLException ex) {
				
			}
		}
	}
	public int AddPaidClaim(Row row,String pharmacy) {
		String sql = "INSERT INTO `PAID` (`_id`,`pharmacy`,`date`,`rx_number`,`name`,`phone_number`,`medication`,`bin`,`grp`,`pcn`,`ndc`,`profit`,`source`) VALUES "
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement stmt = null;
		try {
			stmt = connect.prepareStatement(sql);
			stmt.setString(1, getCellValue(row.getCell(Rows.DATE))+" "+getCellValue(row.getCell(Rows.RX_NUMBER)));
			stmt.setString(2, pharmacy);
			stmt.setString(3, getCellValue(row.getCell(Rows.DATE)));
			stmt.setString(4, getCellValue(row.getCell(Rows.RX_NUMBER)));
			stmt.setString(5, getCellValue(row.getCell(Rows.NAME)));
			stmt.setString(6, getCellValue(row.getCell(Rows.PHONE)).replaceAll("[()\\s-]+", ""));
			stmt.setString(7, getCellValue(row.getCell(Rows.MEDICATION)));
			stmt.setString(8, getCellValue(row.getCell(Rows.BIN)));
			stmt.setString(9, getCellValue(row.getCell(Rows.GRP)));
			stmt.setString(10, getCellValue(row.getCell(Rows.PCN)));
			stmt.setString(11, getCellValue(row.getCell(Rows.NDC)));
			stmt.setString(12, getCellValue(row.getCell(Rows.PROFIT_MARGIN)));
			stmt.setString(13, getSource(row.getCell(Rows.PHONE)));
			return stmt.executeUpdate();
		} catch(SQLException ex) {
			if(!(ex.getErrorCode()==1062));
				ex.printStackTrace();
			return ex.getErrorCode();
		} finally {
			try {
				if(stmt!=null) stmt.close();
			} catch(SQLException ex) {
				
			}
		}
	}
	public int DeletePaidClaim(Cell cell) {
		String sql = "DELETE FROM `Leads` WHERE `phonenumber` = '"+getCellValue(cell).replaceAll("[()\\s-]+", "")+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
	} catch(SQLException ex) {
		if(!(ex.getErrorCode()==1062));
			ex.printStackTrace();
		return ex.getErrorCode();
	} finally {
		try {
			if(stmt!=null) stmt.close();
		} catch(SQLException ex) {
			
		}
	}
	}
	private String getSource(Cell cell) {
		String sql = "SELECT * FROM `Leads` WHERE `phonenumber` = '"+getCellValue(cell).replaceAll("[()\\s-]+", "")+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next()) 
				return set.getString("SOURCE");
			else {
				String source = GetTableSource(cell,"Healthcare");
				if(!source.equalsIgnoreCase(""))
					return source;
				else {
					source = GetTableSource(cell,"TELMED");
					if(!source.equalsIgnoreCase(""))
						return source;
					else
						return "UNKNOWN";
				}
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
			return "ERROR";
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private String GetTableSource(Cell cell,String table) {
		String sql = "SELECT * FROM `"+table+"` WHERE `phonenumber` = '"+getCellValue(cell).replaceAll("[()\\s-]+", "")+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next()) {
				if(table.equalsIgnoreCase("TELMED"))
					return set.getString("SOURCE");
				else
					return table;
			}
			else
				return "";
		} catch(SQLException ex) {
			ex.printStackTrace();
			return "ERROR";
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private String getCellValue(Cell cell) {
		if(cell==null)
			return "";
		if(cell.getCellType()==CellType.NUMERIC && cell.getRowIndex()==Rows.PROFIT_MARGIN)
			return ""+cell.getNumericCellValue();
		else if(cell.getCellType()==CellType.NUMERIC)
			return ""+(int)cell.getNumericCellValue();
		else
			return cell.getStringCellValue().trim();
	}
	public int SetClaimStatus(String date,String rxNumber,String column) {
		System.out.println(column);
		System.out.println(date+" "+rxNumber);
		String sql = "UPDATE `PAID` SET `"+column+"` = 1 WHERE `_id` = '"+date+" "+rxNumber+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			return ex.getErrorCode();
		} finally {
			try {
				if(stmt!=null)stmt.close();
			} catch(SQLException ex) {
				
			}
		}
	}
	public static class Columns {
		public static final String FIRST_NAME = "first_name";
		public static final String LAST_NAME = "last_name";
		public static final String DOB = "dob";
		public static final String PHONE_NUMBER = "phonenumber";
		public static final String ADDRESS = "address";
		public static final String CITY = "city";
		public static final String STATE = "state";
		public static final String ZIPCODE = "zip";
		public static final String EMDEON_STATUS = "EMDEON_STATUS";
		public static final String TYPE = "TYPE";
		public static final String LAST_EMDEON_DATE = "LAST_EMDEON_DATE";
		public static final String CARRIER = "carrier";
		public static final String POLICY_ID = "policy_id";
		public static final String BIN = "bin";
		public static final String GROUP = "grp";
		public static final String PCN = "pcn";
		public static final String NPI = "npi";
		public static final String DR_FIRST = "dr_first_name";
		public static final String DR_LAST = "dr_last_name";
		public static final String DR_ADDRESS1 = "dr_address1";
		public static final String DR_CITY = "dr_city";
		public static final String DR_STATE = "dr_state";
		public static final String DR_ZIP = "dr_zip";
		public static final String DR_PHONE = "dr_phone";
		public static final String DR_FAX = "dr_fax";
		public static final String SSN = "ssn";
		public static final String ID = "_id";
		public static final String GENDER = "gender";
		public static final String EMAIL = "email";
		public static final String FAX_DISPOSITION = "FAX_DISPOSITION";
		public static final String FAX_DISPOSITION_DATE = "FAX_DISPOSITION_DATE";
		public static final String FAX_SENT_DATE = "FAX_SENT_DATE";
		public static final String LAST_UPDATED = "LAST_UPDATED";
		public static final String MESSAGE_STATUS = "MESSAGE_STATUS";
		public static final String TELMED_DISPOSITION = "TELMED_DISPOSITION";
		public static final String MESSAGE_ID = "MESSAGE_ID";
		public static final String FAXES_SENT = "FAXES_SENT";
		public static final String FAX_ATTEMPTS = "FAX_ATTEMPTS";
		public static final String CONFIRM_DOCTOR = "CONFIRM_DOCTOR";
		public static final String AFID = "AFID";
		public static final String PHARMACY= "PHARMACY";
		public static final String NOTES = "notes";
		public static final String USED = "USED";
		public static final String AGENT = "agent";
		public static final String DATE_ADDED = "DATE_ADDED";
		public static final String LAST_CHASE_DATE = "LAST_CHASE_DATE";
		public static final String PAIN_LOCATION = "PAIN_LOCATION";
		public static final String PAIN_CAUSE = "PAIN_CAUSE";
		public static final String DR_CHASE_AGENT = "DR_CHASE_AGENT";
		public static final String CHASE_COUNT = "CHASE_COUNT";
		public static final String CALL_CENTER = "CALL_CENTER";
		public static final String PRODUCTS = "PRODUCTS";
		public static final String ALL = "ALL";
		public static final String[] HEADERS = {ALL,FIRST_NAME,LAST_NAME,DOB,PHONE_NUMBER,ADDRESS,CITY,STATE,ZIPCODE,
				CARRIER,POLICY_ID,BIN,GROUP,PCN,NPI,DR_FIRST,DR_LAST,DR_ADDRESS1,DR_CITY,DR_STATE,EMDEON_STATUS,ID,LAST_EMDEON_DATE,
				DR_ZIP,DR_PHONE,DR_FAX,SSN,GENDER,FAX_DISPOSITION,MESSAGE_STATUS,AFID,PHARMACY,NOTES,MESSAGE_ID,AGENT,CALL_CENTER,DATE_ADDED};
	}
	private class DatabaseErrorCodes {
		public static final int MYSQL_DUPLICATE_PK = 1062;
	}
}

