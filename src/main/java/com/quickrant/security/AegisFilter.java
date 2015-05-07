package com.quickrant.security;

import com.quickrant.model.Session;
import com.quickrant.service.SessionService;
import com.quickrant.util.RequestWrapper;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
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

    private HttpStatus processRequest(RequestWrapper wrapper) {
        HttpStatus status;
        switch(wrapper.getMethod()) {
            case "GET":
                status = filterGet(wrapper);
                break;
            case "POST":
                status = filterPost(wrapper);
                break;
            default:
                status = HttpStatus.NOT_IMPLEMENTED;
                break;
        }
        return status;
    }

    private HttpStatus filterPost(RequestWrapper wrapper) {
        Cookie cookie = wrapper.getCookie("quickrant-uuid");
        if (cookie != null) {
            return HttpStatus.OK;
        } else {
            log("POST attempted without cookies");
            return HttpStatus.FORBIDDEN;
        }
    }

    private HttpStatus filterGet(RequestWrapper wrapper) {
        if (wrapper.getCookie(COOKIE_NAME) == null) {
            Session session = getNewSession(wrapper);
            sessionService.save(session);
            sessionCache.add(session);
            Cookie cookie = getCookie(session.getId());
            wrapper.getResponse().addCookie(cookie);
            log("Cookie created");
        }
        return HttpStatus.OK;
    }

    private Session getNewSession(RequestWrapper wrapper) {
        return new Session(wrapper.getIpAddress(), wrapper.getUserAgent());
    }

    private Cookie getCookie(String value) {
        Cookie cookie = new Cookie(COOKIE_NAME, value);
        cookie.setMaxAge(30*24*60*60);  // 1 month
        cookie.setPath("/");
        return cookie;
    }

    private void log(String message) {
        log.info(message + ": " + IpAddress + " -- " + UserAgent);
    }

}
