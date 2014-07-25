package com.quickrant.web.controller;

import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.quickrant.api.Params;
import com.quickrant.api.models.Rant;
import com.quickrant.api.models.Visitor;
import com.quickrant.api.services.EmotionService;
import com.quickrant.api.services.QuestionService;
import com.quickrant.api.services.RantService;
import com.quickrant.api.services.VisitorService;
import com.quickrant.web.Controller;
import com.quickrant.web.cache.CookieCache;
import com.quickrant.web.security.Aegis;
import com.quickrant.web.utils.Utils;

public class RantController extends Controller {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(RantController.class);
	
	private RantService rantSvc;
	private EmotionService emotionSvc;
	private VisitorService visitorSvc;
	private QuestionService questionSvc;
	private Aegis aegis;
	
	@Override
	protected String basePath() { return ""; }
	
	@Override
	protected void initActions(ServletConfig config) {
		log.info("Initializing controller");

		/* Load dependencies */
		setRantService(config.getInitParameter("rant-service"));
		setEmotionService(config.getInitParameter("emotion-service"));
		setVisitorService(config.getInitParameter("visitor-service"));
		setQuestionService(config.getInitParameter("question-service"));
		
		/* Initialize Aegis */
		aegis = new Aegis(CookieCache.getCache(), visitorSvc);
				
		/* Add servlet actions */
		addAction(null, new DelegateAction());
	}	

	@Override
	protected Action defaultAction() {
		return new DelegateAction();
	}

	private void setRantService(String rantSvcClass) {
		rantSvc = (RantService) Utils.newInstance(rantSvcClass);
	}	
	
	private void setEmotionService(String emotionSvcClass) {
		emotionSvc = (EmotionService) Utils.newInstance(emotionSvcClass);
	}
	
	private void setVisitorService(String visitorSvcClass) {
		visitorSvc = (VisitorService) Utils.newInstance(visitorSvcClass);
	}

	public void setQuestionService(String questionSvcClass) {
		questionSvc = (QuestionService) Utils.newInstance(questionSvcClass);		
	}

	/**
	 * Dispatch request based on method type
	 * @author Mike
	 *
	 */
	public class DelegateAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String method = request.getMethod().toUpperCase();
			
			if (method.equals("GET")) return new GetAction().execute(request, response);
			if (method.equals("POST")) return new PostAction().execute(request, response);
			
			/* We really should get here, but if we do, just redirect back home */
			response.sendRedirect(request.getContextPath() + "/" + basePath());
			return null;
		}
	}

	/**
	 * Handle GET requests to /rant
	 */
	public class GetAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String action = request.getPathInfo();
			
			/* Match action to root (/rant/) or /rant/[number] */
			if (action == null || action.equals("") || action.equals("/")) {
				request.setAttribute("questions", questionSvc.fetch());
				request.setAttribute("emotions", emotionSvc.fetch());
				request.setAttribute("rants", rantSvc.fetch());
			} else if (action.matches("\\/([0-9]+)$")) {
				Rant rant = (Rant) rantSvc.fetchById(getId(action));
				if (rant == null) { 
					response.sendError(404); 
					return null;
				}
				request.setAttribute("rant", rant);
			}
			return basePath() + "/index.jsp";
		}
		
		private int getId(String action) {
			return Integer.valueOf(action.replaceAll("/", ""));
		}		
	}

	/**
	 * Handle POST requests to /rant
	 */
	public class PostAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			Params params = new Params(request);
			
			/* Get existing visitor from the request */
			Visitor visitor = visitorSvc.getExistingVisitorFromCookie(params.getCookieValue(CookieCache.name));
			
			/* Reject POST if the visitor object isn't complete */
			if (aegis.protectFromIncompleteVisitor(visitor, params)) {
				response.sendError(403);
				return null;
			}
			
			/* Stuff the cookie in the map to be parsed out later */
			Map<String, String> map = params.getMap();
			map.put("cookie", visitor.getCookie());
			
			/* Save the rant */
			if (rantSvc.save(map)) {
				request.getSession().setAttribute("success", true);
			} else {
				request.getSession().setAttribute("success", false);
			}
			
			response.sendRedirect(request.getContextPath() + "/" + basePath());
			return null;
		}

	}
	
}