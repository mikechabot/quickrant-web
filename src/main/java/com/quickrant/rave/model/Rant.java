package com.quickrant.rave.model;

import java.sql.Timestamp;

import org.javalite.activejdbc.Model;

import com.quickrant.rave.utils.DateUtils;

public class Rant extends Model {
	
	static { validatePresenceOf("rant"); }

	public void setVisitorId(String visitorId) {
		set("visitorId", visitorId);
	}
	
	public String getVisitorId() {
		return getString("visitorId");
	}

	public void setVisitorName(String visitorName) {
		set("visitorName", visitorName);
	}
	
	public String getVisitorName() {
		return getString("visitorName");
	}
	
	public void setRant(String rant) {
		set("rant", rant);
	}
	
	public String getRant() {
		return getString("rant");
	}
	
	public void setEmotion(String emotion) {
		set("emotion", emotion);
	}
	
	public String getEmotion() {
		return getString("emotion");
	}

	public void setQuestion(String question) {
		set("question", question);
	}
	
	public String getQuestion() {
		return getString("question");
	}
	
	public void setLocation(String location) {
		set("location", location);
	}
	
	public String getLocation() {
		return getString("location");
	}
	
	public void setCreated(Timestamp created) {
		set("created_at", created);
	}
	
	public String getCreated() {
		return DateUtils.getFormattedDate(getTimestamp("created_at"));
	}
	
}