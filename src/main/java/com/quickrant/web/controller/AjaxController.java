package com.quickrant.web.controller;

import javax.servlet.ServletConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quickrant.api.Params;
import com.quickrant.api.models.Visitor;
import com.quickrant.api.services.VisitorService;
import com.quickrant.web.Controller;
import com.quickrant.web.cache.CookieCache;
import com.quickrant.web.utils.Utils;

public class AjaxController extends Controller {

	private static final long serialVersionUID = 1L;
	
	private VisitorService visitorSvc;
	private CookieCache cache;
	
	@Override
	protected String basePath() { return ""; }
	
	@Override
	protected void initActions(ServletConfig config) {
		/* Get a copy of the cache */
		cache = CookieCache.getCache();
		
		/* Add dependencies */
		setVisitorService(config.getInitParameter("visitor-service"));
		
		/* Add servlet actions */
		addAction(null, new DefaultAction());
		addAction("/phonehome", new PhoneHomeAction());
	}

	@Override
	protected Action defaultAction() {
		return new DefaultAction();
	}
	
	private void setVisitorService(String visitorSvcClass) {
		visitorSvc = (VisitorService) Utils.newInstance(visitorSvcClass);
	}

	public class DefaultAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			return basePath();		
		}
	}
	
	/**
	 * Return a new cookie to the site visitor 
	 * when the AJAX request is received
	 */
	public class PhoneHomeAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

			/* No GETs allowed */
			Params params = new Params(request);
			if (params.isGet()) {
				response.sendRedirect(basePath() + "/index.jsp");
				return null;
			}			
		
			/* Get the old cookie from the request */
			String oldCookie = params.getCookie(CookieCache.name).getValue();

			/* Generate an updated cookie, and update the cache */
			Cookie newCookie = cache.getCookie(oldCookie + "*");
			cache.updateByValue(oldCookie, newCookie.getValue());
			
			/* Update existing Visitor associated to the cookie */
			Visitor existing = (Visitor) visitorSvc.fetchFirst("cookie = ?", oldCookie);
			visitorSvc.completeVisitor(existing, params, newCookie.getValue());
			
			/* Send the cookie back to the browser */
			response.addCookie(newCookie);
			return basePath();			
		}		
	}
}
