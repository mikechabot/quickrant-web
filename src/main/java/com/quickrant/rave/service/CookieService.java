package com.quickrant.rave.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import com.quickrant.rave.utils.DateUtils;

public class CookieService {
	
	public static final String COOKIE_NAME = "quickrant-uuid";
	
	private static Logger log = Logger.getLogger(CookieService.class);	
	private static ConcurrentMap<Timestamp, String> cookies = new ConcurrentHashMap<Timestamp, String>();
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
		PurgeCookiesJob purgeOreos = new PurgeCookiesJob(conf);
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
	    String selectSql = "select created_at, cookie from visitors where isActive = true";
		try {
			database = new Database();
			database.open();
			select = database.getPreparedStatement(selectSql);		
			resultSet = select.executeQuery();
			while (resultSet.next()) {
				cookies.put(resultSet.getTimestamp(1),  resultSet.getString(2));
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
	public static Cookie newCookie() {
	    Cookie cookie = new Cookie(COOKIE_NAME, getRandomUUID());
	    cookie.setMaxAge(cookieAge*60);
	    putInCache(cookie);
		return cookie;
	}
	
	private static void putInCache(Cookie oreoCookie) {
		cookies.put(DateUtils.getCurrentTimeStamp(), oreoCookie.getValue());
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
		cookie.setMaxAge(cookieAge*60);
		cookie.setPath("/");
		log.info(cookie.getValue());
		return cookie;
	}
	
	/**
	 * Update the cache with a new cookie value
	 * @param cookie
	 * @param value
	 */
	private static void updateCache(Cookie cookie, String newValue) {
		for(Map.Entry<Timestamp, String> temp : cookies.entrySet()) {
			if(temp.getValue().equals(cookie.getValue())) {
				cookies.put(temp.getKey(), newValue);
			}
		}
	}

	/*
	 * Purge old cookies from the cache
	 */
	public static void clean() {
		if(cookies != null) {
			int start = cookies.size();
			for(Timestamp temp : cookies.keySet()) {
				long diff = System.currentTimeMillis() - temp.getTime();
				if (diff > cookieAge*60*1000) { 
					cookies.remove(temp);
				}
			}
			int finish = cookies.size();
	 		log.info("Cleaned up " + (start-finish) + " cached cookies (" + finish + " active)");
		}
	}
}