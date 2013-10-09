package com.chabot.quickrant.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import com.chabot.quickrant.Params;
import com.chabot.quickrant.database.Database;
import com.chabot.quickrant.model.RantCookie;
import com.chabot.quickrant.model.Ranter;
import com.chabot.quickrant.utils.DateUtils;

public class RanterService {

	private static Logger log = Logger.getLogger(RanterService.class);
	
	public static boolean isComplete(Params params) {    	
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
	
	public static void createRanter(Cookie cookie) {
		RantCookie rantCookie = (RantCookie) cookie;
		Connection connection =  new Database().getConnection();
	    String insertSql = "insert into ranter (id, created, cookievalue, cookieissued, cookieactive, complete) values (nextval('ranter_id_seq'),?,?,?,?,?);";		    
	    PreparedStatement insertStatement = null;	    
		try {
			insertStatement = connection.prepareStatement(insertSql);
	    	insertStatement.setTimestamp(1, DateUtils.getCurrentTimeStamp());
	    	insertStatement.setString(2, rantCookie.getValue());
	    	insertStatement.setLong(3, rantCookie.getIssued());
	    	insertStatement.setBoolean(4, true);
	    	insertStatement.setBoolean(5, false);
	    	insertStatement.executeUpdate();	    	
	        if (insertStatement != null) insertStatement.close();
			if (connection != null) connection.close();	    	
		} catch (SQLException e) {
			log.error("Error persisting ranter", e);
		}		
	}
	
	public static void updateRanter(Ranter ranter) {
		Connection connection =  new Database().getConnection();
		String updateSql = "update ranter set cookievalue = ?, ipaddress = ?, useragent = ?, screenheight = ?, screenwidth = ?, screencolor = ?, key = ?, complete = ? where cookievalue = '" + ranter.getCookieValue() + "'";	
		PreparedStatement updateStatement = null;	        
		try {
			updateStatement = connection.prepareStatement(updateSql);
			updateStatement.setString(1, ranter.getCookieValue() + "-COMPLETE");
			updateStatement.setString(2, ranter.getIpAddress());
			updateStatement.setString(3, ranter.getUserAgent());
			updateStatement.setInt(4, ranter.getScreenHeight());
	    	updateStatement.setInt(5, ranter.getScreenWidth());
	    	updateStatement.setInt(6, ranter.getScreenColor());
	    	updateStatement.setString(7, ranter.getKey());
	    	updateStatement.setBoolean(8, true);
	    	updateStatement.executeUpdate();  
	        if (updateStatement != null) updateStatement.close();
			if (connection != null) connection.close();	    	
		} catch (SQLException e) {
			log.error("Error updating ranter", e);
		}		
	}
	
	public static void clean() {
		// Deactivate persisted cookies
		Connection connection =  new Database().getConnection();
	    String updateSql = "update ranter set cookieactive = false where cookieactive = true and (to_number(replace(to_char(extract(epoch from now()),'9999999999D999'),'.',''),'9999999999999') - cookieissued) > " + CookieService.COOKIE_AGE*60*1000;		    
	    PreparedStatement updateStatement = null;	    
		try {
			updateStatement = connection.prepareStatement(updateSql);
			int rows = updateStatement.executeUpdate();	    	
	        if (updateStatement != null) updateStatement.close();
			if (connection != null) connection.close();	    	
			log.info("Cleaned up " + rows + " persisted cookies");
		} catch (SQLException e) {
			log.error("Error cleaning persisted cookie", e);
		}
	}
	
}