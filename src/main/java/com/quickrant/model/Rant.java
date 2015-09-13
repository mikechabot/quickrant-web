package com.quickrant.model;

import com.quickrant.domain.Comment;
import com.quickrant.domain.Emotion;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Document
public class Rant extends MongoDocument {

    @NotNull
    private String name;

    @NotNull
    private String location;

    @NotNull
    private String text;

    @NotNull
    private Emotion emotion;

    @NotNull
    private String question;

    @NotNull
    private String cookie;

    @NotNull
    private String userAgent;

    private List<String> hashtags;
    private boolean allowComments;
    private long commentCount;
    private List<Comment> comments;

    public Rant() {
        //
    }

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

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
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

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
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

}


