package com.chabot.quickrant.service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.Cookie;

public class CookieService {
	
	private static final String COOKIE_NAME = "quickrant-uid";
	private static final long COOKIE_AGE = 21600; // 6 hours (in seconds)
	private static ConcurrentMap<Long, String> cookies = new ConcurrentHashMap<Long, String>();
	
	public static Cookie createNewCookie() {
		Cookie cookie = new Cookie(COOKIE_NAME, UUID.randomUUID().toString());
		cookie.setMaxAge((int)COOKIE_AGE);
		cookies.put(new Date().getTime(), cookie.getValue());
		return cookie;
	}
	
	public static boolean findCookie(Cookie[] requestCookies) {
		if(requestCookies == null) {
			return false;
		} else {
			for(Cookie cookie : requestCookies) {
				if (cookies.containsValue((cookie.getValue()))) return true;
			}
		}
		return false;
	}
	
	public static int getCookiesSize() {
		return cookies.size();
	}
	
	public static void clean() {
		if(cookies != null) {
			for(Long temp : cookies.keySet()) {
				if (new Date().getTime() - temp > COOKIE_AGE*1000) {
					cookies.remove(temp);
				}
			}
		}
	}
}
