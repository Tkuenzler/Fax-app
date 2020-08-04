package Clients;

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

import javax.swing.JOptionPane;

import source.CSVFrame;
import table.Record;

public class InfoDatabase {
	private Connection connect = null;
	public InfoDatabase() {
		try {
			//This will load the MySQL driver, each DB has its own driver
			 Class.forName("com.mysql.jdbc.Driver"); 
			 //Connect to database
			 connect = DriverManager
				      .getConnection("jdbc:mysql://ltf5469.tam.us.siteprotect.com:3306/Info_Table", "tkuenzler","Tommy6847");
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
	public String GetPVerifyId(String phone) {
		String sql = "SELECT * FROM `PVERIFY_ID` WHERE `phonenumber` = '"+phone+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next())
				return set.getString("request_id");
			else return null;
		} catch(SQLException e) {
			e.printStackTrace();
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
	public String[] LoadAgents(String callCenter) {
		String sql = "SELECT * FROM `AGENTS` WHERE `CALL_CENTER` = '"+callCenter+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			List<String> list = new ArrayList<String>();
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next())
				list.add(set.getString("AGENT"));
			return list.toArray(new String[list.size()]);
		} catch(SQLException e) {
			e.printStackTrace();
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
	public void LoadRecordInfo(Record record) {
		String sql = "SELECT * FROM `Checked` WHERE `address` = '"+record.getAddress()+"' AND `first_name` = '"+record.getFirstName()+"' AND `last_name` = '"+record.getLastName()+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next()) {
				record.setAddress(set.getString("address"));
				record.setCity(set.getString("city"));
				record.setState(set.getString("state"));
				record.setZip(set.getString("zip"));
				record.setSsn(set.getString("ssn"));
				record.setDob(set.getString("dob"));
				record.setGender(set.getString("gender"));
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
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
	public boolean CheckDrType(String code) {
		String sql = "SELECT * FROM `DOCTOR_TYPE` WHERE `CODE` = '"+code+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			return set.next();
		} catch(SQLException ex) {
			ex.printStackTrace();
			return true;
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
	public void update() {
		String sql = "SELECT * FROM `AGENTS`";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			DatabaseClient client = new DatabaseClient(false);
			while(set.next()) {
				String agent = set.getString("AGENT");
				String callCenter = set.getString("CALL_CENTER");
				int update = client.updateCallCenter(agent,callCenter);
				if(update<1)
					System.out.println(agent+" "+callCenter);
			}
		} catch(SQLException ex) {
			ex.getMessage();
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
	public String GetCallCenter(String agent) {
		String sql = "SELECT * FROM `AGENTS` WHERE `AGENT` = '"+agent+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next()) 
				return set.getString("CALL_CENTER");
			else
				return "NONE";
		} catch(SQLException ex) {
			ex.getMessage();
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
	public boolean isClosed() {
		try {
			return connect.isClosed();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}
	public int GetOffSet(String zipcode) {
		String sql = "SELECT `offset` FROM `ALL_ZIPCODE_INCOME` WHERE `ZIP_CODE` = '"+zipcode+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set =  stmt.executeQuery(sql);
			if(set.next())
				return set.getInt("offset");
			else 
				return 0;
		} catch(SQLException ex) {
			return ex.getErrorCode();
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
	public int AddFIP(String ZIP,String FIP) {
		String sql = "INSERT INTO `FIPS` (`ZIP`, `FIP`) VALUES ('"+ZIP+"', '"+FIP+"')";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} finally {
				try {
					if(stmt!=null)stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public String getDoctorType(String code) {
		String sql = "SELECT `TYPE` FROM `DOCTOR_TYPE` WHERE `CODE` = '"+code+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			String FIP = null;
			if(set.next())
				return set.getString("TYPE");
			else
				return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
				try {
					if(stmt!=null)stmt.close();
					if(set!=null)set.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public String getFIP(String zip) {
		String sql = "SELECT `FIP` FROM `FIPS` WHERE `ZIP` = '"+zip+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			String FIP = null;
			if(set.next())
				return set.getString("FIP");
			else
				return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
				try {
					if(stmt!=null)stmt.close();
					if(set!=null)set.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public ArrayList<String> getAllZips() {
		ArrayList<String> zipcodes = new ArrayList<String>();
		String sql = "SELECT `ZIP_CODE` FROM `ALL_ZIPCODE_INCOME` WHERE `state` = ''";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next()) {
				zipcodes.add(set.getString("ZIP_CODE"));
			}
			return zipcodes;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(stmt!=null)stmt.close();
				if(set!=null)set.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public int setState(String zip,String state) {
		String update = "UPDATE `ALL_ZIPCODE_INCOME` SET `state` = '"+state+"' WHERE `ZIP_CODE` = '"+zip+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(update);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} finally {
			try {
				if(stmt!=null)stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public int addToDNF(String fax) {
		String sql = "INSERT INTO `DNF` (`DONT_FAX`) VALUES ('"+fax+"')";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
				ex.printStackTrace();
				return -1;
		} finally {
			try {
				if(stmt!=null)stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public boolean IsInDNF(String fax) {
		String sql  = "SELECT * FROM `DNF` WHERE `DONT_FAX` = '"+fax+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			boolean result;
			if(set.next())
				return true;
			else
				return false;

		} catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				if(stmt!=null)stmt.close();
				if(set!=null)set.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	public String getIncome(String zip) {
		String sql = "SELECT * FROM `ALL_ZIPCODE_INCOME` WHERE `ZIP_CODE` = '"+zip+"'";
		try {
			Statement stmt = connect.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			String income = null;
			if(set.next())
				income = set.getString("45_64_INCOME");
			set.close();
			stmt.close();
			return income;
		} catch(SQLException ex) {
			ex.printStackTrace();
			return ex.getMessage();
		}
	}
	public int load(String zip,String state) {
		String insert = "INSERT INTO `SAMPLES_ZIP_CODES` (`ZIP_CODE`,`state`) VALUES ('"+zip+"','"+state+"')";
		try {
			Statement stmt = connect.createStatement();
			int value = stmt.executeUpdate(insert);
			stmt.close();
			return value;
		} catch(SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
	}
	public int AddToRequalifyCampaign(Record record,String marketingTeam) {
		String insert = " INSERT INTO `REQUALIFY` (`first_name`, `last_name`, `phone_number`,`medication_category`,`pharmacy`, `marketing_team`,`DATE_ADDED`)"
		        + " values (?, ?, ?,?, ?, ? , ?)";
		try {
			PreparedStatement preparedStmt = connect.prepareStatement(insert);
		    preparedStmt.setString(1, record.getFirstName());
		    preparedStmt.setString(2, record.getLastName());
		    preparedStmt.setString(3, record.getPhone());
		    preparedStmt.setString(4, record.getEmail());
		    preparedStmt.setString(5, record.getPharmacy());
		    preparedStmt.setString(6, marketingTeam);
		    preparedStmt.setString(7, getCurrentDate("yyyy-MM-dd"));
		    preparedStmt.execute();
		    return 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if(e.getErrorCode()==DatabaseErrorCodes.MYSQL_DUPLICATE_PK)
				return -1;
			else
				System.out.println(e.getMessage());
			return 0;
		}
	}
	public int DeleteCheckedRecord(String phone) {
		String sql = "DELETE FROM `Checked` WHERE `phonenumber` = '"+phone+"'";
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
	public int AddAuditedPatient(String first,String last,String phone,String date,String company) {
		String sql = "INSERT INTO `AUDIT` (`FIRST`,`LAST`,`PHONE`,`DATE_ADDED`,`COMPANY`) VALUES ('"+first+"','"+last+"','"+phone+"','"+getCurrentDate("yyyy-MM-dd")+"','"+company+"')";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			if(ex.getErrorCode()==1062)
				return 1062;
			else
				ex.printStackTrace();
			return ex.getErrorCode();
		} finally {
			try {
				if(stmt!=null)stmt.close();
			} catch(SQLException ex) {
				
			}
		}
	}
	public boolean CheckIfAudited(String phone) {
		String sql = "SELECT * FROM `AUDIT` WHERE `PHONE` = '"+phone+"'";
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
				if(stmt!=null)stmt.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	public boolean IsFilledAfter(String phone,String date) {
		String sql = "SELECT * FROM `AUDIT` WHERE `PHONE` = '"+phone+"' AND `DATE_ADDED` > '"+date+"'";
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
				if(stmt!=null)stmt.close();
				if(set!=null)set.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	public int UpdateFillDate(String phone,String date) {
		String sql = "UPDATE `AUDIT` SET `DATE_ADDED` = '"+date+"' WHERE `PHONE` = '"+phone+"'";
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
				ex.printStackTrace();
			}
		}
	}
	public void GetAuditPatients() {
		String sql = "SELECT * FROM `AUDIT`";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next()) {
				Record record = new Record();
				record.setFirstName(set.getString("FIRST"));
				record.setLastName(set.getString("LAST"));
				record.setPhone(set.getString("PHONE"));
				record.setType(set.getString("VENDOR"));
				CSVFrame.model.addRow(record);
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(stmt!=null)stmt.close();
				if(set!=null)set.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	public void GetInsuranceInfo(Record record) {
		String sql = "SELECT * FROM `Checked` WHERE `PHONE` = '"+record.getPhone()+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next()) {
				record.setStatus(set.getString("STATUS"));
				if(record.getStatus().equalsIgnoreCase("FOUND")) {
					record.setCarrier(set.getString("carrier"));
					record.setPolicyId(set.getString("policy_id"));
					record.setBin(set.getString("bin"));
					record.setGrp(set.getString("grp"));
					record.setPcn(set.getString("pcn"));
					record.setEmail(set.getString("additional_info"));
				}
			}
		} catch(SQLException ex) {
			ex.getMessage();
		} finally {
			try {
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public int SetInsurance(Record record) {
		String sql = "UPDATE `Checked` SET `carrier` = '"+record.getCarrier()+"', `policy_id` = '"+record.getPolicyId()+"', `bin` = '"+record.getBin()+"',`grp` = '"+record.getGrp()+"',"
				+ "`pcn` = '"+record.getPcn()+"',`additional_info` = '"+record.getEmail()+"' WHERE `PHONE` = '"+record.getPhone()+"'";
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
	public void GetLookUps(String afid) {
		String sql = "SELECT * FROM `Checked` WHERE `AFID` = '"+afid+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			Record record = null;
			while(set.next()) {
				record = new Record();
				record.setFirstName(set.getString("FIRST"));
				record.setLastName(set.getString("LAST"));
				record.setPhone(set.getString("PHONE"));
				record.setAddress(set.getString("ADDRESS"));
				record.setCity(set.getString("CITY"));
				record.setState(set.getString("STATE"));
				record.setZip(set.getString("ZIP"));
				record.setDob(set.getString("DOB"));
				record.setSsn(set.getString("SSN"));
				record.setStatus(set.getString("STATUS"));
				record.setCarrier(set.getString("carrier"));
				record.setPolicyId(set.getString("policy_id"));
				record.setBin(set.getString("bin"));
				record.setGrp(set.getString("grp"));
				record.setPcn(set.getString("pcn"));
				record.setEmail(set.getString("additional_info"));
				CSVFrame.model.addRow(record);
			}
	} catch(SQLException ex) {
		ex.getMessage();
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
	public int TEMPORARY(String zip,String city, int offset) {
		String sql = "UPDATE `ALL_ZIPCODE_INCOME` SET `city` = '"+city+"', `offset` = "+offset+" WHERE `ZIP_CODE` = '"+zip+"'";
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
	public boolean IsAudtied(String phone) {
		String sql = "SELECT * FROM `AUDIT` WHERE `PHONE` = '"+phone+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			Record record = null;
			if(set.next()) 
				return true;
			else
				return false;
	} catch(SQLException ex) {
		ex.getMessage();
		return false;
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
	private String getCurrentDate(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format); 
		Date date = new Date(); 
		return formatter.format(date);
	}
	public static String StringToDate(String dob) {
	      //Instantiating the SimpleDateFormat class
	      SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	      //Parsing the given String to Date object
	      Date date = null;
		try {
			date = formatter.parse(dob);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return formatter.format(date);
	}
	private class DatabaseErrorCodes {
		public static final int MYSQL_DUPLICATE_PK = 1062;
	}
}
