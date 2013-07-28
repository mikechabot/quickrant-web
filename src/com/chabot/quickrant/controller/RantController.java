package com.chabot.quickrant.controller;

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
		addAction("/list", new ListAction());
		addAction("/view", new ViewAction());
		addAction("/post", new PostAction());
	}

	@Override
	protected Action defaultAction() {
		return new ListAction();
	}
	
	public class ListAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			request.getSession().setAttribute("rants", RantService.getRants());
			response.sendRedirect(request.getContextPath()+"/"+basePath());
			return null;
		}		
	}
	
	public class ViewAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			/*
			Params params = new Params(request);
			Person temp = new Person().parse(params);
			Database database = Database.getInstance();
			PersonService svc = new PersonService(database);

			Person person = svc.find(temp);
			if (person == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return null;
			}
			
			request.setAttribute("person", person);
			return basePath() + "/view.jsp";
			*/
			return null;
		}
	}	
	
	public class PostAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			Params params = new Params(request);
			if (params.isGet()) throw new ServletException("This action only responds to POST requests");
			
			Rant rant = new Rant().parse(params);			
			log.info(rant.toString());
			
			if (rant.isValid()) {				
				RantService.create(rant);
				request.getSession().setAttribute("success", "true");
				response.sendRedirect(request.getContextPath()+"/"+basePath());
				return null;				
			}
			request.setAttribute("action", basePath() + "/post");
			request.setAttribute("rant", rant); //now check to see if comment has error in the view
			return basePath() + "/index.jsp";
		}		
	}
}