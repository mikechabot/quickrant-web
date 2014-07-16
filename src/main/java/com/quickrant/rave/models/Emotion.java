package com.quickrant.rave.models;

import org.javalite.activejdbc.Model;

public class Emotion extends Model {

	public void setEmotion(String emotion) {
		setString("emotion", emotion);
	}
	
	public String getEmotion() {
		return getString("emotion");
	}

}
