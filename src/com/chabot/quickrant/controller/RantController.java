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
		addAction(null, new GetAction());
		addAction("/post", new PostAction());
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
			
			// No POST requests allowed
			Params params = new Params(request);
			if (params.isGet()) {
				response.sendRedirect(basePath() + "/index.jsp");
				return null;
			}
			
			Rant rant = new Rant().parse(params);			
			log.debug(rant.toString());
					
			if (rant.isValid()) {				
				try {
					RantService.persistRant(rant);
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