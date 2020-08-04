package Clients;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONException;

import Info.DatabaseUser;
import source.LoadInfo;

public class ErrorSQLLog {
	
	public static int AddErrorMessage(int httpcode,String error,String endpoint, String location,String fax) {
		Connection connect = null;
		try {
			String company = LoadInfo.getAppName();
			Class.forName("com.mysql.jdbc.Driver"); 
			 //Connect to database
			connect = DriverManager
					.getConnection("jdbc:mysql://ltf5469.tam.us.siteprotect.com:3306/Info_Table", "tkuenzler","Tommy6847");
			String sql = "INSERT INTO `ERROR_LOG`(`HTTP_CODE`,`ERROR`,`END_POINT`, `COMPANY`, `LOCATION`,`FAX_NUMBER`) VALUES ('"+httpcode+"','"+error+"','"+endpoint+"','"+company+"','"+location+"','"+fax+"')";
			Statement stmt = connect.createStatement();
			int value = stmt.executeUpdate(sql);
			return value;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -99;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return e.getErrorCode();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -98;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -98;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -98;
		} finally {
			try {
				connect.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				return e.getErrorCode();
			} 
		}
	}
}