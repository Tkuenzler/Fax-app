package Client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

public class DatabaseClient {
	private Connection connect = null;
	String table;

	public DatabaseClient(String table) {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Connect to database
			connect = DriverManager.getConnection("jdbc:mysql://ltf5469.tam.us.siteprotect.com:3306/contacts101",
					"tkuenzler", "Tommy6847");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: " + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: " + e.getMessage());
			e.printStackTrace();
		}
		this.table = table;
	}

	public DatabaseClient(boolean b) {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Connect to database
			connect = DriverManager.getConnection("jdbc:mysql://ltf5469.tam.us.siteprotect.com:3306/MT_MARKETING",
					"tkuenzler", "Tommy6847");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: " + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: " + e.getMessage());
			e.printStackTrace();
		}
		this.table = "TELMED";
	}

	public void reconnect() {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Connect to database
			connect = DriverManager.getConnection("jdbc:mysql://ltf5469.tam.us.siteprotect.com:3306/contacts101",
					"tkuenzler", "Tommy6847");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: " + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String addRecord(Record record) {
		PreparedStatement stmt = null;
		try {
			stmt = connect.prepareStatement(buildAddStatement());
			for (int i = 0; i < Columns.HEADERS.length; i++) {
				String column = Columns.HEADERS[i];
				switch (column) {
				case Columns.FIRST_NAME:
					stmt.setString(i, record.getFirstName());
					break;
				case Columns.LAST_NAME:
					stmt.setString(i, record.getLastName());
					break;
				case Columns.DOB:
					stmt.setString(i, record.getDob());
					break;
				case Columns.ID:
					stmt.setString(i, record.getId());
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
				case Columns.GENDER:
					stmt.setString(i, record.getGender());
					break;
				case Columns.CARRIER:
					stmt.setString(i, record.getCarrier());
					break;
				case Columns.VENDOR_ID:
					stmt.setString(i, record.getVendorId() + " " + record.getSubId());
					break;
				case Columns.EMAIL:
					stmt.setString(i, record.getEmail());
					break;
				case Columns.IP:
					stmt.setString(i, record.getIp());
					break;
				case Columns.DATE_ADDED:
					stmt.setTimestamp(i, new Timestamp(new Date().getTime()));
					break;
				}
			}
			int insert = stmt.executeUpdate();
			if (insert == 1)
				return "Successful";
			else
				return "ERROR OCCURED";
		} catch (SQLException ex) {
			if (ex.getErrorCode() == ErrorCodes.MYSQL_DUPLICATE_PK) {
				System.out.println("DUPLICATE RECORD " + record.getFirstName() + record.getLastName());
				return "Duplicate Record";
			}
			System.out.println(ex.getMessage());
			return ex.getMessage();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String buildAddStatement() {
		StringBuilder base = new StringBuilder("INSERT into " + table + " (");
		for (String s : Columns.HEADERS) {
			if (s.equalsIgnoreCase(Columns.ALL))
				continue;
			else if (s.equalsIgnoreCase(Columns.DATE_ADDED))
				base.append(s + ")");
			else
				base.append(s + ",");

		}
		base.append(" values(");
		for (int i = 0; i < Columns.HEADERS.length - 1; i++) {
			if (i == Columns.HEADERS.length - 2)
				base.append("?)");
			else
				base.append("?,");
		}
		return base.toString();
	}

	public boolean IsInDatabase(Record record) {
		String sql = "SELECT * FROM `" + table + "` WHERE `" + Columns.PHONE_NUMBER + "` = '" + record.getPhone() + "'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if (set.next())
				return true;
			else
				return false;
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			return false;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (set != null)
					set.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean IsInDatabase(String phone) {
		String sql = "SELECT * FROM `" + table + "` WHERE `" + Columns.PHONE_NUMBER + "` = '" + phone + "'";
		Statement stmt = null;
		ResultSet set = null;
		try {

			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if (set.next())
				return true;
			else
				return false;
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			return false;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (set != null)
					set.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String CheckDuplicateResponse(String phone) {
		String sql = "SELECT * FROM `TELMED` WHERE `phonenumber` = '" + phone + "'";
		String response = null;
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if (set.next()) {
				String name = set.getString("first_name") + " " + set.getString("last_name");
				String recordId = set.getString("RECORD_ID");
				String agent = set.getString("agent");
				String date = set.getString("DATE_ADDED");
				StringBuilder sb = new StringBuilder();
				sb.append("<html>");
				sb.append("<body>");
				sb.append("<h1>DUPLICATE ENTRY</h1>");
				sb.append("<h2>Patient Name: " + name + "</h2>");
				sb.append("<h2>Record ID: " + recordId + "</h2>");
				sb.append("<h2>Submitted By: " + agent + "</h2>");
				sb.append("<h2>Date Added: " + date + "</h3>");
				sb.append("</body>");
				sb.append("</html>");
				response = sb.toString();
			} else
				response = "";
			return response;
		} catch (SQLException ex) {
			return null;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (set != null)
					set.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public int AddToTelmed(Record record, String recordId) {
		String sql = "INSERT INTO `TELMED` (`first_name`,`last_name`,`phonenumber`,`RECORD_ID`,`agent`) " + "VALUES ('"
				+ record.getFirstName() + "','" + record.getLastName() + "','" + record.getPhone() + "','" + recordId
				+ "','" + record.getAgent() + "')";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			if (ex.getErrorCode() == ErrorCodes.MYSQL_DUPLICATE_PK)
				return -1;
			else
				return 0;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String addRecordToTelmed(Record record, String pain_level, String pain_location, String pain_type) {
		PreparedStatement stmt = null;
		try {
			stmt = connect.prepareStatement(buildAddStatementForTelmed());
			for (int i = 0; i < Columns.TELMED_HEADERS.length; i++) {
				String column = Columns.TELMED_HEADERS[i];
				switch (column) {
				case Columns.FIRST_NAME:
					stmt.setString(i + 1, record.getFirstName());
					break;
				case Columns.LAST_NAME:
					stmt.setString(i + 1, record.getLastName());
					break;
				case Columns.DOB:
					stmt.setString(i + 1, record.getDob());
					break;
				case Columns.PHONE_NUMBER:
					stmt.setString(i + 1, record.getPhone());
					break;
				case Columns.ADDRESS:
					stmt.setString(i + 1, record.getAddress());
					break;
				case Columns.CITY:
					stmt.setString(i + 1, record.getCity());
					break;
				case Columns.STATE:
					stmt.setString(i + 1, record.getState());
					break;
				case Columns.ZIPCODE:
					stmt.setString(i + 1, record.getZip());
					break;
				case Columns.GENDER:
					stmt.setString(i + 1, record.getGender());
					break;
				case Columns.EMAIL:
					stmt.setString(i + 1, record.getEmail());
					break;
				case Columns.IP:
					stmt.setString(i + 1, record.getIp());
					break;
				case Columns.SUB_ID:
					stmt.setString(i + 1, record.getSubId());
					break;
				case Columns.DATE_ADDED:
					stmt.setTimestamp(i + 1, new Timestamp(new Date().getTime()));
					break;
				case Columns.PAIN_LEVEL:
					stmt.setString(i + 1, pain_level);
					break;
				case Columns.PAIN_LOCATION:
					stmt.setString(i + 1, pain_location);
					break;
				case Columns.PAIN_TYPE:
					stmt.setString(i + 1, pain_type);
					break;
				}
			}
			int insert = stmt.executeUpdate();
			if (insert == 1)
				return "Successful";
			else
				return "ERROR OCCURED";
		} catch (SQLException ex) {
			if (ex.getErrorCode() == ErrorCodes.MYSQL_DUPLICATE_PK) {
				System.out.println("DUPLICATE RECORD " + record.getFirstName() + record.getLastName());
				return "Duplicate Record";
			}
			System.out.println(ex.getMessage());
			return ex.getMessage();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String buildAddStatementForTelmed() {
		StringBuilder base = new StringBuilder("INSERT into `" + table + "` (");
		for (String s : Columns.TELMED_HEADERS) {
			if (s.equalsIgnoreCase(Columns.PAIN_LEVEL))
				base.append(s + ")");
			else
				base.append(s + ",");

		}
		base.append(" values(");
		for (int i = 0; i < Columns.TELMED_HEADERS.length; i++) {
			if (i == Columns.TELMED_HEADERS.length - 1)
				base.append("?)");
			else
				base.append("?,");
		}
		return base.toString();
	}

	public static class Columns {
		public static final String ALL = "ALL";
		public static final String FIRST_NAME = "first_name";
		public static final String LAST_NAME = "last_name";
		public static final String DOB = "dob";
		public static final String PHONE_NUMBER = "phonenumber";
		public static final String ID = "_id";
		public static final String ADDRESS = "address";
		public static final String CITY = "city";
		public static final String STATE = "state";
		public static final String ZIPCODE = "zip";
		public static final String GENDER = "gender";
		public static final String CARRIER = "carrier";
		public static final String VENDOR_ID = "VENDOR_ID";
		public static final String EMAIL = "email";
		public static final String IP = "ip";
		public static final String SUB_ID = "subid";
		public static final String CLICK_ID = "clickId";
		public static final String DATE_ADDED = "DATE_ADDED";
		public static final String PAIN_TYPE = "pain_type";
		public static final String PAIN_LOCATION = "pain_location";
		public static final String PAIN_LEVEL = "pain_level";

		public static final String[] HEADERS = { ALL, FIRST_NAME, LAST_NAME, DOB, PHONE_NUMBER, ID, ADDRESS, CITY,
				STATE, ZIPCODE, GENDER, CARRIER, VENDOR_ID, EMAIL, IP, DATE_ADDED };
		public static final String[] TELMED_HEADERS = { FIRST_NAME, LAST_NAME, DOB, PHONE_NUMBER, ADDRESS, CITY, STATE,
				ZIPCODE, GENDER, EMAIL, IP, SUB_ID, DATE_ADDED, PAIN_TYPE, PAIN_LOCATION, PAIN_LEVEL };
	}

	private class ErrorCodes {
		public static final int MYSQL_DUPLICATE_PK = 1062;
	}
}
