package com.quickrant.model;

import com.quickrant.domain.Comment;
import com.quickrant.domain.Emotion;

import com.quickrant.model.MongoDocument;
import org.springframework.data.mongodb.core.mapping.Document;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

@Document
public class Rant extends MongoDocument {

    private String name;
    private String location;
    private String text;
    private Emotion emotion;
    private String question;
    private String cookieValue;
    private String userAgent;
    private String ipAddress;
    private boolean allowComments;
    private long commentCount;
    private List<String> hashtags;
    private List<Comment> comments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCookieValue() {
        return cookieValue;
    }

    public void setCookieValue(String cookieValue) {
        this.cookieValue = cookieValue;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public boolean isAllowComments() {
        return allowComments;
    }

    public void setAllowComments(boolean allowComments) {
        this.allowComments = allowComments;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        if (comment == null) throw new IllegalArgumentException ("Comment cannot be null");
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comment.setCreatedTime(new DateTime());
        comments.add(comment);
        commentCount++;
    }

    @Override
    public String toString() {
        return "Rant{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", text='" + text + '\'' +
                ", emotion=" + emotion +
                ", question='" + question + '\'' +
                ", cookieValue='" + cookieValue + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", allowComments=" + allowComments +
                ", commentCount=" + commentCount +
                ", hashtags=" + hashtags +
                ", comments=" + comments +
                '}';
    }
}


