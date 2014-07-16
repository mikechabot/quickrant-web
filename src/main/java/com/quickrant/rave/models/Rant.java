package com.quickrant.rave.models;

import java.sql.Timestamp;

import org.javalite.activejdbc.Model;

import com.quickrant.rave.utils.TimeUtils;

public class Rant extends Model {
	
	static { validatePresenceOf("rant", "emotion_id", "question_id"); }

	public void setVisitorId(int visitorId) {
		set("visitor_id", visitorId);
	}
	
	public int getVisitorId() {
		return getInteger("visitor_id");
	}

	public void setVisitorName(String visitorName) {
		set("visitor_name", visitorName);
	}
	
	public String getVisitorName() {
		return getString("visitor_name");
	}
	
	public void setRant(String rant) {
		set("rant", rant);
	}
	
	public String getRant() {
		return getString("rant");
	}
	
	public void setEmotionId(int emotionId) {
		set("emotion_id", emotionId);
	}
	
	public int getEmotionId() {
		return getInteger("emotion_id");
	}

	public void setQuestionId(int questionId) {
		set("question_id", questionId);
	}
	
	public int getQuestionId() {
		return getInteger("question_id");
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
		return TimeUtils.getFormattedDate(getTimestamp("created_at"));
	}
	
}