package com.quickrant.rave.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.quickrant.rave.Params;
import com.quickrant.rave.database.CustomSql;
import com.quickrant.rave.database.Database;
import com.quickrant.rave.database.DatabaseUtils;
import com.quickrant.rave.model.Rant;


public class RantService {

	private static Logger log = Logger.getLogger(RantService.class);
	
	/**
	 * Fetch all rants
	 * @return
	 */
	public static List<Rant> fetchRants() {
		List<Rant> rants = new ArrayList<Rant>();
		Database database = null;
		try {
			database = new Database();
			database.open();
			List<Rant> temp = Rant.findBySQL(CustomSql.FETCH_TOP_40_RANTS);
			/* Iterate to fetch */
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
	
	/**
	 * Fetch a single Rant object
	 * @param id
	 * @return Rant
	 */
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

	/**
	 * Save a new rant
	 * @param rant
	 */
	public static boolean saveRant(Params params) {
		Database database = null;
		try {
			database = new Database();
			database.open();
			Rant rant = parseFromParams(params);
			if (!rant.isValid()) return false;
			rant.saveIt();
		} catch (SQLException e) {
			log.error("Unable to open connection to database", e);
		} finally {
			DatabaseUtils.close(database);
		}
		return true;
	}
	
	/**
	 * Parse Rant from the form
	 * @param params
	 * @return
	 */
	private static Rant parseFromParams(Params params) {
		Map<String, String> map = params.getMap();
		Rant rant = new Rant();
		rant.fromMap(map);

		/* Set defaults */
		if (rant.getVisitorName() == null || rant.getVisitorName().isEmpty()) {
			rant.setVisitorName("Anonymous");
		}
		if (rant.getLocation() == null || rant.getLocation().isEmpty()) {
			rant.setLocation("Earth");
		}
		return rant;
	}
	
}