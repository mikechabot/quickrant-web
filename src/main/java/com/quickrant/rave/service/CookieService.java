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
	private static String cookieName;
	private static int cookieAge;
	private boolean initialized = false;
	
	private Configuration conf;

	
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
		cookieName =  conf.getOptionalString("cookie-name", "quickrant-uid");
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
	 * Determine if a cookie exists in the cache
	 * @param requestCookies
	 * @return
	 */
	public static boolean cookieExists(Cookie[] requestCookies) {
		if(requestCookies == null) {
			return false;
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
	public static RantCookie createCookie() {
	    RantCookie rantCookie = new RantCookie(cookieName, UUID.randomUUID().toString());
	    rantCookie.setMaxAge((int)cookieAge*60);
	    rantCookie.setIssued(new Date().getTime());
		cookies.put(rantCookie.getIssued(), rantCookie.getValue());
		return rantCookie;
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
		cookie.setMaxAge((int)cookieAge*60);
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