package com.chabot.quickrant.service;

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

import com.chabot.quickrant.database.Database;
import com.chabot.quickrant.model.RantCookie;

public class CookieService {
	
	private static Logger log = Logger.getLogger(CookieService.class);
	
	public static final String COOKIE_NAME = "quickrant-uid";
	public static final long COOKIE_AGE = 21600; 	// 6 hours (in seconds)
	private static ConcurrentMap<Long, String> cookies = new ConcurrentHashMap<Long, String>();
	
	public static int getCookieCacheSize() {
		return cookies.size();
	}
	
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
	
	public static Cookie updateCookie(Cookie cookie) {
		String newCookieValue = cookie.getValue() + "-COMPLETE";		
		// Update cookie cache
		for(Map.Entry<Long, String> temp : cookies.entrySet()) {
			if(temp.getValue().equals(cookie.getValue())) {
				cookies.put(temp.getKey(), newCookieValue);
			}
		}		
		// Set response cookie
		cookie.setValue(newCookieValue);
		cookie.setMaxAge((int)CookieService.COOKIE_AGE);
		cookie.setPath("/");		
		return cookie;
	}
	
	public static RantCookie createCookie() {
	    RantCookie rantCookie = new RantCookie(COOKIE_NAME, UUID.randomUUID().toString());
	    rantCookie.setMaxAge((int)COOKIE_AGE);
	    rantCookie.setIssued(new Date().getTime());
		cookies.put(rantCookie.getIssued(), rantCookie.getValue());
		return rantCookie;
	}
	
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
	}
		
	public static void clean() {
		// Clean cookie cache
		if(cookies != null) {
			int start = cookies.size();
			for(Long temp : cookies.keySet()) {
				if (new Date().getTime() - temp > COOKIE_AGE*1000) { 
					cookies.remove(temp);
				}
			}
			int finish = cookies.size();
	 		log.info("Cleaned up " + (start-finish) + " cached cookies (" + finish + " active)");
		}
	}
}
