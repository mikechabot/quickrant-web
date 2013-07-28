package com.chabot.quickrant.model;

import java.util.Date;

import com.chabot.quickrant.Model;
import com.chabot.quickrant.Params;
import com.chabot.quickrant.Require;

public class Rant extends Model<Rant> {
		
	@Require
	private String rant;
	
	private String id;
	private String emotion;
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

	public String getRant() {
		return rant;
	}

	public void setRant(String rant) {
		this.rant = rant;
	}

	public String getEmotion() {
		return emotion;
	}
	
	public void setEmotion(String emotion) {
		this.emotion = emotion;
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
	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}
	
	@Override
	public String toString() {
		return "Rant [id=" + id + ", rant=" + rant + ", emotion="
				+ emotion + ", ranter=" + ranter + ", location="
				+ location + ", created=" + created + "]";
	}
	
	@Override
	public Rant parse(Params params) {
		Rant rant = new Rant();
		rant.emotion = params.getString("emotion");
		rant.rant = params.getString("rant");
		rant.ranter = params.getString("ranter");
		rant.location = params.getString("location");
		return rant;
	}
}