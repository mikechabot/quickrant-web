package com.quickrant.service;

import javax.servlet.http.Cookie;

public interface SessionCookieService {

    public Cookie createCookieWithRandomValue(String key);

}
