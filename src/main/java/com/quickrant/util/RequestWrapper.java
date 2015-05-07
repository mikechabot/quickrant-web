package com.quickrant.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestWrapper {

    private HttpServletRequest request;
    private HttpServletResponse response;

    public RequestWrapper(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public String getMethod() {
        return request.getMethod().toUpperCase();
    }

    public boolean isGet() {
        return request.getMethod().equalsIgnoreCase("GET");
    }

    public boolean isPost() {
        return request.getMethod().equalsIgnoreCase("POST");
    }

    public boolean isPut() {
        return request.getMethod().equalsIgnoreCase("PUT");
    }

    public boolean isDelete() {
        return request.getMethod().equalsIgnoreCase("DELETE");
    }

    public String getIpAddress() {
        return request.getRemoteAddr();
    }

    public String getUserAgent() {
        return request.getHeader("User-Agent");
    }

    public Cookie[] getCookies() {
        return request.getCookies();
    }

    public String getCookieValue(String name) {
        if (name == null || name.isEmpty()) return null;
        if (getCookies() == null || getCookies().length == 0) return null;
        for (Cookie cookie : getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public Cookie getCookie(String name) {
        if (name == null || name.isEmpty()) return null;
        if (getCookies() == null || getCookies().length == 0) return null;
        for (Cookie cookie : getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

    public void setNoCacheResponse() {
        if (response != null) {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
        }
    }

}
