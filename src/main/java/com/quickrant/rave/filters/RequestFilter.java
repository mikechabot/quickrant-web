package com.quickrant.rave.filters;

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

import org.apache.log4j.Logger;

import com.quickrant.rave.Params;
import com.quickrant.rave.cache.CookieCache;
import com.quickrant.rave.services.VisitorService;
import com.quickrant.rave.utils.Utils;

public class RequestFilter implements Filter {

	private static Logger log = Logger.getLogger(RequestFilter.class);

	private VisitorService visitorSvc;
	private CookieCache cache;

	@Override
	public void init(FilterConfig config) {
		log.info("Initializing filter");
		/* Get a copy of the cache */
		cache = (CookieCache) CookieCache.getCache();
		
		/* Load dependencies */
		setVisitorService(config.getInitParameter("visitor-service"));
	}

	@Override
	public void destroy() {
		log.info("Destroying filter");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		/* Set response headers to 'no-cache' */
		setResponseHeaders(response);
		
		/* If necessary, attach a cookie to the response */
		Params params = new Params(request);
		if (!cache.containsValue(params.getCookieValue(CookieCache.name))) {
			Cookie cookie = cache.newCookie();
			visitorSvc.save(params, cookie);
			response.addCookie(cookie);
		}
		
		chain.doFilter(request, response);
	}

	private void setVisitorService(String visitorSvcClass) {
		visitorSvc = (VisitorService) Utils.newInstance(visitorSvcClass);
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