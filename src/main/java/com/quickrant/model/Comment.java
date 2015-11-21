package com.quickrant.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Comment extends AbstractEntity {

    public String name;
    public String location;
    private String text;

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

    @Override
    public String toString() {
        return "Comment{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

}
