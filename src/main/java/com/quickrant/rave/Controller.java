package com.quickrant.rave;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public abstract class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private Action defaultAction;
	private Map<String, Action> actions = new HashMap<String, Action>();
		
	public void init(ServletConfig config) throws ServletException {
		initActions();
		defaultAction = defaultAction();
		if (defaultAction == null) throw new ServletException("A default action was not specified");
		System.out.println("Loaded Controller with Base Path of: /"+basePath());
	}
	
	protected abstract void initActions();
	protected abstract Action defaultAction();
	protected abstract String basePath();
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		String action = request.getPathInfo();
		String view = null;
		try {
			if (action == null || action.equals("") || action.equals("/") || action.matches("\\/([0-9]+)$")) {
				view = defaultAction.execute(request, response);
			} else if (actions.get(action) == null) {
				response.sendError(404);
			} else {
				view = actions.get(action).execute(request, response);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}		

		if (view != null && !view.equals("")) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/"+view);
			if (dispatcher == null) throw new ServletException("The view file (WEB-INF/views/"+view+") was not found!");			
			dispatcher.forward(request, response);
		}
	}
	
	public void addAction(String path, Action action) {
		actions.put(path, action);
	}
	
	public interface Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
	}
	
}