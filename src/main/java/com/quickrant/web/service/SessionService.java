package com.quickrant.web.service;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

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
	 * Create a new session cookie, and store the value in the cache
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
	 * @param cookie
	 * @return
	 */
	public boolean cookieExists(Cookie cookie) {
        if (cookie == null) throw new IllegalArgumentException("Cookie cannot be null");
		return containsValue(cookie.getValue());
	}

    /**
     * Determine whether an active session was attached to the request
     * @param request
     * @return
     */
    public boolean hasActiveSession(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return false;
        } else {
            Cookie session = null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(id)) {
                    session = cookie;
                }
            }
            if (session == null) {
                return false;
            }
            return cookieExists(session);
        }
    }

    /**
     * Update an existing session with a new session id
     * @param oldSessionId
     * @param newSessionId
     */
    public void updateSession(String oldSessionId, String newSessionId) {
        updateByValue(oldSessionId, newSessionId);
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