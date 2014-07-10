package com.quickrant.rave.model;

import javax.servlet.http.Cookie;

/**
 * Cookies represent unique users of the system
 *
 */
public class RantCookie extends Cookie {

	public static final String COOKIE_NAME = "quickrant-uuid";
	
	private long issued;
	
	public RantCookie(String value) {
		super(COOKIE_NAME, value);
	}
	
	public void initialize(int cookieAge) {
	    setMaxAge(cookieAge*60);
	    this.issued = System.currentTimeMillis();
	}
		
	public long getIssued() {
		return issued;
	}
	
}