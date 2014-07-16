package com.quickrant.rave.cache;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import com.quickrant.rave.database.CustomSql;
import com.quickrant.rave.database.Database;
import com.quickrant.rave.database.DatabaseUtils;
import com.quickrant.rave.models.Visitor;

/**
 * Thread-safe, self-cleaning, singleton cache
 * @author Mike Chabot
 *
 */
public class CookieCache extends Cache {
	
private static Logger log = Logger.getLogger(CookieCache.class);

	private static CookieCache cache;

	private CookieCache() { }
	
	public static CookieCache getCache() {
	    if (cache == null) {
	    	cache = new CookieCache();
	    }
	    return cache;
	}
	
	/**
	 * Populate the cookie cache from the backend
	 */
	public void populateCookieCache() {
		Database database = null;
		try {
			database = new Database();
			database.open();
			List<Visitor> visitors = Visitor.findBySQL(CustomSql.POPULATE_COOKIE_CACHE);
			for (Visitor visitor : visitors) {
				put(visitor.getCreatedAt(), visitor.getCookie());
			}
			log.info("Loaded " + size() + " persisted cookie(s)...");
		} catch (SQLException e) {
			log.error("Error fetching cookies", e);
		} finally {
			DatabaseUtils.close(database);
		}
	}

	/**
	 * Create an HTTP new cookie with a random string
	 *   (e.g. b0461f4e-e0e6-4c42-8f81-d77d287fad56)
	 * @return Cookie
	 */
	public Cookie newCookie() {
		String value = Util.getRandomUUID();
		put(newEntry(value));
		return getCookie(value);
	}

	/**
	 * Create a new cookie with a specific value
	 * @param value
	 * @return
	 */
	public Cookie getCookie(String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxEntryAgeInMin);
		cookie.setPath("/");
		return cookie;
	}
	
	/**
	 * See if the cache contains a cookie
	 * @param cookies
	 * @return
	 */
	public boolean containsCookie(Cookie[] cookies) {
		if (cookies != null) {
			for (Cookie each : cookies) {
				if (containsValue(each.getValue())) return true;
			}
		}
		return false;
	}

	/**
	 * Utility class for random string generation
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

}