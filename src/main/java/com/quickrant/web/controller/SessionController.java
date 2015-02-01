package com.quickrant.web.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        registerPostMapping("/auth", new PostActionAuthentication(HttpMethod.POST));
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

    public class PostActionAuthentication extends Action {
        public PostActionAuthentication(HttpMethod methodType) { super(methodType); }
        @Override
        public JsonElement execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            /* Aegis cleans up any bad requests, we can trust these are valid sessions */
            Cookie cookie = sessionService.getSession(request);
            if (!sessionService.isAuthenticated(cookie)) {
                Cookie authenticated = sessionService.newCookie(sessionService.generateSessionId(), "*");
                sessionService.updateSession(cookie.getValue(), authenticated.getValue());
                response.addCookie(authenticated);
                return AjaxResponseFactory.getSuccess(SessionStates.AUTHENTICATING.getState(), cookieSwap(cookie, authenticated)).getResponse();
            } else {
              return AjaxResponseFactory.getSuccess(SessionStates.EXISTING_SESSION.getState(), null).getResponse();
            }
        }

        private JsonObject cookieSwap(Cookie previous, Cookie updated) {
            JsonObject data = new JsonObject();
            data.addProperty("previous", previous.getValue());
            data.addProperty("updated", updated.getValue());
            return data;
        }
    }

    private enum SessionStates {
        AUTHENTICATING("Authenticating"),
        EXISTING_SESSION("Existing session");

        private final String state;
        SessionStates(String state) {
            this.state = state;
        }
        public String getState() {
            return state;
        }
    }

}