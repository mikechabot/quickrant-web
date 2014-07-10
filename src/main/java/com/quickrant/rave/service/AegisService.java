package com.quickrant.rave.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.quickrant.rave.database.Database;
import com.quickrant.rave.database.DatabaseUtils;

public class AegisService {
	
	private static Logger log = Logger.getLogger(AegisService.class);

	public static boolean isBanned(String ipAddress) {
	    Database database = null;
	    PreparedStatement statement = null;
	    ResultSet resultSet = null;
	    String sql = "select version()";
		try {
			database = new Database();
			database.open();
			statement = database.getPreparedStatement(sql);		
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				resultSet.getString(1);
			}
		} catch (SQLException e) {
			log.error("Error checking for ranter completeness", e);
		} finally {
			DatabaseUtils.close(resultSet);
			DatabaseUtils.close(statement);
			DatabaseUtils.close(database);
		}
		return false;
	}
	
}