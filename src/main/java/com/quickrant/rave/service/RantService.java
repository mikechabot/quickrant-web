package com.quickrant.rave.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.quickrant.rave.database.Database;
import com.quickrant.rave.database.DatabaseUtils;
import com.quickrant.rave.model.Rant;


public class RantService {

	private static Logger log = Logger.getLogger(RantService.class);
	
	public static List<Rant> fetchRants() {
		List<Rant> rants = new ArrayList<Rant>();
		Database database = null;
		String sql = "select id, created_at, emotion, question, rant, visitor_name, location from rants order by id desc limit 40";
		try {
			database = new Database();
			database.open();
			List<Rant> temp = Rant.findBySQL(sql);
			for (Rant each : temp) {
				rants.add(each);
			}
		} catch (SQLException e) {
			log.error("Unable to open connection to database", e);
		} finally {
			DatabaseUtils.close(database);
		}
		return rants;		
	}
	
	public static Rant fetchRant(int id) {
		Rant rant = null;
		Database database = null;
		try {
			database = new Database();
			database.open();
			rant = Rant.findById(id);
		} catch (SQLException e) {
			log.error("Unable to open connection to database", e);
		} finally {
			DatabaseUtils.close(database);
		}
		return rant;		
	}

	public static void saveRant(Rant rant) {
		Database database = null;
		try {
			database = new Database();
			database.open();
			rant.saveIt();
		} catch (SQLException e) {
			log.error("Unable to open connection to database", e);
		} finally {
			DatabaseUtils.close(database);
		}		
	}
	
}