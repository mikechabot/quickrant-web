package com.quickrant.web.service;

import java.util.UUID;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import com.quickrant.api.Cache;

public class SessionService extends Cache {
	
	private static Logger log = Logger.getLogger(SessionService.class);

	private static SessionService cache;

	private SessionService() { }

	public static SessionService getInstance() {
	    if (cache == null) {
	    	cache = new SessionService();
	    }
	    return cache;
	}

	/**
	 * Create and store a new session in the cache, return a Cookie
	 *   (e.g. b0461f4e-e0e6-4c42-8f81-d77d287fad56)
	 * @return Cookie
	 */
	public Cookie createNewSession() {
		String value = Util.getRandomUUID();
		put(newEntry(value));
		return generateCookie(value);
	}

	/**
	 * Generate a cookie with a specific value
	 * @param value
	 * @return
	 */
	public Cookie generateCookie(String value) {
		Cookie cookie = new Cookie(id, value);
		cookie.setMaxAge((int) expiry * 60);
		cookie.setPath("/");
		return cookie;
	}
	
	/**
	 * See if the cache contains a cookie
	 * @param cookies
	 * @return
	 */
	public boolean sessionExists(Cookie cookie) {
		return containsValue(cookie.getValue());
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