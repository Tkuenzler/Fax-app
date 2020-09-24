package Clients;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.json.JSONException;
import Info.RoadMap;
import PBM.InsuranceFilter;
import PBM.InsuranceType;
import objects.PharmacyMap;
import source.LoadInfo;
import table.Record;

public class RoadMapClient {
	String table,database,roadMap;
	public Connection connect = null;
	private final String HOST_NAME = "ltf5469.tam.us.siteprotect.com";
	public RoadMapClient() {
		try {
			//This will load the MySQL driver, each DB has its own driver
			 Class.forName("com.mysql.jdbc.Driver"); 
			 connect = DriverManager
				      .getConnection("jdbc:mysql://"+HOST_NAME+":3306/Road_Map", "tkuenzler","Tommy6847");
			 roadMap = LoadInfo.getRoadMap().getRoadMap();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		this.database = "Road_Map";
	}
	public RoadMapClient(String table) {
		try {
			//This will load the MySQL driver, each DB has its own driver
			 Class.forName("com.mysql.jdbc.Driver"); 
			 connect = DriverManager
				      .getConnection("jdbc:mysql://"+HOST_NAME+":3306/Road_Map", "tkuenzler","Tommy6847");
			 roadMap = LoadInfo.getRoadMap().getRoadMap();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		this.database = "Road_Map";
		this.table = table;
	}
	public void setTable(String pharmacy) {
		this.table = pharmacy;
	}
	public int createTable(String table) {
		String sql = "CREATE TABLE `"+table+"` LIKE `BLANK_ROADMAP`";
		String addRows = "INSERT INTO `"+table+"` SELECT * FROM `BLANK_ROADMAP`";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			int addTable  = stmt.executeUpdate(sql);
			int addStates = stmt.executeUpdate(addRows);
			return addStates;
		} catch(SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public boolean InvalidPBM(Record record) {
		String[] pbms = getPbms();
		for(String pbm: pbms) { 
			if(record.getCarrier().equalsIgnoreCase(pbm))
				return false;
		}
		return true;
	}
	public String getPharmacyQueryForDrChase() {
		String sql = "SELECT * FROM `"+roadMap+"` WHERE `FAX_CHASE` = 1";
		Statement stmt = null;
		ResultSet set = null;
		try {
			StringBuilder sb = new StringBuilder();
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			sb.append("(");
			while(set.next()) {
				String pharmacy = set.getString("PHARMACY");
				sb.append("`PHARMACY` = '"+pharmacy+"'");
				if(!set.isLast())
					sb.append(" OR ");
			} 
			sb.append(")");
			return sb.toString();
		} catch (SQLException e) {
			return "";
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();	
			} catch (SQLException e) {
				
			}
		}
	}
	public String getPharmaciesQuery() {
		Statement stmt = null;
		ResultSet set = null;
		try {
			RoadMap roadmap = LoadInfo.getRoadMap();
			String sql = "SELECT * FROM `"+roadmap.getRoadMap()+"`";
			StringBuilder sb = new StringBuilder();
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			sb.append("(");
			while(set.next()) {
				String pharmacy = set.getString("PHARMACY");
				sb.append("`PHARMACY` = '"+pharmacy+"'");
				if(!set.isLast())
					sb.append(" OR ");
			} 
			sb.append(")");
			return sb.toString();
		} catch (SQLException e) {
			return "";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();	
			} catch (SQLException e) {
				
			}
		}
		return null;
	}
	
	public String getNotPharmacyQuery() {
		Statement stmt = null;
		ResultSet set = null;
		try {
			RoadMap roadmap = LoadInfo.getRoadMap();
			String sql = "SELECT * FROM `"+roadmap.getRoadMap()+"`";
			StringBuilder sb = new StringBuilder();
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			sb.append("(");
			sb.append("`PHARMACY` <> 'NOT FOUND' AND `PHARMACY` <> 'Medicaid' AND `PHARMACY` <> 'No Home' AND ");
			while(set.next()) {
				String pharmacy = set.getString("PHARMACY");
				sb.append("`PHARMACY` <> '"+pharmacy+"'");
				if(!set.isLast())
					sb.append(" AND ");
			} 
			sb.append(")");
			System.out.println(sb.toString());
			return sb.toString();
		} catch (SQLException e) {
			return "";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();	
			} catch (SQLException e) {
				
			}
		}
		return null;
	}
	
	public String[] getPbms() {
		String sql = "SELECT * FROM `BLANK_ROADMAP` ORDER BY `State` ASC";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			List<String> list = new ArrayList<String>();
			ResultSetMetaData data = set.getMetaData();
			for(int i = 1;i<=data.getColumnCount();i++) {
				if(data.getColumnName(i).equalsIgnoreCase("State"))
					continue;
				else 
					list.add(data.getColumnName(i));
			}
			return list.toArray(new String[list.size()]);
		}catch(SQLException e) {
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
	public String[] get50States() {
		String sql = "SELECT * FROM `BLANK_ROADMAP` ORDER BY `State` ASC";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			List<String> list = new ArrayList<String>();
			while(set.next())
				list.add(set.getString("State"));
			return list.toArray(new String[list.size()]);
		}catch(SQLException e) {
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
	public String[] getPharmacies() {
		String sql = "SELECT * FROM `"+roadMap+"` WHERE `FAX_CHASE` = 1";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			List<String> list = new ArrayList<String>();
			list.add("All");
			while(set.next())
				list.add(set.getString("PHARMACY"));
			return list.toArray(new String[list.size()]);
		}catch(SQLException e) {
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
	public String[] getStates(String pharmacy) {
		String sql = "SELECT `State` FROM `"+pharmacy+"` WHERE (`Caremark`+`Express Scripts`+`Prime Therapeutics`+`Cigna`+`Aetna`+`Humana`+`OptumRx`+`Catamaran`+`Medimpact`+`Argus`+`Navitus`+`Anthem`+`Catalyst Rx`+`Envision RX`) > 0";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			List<String> list = new ArrayList<String>();
			while(set.next())
				list.add(set.getString("State"));
			return list.toArray(new String[list.size()]);
		}catch(SQLException e) {
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
	public boolean isValidPharmacy(String pharmacy) {
		String sql = "SELECT * FROM `"+roadMap+"` WHERE `PHARMACY` = '"+pharmacy+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next())
				return true;
			else
				return false;
		} catch(SQLException e) {
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
	public String getInsuranceTypeQuery(String pharmacy) {
		String sql = "SELECT * FROM `"+roadMap+"` WHERE `PHARMACY` = '"+pharmacy+"'";
		Statement stmt = null;
		ResultSet set = null;
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next()) {
				boolean commercial = set.getInt("COMMERCIAL_TIER")>=1;
				boolean medicare = set.getInt("MEDICARE_TIER")>=1;
				if(commercial) 
					builder.append("`TYPE` = 'Private Insurance' OR `TYPE` = 'Marketplace' OR `TYPE` = 'Provided by Job'");
				if(medicare) {
					if(commercial)
						builder.append(" OR ");
					builder.append("`TYPE` = 'Medicare' OR `TYPE` = ''");
				}
			}
			builder.append(")");
			return builder.toString();
	}catch(SQLException e) {
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
	
	public int updateRow(String state, String pbm, String accept) {
		int accepted = 0;
		if(accept.equalsIgnoreCase("NO"))
			accepted = 0;
		else 
			accepted = 1;
		String sql = "UPDATE `"+table+"` SET `"+pbm+"` = "+accepted+" WHERE `State` = '"+state+"'";
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(sql);
		} catch(SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public String getPharmacy() {
		String[] pharmacies = getPharmacies();
		String pharmacy = (String) JOptionPane.showInputDialog(new JFrame(), "Select a Pharmacy", "Pharmacies:", JOptionPane.QUESTION_MESSAGE, null, pharmacies, pharmacies[0]);
		return pharmacy;
	}
	public PharmacyMap getPharmacy(String pharmacy) {
		String sql = "SELECT * FROM `"+roadMap+"` WHERE `PHARMACY` = '"+pharmacy+"'";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next()) 
				return new PharmacyMap(set);
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
	public void LoadAllStates(PharmacyMap map) {
		String sql = "SELECT * FROM `"+map.getPharmacyName()+"`";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next()) {
				map.addState(new objects.RoadMap(map.getPharmacyName(),set));
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	public String GetRoadMap(String pharmacy) {
		String sql = "SELECT * FROM `"+pharmacy+"` ORDER BY `State` ASC";
		Statement stmt = null;
		ResultSet set = null;
		StringBuilder sb = new StringBuilder();
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			ResultSetMetaData data = set.getMetaData();
			for(int i=1;i<=data.getColumnCount();i++) {
				if(i==data.getColumnCount())
					sb.append(data.getColumnName(i));
				else
					sb.append(data.getColumnName(i)+",");
			}
			sb.append("\r\n");
			while(set.next()) {
				for(int i=1;i<=data.getColumnCount();i++) {
					if(i==data.getColumnCount())
						sb.append(set.getString(i));
					else
						sb.append(set.getString(i)+",");
				}
				sb.append("\r\n");
			}
			return sb.toString();
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
	public int isInRoadMap(String pharmacy,String pbm,String state) {
		String sql = "SELECT `"+pbm+"` FROM `"+pharmacy+"` WHERE `State` = '"+state+"'";
		int value;
		ResultSet set = null;
		Statement stmt  = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next()) 
				value = set.getInt(pbm);
			else  
				return -2;
			return value;
		} catch(SQLException ex) {
			ex.printStackTrace();
			return -1;
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
	public int isInRoadMap(Record record,String pharmacy) {
		String sql = "SELECT `"+InsuranceFilter.GetPBMFromBin(record)+"` FROM `"+pharmacy+"` WHERE `State` = '"+record.getState()+"'";
		int value;
		ResultSet set = null;
		Statement stmt  = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next()) 
				value = set.getInt(InsuranceFilter.GetPBMFromBin(record));
			else  
				return -2;
			return value;
		} catch(SQLException ex) {
			return -1;
		} finally {
			try {
				if(set!=null) set.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {

			}
		}
	}
	public int hasState(Record record,String pharmacy) {
		String[] pbms = getPbms();
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for(int i=0;i<pbms.length;i++) {
			if(i==pbms.length-1)
				sb.append("`"+pbms[i]+"`");
			else
				sb.append("`"+pbms[i]+"`+");
		}
		sb.append(")");
		String sql = "SELECT "+sb.toString()+" AS SUM FROM `"+pharmacy+"` WHERE `State` = '"+record.getState()+"'";
		System.out.println(sql);
		int value = 0;
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next()) {
				value = set.getInt("SUM");
			}
			return value;
		} catch(SQLException ex) {
			ex.printStackTrace();
			return -1;
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
	public String CheckPrivateTier(Record record,boolean pbm) {
		String sql = "SELECT * FROM `"+roadMap+"` WHERE `COMMERCIAL_TIER` > 0 ORDER BY `COMMERCIAL_TIER` ASC";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next()) {
				String pharmacy = set.getString("PHARMACY");
				if(pbm) {
					if(isInRoadMap(record,pharmacy)>0)
						return pharmacy;
					else 
						continue;
				}
				else {
					if(hasState(record,pharmacy)>0)
						return pharmacy;
					else
						continue;
				}
			}
			return null;
		} catch(SQLException ex) {
			ex.printStackTrace();
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
	public boolean CanTakeMedicare(String pharmacy) {
		String sql = "SELECT * FROM `"+roadMap+"` WHERE `PHARMACY` = '"+pharmacy+"' AND `MEDICARE_TIER` > 0";
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
	public String CheckMedicareTier(Record record,boolean pbm) {
		String sql = "SELECT * FROM `"+roadMap+"` WHERE `MEDICARE_TIER` > 0 ORDER BY `MEDICARE_TIER` ASC";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next()) {
				String pharmacy = set.getString("PHARMACY");
				if(pbm) {
					if(isInRoadMap(record,pharmacy)>0)
						return pharmacy;
					else 
						continue;
				}
				else {
					if(hasState(record,pharmacy)>0)
						return pharmacy;
					else
						continue;
				}
			}
			return null;
		} catch(SQLException ex) {
			ex.printStackTrace();
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
	public String CheckTelmedPrivate(Record record,boolean pbm) {
		String sql = "SELECT * FROM `"+roadMap+"` WHERE `COMMERCIAL_TELMED` > 0 ORDER BY `COMMERCIAL_TIER` ASC";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next()) {
				String pharmacy = set.getString("PHARMACY");
				if(pbm) {
					if(isInRoadMap(record,pharmacy)>0)
						return pharmacy;
					else 
						continue;
				}
				else {
					if(hasState(record,pharmacy)>0)
						return pharmacy;
					else
						continue;
				}
			}
		return null;
		} catch(SQLException ex) {
			ex.printStackTrace();
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
	public String CheckTelmedMedicare(Record record, boolean pbm) {
		String sql = "SELECT * FROM `"+roadMap+"` WHERE `MEDICARE_TELMED` > 0 ORDER BY `MEDICARE_TIER` ASC";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next()) {
				String pharmacy = set.getString("PHARMACY");
				if(pbm) {
					if(isInRoadMap(record,pharmacy)>0)
						return pharmacy;
					else 
						continue;
				}
				else {
					if(hasState(record,pharmacy)>0)
						return pharmacy;
					else
						continue;
			
				}
			}
			return null;
		} catch(SQLException ex) {
			ex.printStackTrace();
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
	public boolean CanTelmed(Record record,String type) {
		switch(type) {
			case "Medicare":
				if(CheckTelmedMedicare(record,false)!=null)
					return true;
				else
					return false;
			case "Private":
			case "Provided By Job":
			case "Market Place":
				if(CheckTelmedPrivate(record,false)!=null)
					return true;
				else
					return false;
			default: 
				return false;
		}
	}
	public String[] GetPharmacies(String pbm,String state,String insurance_type) {
		List<String> list = new ArrayList<String>();
		String type = null;
		switch(insurance_type) {
			case "Commercial":
				type = " WHERE `COMMERCIAL_TIER` >= 1";
			case "Medicare":
				type = " WHERE `MEDICARE_TIER` >= 1";
		}
		String sql = "SELECT * FROM `"+roadMap+"`"+type;
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			while(set.next()) {
				String pharmacy = set.getString("PHARMACY");
				String canTake = CanPharmacyTake(pharmacy,pbm,state);
				if(canTake!=null)
					list.add(canTake);
			} 
			return list.toArray(new String[list.size()]);
		} catch(SQLException ex) {
				ex.printStackTrace();
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
	private String CanPharmacyTake(String pharmacy,String pbm,String state) {
		String sql = "SELECT * FROM `"+pharmacy+"` WHERE `State` = '"+state+"' AND `"+pbm+"` >= 1";
		Statement stmt = null;
		ResultSet set = null;
		try {
			stmt = connect.createStatement();
			set = stmt.executeQuery(sql);
			if(set.next())
				return pharmacy;
			else
				return null;
		} catch(SQLException ex) {
				ex.printStackTrace();
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
	
	public boolean CanTakeNotFound(Record record,String pharmacy) {
		String sql = "SELECT * FROM `"+pharmacy+"` WHERE `State` = '"+record.getState()+"' AND `Not Found` >= 1";
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
				ex.printStackTrace();
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
	
	public boolean CanPharmacyTake(Record record,String pharmacy) {
		String sql = "SELECT * FROM `"+pharmacy+"` WHERE `State` = '"+record.getState()+"' AND `"+record.getCarrier()+"` >= 1";
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
				ex.printStackTrace();
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
	public void close() {
		try {
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
