package com.quickrant.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quickrant.web.service.SessionService;
import org.apache.log4j.Logger;

import com.quickrant.web.security.AegisService;
import com.quickrant.web.utils.Utils;

/**
 * Shield against certain HTTP requests
 * 
 */
public class AegisFilter implements Filter {

	private static Logger log = Logger.getLogger(AegisFilter.class);

	private AegisService aegisService;

	@Override
	public void init(FilterConfig config) {
		log.info("Initializing filter");
		/* Load dependencies */
		setAegisService(config.getInitParameter("aegis"));
		setCache(SessionService.getInstance());
	}

	private void setAegisService(String aegisClass) {
		aegisService = (AegisService) Utils.newInstance(aegisClass);
	}
	
	private void setCache(SessionService cache) {
		aegisService.setSessionService(cache);
	}

	@Override
	public void destroy() {
		log.info("Destroying filter");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
		/* Deny the request if necessary */
		if (aegisService.denyRequest(request)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		chain.doFilter(request, response);
	}

}
