package com.quickrant.domain;

import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;

public class Comment {

    @NotNull
    public String name;

    @NotNull
    public String location;

    @NotNull
    private String text;

    @NotNull
    private DateTime createdTime;

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

    public DateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(DateTime createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", text='" + text + '\'' +
                ", createdTime=" + createdTime +
                '}';
    }

}
