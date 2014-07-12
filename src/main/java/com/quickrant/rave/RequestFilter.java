package com.quickrant.rave;

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

import com.quickrant.rave.service.AegisService;
import com.quickrant.rave.service.CookieService;
import com.quickrant.rave.service.VisitorService;

public class RequestFilter implements Filter {

	private static Logger log = Logger.getLogger(RequestFilter.class);

	@Override
	public void init(FilterConfig filter) {
		log.info("Initializing filter");
	}

	@Override
	public void destroy() {
		log.info("Destroying filter");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		//* Require the client to request fresh content */
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // Proxies	
		
		/* Shield against assholes */
		if (AegisService.shieldAgainstRequest(request, response)) {
			response.sendError(403);
			return;
		}

		/* If the GET didn't contain a cookie, attach one */
		Params params = new Params(request);
		if (!CookieService.existsInCache(params.getCookies())) {
			if (params.isGet()) {
				Cookie cookie = VisitorService.addVisitor(params);
				response.addCookie(cookie);
			}
		}

		chain.doFilter(request, response);
	}

}