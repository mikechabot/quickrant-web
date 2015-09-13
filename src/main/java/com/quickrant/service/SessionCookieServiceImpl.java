package com.quickrant.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.UUID;

@Service
public class SessionCookieServiceImpl implements SessionCookieService {

    @Override
    public Cookie createCookieWithRandomValue(String key) {
        Cookie cookie = new Cookie(SessionServiceImpl.SESSION_COOKIE_NAME, String.valueOf(UUID.randomUUID()));
        cookie.setMaxAge(SessionServiceImpl.CACHE_ENTRY_EXPIRY_IN_DAYS *24*60*60);
        cookie.setPath("/");
        return cookie;
    }

}
