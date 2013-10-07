package com.chabot.quickrant.model;

import javax.servlet.http.Cookie;

public class RantCookie extends Cookie{

	private static final long serialVersionUID = 1L;
	
	long issued;
	
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
