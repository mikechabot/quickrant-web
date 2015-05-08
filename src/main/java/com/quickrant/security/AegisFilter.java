package com.quickrant.security;

import com.quickrant.model.Session;
import com.quickrant.service.SessionService;
import com.quickrant.util.RequestWrapper;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class AegisFilter extends OncePerRequestFilter implements Filter {

    private static Logger log = Logger.getLogger(AegisFilter.class);

    private final String COOKIE_NAME = "quickrant-uuid";

    private SessionCache sessionCache = SessionCache.INSTANCE;

    @Autowired
    SessionService sessionService;

    // Used for logging
    private String IpAddress;
    private String UserAgent;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        RequestWrapper wrapper = new RequestWrapper(request, response);
        wrapper.setNoCacheResponse();

        IpAddress = wrapper.getIpAddress();
        UserAgent = wrapper.getUserAgent();

        HttpStatus status = processRequest(wrapper);

        if (status.equals(HttpStatus.OK)) {
            filterChain.doFilter(request, response);
        } else {
            response.sendError(status.value());
            return;
        }

    }

    /**
     * Process a request based on method type
     * @param wrapper
     * @return the status of the request
     */
    private HttpStatus processRequest(RequestWrapper wrapper) {
        HttpStatus status;
        switch(wrapper.getMethod()) {
            case "GET":
                status = processGet(wrapper);
                break;
            case "POST":
                status = processPost(wrapper);
                break;
            default:
                status = HttpStatus.NOT_IMPLEMENTED;
                break;
        }
        return status;
    }

    private HttpStatus processPost(RequestWrapper wrapper) {
        Cookie cookie = wrapper.getCookie("quickrant-uuid");
        if (cookie != null) {
            return HttpStatus.OK;
        } else {
            log("POST attempted without cookies");
            return HttpStatus.FORBIDDEN;
        }
    }

    /**
     * Attach sessions to /GET requests
     * @param wrapper
     * @return
     */
    private HttpStatus processGet(RequestWrapper wrapper) {
        if (!hasSession(wrapper)) {
            String cookieValue = createCookie(wrapper);
            createSession(wrapper, cookieValue);
            log("Session created " + cookieValue);
        }
        return HttpStatus.OK;
    }

    /**
     * Determine if the request is attached to an active session
     * @param wrapper
     * @return
     */
    private boolean hasSession(RequestWrapper wrapper) {
        Cookie cookie = wrapper.getCookie(COOKIE_NAME);
        if (cookie != null) {
            return sessionCache.contains(cookie.getValue());
        }
        return false;
    }

    /**
     * Create a Cookie and attach it to the response
     * @param wrapper
     * @return the value of the cookie
     */
    private String createCookie(RequestWrapper wrapper) {
        String uuid = String.valueOf(UUID.randomUUID());
        Cookie cookie = getCookie(uuid);
        wrapper.getResponse().addCookie(cookie);
        return uuid;
    }


    /**
     * Create a session, add it to the database, then save it to the cache
     * @param wrapper
     * @param cookieValue
     */
    private void createSession(RequestWrapper wrapper, String cookieValue) {
        Session session = new Session();
        session.setIpAddress(wrapper.getIpAddress());
        session.setUserAgent(wrapper.getUserAgent());
        session.setCookieValue(cookieValue);
        sessionService.save(session);
        sessionCache.add(session);
    }

    /**
     * Create a new cookie
     * @param value
     * @return the cookie
     */
    private Cookie getCookie(String value) {
        Cookie cookie = new Cookie(COOKIE_NAME, value);
        cookie.setMaxAge(30*24*60*60);  // 1 month
        cookie.setPath("/");
        return cookie;
    }

    /**
     * Log a message with some additional details
     * @param message
     */
    private void log(String message) {
        log.info(message + ": " + IpAddress + " -- " + UserAgent);
    }

}
