package com.quickrant.rave.model;

import java.sql.Timestamp;

import org.javalite.activejdbc.Model;

import com.quickrant.rave.util.DateUtils;

public class Rant extends Model {
	
	static { validatePresenceOf("rant"); }
	
	String rant;
	String ranter;
	String emotion;
	String question;
	String location;
	Timestamp created;
	
	public void setRant(String rant) {
		set("rant", rant);
	}
	
	public String getRant() {
		return getString("rant");
	}
	
	public void setRanter(String ranter) {
		set("ranter", ranter);
	}
	
	public String getRanter() {
		return getString("ranter");
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