package com.quickrant.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quickrant.web.service.SessionService;
import org.apache.log4j.Logger;

public class SessionFilter implements Filter {

	private static Logger log = Logger.getLogger(SessionFilter.class);
	
	private SessionService sessionService;

	@Override
	public void init(FilterConfig config) {
		log.info("Initializing SessionFilter");
		sessionService = SessionService.getInstance();
	}

	@Override
	public void destroy() {
		log.info("Destroying SessionFilter");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		/* Set response headers to 'no-cache' */
		setResponseHeaders(response);

		if (!sessionService.hasActiveSession(request)) {
            Cookie session = sessionService.newSession();
            response.addCookie(session);
            // TODO: create and save a new visitor (cookie, ip, user-agent, fingerprint, completed status)
        }

		chain.doFilter(request, response);
	}

	/**
	 * Require the client to request fresh content
	 * @param response
	 */
	private void setResponseHeaders(HttpServletResponse response) {		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // Proxies
	}

}