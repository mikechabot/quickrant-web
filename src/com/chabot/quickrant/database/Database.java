package com.chabot.quickrant.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.chabot.quickrant.Configuration;
import com.chabot.quickrant.Configuration.ConfigurationException;

public class Database {
	
	private static Logger log = Logger.getLogger(Database.class);
	
	public Connection getConnection() {
		
		Configuration config = Configuration.getInstance();
		try {
			config.initialize();
		} 
		catch (ConfigurationException e) {
			log.error("Could not load configuration for some reason", e);
		}

		String url = config.getRequiredString("postgresDbUrl");
		String user = config.getRequiredString("postgresDbUser");
		String pass = config.getRequiredString("postgresDbPass");		
		
		Connection connection = null;
		
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, user, pass);				
		} catch (ClassNotFoundException e) {
			log.error("Unable to locate Postgres driver", e);
		} catch (SQLException e) {
			log.error("Unable to connect to Postgres server", e);
		}
		return connection;
	}	
}