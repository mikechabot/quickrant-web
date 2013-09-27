package com.chabot.quickrant.controller;

import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.chabot.quickrant.Controller;
import com.chabot.quickrant.Params;
import com.chabot.quickrant.model.Rant;
import com.chabot.quickrant.service.RantService;

public class RantController extends Controller {

	private static Logger log = Logger.getLogger(RantController.class);
	
	private static final long serialVersionUID = 1L;

	@Override
	protected String basePath() { return ""; }	
	
	@Override
	protected void initActions() {
		addAction("/get", new GetAction());
		addAction("/post", new PostAction());
	}

	@Override
	protected Action defaultAction() {
		return new GetAction();
	}
	
	public class GetAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Params params = new Params(request);
		if (params.isPost()) throw new ServletException("This action only responds to GET requests");
		
		String action = request.getPathInfo();
		
		if(action == null || action.equals("") || action.equals("/")) {
			try {				
				request.setAttribute("rants", RantService.getRants());
			} catch (SQLException e) {
				log.error("Error fetching rants: " + e.getMessage());
				request.setAttribute("success", false);
			}		
		} else if (action.matches("\\/([0-9]+)$")) {
			try {
				request.setAttribute("rant", RantService.getRant(action.replaceAll("/", "")));
			} catch (SQLException e) {
				log.error("Error fetching rant: " + e.getMessage());
				request.setAttribute("success", false);
			}			
		}		
		return basePath() + "/index.jsp";
		}		
	}
	
	public class PostAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			Params params = new Params(request);
			if (params.isGet()) throw new ServletException("This action only responds to POST requests");
			
			Cookie[] cookies = request.getCookies();
			if (cookies == null || cookies.length == 0) {
				return basePath() + "/error.jsp";
			} 
			
			Rant rant = new Rant().parse(params);			
			log.debug(rant.toString());
					
			if (rant.isValid()) {				
				try {
					RantService.postRant(rant);
					request.getSession().setAttribute("success", true);
				} catch (SQLException e) {
					log.error("error inserting rant: " + e.getMessage());
					request.getSession().setAttribute("success", false);
				}
				response.sendRedirect(request.getContextPath()+"/"+basePath());
				return null;				
			}
			request.setAttribute("action", basePath() + "/post");
			request.setAttribute("rant", rant); //now check to see if comment has error in the view
			return basePath() + "/index.jsp";
		}		
	}
}