package com.quickrant.rave.model;

import java.sql.Timestamp;

import org.javalite.activejdbc.Model;

public class Visitor extends Model {
	
	public void setCreatedAt(Timestamp createdAt) {
		set("created_at", createdAt);
	}	
	
	public Timestamp getCreatedAt() {
		return getTimestamp("created_at");
	}

	public void setLastRant(Timestamp lastRant) {
		set("last_rant", lastRant);
	}
	
	public Timestamp getLastRant() {
		return getTimestamp("last_rant");
	}

	public void setCookie(String cookie) {
		set("cookie", cookie);
	}
	
	public String getCookie() {
		return getString("cookie");
	}

	public void setUserAgent(String userAgent) {
		set("user_agent", userAgent);
	}

	public String getUserAgent() {
		return getString("user_agent");
	}
	
	public void setIpAddress(String ipAddress) {
		set("ip_address", ipAddress);
	}
	
	public String getIpAddress() {
		return getString("ip_address");
	}

	public void setFingerprint(String fingerprint) {
		set("fingerprint", fingerprint);
	}

	public String getFingerprint() {
		return getString("fingerprint");
	}

	public void setRantAttempts(int rantAttempts) {
		set("rant_attempts", rantAttempts);
	}

	public int getRantAttempts() {
		return getInteger("rant_attempts");
	}

	public void setScreenHeight(int screenHeight) {
		set("screen_height", screenHeight);
	}

	public int getScreenHeight() {
		return getInteger("screen_height");
	}

	public void setScreenWidth(int screenWidth) {
		set("screen_width", screenWidth);
	}

	public int getScreenWidth() {
		return getInteger("screen_width");
	}

	public void setScreenColor(int screenColor) {
		set("screen_color", screenColor);
	}
	
	public int getScreenColor() {
		return getInteger("screen_color");
	}

	public void setActive(boolean isActive) {
		set("is_active", isActive);
	}

	public boolean isActive() {
		return getBoolean("is_active");
	}
	
	public void setComplete(boolean isComplete) {
		set("is_complete", isComplete);
	}
	
	public boolean isComplete() {
		return getBoolean("is_complete");
	}

}
