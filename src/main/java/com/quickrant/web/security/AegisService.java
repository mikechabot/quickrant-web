package com.quickrant.web.security;

import java.io.IOException;

import com.quickrant.web.controller.HttpMethod;
import com.quickrant.web.service.SessionService;
import org.apache.log4j.Logger;

import com.quickrant.api.Params;
import com.quickrant.api.models.Visitor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class AegisService {
	
	private static Logger log = Logger.getLogger(AegisService.class);
	
	private SessionService sessionService;

	public AegisService() { }
	
	public AegisService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	
	public void setSessionService(SessionService cookieCache) {
		sessionService = cookieCache;
	}
		
	/**
	 * Shield against certain HTTP requests
	 * @param request
	 * @return boolean
	 * @throws IOException
	 */
	public boolean denyRequest(HttpServletRequest request) throws IOException {
        /* This goes for all request types */
        if (isBanned(request)) return true;

        /* Check for certain circumstances based on request type */
        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        switch (method) {
            case POST:
                if (!sessionService.hasActiveSession(request)) {
                    log.warn("IP address (" + request.getRemoteAddr() + ") attempted a POST without a valid session");
                    return true;
                }
                break;
        }

		return false;
	}

    /**
	 * Deny access if IP is banned
	 * @param request
	 * @return
	 */
	private boolean isBanned(HttpServletRequest request) {
	    // check for ban status
		return false;
	}
	
	/**
	 * Deny access if the visitor object isn't complete
	 * @param params
	 * @return boolean
	 */
	public boolean protectFromIncompleteVisitor(Visitor visitor, Params params) {
		if (visitor == null) return true;
		if (!visitor.isComplete()) {
			log.warn("IP address (" + params.getIpAddress()	+ ") attempted a POST without completing the roundtrip");
			return true;
		}
		return false;
	}

}