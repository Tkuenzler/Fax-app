package objects;

public class Database {
	String localHost,database,username,password,port;
	public Database() {
		
	}
	public String getLocalHost() {
		return localHost;
	}
	public void setLocalHost(String localHost) {
		if(localHost==null)
			this.localHost = "";
		else
			this.localHost = localHost;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		if(database==null)
			this.database = "";
		else
			this.database = database;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		if(username==null)
			this.username = "";
		else
			this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		if(password==null) 
			password = "";
		else
			this.password = password;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		if(port==null)
			this.port = "";
		else
			this.port = port;
	}
	
}
