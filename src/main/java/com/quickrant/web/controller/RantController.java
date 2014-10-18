package com.quickrant.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.quickrant.api.Params;
import com.quickrant.api.models.Emotion;
import com.quickrant.api.models.Question;
import com.quickrant.api.models.Rant;
import com.quickrant.api.models.Rant.RantNotFoundException;
import com.quickrant.api.models.Visitor;
import com.quickrant.api.utils.TimeUtils;
import com.quickrant.web.Controller;
import com.quickrant.web.cache.CookieCache;
import com.quickrant.web.security.Aegis;

/**
 * RESTful controller that handles HTTP requests to /rant
 */
@SuppressWarnings("serial")
public class RantController extends Controller {

	private static Logger log = Logger.getLogger(RantController.class);
	
	public static final String RANT_SQL = "select id, created_at, emotion_id, question_id, rant, visitor_name, location from rants ";
	public static final String GET_RANTS = RANT_SQL + "order by id desc limit 20";
	public static String GET_RANTS_OFFSET = RANT_SQL + "where id < ? order by id desc limit 20";
	
	private Aegis aegis;
	
	@Override
	protected String basePath() { return ""; }
	
	@Override
	protected void initActions(ServletConfig config) {
		log.info("Initializing controller");	
		/* Initialize Aegis */
		aegis = new Aegis(CookieCache.getCache());				
		/* Add servlet actions */
		addAction(null, new DelegateAction());
	}	

	@Override
	protected Action defaultAction() {
		return new DelegateAction();
	}

	/**
	 * Dispatch request based on method type
	 *
	 */
	public class DelegateAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String method = request.getMethod().toUpperCase();
			
			if (method.equals("GET")) return new GetAction().execute(request, response);
			if (method.equals("POST")) return new PostAction().execute(request, response);
			
			/* We really shouldn't  get here, but if we do, just redirect back home */
			response.sendRedirect(request.getContextPath() + "/" + basePath());
			return null;
		}
	}

	/**
	 * Handle GET requests to /rant
	 */
	public class GetAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String path = request.getPathInfo();
			int action = getAction(path);
			switch(getAction(path)) {
			case -1: 
				response.sendError(404);
				return null;
			case 0:
				List<Rant> rants = Rant.findBySQL(GET_RANTS);
				request.setAttribute("minId", rants.get(rants.size()-1).getId());
				request.setAttribute("rants", rants);
				break;
			case 1:
				try {
					request.setAttribute("rant", Rant.getById(getId(path)));
				} catch (RantNotFoundException ex) {
					response.sendError(404);
					return null;
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown action: " + action + " via " + path);
			}
			request.setAttribute("questions", Question.findAll());
			request.setAttribute("emotions", Emotion.findAll());
			return basePath() + "/index.jsp";			
		}
		
		/**
		 * @param action
		 * Return scenarios:
		 *   Action		Return
		 *   (null)		0
		 *   /			0
		 *   /52		1
		 *   /52foo		-1
		 *   /foobar	-1
		 * @return int
		 */
		private int getAction(String action) {
			if (action == null || action.equals("/") || action.equals("")) {
				return 0;
			} else if (action.matches("\\/([0-9]+)$"))  {
				return 1;
			} else {
				return -1;
			}
		}
		
		/**
		 * Replace "/52" with 52
		 * @param action
		 * @return long
		 */
		private long getId(String action) {
			return Long.valueOf(action.replace("/", ""));
		}
	}

	/**
	 * Handle POST requests to /rant
	 */
	public class PostAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			Params params = new Params(request);			
			String cookie = params.getCookieValue(CookieCache.name);

			/* Reject the POST if the visitor isn't complete */
			Visitor visitor = getExistingVisitorFromCookie(cookie);
			if (aegis.protectFromIncompleteVisitor(visitor, params)) {
				response.sendError(403);
				return null;
			}

			/* Save the rant */
			if (saveRant(params.getMap(), cookie)) {
				request.getSession().setAttribute("success", true);
			} else {
				request.getSession().setAttribute("success", false);
			}
			
			response.sendRedirect(request.getContextPath() + "/" + basePath());
			return null;
		}
		
		public Visitor getExistingVisitorFromCookie(String cookie) {
			if (cookie == null || cookie.isEmpty()) return null;
			return (Visitor) Visitor.findFirst("cookie = ?", cookie);
		}
		
		// TODO: what is gods name are we doing here.
		public boolean saveRant(Map<String, String> map, String cookie) {
			Rant rant = new Rant();
			Visitor visitor = new Visitor();
			Emotion emotion = new Emotion();
			Question question = new Question();
			
			/* Parse objects from parameter map */
			rant.fromMap(map);
			visitor.fromMap(map);
			emotion.fromMap(map);
			question.fromMap(map);
			
			/* Scrub the text of any bullshit */
			scrubRant(rant);
			
			/* Set some defaults if necessary */
			setDefaults(rant);
			
			/* Fetch question and emotion */
			visitor = Visitor.findFirst("cookie = ?", cookie);
			emotion = Emotion.findFirst("emotion = ?", emotion.getEmotion());
			question = Question.findFirst("question = ?", question.getQuestion());
	
			/* Set last rant time */
			visitor.setLastRant(TimeUtils.getNowTimestamp());
	
			/* Set foreign keys */
			rant.setVisitorId((int) visitor.getId());
			rant.setEmotionId((int) emotion.getId());
			rant.setQuestionId((int) question.getId());
	
			/* Check if rant is valid, then save */
			if (!rant.isValid()) return false;
			if (!visitor.isValid()) return false;
			rant.saveIt();
			visitor.saveIt();
			
			return true;
		}

		/**
		* Set default data on some fields
		* @param rant
		*/
		private void setDefaults(Rant rant) {
			if (rant.getVisitorName() == null || rant.getVisitorName().isEmpty()) {
			rant.setVisitorName("Anonymous");
			}
			if (rant.getLocation() == null || rant.getLocation().isEmpty()) {
			rant.setLocation("Earth");
			}
		}

		/*
		 * Escape harmful characters
		 */
		private void scrubRant(Rant rant) {
			rant.setRant(StringEscapeUtils.escapeHtml(rant.getRant()));
			rant.setVisitorName(StringEscapeUtils.escapeHtml(rant.getVisitorName()));
			rant.setLocation(StringEscapeUtils.escapeHtml(rant.getLocation()));
		}
	}
}