package com.quickrant.model;

import org.springframework.beans.factory.annotation.Required;

public class Ranter {

    private String name;
    private String location;
    private String userAgent;

    public String getName() {
        return name;
    }

    @Required
    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    @Required
    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserAgent() {
        return userAgent;
    }

    @Required
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}
