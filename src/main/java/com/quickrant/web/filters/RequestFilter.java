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

import org.apache.log4j.Logger;

import com.quickrant.api.Params;
import com.quickrant.api.models.Visitor;
import com.quickrant.web.cache.CookieCache;

public class RequestFilter implements Filter {

	private static Logger log = Logger.getLogger(RequestFilter.class);
	
	private CookieCache cache;

	@Override
	public void init(FilterConfig config) {
		log.info("Initializing filter");
		/* Get a copy of the cache */
		cache = (CookieCache) CookieCache.getCache();
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
		
		/* If necessary, create a new Visitor and attach a cookie to the response */
		Params params = new Params(request);
		String value = params.getCookieValue(CookieCache.name);
		if (!cache.containsValue(value)) {
			Cookie cookie = cache.newCookie();
			response.addCookie(cookie);
			Visitor visitor = new Visitor();
			visitor.setCookie(cookie.getValue());
			visitor.setIpAddress(params.getIpAddress());
			visitor.setUserAgent(params.getUserAgent());
			visitor.setFingerprint(getFingerprint(params));
			visitor.setComplete(false);
			visitor.saveIt();
			value = visitor.getCookie();
		}
		request.setAttribute("cookieValue", value);
		chain.doFilter(request, response);
	}

	/**
	 * Generate a fingerprint
	 * @param params
	 * @return
	 */
	private String getFingerprint(Params params) {
		return params.getIpAddress() + ":" + params.getUserAgent();
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