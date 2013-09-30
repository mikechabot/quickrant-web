package com.chabot.quickrant.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.Cookie;

import com.chabot.quickrant.database.Database;

public class RequestService {
	
	private static final String COOKIE_NAME = "quickrant-uid";
	private static CopyOnWriteArrayList<String> cookies = new CopyOnWriteArrayList<String>();
	
	public static void saveCookiesToDatabase() throws SQLException {		
		Connection connection =  new Database().getConnection();
		String dropSql = "drop table if exists cookies;";
		String createSql = "create table cookies (id serial unique, created timestamp (60), cookie varchar(50));";
	    String insertSql = "insert into cookies (id, created, cookie) values (nextval('cookies_id_seq'),?,?);";		    
	    
	    PreparedStatement dropStatement = connection.prepareStatement(dropSql);
	    PreparedStatement createStatement = connection.prepareStatement(createSql);
	    PreparedStatement insertStatement = connection.prepareStatement(insertSql);
	    
	    dropStatement.executeUpdate();
	    createStatement.executeUpdate();
	    
	    for(String cookie : cookies) {
	    	insertStatement.setTimestamp(1, getCurrentTimeStamp());
	    	insertStatement.setString(2, cookie);
	    	insertStatement.executeUpdate();
	    }
	  
	    if (dropStatement != null) dropStatement.close();
	    if (createStatement != null) createStatement.close();
	    if (insertStatement != null) insertStatement.close();
		if (connection != null) connection.close();
	}
	
	public static void getCookiesFromDatabase() throws SQLException {
		Connection connection = new Database().getConnection();	  		
	    String selectSql = "select cookie from cookies;";	    
	    PreparedStatement selectStatement = connection.prepareStatement(selectSql);		    	    
	    ResultSet rs = selectStatement.executeQuery();    
	 
		while (rs.next()) {
			String cookie = rs.getString(1);
			cookies.add(cookie);
		}	
		
		if (selectStatement != null) selectStatement.close();
		if (connection != null) connection.close();	
	
	}
	
	public static Cookie getNewCookie() {
		Cookie cookie = new Cookie(COOKIE_NAME, UUID.randomUUID().toString());
		cookie.setMaxAge(86400);
		cookies.add(cookie.getValue());
		return cookie;
	}
	
	public static boolean findCookie(Cookie[] requestCookies) {
		for(Cookie cookie : requestCookies) {
			if (cookies.contains(cookie.getValue())) {
				return true;
			} 
		}
		return false;
	}

	public static CopyOnWriteArrayList<String> getCookies() {
		return cookies;
	}
	
	private static Timestamp getCurrentTimeStamp() {
		return new Timestamp(new Date().getTime()); 
	}
	
}
