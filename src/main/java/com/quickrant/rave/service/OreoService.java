package com.quickrant.rave.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import com.quickrant.rave.Configuration;

import com.quickrant.rave.database.Database;
import com.quickrant.rave.database.DatabaseUtils;
import com.quickrant.rave.jobs.PurgeOreosJob;
import com.quickrant.rave.model.Oreo;

public class OreoService {
	
	private static Logger log = Logger.getLogger(OreoService.class);	
	private static ConcurrentMap<Long, String> oreos = new ConcurrentHashMap<Long, String>();
	private static int oreoAge;
	
	private Configuration conf;
	private boolean initialized = false;
		
	public OreoService(Configuration conf) {
		this.conf = conf;
	}
	
	public void initialize() {
		if (initialized) return;
		getConfig();
		startPurgeJob();
		populateCache();
		initialized = true;
	}

	private void getConfig() {
		oreoAge = conf.getOptionalInt("cookie-age", 10);
	}

	private void startPurgeJob() {
		PurgeOreosJob purgeOreos = new PurgeOreosJob(conf);
		purgeOreos.start();
	}
		
	/**
	 * Populate the cookie cache. This ensures *active* users that were 
	 * issued a cookie prior to a web server restart are able to post 
	 * without service interruption, as posting with an invalid cookie is 
	 * not allowed.
	 */
	private void populateCache() {
		Database database = null;
	    PreparedStatement select = null;		    	    
	    ResultSet resultSet = null;
	    String selectSql = "select cookieissued, cookievalue from ranter where cookieactive = true;";
		try {
			database = new Database();
			database.open();
			select = database.getPreparedStatement(selectSql);		
			resultSet = select.executeQuery();
			while (resultSet.next()) {
				oreos.put(resultSet.getLong(1),  resultSet.getString(2));
			}
			log.info("Fetched " + oreos.size() + " cookies");
		} catch (SQLException e) {
			log.error("Error fetching cookies", e);
		} finally {
			DatabaseUtils.close(resultSet);
			DatabaseUtils.close(select);
			DatabaseUtils.close(database);			
		}
	}

	public static int getCacheSize() {
		return oreos.size();
	}
	
	/**
	 * Determine if a quickrant cookie exists in the cache
	 * @param requestCookies
	 * @return
	 */
	public static boolean inCache(Cookie[] requestCookies) {
		if(requestCookies == null || oreos.size() == 0) {
		} else {
			for(Cookie cookie : requestCookies) {
				if (oreos.containsValue((cookie.getValue()))) return true;
			}
		}
		return false;
	}
	
	/**
	 * Create an HTTP cookie
	 * @return RantCookie
	 */
	public static Oreo newOreo() {
	    Oreo oreoCookie = new Oreo(getRandomUUID());
	    oreoCookie.initialize(oreoAge);
	    putInCache(oreoCookie);
		return oreoCookie;
	}
	
	private static void putInCache(Oreo oreoCookie) {
		oreos.put(oreoCookie.getIssued(), oreoCookie.getValue());
	}

	/**
	 * Generate a decently random string
	 * @return a random UUID (e.g. 067e6162-3b6f-4ae2-a171-2470b63dff0)
	 */
	private static String getRandomUUID() {
		return String.valueOf(UUID.randomUUID());
	}

	/**
	 * Update cookie with "*" to signify the AJAX 
	 * response was received
	 * @param cookie
	 * @return Cookie
	 */
	public static Cookie updateCookie(Cookie cookie) {
		String newValue = cookie.getValue() + "*";
		updateCache(cookie, newValue);
		cookie.setValue(newValue);
		cookie.setMaxAge(oreoAge*60);
		cookie.setPath("/");
		return cookie;
	}
	
	/**
	 * Update the cache with a new cookie value
	 * @param cookie
	 * @param value
	 */
	private static void updateCache(Cookie cookie, String newValue) {
		for(Map.Entry<Long, String> temp : oreos.entrySet()) {
			if(temp.getValue().equals(cookie.getValue())) {
				oreos.put(temp.getKey(), newValue);
			}
		}
	}

	/*
	 * Purge old cookies from the cache
	 */
	public static void clean() {
		if(oreos != null) {
			int start = oreos.size();
			for(Long temp : oreos.keySet()) {
				if (new Date().getTime() - temp > oreoAge*60*1000) { 
					oreos.remove(temp);
				}
			}
			int finish = oreos.size();
	 		log.info("Cleaned up " + (start-finish) + " cached cookies (" + finish + " active)");
		}
	}
}