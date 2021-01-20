package database;

import java.sql.*;

public class DBConnection {

	private static DBConnection db = new DBConnection();
	private Connection conn = null;
	
	private final String IPADDRESS = "localhost";
	private final String USERNAME = "root";
	private final String PASSWORD = "093410";
	private final String DBNAME = "parkingreservation";
	
	private final String JDBCDRIVER = "com.mysql.jdbc.Driver";
	private final String JDBCURL = "jdbc:mysql://" + IPADDRESS + "/" + DBNAME + "?useSSL=false";

	protected static DBConnection getInstance() {
		return db;
	}

	protected Connection getConnection() {
		
		try {
			Class.forName(JDBCDRIVER);
			conn = DriverManager.getConnection(JDBCURL, USERNAME, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	}

}