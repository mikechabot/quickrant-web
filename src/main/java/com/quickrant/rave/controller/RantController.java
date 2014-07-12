package com.quickrant.rave.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quickrant.rave.Controller;
import com.quickrant.rave.Params;
import com.quickrant.rave.model.Rant;
import com.quickrant.rave.service.RantService;

public class RantController extends Controller {

	private static final long serialVersionUID = 1L;

	@Override
	protected String basePath() { return ""; }
	
	@Override
	protected void initActions() {
		addAction(null, new DelegateAction());
	}

	@Override
	protected Action defaultAction() {
		return new DelegateAction();
	}
	
	public class DelegateAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String method = request.getMethod().toUpperCase();
			if (method.equals("GET")) {
				return new GetAction().execute(request, response);
			} else if (method.equals("POST")) {
				return new PostAction().execute(request, response);
			}
			response.sendRedirect(request.getContextPath()+"/"+basePath());
			return null;
		}		
	}
	
	/**
	 * Handle GET requests to /rant
	 * @author Mike
	 *
	 */
	public class GetAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			String action = request.getPathInfo();
			/* Match root (/rant/) */
			if (action == null || action.equals("") || action.equals("/")) {
				request.setAttribute("rants", RantService.fetchRants());
				return basePath() + "/index.jsp";
			}
			
			/* Match /rant/[number] */
			if (action.matches("\\/([0-9]+)$")) {
				int id = Integer.valueOf(action.replaceAll("/", ""));
				Rant rant = RantService.fetchRant(id);
				if (rant == null) { response.sendError(404); return null; }
				request.setAttribute("rant", rant);
			}
			
			return basePath() + "/index.jsp";
		}		
	}

	/**
	 * Handle POST requests to /rant
	 * @author Mike
	 *
	 */
	public class PostAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			
			/* No GET requests allowed */
			Params params = new Params(request);
			if (params.isGet()) {
				response.sendRedirect(basePath() + "/index.jsp");
				return null;
			}
			
			if (RantService.saveRant(params)) {
				request.getSession().setAttribute("success", true);
			} else {
				request.getSession().setAttribute("success", false);
			}

			response.sendRedirect(request.getContextPath()+"/"+basePath());
			return null;
		}
		
	}
	
}