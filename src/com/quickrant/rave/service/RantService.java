package com.quickrant.rave.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.quickrant.rave.Configuration;
import com.quickrant.rave.Params;
import com.quickrant.rave.Configuration.ConfigurationException;
import com.quickrant.rave.database.Database;
import com.quickrant.rave.model.Rant;
import com.quickrant.rave.utils.DateUtils;

public class RantService {
	
	private static Logger log = Logger.getLogger(RantService.class);
	
	/**
	 * Create a new rant
	 * @param rant
	 * @param params
	 * @throws SQLException
	 */
	public static void createRant(Rant rant, Params params) throws SQLException {				
		
    	Configuration config = Configuration.getInstance();
		try {
			config.initialize();
		} catch (ConfigurationException e) {
			log.error("Unable to load configuration", e);
		}
		
		Connection connection =  new Database().getConnection();
	    String selectSql = "select id, rantattempts, lastrant from ranter where cookievalue = '" + params.getCookieValue(CookieService.getCookieName()) + "';";
		String insertSql = "insert into rants (id, created, emotion, question, rant, ranter, location, ranter_id) values (nextval('rants_id_seq'),?,?,?,?,?,?,?);";		    
		String updateSql = "update ranter set lastrant = " + new Date().getTime() +" where id = %id%";
		PreparedStatement selectStatement = connection.prepareStatement(selectSql);
		PreparedStatement insertStatement = connection.prepareStatement(insertSql);
		PreparedStatement updateStatement = null;
	    
	    if(rant.getRanter() == null || rant.getRanter().trim().length() == 0) {
	    	 rant.setRanter("Anonymous");
	    }
	    if(rant.getLocation() == null || rant.getLocation().trim().length() == 0) {
	    	 rant.setLocation("Earth");
	    }		    
	    
	    int id = 0;	    
	    ResultSet rs = selectStatement.executeQuery();
	    if (rs.next()) id = rs.getInt(1);	    	
	    	    
	    insertStatement.setTimestamp(1, DateUtils.getCurrentTimeStamp());
	    insertStatement.setString(2,  rant.getEmotion());
	    insertStatement.setString(3,  rant.getQuestion());
	    insertStatement.setString(4,  rant.getRant());
	    insertStatement.setString(5,  rant.getRanter());
	    insertStatement.setString(6,  rant.getLocation());
	    insertStatement.setInt(7, id);
	    insertStatement.executeUpdate();
	    
	    updateStatement = connection.prepareStatement(updateSql.replace("%id%", String.valueOf(id)));
	    updateStatement.execute();

		if (insertStatement != null) insertStatement.close();
		if (selectStatement != null) selectStatement.close();
		if (updateStatement != null) updateStatement.close();
		if (connection != null) connection.close();
	}
	
	/**
	 * Fetch rants from the backend
	 * @return
	 * @throws SQLException
	 */
	public static List<Rant> fetchRants() throws SQLException {
		Connection connection = new Database().getConnection();	  		
	    String rantSQl = "select id, created, emotion, question, rant, ranter, location from rants order by id desc limit 40;";	    
	    PreparedStatement preparedStatement = connection.prepareStatement(rantSQl);		    	    
	    ResultSet rs = preparedStatement.executeQuery();
	    
	    List<Rant> rants = new ArrayList<Rant>();
		while (rs.next()) {
			Rant rant = new Rant();
			rant.setId(rs.getString(1));
			rant.setCreated(DateUtils.getFormattedDate(rs.getTimestamp(2)));
			rant.setEmotion(rs.getString(3));
			rant.setQuestion(rs.getString(4));
			rant.setRant(rs.getString(5));
			rant.setRanter(rs.getString(6));
			rant.setLocation(rs.getString(7));
			rants.add(rant);
		}	
		
		if (preparedStatement != null) preparedStatement.close();
		if (connection != null) connection.close();
		return rants;
	}

	/**
	 * Fetch a single rant from the backend
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static Rant fetchRant(String id) throws SQLException {
		Connection connection = new Database().getConnection();	  		
	    String rantSQl = "select id, created, emotion, question, rant, ranter, location from rants where id = " + id + ";";	    
	    PreparedStatement preparedStatement = connection.prepareStatement(rantSQl);		    	    
	    ResultSet rs = preparedStatement.executeQuery();
	    
	    Rant rant = null;
		if (rs.next()) {
			rant = new Rant();
			rant.setId(rs.getString(1));
			rant.setCreated(DateUtils.getFormattedDate(rs.getTimestamp(2)));
			rant.setEmotion(rs.getString(3));
			rant.setQuestion(rs.getString(4));
			rant.setRant(rs.getString(5));
			rant.setRanter(rs.getString(6));
			rant.setLocation(rs.getString(7));
		}
		
		if (preparedStatement != null) preparedStatement.close();
		if (connection != null) connection.close();
		return rant;
	}
	
}