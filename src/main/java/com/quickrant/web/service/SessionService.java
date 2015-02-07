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

    /**
     * Singleton cache
     * @return
     */
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
	public Cookie newSession() {
		String value = generateSessionId();
		put(newEntry(value));
		return newCookie(value, null);
	}

	/**
	 * Generate a cookie with a specific value
	 * @param value
	 * @return
	 */
	public Cookie newCookie(String value, String suffix) {
        if (suffix != null)  value += suffix;
		Cookie cookie = new Cookie(id, value);
		cookie.setMaxAge((int) expiry * 60);
		cookie.setPath("/");
		return cookie;
	}

    /**
     * Get the quickrant cookie from a session
     * @param request
     * @return
     */
    public Cookie getSession(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(id)) {
                return cookie;
            }
        }
        return null;
    }

    /**
     * Determine whether an active session was attached to the request
     * @param request
     * @return
     */
    public boolean hasActiveSession(HttpServletRequest request) {
        Cookie cookie = getSession(request);
        if (cookie == null) return false;
        return exists(cookie);
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
     * Determine if the cache contains a cookie
     * @param cookie
     * @return
     */
    public boolean exists(Cookie cookie) {
        if (cookie == null) throw new IllegalArgumentException("Cookie cannot be null");
        return containsValue(cookie.getValue());
    }

    /**
     * Determine if the cookie ends with "*", in which
     * case the user's browser completed the round-trip
     *
     * This can be spoofed, but it won't do any good
     *
     * @param cookie
     * @return
     */
    public boolean isAuthenticated(Cookie cookie) {
        Pattern pattern = Pattern.compile("\\*$");
        Matcher matcher = pattern.matcher(cookie.getValue());
        return matcher.find();
    }

    public String generateSessionId() {
        return Util.getRandomUUID();
    }

    /**
	 * Utility class for random string generation
	 */
	private static class Util {
		/**
		 * Generate a sufficiently random String
		 * @return a random UUID (e.g. 067e6162-3b6f-4ae2-a171-2470b63dff0)
		 */
		public static String getRandomUUID() {
			return String.valueOf(UUID.randomUUID());
		}
	}

}