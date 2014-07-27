package com.quickrant.web.cache;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;
import org.javalite.activejdbc.Model;

import com.quickrant.api.Cache;
import com.quickrant.api.models.Visitor;
import com.quickrant.api.services.VisitorService;

/**
 * Thread-safe, self-cleaning, singleton cache
 * @author Mike Chabot
 *
 */
public class CookieCache extends Cache {
	
private static Logger log = Logger.getLogger(CookieCache.class);

    public static final String POPULATE_COOKIE_CACHE = "select * from visitors where created_at > (now() - interval '30 days')";

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
		VisitorService visitorSvc = new VisitorService();
		List<Model> visitors = visitorSvc.fetchBySql(POPULATE_COOKIE_CACHE);
		for (Model each : visitors) {
			Visitor visitor = (Visitor) each; 
			put(visitor.getCreatedAt(), visitor.getCookie());
		}
		log.info("Loaded " + size() + " persisted cookie(s)...");
	}
	
	/**
	 * Create a HTTP new cookie with a random string,
	 * and put it in the cache
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
		cookie.setMaxAge((int) expiry * 60);
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