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
import com.quickrant.rave.jobs.PurgeCookiesJob;
import com.quickrant.rave.model.RantCookie;

public class CookieService {
	
	private static Logger log = Logger.getLogger(CookieService.class);	
	private static ConcurrentMap<Long, String> cookies = new ConcurrentHashMap<Long, String>();
	private static int cookieAge;
	
	private Configuration conf;
	private boolean initialized = false;
		
	public CookieService(Configuration conf) {
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
		cookieAge = conf.getOptionalInt("cookie-age", 10);
	}

	private void startPurgeJob() {
		PurgeCookiesJob purgeCookies = new PurgeCookiesJob(conf);
		purgeCookies.start();
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
				cookies.put(resultSet.getLong(1),  resultSet.getString(2));
			}
			log.info("Fetched " + cookies.size() + " cookies");
		} catch (SQLException e) {
			log.error("Error fetching cookies", e);
		} finally {
			DatabaseUtils.close(resultSet);
			DatabaseUtils.close(select);
			DatabaseUtils.close(database);			
		}
	}

	public static int getCacheSize() {
		return cookies.size();
	}
	
	/**
	 * Determine if a quickrant cookie exists in the cache
	 * @param requestCookies
	 * @return
	 */
	public static boolean inCache(Cookie[] requestCookies) {
		if(requestCookies == null || cookies.size() == 0) {
		} else {
			for(Cookie cookie : requestCookies) {
				if (cookies.containsValue((cookie.getValue()))) return true;
			}
		}
		return false;
	}
	
	/**
	 * Create an HTTP cookie
	 * @return RantCookie
	 */
	public static RantCookie newCookie() {
	    RantCookie rantCookie = new RantCookie(getCookieValue());
	    rantCookie.initialize(cookieAge);
	    putInCache(rantCookie);
		
		return rantCookie;
	}
	
	private static void putInCache(RantCookie rantCookie) {
		cookies.put(rantCookie.getIssued(), rantCookie.getValue());
	}

	/**
	 * Generate a decently random string
	 * @return a random UUID (e.g. 067e6162-3b6f-4ae2-a171-2470b63dff0)
	 */
	private static String getCookieValue() {
		return String.valueOf(UUID.randomUUID());
	}

	/**
	 * Update a cookie with "*" to signify the AJAX response 
	 * was received, then update the local cookies cache
	 * @param cookie
	 * @return Cookie
	 */
	public static Cookie updateCookie(Cookie cookie) {
		String newCookieValue = cookie.getValue() + "*";
		for(Map.Entry<Long, String> temp : cookies.entrySet()) {
			if(temp.getValue().equals(cookie.getValue())) {
				cookies.put(temp.getKey(), newCookieValue);
			}
		}
		cookie.setValue(newCookieValue);
		cookie.setMaxAge(cookieAge*60);
		cookie.setPath("/");		
		return cookie;
	}
	
	/*
	 * Purge old cookies from the cache
	 */
	public static void clean() {
		if(cookies != null) {
			int start = cookies.size();
			for(Long temp : cookies.keySet()) {
				if (new Date().getTime() - temp > cookieAge*60*1000) { 
					cookies.remove(temp);
				}
			}
			int finish = cookies.size();
	 		log.info("Cleaned up " + (start-finish) + " cached cookies (" + finish + " active)");
		}
	}
}