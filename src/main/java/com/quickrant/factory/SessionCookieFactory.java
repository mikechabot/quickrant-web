package com.quickrant.factory;

import com.quickrant.beans.SessionCacheProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.util.UUID;

@Component
public class SessionCookieFactory {

    @Autowired
    private SessionCacheProperties sessionCacheProperties;

    public Cookie getSessionCookie() {
        Cookie cookie = new Cookie(sessionCacheProperties.getCacheName(), String.valueOf(UUID.randomUUID()));
        cookie.setMaxAge(sessionCacheProperties.getCacheEntryExpiry()*24*60*60);
        cookie.setPath("/");
        return cookie;
    }

}
