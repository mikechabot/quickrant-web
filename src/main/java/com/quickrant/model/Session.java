package com.quickrant.model;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.servlet.http.Cookie;

@Document
public class Session extends AbstractEntity {

    private Cookie cookie;
    private String ipAddress;
    private String userAgent;

    private boolean active;

    public Session(Cookie cookie, String ipAddress, String userAgent) {
        this.cookie = cookie;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.active = true;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public String getSessionKey() {
        return cookie != null ? cookie.getValue() : null;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
