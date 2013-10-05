package com.chabot.quickrant.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import com.chabot.quickrant.database.Database;
import com.chabot.quickrant.utils.DateUtils;

public class CookieService {
	
	private static Logger log = Logger.getLogger(CookieService.class);
	
	private static final String COOKIE_NAME = "quickrant-uid";
	private static final long COOKIE_AGE = 21600; // 6 hours (in seconds)
	private static ConcurrentMap<Long, String> cookies = new ConcurrentHashMap<Long, String>();
	
	public static Cookie createCookieAndPersistInfo() {
		Connection connection =  new Database().getConnection();
	    String insertSql = "insert into cookies (id, created, issued, cookie) values (nextval('cookies_id_seq'),?,?,?);";		    
	    PreparedStatement insertStatement = null;
	    
	    Cookie cookie = new Cookie(COOKIE_NAME, UUID.randomUUID().toString());
		cookie.setMaxAge((int)COOKIE_AGE);
		
		long issued = new Date().getTime();
		cookies.put(issued, cookie.getValue());	   
	    
		try {
			insertStatement = connection.prepareStatement(insertSql);
	    	insertStatement.setTimestamp(1, DateUtils.getCurrentTimeStamp());
	    	insertStatement.setLong(2, issued);
	    	insertStatement.setString(3, cookie.getValue());
	    	insertStatement.executeUpdate();	    	
	        if (insertStatement != null) insertStatement.close();
			if (connection != null) connection.close();	    	
		} catch (SQLException e) {
			log.error("Error persisting cookie", e);
		}		
		
		return cookie;
	}
	
	public static void fetchAndSetCookies() {
		Connection connection = new Database().getConnection();	  		
	    String selectSql = "select issued, cookie from cookies;";	    
	    PreparedStatement selectStatement = null;		    	    
	    ResultSet rs = null;
		try {
			selectStatement = connection.prepareStatement(selectSql);		
			rs = selectStatement.executeQuery();
			while (rs.next()) {
				long issued = rs.getLong(1);
				String cookie = rs.getString(2);
				cookies.put(issued, cookie);
			}	
			if (selectStatement != null) selectStatement.close();
			if (connection != null) connection.close();	
			
		} catch (SQLException e) {
			log.error("Error persisting cookie", e);
		}    
	}
	
	public static boolean findCookie(Cookie[] requestCookies) {
		if(requestCookies == null) {
			return false;
		} else {
			for(Cookie cookie : requestCookies) {
				if (cookies.containsValue((cookie.getValue()))) return true;
			}
		}
		return false;
	}
	
	public static int getCookiesSize() {
		return cookies.size();
	}
	
	// invoked by FlushCookiesJob
	public static void clean() {
		if(cookies != null) {
			for(Long temp : cookies.keySet()) {
				if (new Date().getTime() - temp > COOKIE_AGE*1000) {
					cookies.remove(temp);
				}
			}
		}
	}

}
