package com.quickrant.security;

import java.io.IOException;

import com.quickrant.model.Session;
import com.quickrant.http.HttpMethod;
import com.quickrant.http.RequestWrapper;

import com.quickrant.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@Component
public class AegisFilter extends OncePerRequestFilter implements Filter {

    private static Logger log = Logger.getLogger(AegisFilter.class);

    @Autowired
    private SessionService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        RequestWrapper wrapper = new RequestWrapper(request, response);
        wrapper.setNoCacheResponse();

        HttpStatus status = getStatusOfRequest(wrapper);

        switch(status) {
            case OK:
                // Do nothing
                break;
            case FORBIDDEN:
                response.sendError(status.value());
                break;
            default:
                response.sendError(status.value());

        }

        filterChain.doFilter(request, response);
    }

    /**
     * Process a request based on method type
     * @param wrapper
     * @return the status of the request
     */
    private HttpStatus getStatusOfRequest(RequestWrapper wrapper) {
        HttpMethod method = wrapper.getMethod();
        HttpStatus status;
        switch(method) {
            case GET:
                status = processGet(wrapper);
                break;
            case POST:
                status = processPost(wrapper);
                break;
            default:
                status = HttpStatus.NOT_IMPLEMENTED;
                break;
        }
        return status;
    }

    private HttpStatus processPost(RequestWrapper wrapper) {
        if (hasSession(wrapper)) {
            return HttpStatus.OK;
        } else {
            log.info("POST attempted without cookies: " + wrapper.getIpAddress() + " -- " + wrapper.getUserAgent());
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
            Session session = sessionService.createSession(wrapper.getIpAddress(), wrapper.getUserAgent());
            wrapper.getResponse().addCookie(session.getCookie());
        }
        return HttpStatus.OK;
    }

    /**
     * Determine if the request is attached to an active session
     * @param wrapper
     * @return
     */
    public boolean hasSession(RequestWrapper wrapper) {
        Cookie cookie = wrapper.getCookie(sessionService.getCacheName());
        return cookie != null ?  isValid(cookie) : false;
    }

    /**
     * Check the cache for the cookie
     * @param cookie
     * @return
     */
    private boolean isValid(Cookie cookie) {
        return sessionService.exists(cookie.getValue());
    }

}
