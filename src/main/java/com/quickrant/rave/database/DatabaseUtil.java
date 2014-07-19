package com.quickrant.rave.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DatabaseUtil {

	private static Logger log = Logger.getLogger(DatabaseUtil.class);
	
	/**
	 * Close Database
	 * @param database
	 */
	public static void close(Database database) {
		if (database != null) {
			database.close();
		}
	}
	
	/**
	 * Close ResultSet
	 * @param resultSet
	 */
	public static void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				log.error("Error closing result set");
			}
		}
	}
	
	/**
	 * Close PreparedStatement
	 * @param preparedStatement
	 */
	public static void close(PreparedStatement preparedStatement) {
		try {
			if (preparedStatement != null) {
				preparedStatement.close();
			}			
		} catch (SQLException e) {
			log.error("Unable to close PreparedStatement (Sybase)", e);
		}
	}
	
}
