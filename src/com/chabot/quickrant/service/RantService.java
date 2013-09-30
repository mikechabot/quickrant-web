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
	
	public static void postRant(Rant rant) throws SQLException {				
		
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
	
	public static List<Rant> getRants() throws SQLException {
		Connection connection = new Database().getConnection();	  		
	    String rantSQl = "select id, created, emotion, question, rant, ranter, location from rants order by id desc limit 40;";	    
	    PreparedStatement preparedStatement = connection.prepareStatement(rantSQl);		    	    
	    ResultSet rs = preparedStatement.executeQuery();
	    
	    List<Rant> rants = new ArrayList<Rant>();
		while (rs.next()) {
			Rant rant = new Rant();
			rant.setId(rs.getString(1));
			rant.setCreated(getFormattedDate(rs.getTimestamp(2)));
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
	
	public static Rant getRant(String id) throws SQLException {
		Connection connection = new Database().getConnection();	  		
	    String rantSQl = "select id, created, emotion, question, rant, ranter, location from rants where id = " + id + ";";	    
	    PreparedStatement preparedStatement = connection.prepareStatement(rantSQl);		    	    
	    ResultSet rs = preparedStatement.executeQuery();
	    
	    Rant rant = null;
		if (rs.next()) {
			rant = new Rant();
			rant.setId(rs.getString(1));
			rant.setCreated(getFormattedDate(rs.getTimestamp(2)));
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
	
	private static String getFormattedDate(Timestamp timestamp) {
		return new SimpleDateFormat("MM/dd/yyyy h:m a").format(timestamp);
	}

	private static Timestamp getCurrentTimeStamp() {
		return new Timestamp(new Date().getTime()); 
	}
}