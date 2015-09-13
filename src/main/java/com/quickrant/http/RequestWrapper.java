package com.quickrant.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class RequestWrapper {

    private HttpServletRequest request;
    private HttpServletResponse response;

    public RequestWrapper(HttpServletRequest request) {
        new RequestWrapper(request, null);
    }

    public RequestWrapper(HttpServletResponse response) {
        new RequestWrapper(null, response);
    }

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

    public HttpMethod getMethod() {
       return HttpMethod.valueOf(request.getMethod().toUpperCase());
    }

    public boolean isGet() {
        return getMethod().equals(HttpMethod.GET);
    }

    public boolean isPost() {
        return getMethod().equals(HttpMethod.POST);
    }

    public boolean isPut() {
        return getMethod().equals(HttpMethod.PUT);
    }

    public boolean isDelete() {
        return getMethod().equals(HttpMethod.DELETE);
    }

    public String getIpAddress() {
        return request.getRemoteAddr();
    }

    public String getUserAgent() {
        return request.getHeader("User-Agent");
    }

    public String getHeader(String name) {
        return request.getHeader(name);
    }

    public Enumeration<String> getHeaderNames() {
        return request.getHeaderNames();
    }

    public Enumeration<String> getHeaders(String name) {
        return request.getHeaders(name);
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
