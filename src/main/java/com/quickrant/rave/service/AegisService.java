package com.quickrant.rave.service;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.quickrant.rave.Params;
import com.quickrant.rave.database.Database;
import com.quickrant.rave.database.DatabaseUtils;

public class AegisService {
	
	private static Logger log = Logger.getLogger(AegisService.class);

	/**
	 * Shield against assholes 
	 * @param request
	 * @param response
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean shieldAgainstRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Params params = new Params(request);
		
		/* Check for banned IP */
		if (isBanned(params)) {
			log.info("Banned IP (" + params.getIpAddress() + ") detected");
			return true;
		}

		/* If the POST didn't contain a cookie, deny access */
		if (params.isPost() && !isCookieInCache(params)) {
			log.info("IP address (" + params.getIpAddress()	+ ") attempted a POST without a valie cookie");
			return true;
		}
		
		return false;
	}
	
	private static boolean isCookieInCache(Params params) {
		return !OreoService.inCache(params.getCookies());
	}
	
	/**
	 * Check for IP ban 
	 * @param ipAddress
	 * @return
	 */
	public static boolean isBanned(Params params) {
		String ipAddress = params.getIpAddress();
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
			DatabaseUtils.close(resultSet);
			DatabaseUtils.close(statement);
			DatabaseUtils.close(database);
		}
		return false;
	}

	public static boolean checkForValidCookie(HttpServletRequest request, HttpServletResponse response) {

		return false;
	}
	
}