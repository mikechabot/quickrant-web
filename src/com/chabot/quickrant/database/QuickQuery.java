package com.chabot.quickrant.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class QuickQuery {
	
	private static Logger log = Logger.getLogger(QuickQuery.class);
	
	Connection connection = new Database().getConnection();	 
	Statement select = null;
	
	public ResultSet execute(String sql) {		
		ResultSet resultSet = null;	    			   
		try {				
			select = connection.createStatement();
			resultSet = select.executeQuery(sql);
		} catch (SQLException e) {
			log.error("Unable to perform query", e);
		}
		return resultSet;
	}
	
	public void close() throws SQLException {
		select.close();
		connection.close();
	}
}
