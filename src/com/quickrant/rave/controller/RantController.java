package com.quickrant.rave.controller;

import java.sql.SQLException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.quickrant.rave.Controller;
import com.quickrant.rave.Params;
import com.quickrant.rave.model.Rant;
import com.quickrant.rave.model.Ranter;
import com.quickrant.rave.service.*;

public class RantController extends Controller {

	private static Logger log = Logger.getLogger(RantController.class);
	
	private static final long serialVersionUID = 1L;

	@Override
	protected String basePath() { return ""; }	
	
	@Override
	protected void initActions() {
		addAction(null, new GetAction());
		addAction("/post", new PostAction());
		addAction("/ajax", new AjaxAction());
	}

	@Override
	protected Action defaultAction() {
		return new GetAction();
	}
	
	public class GetAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String action = request.getPathInfo();
		if(action == null || action.equals("") || action.equals("/")) {
			try {				
				request.setAttribute("rants", RantService.fetchRants());
			} catch (SQLException e) {
				log.error("Error fetching rants: " + e.getMessage());		
			}		
		} else if (action.matches("\\/([0-9]+)$")) {
			try {
				Rant rant = RantService.fetchRant(action.replaceAll("/", ""));
				if (rant != null) {
					request.setAttribute("rant", rant);	
				} else {
					response.sendError(404);
					return null;
				}
			} catch (SQLException e) {
				log.error("Error fetching rant: " + e.getMessage());		
			}			
		} 
		return basePath() + "/index.jsp";
		}		
	}
	
	public class PostAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			
			// No GET requests allowed
			Params params = new Params(request);
			if (params.isGet()) {
				response.sendRedirect(basePath() + "/index.jsp");
				return null;
			}
			
			Rant rant = new Rant().parse(params);			
			if (rant.isValid()) {				
				try {
					RantService.createRant(rant, params);
					request.getSession().setAttribute("success", true);
				} catch (SQLException e) {
					log.error("error inserting rant: " + e.getMessage());
					request.getSession().setAttribute("success", false);
				}
				response.sendRedirect(request.getContextPath()+"/"+basePath());
				return null;				
			}
			
			return basePath() + "/index.jsp";
		}		
	}
	
	public class AjaxAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// No GET requests allowed
		Params params = new Params(request);
		if (params.isGet()) {
			response.sendRedirect(basePath() + "/index.jsp");
			return null;
		}		

		if(!RanterService.isComplete(params)) {			
			RanterService.updateRanter(new Ranter().parse(params));
			Cookie cookie = CookieService.updateCookie(params.getCookie(CookieService.COOKIE_NAME));
			response.addCookie(cookie);
		} 
		return basePath();
		}		
	}
	
}