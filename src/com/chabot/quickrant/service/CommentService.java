package com.chabot.quickrant.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import com.chabot.quickrant.database.Database;
import com.chabot.quickrant.model.Comment;

public class CommentService {
	
	private static Logger log = Logger.getLogger(CommentService.class);
	
	public static void create(Comment comment) throws SQLException  {				
		
		Connection connection = null;
	    PreparedStatement insert = null;
	    	    
	    try {
	    	connection = new Database().getConnection();	    	
		    String insertSql = "insert into comments"
		    		+ "(id, created, emotion, comment, commenter, location) values"
		    		+ "(nextval('comments_id_seq'),?,?,?,?,?)";		    
		    PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
		    
		    if(comment.getCommenter() == null) {
		    	comment.setCommenter("Anonymous");
		    }
		    if(comment.getLocation() == null) {
		    	comment.setLocation("Earth");
		    }		    
		    
		    preparedStatement.setTimestamp(1, getCurrentTimeStamp());
		    preparedStatement.setString(2, comment.getEmotion());
		    preparedStatement.setString(3, comment.getComment());
		    preparedStatement.setString(4, comment.getCommenter());
		    preparedStatement.setString(5, comment.getLocation());	
		    preparedStatement .executeUpdate();
	    	
	    } catch (SQLException e) {	    	 
			log.error("Error inserting comment: " + e.getMessage());
 
		} finally { 
			if (insert != null) {
				insert.close();
			} 
			if (connection != null) {
				connection.close();
			}
		}
	}
	
	private static Timestamp getCurrentTimeStamp() {
		Date today = new Date();
		return new Timestamp(today.getTime()); 
	}

}