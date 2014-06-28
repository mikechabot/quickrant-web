package com.quickrant.rave.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import com.quickrant.rave.Configuration;
import com.quickrant.rave.Configuration.ConfigurationException;
import com.quickrant.rave.database.Database;
import com.quickrant.rave.model.RantCookie;

public class CookieService {
	
	private static final String COOKIE_NAME = "quickrant-uid";
	private static int COOKIE_AGE;

	private static Logger log = Logger.getLogger(CookieService.class);
	
	private static ConcurrentMap<Long, String> cookies = new ConcurrentHashMap<Long, String>();
	
	public static void initialize() {
		Configuration config = Configuration.getInstance();
		try {
			config.initialize("WEB-INF/etc");
		} catch (ConfigurationException e) {
			log.error("Unable to intialize configuration", e);
		}
		COOKIE_AGE = config.getRequiredInt("cookie-age");
		log.info("Cookie age: " + COOKIE_AGE + " minutes (or " + (COOKIE_AGE / 60) + " hours)");
		
		populateCookieCache();
	}
	
	public static String getCookieName() {
		return COOKIE_NAME;
	}
	
	public static long getCookieAge() {
		return COOKIE_AGE;
	}
	
	public static int getCacheSize() {
		return cookies.size();
	}
	
	/**
	 * Determine if a cookie exists in the cache
	 * @param requestCookies
	 * @return
	 */
	public static boolean cookieExists(Cookie[] requestCookies) {
		if(requestCookies == null) {
			return false;
		} else {
			for(Cookie cookie : requestCookies) {
				if (cookies.containsValue((cookie.getValue()))) return true;
			}
		}
		return false;
	}
	
	/**
	 * Create an HTTP cookie
	 * @return RantCookie
	 */
	public static RantCookie createCookie() {
	    RantCookie rantCookie = new RantCookie(COOKIE_NAME, UUID.randomUUID().toString());
	    rantCookie.setMaxAge((int)COOKIE_AGE*60);
	    rantCookie.setIssued(new Date().getTime());
		cookies.put(rantCookie.getIssued(), rantCookie.getValue());
		return rantCookie;
	}
	
	/**
	 * Update a cookie with "*" to signify the AJAX response 
	 * was received, then update the local cookies cache
	 * @param cookie
	 * @return Cookie
	 */
	public static Cookie updateCookie(Cookie cookie) {
		String newCookieValue = cookie.getValue() + "*";
		for(Map.Entry<Long, String> temp : cookies.entrySet()) {
			if(temp.getValue().equals(cookie.getValue())) {
				cookies.put(temp.getKey(), newCookieValue);
			}
		}
		cookie.setValue(newCookieValue);
		cookie.setMaxAge((int)CookieService.COOKIE_AGE*60);
		cookie.setPath("/");		
		return cookie;
	}
	
	/**
	 * Populate the local cache from the backend. This ensures active 
	 * users that were issued a cookie prior to a web server restart  
	 * are able to post without being issued another cookie, as posting 
	 * with an invalid cookie is not allowed
	 */
	public static void populateCookieCache() {
		Connection connection = new Database().getConnection();	  		
	    String selectSql = "select cookieissued, cookievalue from ranter where cookieactive = true;";	    
	    PreparedStatement selectStatement = null;		    	    
	    ResultSet rs = null;
		try {
			selectStatement = connection.prepareStatement(selectSql);		
			rs = selectStatement.executeQuery();
			while (rs.next()) {
				cookies.put(rs.getLong(1),  rs.getString(2));
			}	
			if (selectStatement != null) selectStatement.close();
			if (connection != null) connection.close();	
			
		} catch (SQLException e) {
			log.error("Error fetching cookies", e);
		}
		log.info("Fetched " + getCacheSize() + " cookies");
	}
	
	/*
	 * Purge old cookies from the cache
	 */
	public static void clean() {
		if(cookies != null) {
			int start = cookies.size();
			for(Long temp : cookies.keySet()) {
				if (new Date().getTime() - temp > COOKIE_AGE*60*1000) { 
					cookies.remove(temp);
				}
			}
			int finish = cookies.size();
	 		log.info("Cleaned up " + (start-finish) + " cached cookies (" + finish + " active)");
		}
	}
}
