package com.chabot.quickrant.controller;

import java.sql.SQLException;

import javax.servlet.ServletException;
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
		addAction("/getlonglist", new GetLongList());
		addAction("/postrant", new PostRant());
	}

	@Override
	protected Action defaultAction() {
		return new GetLongList();
	}
	
	public class GetLongList implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
					
		try {			
			request.getSession().setAttribute("longlist", RantService.getLongList());
		} catch (SQLException e) {
			request.getSession().setAttribute("success", false);
			log.error("Error getting rants: " + e.getMessage());
		}
		response.sendRedirect(request.getContextPath()+"/"+basePath());
		return null;	
		}		
	}
	
	public class PostRant implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			Params params = new Params(request);
			if (params.isGet()) throw new ServletException("This action only responds to POST requests");
			
			Rant rant = new Rant().parse(params);			
			log.debug(rant.toString());
					
			if (rant.isValid()) {				
				try {
					RantService.create(rant);
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