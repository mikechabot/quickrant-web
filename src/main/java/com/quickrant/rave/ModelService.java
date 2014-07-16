package com.quickrant.rave;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.javalite.activejdbc.Model;

import com.quickrant.rave.database.Database;
import com.quickrant.rave.database.DatabaseUtils;

public abstract class ModelService {
		
	private static Logger log = Logger.getLogger(ModelService.class);

	protected abstract List<Model> findAll();
	protected abstract Model findById(int id);
	protected abstract Model findFirst(String subQuery, Object value);
	protected abstract Model parse(Map<String, String> map);	
	protected abstract boolean save(Map<String, String> map);
	

	/**
	 * Fetch all records
	 * @return
	 */
	public List<Model> fetch() {
		List<Model> list = new ArrayList<Model>();
		Database database = null;
		try {
			database = new Database();
			database.open();
			List<Model> temp = findAll();
			/* Iterate to fetch */
			for (Model each : temp) {
				list.add(each);
			}
		} catch (SQLException e) {
			log.error("Unable to open connection to database", e);
		} finally {
			DatabaseUtils.close(database);
		}
		return list;
	}

	/**
	 * Fetch single record
	 * @param id
	 * @return
	 */
	public Model fetchById(int id) {
		Model model = null;
		Database database = null;
		try {
			database = new Database();
			database.open();
			model = findById(id);
		} catch (SQLException e) {
			log.error("Unable to open connection to database", e);
		} finally {
			DatabaseUtils.close(database);
		}
		return model;
	}

	/**
	 * Fetch first record
	 * @param subQuery ("id = ?")
	 * @param value ("2")
	 * @return Model
	 */
	public Model fetchFirst(String subQuery, Object value) {
		Model model = null;
		Database database = null;
		try {
			database = new Database();
			database.open();
			model = findFirst(subQuery, value);
		} catch (SQLException e) {
			log.error("Unable to open connection to database", e);
		} finally {
			DatabaseUtils.close(database);
		}
		return model;
	}

	/**
	 * Fetch first record
	 * @param model
	 */
	public boolean save(Model model) {
		Database database = null;
		try {
			database = new Database();
			database.open();
			model.saveIt();
		} catch (SQLException e) {
			log.error("Unable to open connection to database", e);
		} finally {
			DatabaseUtils.close(database);
		}
		return true;
	}
	
	/**
	 * Parse a model from a map
	 * @param model
	 * @param map
	 * @return
	 */
	public Model parse(Model model, Map<String, String> map) {
		Database database = null;
		try {
			database = new Database();
			database.open();
			model.fromMap(map);
		} catch (SQLException e) {
			log.error("Unable to open connection to database", e);
		} finally {
			DatabaseUtils.close(database);
		}
		return model;
	}
	
}