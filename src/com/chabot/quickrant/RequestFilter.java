package com.chabot.quickrant;

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

import com.chabot.quickrant.service.CookieService;
import com.chabot.quickrant.service.RanterService;

public class RequestFilter implements Filter {

	private static Logger log = Logger.getLogger(RequestFilter.class);
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
	    HttpServletResponse response = (HttpServletResponse) res;
	       
	    Params params = new Params(request);
	    Cookie[] cookies = params.getCookies();
	    	    
		if (params.isGet() && !CookieService.cookieExists(cookies)) {
			Cookie cookie = CookieService.createCookie();
			RanterService.createRanter(cookie);
			response.addCookie(cookie);	
		} else if (params.isPost() && !CookieService.cookieExists(cookies)) {
			log.info(request.getRemoteAddr() + " attempted a POST without a 'quickrant-uid' cookie");
			response.sendError(403);
			return;
		}
		chain.doFilter(request, response);
	}
	
	@Override
	public void init(FilterConfig filter) {
		log.info("Initializing filter");
	}	
	
	@Override
	public void destroy() {
		log.info("Destroying filter");
	}
}