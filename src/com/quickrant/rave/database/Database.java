package com.quickrant.rave.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.quickrant.rave.Configuration;
import com.quickrant.rave.Configuration.ConfigurationException;

public class Database {
	
	private static Logger log = Logger.getLogger(Database.class);
	
	public Connection getConnection() {
		
		Configuration config = Configuration.getInstance();
		try {
			config.initialize();
		} 
		catch (ConfigurationException e) {
			log.error("Unable to load configuration", e);
		}

		String url = config.getRequiredString("postgres-url");
		String user = config.getRequiredString("postgres-user");
		String pass = config.getRequiredString("postgres-pass");		
		
		Connection connection = null;
		
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, user, pass);				
		} catch (ClassNotFoundException e) {
			log.error("Unable to locate Postgres driver", e);
		} catch (SQLException e) {
			log.error("Unable to connect to Postgres", e);
		}
		return connection;
	}
	
	public static String getVersion() {
		Connection connection = new Database().getConnection();	 
		Statement select = null;
		ResultSet resultSet = null;
		String version = null;
		
		try {				
			select = connection.createStatement();
			resultSet = select.executeQuery("select version();");
			 if (resultSet.next()) {
	             version =  resultSet.getString(1);
	         }
			select.close();
			connection.close();
		} catch (SQLException e) {
			log.error("Unable to perform query", e);
		}	
		
		return version;
	}
	
}