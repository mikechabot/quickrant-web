package com.quickrant.rave.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.quickrant.rave.Controller;
import com.quickrant.rave.Params;
import com.quickrant.rave.model.Rant;
import com.quickrant.rave.service.RantService;

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
			log.debug("GetAction().action=" + action);
			
			/* Match root (/rant/) */
			if (action == null || action.equals("") || action.equals("/")) {
				List<Rant> rants = RantService.fetchRants();
				request.setAttribute("rants", rants);
				return basePath() + "/index.jsp";
			}

			/* Match /rant/[number] */
			if (action.matches("\\/([0-9]+)$")) {
				int id = Integer.valueOf(action.replaceAll("/", ""));
				Rant rant = RantService.fetchRant(id);
				request.setAttribute("rant", rant);
				return basePath() + "/index.jsp";
			} else {
				response.sendError(404);
				return null;
			}
			
		}		
	}
	
	public class PostAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			
			/* No GET requests allowed */
			Params params = new Params(request);
			if (params.isGet()) {
				response.sendRedirect(basePath() + "/index.jsp");
				return null;
			}

			/* Get the rant from the form */
			Map<String, String> map = params.getMap();
			Rant rant = new Rant();
			rant.fromMap(map);
			setDefaults(rant);
	
			if (rant.isValid()) {
				RantService.saveRant(rant);
				request.getSession().setAttribute("success", true);
			} else {
				request.getSession().setAttribute("success", false);
			}

			response.sendRedirect(request.getContextPath()+"/"+basePath());
			return null;
		}
		
		/**
		 * Anonymous, Earth
		 * @param rant
		 */
		private void setDefaults(Rant rant) {
			if (rant.getRanter() == null || rant.getRanter().isEmpty()) {
				rant.set("ranter", "Anonymous");
			}
			if (rant.getLocation() == null || rant.getLocation().isEmpty()) {
				rant.set("location", "Earth");
			}
		}
		
	}
	
	public class AjaxAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
//		// No GET requests allowed
//		Params params = new Params(request);
//		if (params.isGet()) {
//			response.sendRedirect(basePath() + "/index.jsp");
//			return null;
//		}		
//
//		if(!RanterService.isComplete(params)) {			
//			RanterService.updateRanter(new Ranter().parse(params));
//			Cookie cookie = CookieService.updateCookie(params.getCookie(CookieService.getCookieName()));
//			response.addCookie(cookie);
//		} 
		return basePath();
		}		
	}
	
}