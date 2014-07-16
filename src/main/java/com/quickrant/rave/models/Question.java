package com.quickrant.rave.models;

import org.javalite.activejdbc.Model;

public class Question extends Model {

	public void setQuestion(String question) {
		setString("question", question);
	}
	
	public String getQuestion() {
		return getString("question");
	}

}
