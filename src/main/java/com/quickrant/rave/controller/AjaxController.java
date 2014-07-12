package com.quickrant.rave.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quickrant.rave.Controller;
import com.quickrant.rave.Params;
import com.quickrant.rave.model.Visitor;
import com.quickrant.rave.service.CookieService;
import com.quickrant.rave.service.VisitorService;

public class AjaxController extends Controller {

	private static final long serialVersionUID = 1L;

	@Override
	protected String basePath() { return ""; }
	
	@Override
	protected void initActions() {
		addAction(null, new DefaultAction());
		addAction("/phonehome", new CookieAction());
	}

	@Override
	protected Action defaultAction() {
		return new DefaultAction();
	}
	
	public class DefaultAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			return basePath();			
		}		
	}
	
	public class CookieAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			// No GET requests allowed
			Params params = new Params(request);
			if (params.isGet()) {
				response.sendRedirect(basePath() + "/index.jsp");
				return null;
			}

			Map<String, String> map = params.getMap();
			Visitor visitor = new Visitor();
			visitor.fromMap(map);
			VisitorService.updateVisitor(visitor);
			Cookie cookie = CookieService.updateCookie(params.getCookie(CookieService.COOKIE_NAME));
			response.addCookie(cookie);
			
			return basePath();			
		}		
	}
	



}
