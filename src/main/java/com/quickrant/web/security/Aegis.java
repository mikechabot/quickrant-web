package com.quickrant.web.security;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.quickrant.api.Params;
import com.quickrant.api.database.Database;
import com.quickrant.api.database.DatabaseUtil;
import com.quickrant.web.cache.CookieCache;

public class Aegis {
	
	private static Logger log = Logger.getLogger(Aegis.class);
	
	private CookieCache cache;
	
	public void setCache(CookieCache cache) {
		this.cache = cache;
	}
	
	/**
	 * Shield against HTTP requests
	 * @param request
	 * @param response
	 * @return boolean
	 * @throws IOException
	 */
	public boolean protectFrom(Params params) throws IOException {	
		/* Check for banned IP */
		if (isBanned(params.getIpAddress())) {
			log.info("Banned IP (" + params.getIpAddress() + ") detected");
			return true;
		}

		/* If the POST didn't contain a cookie, deny access */
		String cookieValue = params.getCookieValue(CookieCache.name);
		if (params.isPost() && !cache.containsValue(cookieValue)) {
			log.info("IP address (" + params.getIpAddress()	+ ") attempted a POST without a valie cookie");
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check for IP ban 
	 * @param ipAddress
	 * @return
	 */
	public boolean isBanned(String ipAddress) {
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
			log.error("Unable to check for banned status", e);
		} finally {
			DatabaseUtil.close(resultSet);
			DatabaseUtil.close(statement);
			DatabaseUtil.close(database);
		}
		return false;
	}
	
}