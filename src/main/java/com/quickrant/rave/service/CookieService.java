package com.quickrant.rave.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import com.quickrant.rave.Configuration;
import com.quickrant.rave.database.CustomSql;
import com.quickrant.rave.database.Database;
import com.quickrant.rave.database.DatabaseUtils;
import com.quickrant.rave.model.Visitor;
import com.quickrant.rave.utils.TimeUtils;

public class CookieService {
	
	public static final String COOKIE_NAME = "quickrant-uuid";
	public static int COOKIE_AGE_IN_MIN;
	public static int INTERVAL_IN_MIN;
	
	private static Logger log = Logger.getLogger(CookieService.class);	
	private static ConcurrentMap<Timestamp, String> cookies = new ConcurrentHashMap<Timestamp, String>();
	
	private Timer timer;
	private boolean initialized = false;
		
	public CookieService(Configuration conf) {
		COOKIE_AGE_IN_MIN = conf.getOptionalInt("cookie-age", 1440);
		INTERVAL_IN_MIN = conf.getOptionalInt("clean-cookie-cache-interval", 5);
	}
	
	/**
	 * Initialize CookieService
	 */
	public void initialize() {
		if (initialized) return;
		
		/* Start the CleanCacheTask */
		timer = new Timer();
        timer.schedule(new CleanCacheTask(), 5000, INTERVAL_IN_MIN*60*1000);
		
        populateCookieCache();
		initialized = true;
	}
	
	/**
	 * Populate the cookie cache from the backend
	 */
	private void populateCookieCache() {
		Database database = null;
		try {
			database = new Database();
			database.open();
			List<Visitor> visitors = Visitor.findBySQL(CustomSql.POPULATE_COOKIE_CACHE);
			for (Visitor visitor : visitors) {
				cookies.put(visitor.getCreatedAt(), visitor.getCookie());
			}
			log.info("Fetched " + cookies.size() + " cookies");
		} catch (SQLException e) {
			log.error("Error fetching cookies", e);
		} finally {
			DatabaseUtils.close(database);			
		}
	}

	/**
	 * Create an HTTP cookie
	 * @return RantCookie
	 */
	public static Cookie newCookie() {
	    Cookie cookie = new Cookie(COOKIE_NAME, Util.getRandomUUID());
	    cookie.setMaxAge(COOKIE_AGE_IN_MIN*60);
	    cookies.put(TimeUtils.getNowTimestamp(), cookie.getValue());
		return cookie;
	}	

	/**
	 * Determine if a quickrant cookie exists in the cache
	 * @param requestCookies
	 * @return
	 */
	public static boolean existsInCache(Cookie[] requestCookies) {
		if(requestCookies == null || cookies.size() == 0) {
		} else {
			for(Cookie cookie : requestCookies) {
				if (cookies.containsValue((cookie.getValue()))) return true;
			}
		}
		return false;
	}
	
	/**
	 * Update cookie with "*" to signify the AJAX 
	 * response was received
	 * @param cookie
	 * @return Cookie
	 */
	public static Cookie updateCookie(Cookie cookie) {
		String newValue = cookie.getValue() + "*";
		updateCookieInCache(cookie, newValue);
		cookie.setValue(newValue);
		cookie.setMaxAge(COOKIE_AGE_IN_MIN*60);
		cookie.setPath("/");
		return cookie;
	}
	
	/**
	 * Locate an existing cookie an update it
	 * @param cookie
	 * @param value
	 */
	private static void updateCookieInCache(Cookie cookie, String newValue) {		
		for(Map.Entry<Timestamp, String> temp : cookies.entrySet()) {
			if(temp.getValue().equals(cookie.getValue())) {
				cookies.put(temp.getKey(), newValue);
			}
		}
	}
	
	/**
	 * Util class
	 */
	private static class Util {
		/**
		 * Generate a sufficiently random number
		 * @return a random UUID (e.g. 067e6162-3b6f-4ae2-a171-2470b63dff0)
		 */
		public static String getRandomUUID() {
			return String.valueOf(UUID.randomUUID());
		}
	}
	
    /**
     * Clean the cookie cache every N minutes
     */
    private class CleanCacheTask extends TimerTask {    	
    	@Override
    	public void run() {
    		cleanCache();
    		log.info("Next run time: " + TimeUtils.getFutureTimestamp(INTERVAL_IN_MIN));
        }
    	
    	/**
    	 * Remove old cookies from the cache
    	 */
    	private void cleanCache() {
    		if(cookies != null) {
    			int start = cookies.size();
    			for(Timestamp temp : cookies.keySet()) {
    				if (shouldBeRemoved(temp)) {
    					cookies.remove(temp);
    				}
    			}
    	 		log.info("Cleaned up " + (start-cookies.size()) + " cached cookies (" + cookies.size() + " active)");
    		}
    	}
    	
    	/**
    	 * Check to see if the cookie is older than the defined COOKIE_AGE
    	 * @param Timestamp
    	 * @return boolean
    	 */
    	private boolean shouldBeRemoved(Timestamp ts) {
    		return (TimeUtils.getNow() - ts.getTime()) > COOKIE_AGE_IN_MIN*60*1000;
    	}
    }
	
}