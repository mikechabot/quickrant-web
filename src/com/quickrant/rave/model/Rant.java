package com.quickrant.rave.model;

import com.quickrant.rave.Model;
import com.quickrant.rave.Params;
import com.quickrant.rave.Require;

public class Rant extends Model<Rant> {
		
	@Require
	private String rant;
	
	private String id;
	private String emotion;
	private String question;
	private String ranter;
	private String location;
	private String created;
		
	public Rant() {}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}
	
	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}

	public String getEmotion() {
		return emotion;
	}
	
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
		
	public String getRant() {
		return rant;
	}

	public void setRant(String rant) {
		this.rant = rant;
	}
	
	public String getRanter() {
		return ranter;
	}
	
	public void setRanter(String ranter) {
		this.ranter = ranter;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	@Override
	public String toString() {
		return "Rant [rant=" + rant + ", id=" + id + ", emotion=" + emotion
				+ ", question=" + question + ", ranter=" + ranter
				+ ", location=" + location + ", created=" + created + "]";
	}

	@Override
	public Rant parse(Params params) {
		Rant rant = new Rant();
		rant.setEmotion(params.getString("emotion"));
		rant.setQuestion(params.getString("question"));
		rant.setRant(params.getString("rant"));
		rant.setRanter(params.getString("ranter"));
		rant.setLocation(params.getString("location"));
		return rant;
	}
}