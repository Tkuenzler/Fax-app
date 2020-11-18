package Database;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONException;

import Database.Columns.TelmedColumns;
import Database.Query.Query;
import Info.DatabaseUser;
import source.LoadInfo;

public class Database {
	String database;
	String table;
	Connection connect = null;
	DatabaseUser db;
	public Database(String database) {
		this.database = database;
	}
	public Database() {
		
		
	}
	public String getDatabase() {
		return database;
	}
	public boolean loginAndChooseDatabase() throws SQLException  {
		ResultSet set = null;
		try {
			db = LoadInfo.LoadDatabaseUser();
			if(db.isBlank()) {
				JOptionPane.showMessageDialog(null, "No Database info entered");
				return false;
			}
			//This will load the MySQL driver, each DB has its own driver
			 Class.forName("com.mysql.jdbc.Driver"); 
			 //Connect to database
			connect = DriverManager
					      .getConnection("jdbc:mysql://"+db.getLocalHost()+":"+db.getPort()+"/", db.getUser(),db.getPassword());
			if(database==null) {
				set = show("DATABASES");
				List<String> list = new ArrayList<String>();
				String[] databases = null;
				while(set.next()) {
					String database = set.getString("Database");
					if(database.equalsIgnoreCase("information_schema"))
						continue;
					else 
						list.add(database);
				}
				databases = list.toArray(new String[list.size()]);
				this.database = (String) JOptionPane.showInputDialog(new JFrame(), "Select a database", "Database:", JOptionPane.QUESTION_MESSAGE, null, databases, databases[0]);
				close();
				reconnect();
			}
			return true;
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
		} finally {
			if(set!=null)set.close();
		}
		return false;
	}
	public String getTable() {
		return table;
	}
	public void setTable() throws SQLException {
		String[] tables = getTables();
		this.table = (String) JOptionPane.showInputDialog(new JFrame(), "Select a table", "tables:", JOptionPane.QUESTION_MESSAGE, null, tables, tables[0]);
	}
 	public boolean login() throws SQLException  {
		try {
			db = LoadInfo.LoadDatabaseUser();
			if(db.isBlank()) {
				JOptionPane.showMessageDialog(null, "No Database info entered");
				return false;
			}
			//This will load the MySQL driver, each DB has its own driver
			 Class.forName("com.mysql.jdbc.Driver"); 
			 //Connect to database
			connect = DriverManager
					      .getConnection("jdbc:mysql://"+db.getLocalHost()+":"+db.getPort()+"/"+database, db.getUser(),db.getPassword());
			
			return true;
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
		return false;
	}
	public void reconnect() {
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
	public void close() {
		try {
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ResultSet show(String show) throws SQLException {
		Query query = new Query();
		query.show(show);
		Statement stmt = connect.createStatement();
		return stmt.executeQuery(query.getQuery());
	}
	public ResultSet showIn(String show,String in) throws SQLException {
		Query query = new Query();
		query.show(show).in(in);
		Statement stmt = connect.createStatement();
		return stmt.executeQuery(query.getQuery());
	}
	public int insert(String table,String[] columns,Object[] params) throws SQLException {
		Query query = new Query();
		query.insert(table).columns(columns).values(params);
		PreparedStatement stmt = connect.prepareStatement(query.getQuery());
		if(params != null){
			int index = 1;
	        for(Object param : params){
	        	stmt.setObject(index, param);
	            index++;
	         }
	    }
		int value = stmt.executeUpdate();
		stmt.close();
		return value;
	}
	public int update(String table,String[] columns,Object[] params,String requirement) throws SQLException {
		Query query = new Query();
		query.update(table).set(columns).where(requirement);
		PreparedStatement stmt = connect.prepareStatement(query.getQuery());
		if(params != null){
			int index = 1;
	        for(Object param : params){
	        	stmt.setObject(index, param);
	            index++;
	         }
	    }
		return stmt.executeUpdate();
	}
	public String test(String table,String[] columns,String[] params,String requirement)  {
		Query query = new Query();
		query.update(table).set(columns).values(params).where(requirement);
		return query.getQuery();
	}
	public boolean exists(String table, Object[] columns, String requirement, Object[] params) throws SQLException {
		PreparedStatement stmt = null;
		Query query = new Query();
		query.select(columns).from(table).where(requirement);
		stmt = connect.prepareStatement(query.getQuery());
		if(params != null){
			int index = 1;
	        for(Object param : params){
	        	stmt.setObject(index, param);
	            index++;
	         }
	    }
		ResultSet set = stmt.executeQuery();
		if(set.next())
			return true;
		else
			return false;
	}
	public ResultSet select(String table, Object[] columns, String requirement, Object[] params) throws SQLException {
		PreparedStatement stmt = null;
		Query query = new Query();
		query.select(columns).from(table).where(requirement);
		stmt = connect.prepareStatement(query.getQuery());
		if(params != null){
			int index = 1;
	        for(Object param : params){
	        	stmt.setObject(index, param);
	            index++;
	         }
	    }
		return stmt.executeQuery();
	}
	public ResultSet selectAs(String table, Object[] columns,String columnName, String requirement, Object[] params) throws SQLException {
		PreparedStatement stmt = null;
		Query query = new Query();
		query.select(columns).as(columnName).from(table).where(requirement);
		stmt = connect.prepareStatement(query.getQuery());
		if(params != null){
			int index = 1;
	        for(Object param : params){
	        	stmt.setObject(index, param);
	            index++;
	         }
	    }
		return stmt.executeQuery();
	}
	public ResultSet selectSort(String table, Object[] columns, String requirement, Object[] params,String[] order_columns,String[] order_by) throws SQLException {
		PreparedStatement stmt = null;
		Query query = new Query();
		query.select(columns).from(table).where(requirement).orderby(order_columns, order_by);
		stmt = connect.prepareStatement(query.getQuery());
		if(params != null){
			int index = 1;
	        for(Object param : params){
	        	stmt.setObject(index, param);
	            index++;
	         }
	    }
		return stmt.executeQuery();
	}
	public int delete(String table,String requirement) throws SQLException {
		Query query = new Query();
		query.delete(table).where(requirement);
		Statement stmt = connect.createStatement();
		return stmt.executeUpdate(query.getQuery());
	}
	public String[] getTables() throws SQLException {
		List<String> list = new ArrayList<String>();
		Query query = new Query();
		query.show("TABLES").in(database);
		Statement stmt = connect.createStatement();
		ResultSet set = stmt.executeQuery(query.getQuery());
		while(set.next()) {
			if(set.getString("Tables_in_"+database).equalsIgnoreCase("Alternate_Scripts") ||
					set.getString("Tables_in_"+database).equalsIgnoreCase("FAXED") ||
					set.getString("Tables_in_"+database).equalsIgnoreCase("Recordings") || 
					set.getString("Tables_in_"+database).equalsIgnoreCase("NOT_FOUND"))
				continue;
			else
				list.add(set.getString("Tables_in_"+database));
		}
		return list.toArray(new String[list.size()]);
	}
	
}
