package com.quickrant.web.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.quickrant.web.service.SessionService;
import org.apache.log4j.Logger;

import java.io.IOException;

public class SessionController extends JsonRestService {

    private static Logger log = Logger.getLogger(SessionController.class);

    private SessionService sessionService = SessionService.getInstance();

    @Override
    public void registerRequestActions() {
        registerRequestAction(new PostAction(HttpMethod.POST));
    }

    @Override
    public void registerMappings() {
        /* Register POST urls */
        registerPostMapping("/auth", new PostAuthenticateAction(HttpMethod.POST));
    }

    public class PostAction extends Action {
        public PostAction(HttpMethod methodType) { super(methodType); }
        @Override
        public JsonElement execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            String action = request.getPathInfo();
            if (postMappings.get(action) == null) {
                response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
                return null;
            } else {
                return postMappings.get(action).execute(request, response);
            }
        }
    }

    public class PostAuthenticateAction extends Action {
        public PostAuthenticateAction(HttpMethod methodType) { super(methodType); }
        @Override
        public JsonElement execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

            JsonObject results = new JsonObject();

            /* Get the session cookie */
            Cookie cookie = getSessionCookie(request);

            /* Deny access if there's no session */
            if (cookie == null) {
                String message = request.getRemoteAddr() + " is attempting to authenticate without session cookie";
                results.addProperty("success", false);
                results.addProperty("message", message);
                log.warn(message);
            } else {
                /* Deny access if session cookie is invalid */
                if (!sessionService.sessionExists(cookie)) {
                    String message = request.getRemoteAddr() + " is attempting to authenticate without a valid session cookie";
                    results.addProperty("success", false);
                    results.addProperty("message", message);
                    log.warn(message);
                } else {
                    Cookie authCookie = sessionService.generateCookie(cookie.getValue() + "*");
                    String message = request.getRemoteAddr() + " is attempting to authenticate without a valid session cookie BERNDDS";
                    results.addProperty("message", message);
                    sessionService.updateByValue(cookie.getValue(), authCookie.getValue());
                    response.addCookie(authCookie);
                    results.addProperty("success", true);
                }
            }

            return results;
        }

        /**
         * Return a Cookie corresponding to the SessionService id (e.g. 'some-session-id')
         * @param request
         * @return
         */
        public Cookie getSessionCookie(HttpServletRequest request) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(sessionService.getId())) return cookie;
            }
            return null;
        }
    }

}