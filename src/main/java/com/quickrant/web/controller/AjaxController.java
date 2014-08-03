package com.quickrant.web.controller;

import javax.servlet.ServletConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.quickrant.api.Params;
import com.quickrant.api.models.Visitor;
import com.quickrant.web.Controller;
import com.quickrant.web.cache.CookieCache;

@SuppressWarnings("serial")
public class AjaxController extends Controller {

	private static Logger log = Logger.getLogger(AjaxController.class);
	
	private CookieCache cache;
	
	@Override
	protected String basePath() { return ""; }
	
	@Override
	protected void initActions(ServletConfig config) {
		log.info("Initializing controller");
		
		/* Get a copy of the cache */
		cache = CookieCache.getCache();		
		
		/* Add servlet actions */
		addAction(null, new DefaultAction());
		addAction("/phonehome", new PhoneHomeAction());
	}

	@Override
	protected Action defaultAction() {
		return new DefaultAction();
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
			
			/* Get the cookie, modify it, and update the cache */
			String oldCookie = params.getCookie(CookieCache.name).getValue();		
			Cookie newCookie = cache.getCookie(oldCookie + "*");
			cache.updateByValue(oldCookie, newCookie.getValue());
			
			/* Retrieve the existing visitor record, and update it */
			Visitor existing = Visitor.findFirst("cookie = ?", oldCookie);
			Visitor incoming = new Visitor();
			incoming.fromMap(params.getMap());
			completeVisitor(existing, incoming, newCookie.getValue());
			existing.saveIt();
			
			/* Send the cookie back to the browser */
			response.addCookie(newCookie);
			return basePath();			
		}
		
		/**
		* Complete the visitor by adding additional client-side information
		* @param existing
		* @param params
		* @param cookie
		*/
		public void completeVisitor(Visitor existing, Visitor incoming, String cookieValue) {
			/* Update the existing record with request data */
			existing.setScreenColor(incoming.getScreenColor());
			existing.setScreenHeight(incoming.getScreenHeight());
			existing.setScreenWidth(incoming.getScreenWidth());
			existing.setFingerprint(getFingerprint(existing, incoming));	
			existing.setCookie(cookieValue);
			existing.setComplete(true);
		}
		
		/**
		* Build a visitor fingerprint:
		*
		* "0:0:0:0:0:0:0:1:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36
		* (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36"
		*
		* @param Visitor
		* @param existing
		* @return String
		*/
		private String getFingerprint(Visitor existing, Visitor incoming) {
			StringBuilder sb = new StringBuilder();
			sb.append(existing.getFingerprint() + ":");
			sb.append(incoming.getScreenHeight() + ":");
			sb.append(incoming.getScreenWidth() + ":");
			sb.append(incoming.getScreenColor());
			return sb.toString();
		}
	}
}
