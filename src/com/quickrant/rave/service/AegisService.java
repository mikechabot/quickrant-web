package com.quickrant.rave.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.quickrant.rave.Params;
import com.quickrant.rave.database.Database;

public class AegisService {
	
	private static Logger log = Logger.getLogger(AegisService.class);

	public static boolean isBanned(String ipAddress) {    	
	    boolean isComplete = false;
		try {
			Connection connection = new Database().getConnection();
			String selectSql = "select complete from ranter where cookievalue = '" + params.getCookieValue(CookieService.COOKIE_NAME) + "'";
			PreparedStatement selectStatement = connection.prepareStatement(selectSql);		
			ResultSet rs = selectStatement.executeQuery();
			if (rs.next() && rs.getBoolean(1)) isComplete = true; 		
			if (selectStatement != null) selectStatement.close();
			if (connection != null) connection.close();
		} catch (SQLException e) {
			log.error("Error checking for ranter completeness", e);
		}
		return isComplete;
	}
	
}
