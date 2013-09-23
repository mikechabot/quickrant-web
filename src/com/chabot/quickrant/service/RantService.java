package com.chabot.quickrant.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.chabot.quickrant.database.Database;
import com.chabot.quickrant.model.Rant;

public class RantService {
	
	private static Logger log = Logger.getLogger(RantService.class);
	
	public static void create(Rant rant) throws SQLException {				
		
		Connection connection =  new Database().getConnection();  	
	    String insertSql = "insert into rants (id, created, emotion, question, rant, ranter, location) values (nextval('rants_id_seq'),?,?,?,?,?,?);";		    
	    PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
	    
	    if(rant.getRanter() == null || rant.getRanter().trim().length() == 0) {
	    	 rant.setRanter("Anonymous");
	    }
	    if(rant.getLocation() == null || rant.getLocation().trim().length() == 0) {
	    	 rant.setLocation("Earth");
	    }		    
	    	    
	    preparedStatement.setTimestamp(1, getCurrentTimeStamp());
	    preparedStatement.setString(2,  rant.getEmotion());
	    preparedStatement.setString(3,  rant.getQuestion());
	    preparedStatement.setString(4,  rant.getRant());
	    preparedStatement.setString(5,  rant.getRanter());
	    preparedStatement.setString(6,  rant.getLocation());
	    preparedStatement.executeUpdate();

		if (preparedStatement != null) preparedStatement.close();
		if (connection != null) connection.close();
	}
	
	public static List<Rant> getLongList() throws SQLException {
		Connection connection = new Database().getConnection();	  		
	    String rantSQl = "select created, emotion, question, rant, ranter, location from rants order by id desc limit 40;";	    
	    PreparedStatement preparedStatement = connection.prepareStatement(rantSQl);		    	    
	    ResultSet rs = preparedStatement.executeQuery();
	    
	    List<Rant> rants = new ArrayList<Rant>();
		while (rs.next()) {
			Rant rant = new Rant();
			rant.setCreated(getFormattedDate(rs.getTimestamp(1)));
			rant.setEmotion(rs.getString(2));
			rant.setQuestion(rs.getString(3));
			rant.setRant(rs.getString(4));
			rant.setRanter(rs.getString(5));
			rant.setLocation(rs.getString(6));
			rants.add(rant);
			log.debug(rant.toString());
		}	
		
		if (preparedStatement != null) preparedStatement.close();
		if (connection != null) connection.close();
		return rants;
	}
	
	private static String getFormattedDate(Timestamp timestamp) {
		return new SimpleDateFormat("MM/dd/yyyy h:m a").format(timestamp);
	}

	private static Timestamp getCurrentTimeStamp() {
		return new Timestamp(new Date().getTime()); 
	}
}