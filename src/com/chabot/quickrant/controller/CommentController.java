package com.chabot.quickrant.controller;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.chabot.quickrant.Controller;
import com.chabot.quickrant.Params;
import com.chabot.quickrant.database.Database;
import com.chabot.quickrant.exception.ServiceException;
import com.chabot.quickrant.model.Comment;
import com.chabot.quickrant.service.CommentService;

public class CommentController extends Controller {

	private static Logger log = Logger.getLogger(CommentController.class);
	
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
			/*
			Database database = Database.getInstance();
			PersonService svc = new PersonService(database);			
			request.setAttribute("people", svc.findAll());
			return basePath() + "/list.jsp";
			*/
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
			
			Comment comment = new Comment().parse(params);			
			log.info(comment.toString());
			
			if (comment.isValid()) {				
				CommentService.create(comment);
				request.getSession().setAttribute("success", "true");
				response.sendRedirect(request.getContextPath()+"/"+basePath());
				return null;				
			}
			request.setAttribute("action", basePath() + "/post");
			request.setAttribute("comment", comment); //now check to see if comment has error in the view
			return basePath() + "/index.jsp";
		}		
	}

}