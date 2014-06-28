package com.quickrant.rave.model;

import javax.servlet.http.Cookie;

/**
 * Cookies represent unique users of the system
 *
 */
public class RantCookie extends Cookie{

	private static final long serialVersionUID = 1L;
	
	private long issued;
	
	public RantCookie(String name, String value) {
		super(name, value);		
	}
		
	public long getIssued() {
		return issued;
	}
	
	public void setIssued(long issued) {
		this.issued = issued;
	}
	
}