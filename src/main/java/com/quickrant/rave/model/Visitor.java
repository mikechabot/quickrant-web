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
		set("lastRant", lastRant);
	}
	
	public Timestamp getLastRant() {
		return getTimestamp("lastRant");
	}

	public void setCookie(String cookie) {
		set("cookie", cookie);
	}
	
	public String getCookie() {
		return getString("cookie");
	}

	public void setUserAgent(String userAgent) {
		set("userAgent", userAgent);
	}

	public String getUserAgent() {
		return getString("userAgent");
	}

	public void setFingerprint(String fingerprint) {
		set("fingerprint", fingerprint);
	}

	public String getFingerprint() {
		return getString("fingerprint");
	}

	public void setRantAttempts(int rantAttempts) {
		set("rantAttempts", rantAttempts);
	}

	public int getRantAttempts() {
		return getInteger("rantAttempts");
	}

	public void setScreenHeight(int screenHeight) {
		set("screenHeight", screenHeight);
	}

	public int getScreenHeight() {
		return getInteger("screenHeight");
	}

	public void setScreenWidth(int screenWidth) {
		set("screenWidth", screenWidth);
	}

	public int getScreenWidth() {
		return getInteger("screenWidth");
	}

	public void setScreenColor(int screenColor) {
		set("screenColor", screenColor);
	}
	
	public int getScreenColor() {
		return getInteger("screenColor");
	}

	public void setActive(boolean isActive) {
		set("isActive", isActive);
	}

	public boolean isActive() {
		return getBoolean("isActive");
	}
	
	public void setComplete(boolean isComplete) {
		set("isComplete", isComplete);
	}
	
	public boolean isComplete() {
		return getBoolean("isComplete");
	}

}
