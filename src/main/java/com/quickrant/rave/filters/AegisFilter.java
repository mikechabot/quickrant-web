package com.quickrant.rave.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.quickrant.rave.Params;
import com.quickrant.rave.cache.CookieCache;
import com.quickrant.rave.security.Aegis;
import com.quickrant.rave.utils.Utils;

/**
 * Shield against certain HTTP requests
 * 
 */
public class AegisFilter implements Filter {

	private static Logger log = Logger.getLogger(AegisFilter.class);

	private Aegis aegis;

	@Override
	public void init(FilterConfig config) {
		log.info("Initializing filter");
		
		/* Load dependencies */
		setAegis(config.getInitParameter("aegis"));
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
		if (aegis.protectFrom(new Params(request))) {
			response.sendError(403);
			return;
		}
		
		chain.doFilter(request, response);
	}
	
	private void setAegis(String aegisClass) {
		aegis = (Aegis) Utils.newInstance(aegisClass);
		aegis.setCache(CookieCache.getCache());
	}

}
