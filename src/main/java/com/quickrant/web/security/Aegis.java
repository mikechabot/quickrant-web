package com.quickrant.web.security;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.quickrant.api.Params;
import com.quickrant.api.database.Database;
import com.quickrant.api.database.DatabaseUtil;
import com.quickrant.api.models.Visitor;
import com.quickrant.web.cache.CookieCache;

public class Aegis {
	
	private static Logger log = Logger.getLogger(Aegis.class);
	
	private CookieCache cache;
	
	public Aegis() { }
	
	public Aegis(CookieCache cookieCache) {
		cache = cookieCache;
	}
	
	public void setCache(CookieCache cookieCache) {
		cache = cookieCache;
	}
		
	/**
	 * Shield against certain HTTP requests
	 * @param request
	 * @param response
	 * @return boolean
	 * @throws IOException
	 */
	public boolean protectFrom(Params params) throws IOException {		
		if (protectFromBannedIp(params)) return true;		
		if (params.isPost()) {
			if (protectFromNullCookie(params)) return true;
			if (protectFromInvalidCookie(params)) return true;
		}	
		return false;
	}
	
	/**
	 * Deny access if IP is banned
	 * @param params
	 * @return
	 */
	private boolean protectFromBannedIp(Params params) {
	    Database database = null;
	    PreparedStatement statement = null;
	    ResultSet resultSet = null;
	    String sql = "select version()";
		try {
			database = new Database();
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
		}
//		log.info("Banned IP (" + params.getIpAddress() + ") detected");
		return false;
	}

	/**
	 * Deny access if the POST didn't contain a valid cookie 
	 * @param params
	 * @return boolean
	 */
	private boolean protectFromInvalidCookie(Params params) {
		String cookieValue = params.getCookieValue(CookieCache.name);
		
		if (!cache.containsValue(cookieValue)) {
			log.info("IP address (" + params.getIpAddress()	+ ") attempted a POST without a valid cookie");
			return true;
		}
		return false;
	}

	/**
	 * Deny access if the POST didn't contain an issued cookie
	 * @param params
	 * @return boolean
	 */
	private boolean protectFromNullCookie(Params params) {
		String cookieValue = params.getCookieValue(CookieCache.name);
		if (cookieValue == null || cookieValue.isEmpty()) {
			log.info("IP address (" + params.getIpAddress()	+ ") attempted a POST without a null cookie");
			return true;
		}
		return false;
	}
	
	/**
	 * Deny access if the visitor object isn't complete
	 * @param params
	 * @return boolean
	 */
	public boolean protectFromIncompleteVisitor(Visitor visitor, Params params) {
		if (visitor == null) return true;
		if (!visitor.isComplete()) {
			log.info("IP address (" + params.getIpAddress()	+ ") attempted a POST without completing the AJAX roundtrip");
			return true;
		}
		return false;
	}
	
}