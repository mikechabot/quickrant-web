package com.quickrant.domain;

import com.quickrant.model.MongoDocument;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.servlet.http.Cookie;
import javax.validation.constraints.NotNull;

@Document
public class Session extends MongoDocument {

    @NotNull
    private Cookie cookie;

    @NotNull
    private String ipAddress;

    @NotNull
    private String userAgent;

    public Session(Cookie cookie, String ipAddress, String userAgent) {
        this.cookie = cookie;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
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
}
