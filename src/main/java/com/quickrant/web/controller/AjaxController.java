package com.quickrant.web.controller;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.quickrant.api.Params;
import com.quickrant.api.models.Rant;
import com.quickrant.api.models.Rant.RantNotFoundException;
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
		addAction(null, new OffsetAction());
		addAction("/phonehome", new PhoneHomeAction());		
	}

	@Override
	protected Action defaultAction() {
		return new OffsetAction();
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
			if (existing != null) {
				Visitor incoming = new Visitor();
				incoming.fromMap(params.getMap());
				log.info(incoming.toString());
				completeVisitor(existing, incoming, newCookie.getValue());
				existing.saveIt();
				
				/* Send the cookie back to the browser */
				response.addCookie(newCookie);
				return basePath();	
			} else {
				log.warn("IP address (" + params.getIpAddress()	+ ") is phoning home, but we can't find the existing cookie");
				response.sendError(500);
				return basePath();
			}
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
	
	public class OffsetAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			long offset = getOffset(request.getPathInfo());
			if (offset > 0) {				
				try {
					List<Rant> rants = Rant.getBySql(getSql(offset));
					request.setAttribute("rants", rants);
					request.setAttribute("minId", rants.get(rants.size()-1).getId());
					return basePath() + "/rants.jsp";
				} catch (RantNotFoundException ex) {
					log.info("Visitor " + request.getRemoteAddr() + " reached the end of the line");
					return basePath() + "/no-rants.jsp";
				}
			} else {
				log.warn("IP address (" + request.getRemoteAddr() + ") is attempting an offset with an malformed request: /" + request.getPathInfo());
				response.sendError(500);
				return basePath();
			}
		}
	}
	
	public long getOffset(String path) {
		if (path.matches("\\/([0-9]+)$")) {
			return Long.valueOf(path.replace("/", ""));
		} else {
			return -1L;
		}
	}
	
	public String getSql(long offset) {
		return RantController.GET_RANTS_OFFSET.replace("?", String.valueOf(offset));
	}
}
