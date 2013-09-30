package com.chabot.quickrant;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

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

import com.chabot.quickrant.service.RequestService;

public class RequestFilter implements Filter {

	private static Logger log = Logger.getLogger(RequestFilter.class);
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
	    HttpServletResponse response = (HttpServletResponse) res;
	    
	    Params params = new Params(request);
	    Cookie[] cookies = request.getCookies();
	    
		if (params.isGet() && cookies == null) {
			log.info(request.getRemoteAddr() + " added a cookie");
			response.addCookie(RequestService.getNewCookie());			
		} else if (params.isGet() && cookies != null){
			if (!RequestService.findCookie(cookies)) {
				log.info(request.getRemoteAddr() + " added a cookie");
		    	response.addCookie(RequestService.getNewCookie());
		    }
		} else if (params.isPost() && cookies == null) {
			log.info(request.getRemoteAddr() + " attempted a POST request without cookies");
			response.sendError(403);
			return;
		} else if (params.isPost() && cookies != null) {
			if (!RequestService.findCookie(cookies)) {
				log.info(request.getRemoteAddr() + " attempted a POST request without a proper quickrant-uid; cookies attempted");
				response.sendError(403);
				return;
			}
		}
		chain.doFilter(request, response);
	}
	
	@Override
	public void init(FilterConfig filter) throws ServletException {
		try {
			RequestService.getCookiesFromDatabase();
			log.info("initializing, found " + (RequestService.getCookies() != null ? RequestService.getCookies().size() : "0") + " preexisting cookie(s)");
		} catch (SQLException e) {
			log.error("Error pulling cookies: ", e);
		}
	}	
	
	@Override
	public void destroy() {
		try {
			RequestService.saveCookiesToDatabase();
			log.info("destroying, storing " + (RequestService.getCookies() != null ? RequestService.getCookies().size() : "0") + " preexisting cookie(s)");
		} catch (SQLException e) {
			log.error("Error storing cookies: ", e);
		}
	}
}
