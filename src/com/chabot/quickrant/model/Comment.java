package com.chabot.quickrant.model;

import java.util.Date;

import com.chabot.quickrant.Model;
import com.chabot.quickrant.Params;
import com.chabot.quickrant.Require;

public class Comment extends Model<Comment> {
		
	private String id;
	private String emotion;
	private Date created;
	private String commenter;
	private String location;

	@Require
	private String comment;
		
	public Comment() {}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getEmotion() {
		return emotion;
	}
	
	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}
	
	public String getCommenter() {
		return commenter;
	}
	
	public void setCommenter(String commenter) {
		this.commenter = commenter;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	@Override
	public String toString() {
		return "Comment [id=" + id + ", comment=" + comment + ", emotion="
				+ emotion + ", commenter=" + commenter + ", location="
				+ location + ", created=" + created + "]";
	}
	
	@Override
	public Comment parse(Params params) {
		Comment comment = new Comment();
		comment.comment = params.getString("comment");
		comment.commenter = params.getString("commenter");
		comment.location = params.getString("location");
		return comment;
	}
}