package com.quickrant.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Session extends MongoDocument {

    private String ipAddress;
    private String userAgent;

    public Session(String ipAddress, String userAgent) {
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}
