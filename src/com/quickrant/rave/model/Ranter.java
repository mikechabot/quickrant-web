package com.quickrant.rave.model;

import com.quickrant.rave.Model;
import com.quickrant.rave.Params;
import com.quickrant.rave.Require;

public class Ranter extends Model<Ranter>{

	@Require
	String key;
	
	String postgresId;
	String postgresCreated;
	String cookieValue;
	String ipAddress;
	String userAgent;
	boolean active;
	long lastRant;
	int rantAttempts;
	int totalRants;
	int screenHeight;
	int screenWidth;
	int screenColor;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPostgresId() {
		return postgresId;
	}

	public void setPostgresId(String postgresId) {
		this.postgresId = postgresId;
	}

	public String getPostgresCreated() {
		return postgresCreated;
	}

	public void setPostgresCreated(String postgresCreated) {
		this.postgresCreated = postgresCreated;
	}

	public String getCookieValue() {
		return cookieValue;
	}

	public void setCookieValue(String cookie) {
		this.cookieValue = cookie;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getLastRant() {
		return lastRant;
	}

	public void setLastRant(long lastRant) {
		this.lastRant = lastRant;
	}

	public int getRantAttempts() {
		return rantAttempts;
	}

	public void setRantAttempts(int rantAttempts) {
		this.rantAttempts = rantAttempts;
	}

	public int getTotalRants() {
		return totalRants;
	}

	public void setTotalRants(int totalRants) {
		this.totalRants = totalRants;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenColor() {
		return screenColor;
	}

	public void setScreenColor(int screenColor) {
		this.screenColor = screenColor;
	}
		
	@Override
	public String toString() {
		return "Ranter [key=" + key + ", postgresId=" + postgresId
				+ ", postgresCreated=" + postgresCreated + ", cookie=" + cookieValue
				+ ", ipAddress=" + ipAddress + ", userAgent=" + userAgent
				+ ", active=" + active + ", lastRant="
				+ lastRant + ", rantAttempts=" + rantAttempts + ", totalRants="
				+ totalRants + ", screenHeight=" + screenHeight
				+ ", screenWidth=" + screenWidth + ", screenColor="
				+ screenColor + "]";
	}

	@Override
	public Ranter parse(Params params) {
		Ranter ranter = new Ranter();
		ranter.screenHeight = params.getInt("height");
		ranter.screenWidth = params.getInt("width");
		ranter.screenColor = params.getInt("color");
		ranter.cookieValue = params.getCookieValue("quickrant-uid");
		ranter.ipAddress = params.getIpAddress();
		ranter.userAgent = params.getUserAgent();		
		ranter.key = ranter.getIpAddress() + " " + ranter.getUserAgent() + " " + (ranter.getScreenHeight() + ranter.getScreenWidth() + ranter.getScreenColor());
		return ranter;
	}
	
}
