package com.quickrant.web.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.google.gson.JsonObject;
import com.quickrant.web.service.SessionService;

import java.io.IOException;

public class SessionController extends JsonRestService {

    private SessionService sessionService = SessionService.getInstance();

    @Override
    public void registerRequestActions() {
        registerRequestAction(new PostAction(HttpMethod.POST));
    }

    @Override
    public void registerMappings() {
        /* Register POST urls */
        registerPostMapping("/auth", new PostActionAuthentication());
    }

    protected class PostAction extends Action {
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

    /**
     * Aegis cleans up any bad POST requests, we can trust these are valid sessions
     */
    protected class PostActionAuthentication extends Action {
        public PostActionAuthentication() { super(HttpMethod.POST); }
        @Override
        public JsonElement execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            JsonObject data = new JsonObject();
            Cookie cookie = sessionService.getSession(request);
            if (!sessionService.isAuthenticated(cookie)) {
                Cookie authenticated = getNewCookie("*");
                sessionService.updateSession(cookie.getValue(), authenticated.getValue());
                data.addProperty("session", authenticated.getValue());
                response.addCookie(authenticated);
                return AjaxResponseFactory.getSuccess(SessionStates.NEW.getState(), data).getResponse();
            } else {
                data.addProperty("session", cookie.getValue());
              return AjaxResponseFactory.getSuccess(SessionStates.EXISTING.getState(), data).getResponse();
            }
        }

        private Cookie getNewCookie(String suffix) {
            return sessionService.newCookie(sessionService.generateSessionId(), suffix);
        }
    }

    private enum SessionStates {
        NEW("New session"),
        EXISTING("Existing session");

        private final String state;
        SessionStates(String state) {
            this.state = state;
        }
        public String getState() {
            return state;
        }
    }

}