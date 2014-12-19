package com.quickrant.web.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.quickrant.web.service.SessionService;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            Results results = null;

            /* Get the session cookie */
            Cookie cookie = getSessionCookie(request);

            /* Deny access if there's no session cookie */
            if (cookie == null) {
                String message = request.getRemoteAddr() + " is attempting to authenticate without session cookie";
                results = resultsFactory.getSuccessWithMessage(message);
                log.warn(results.getMessage());
            } else {
                /* Can't authenticate if the session doesn't exist */
                if (!sessionService.cookieExists(cookie)) {
                    results = resultsFactory.getFailureWithMessage(request.getRemoteAddr() + " is attempting to authenticate without a valid session cookie");
                    log.warn(results.getMessage());
                } else if (!isAuthenticated(cookie)) {
                    Cookie authCookie = sessionService.generateCookie(cookie.getValue() + "*");
                    sessionService.updateSession(cookie.getValue(), authCookie.getValue());
                    response.addCookie(authCookie);
                    results = resultsFactory.getSuccess();
                }
            }
            JsonObject object = new JsonParser().parse(new Gson().toJson(results)).getAsJsonObject();
            return object;
        }

        /**
         * Determine if the cookie ends with "*"
         * This can be spoofed, but it won't do any good
         *
         * @param cookie
         * @return
         */
        public boolean isAuthenticated(Cookie cookie) {
            Pattern pattern = Pattern.compile("\\*$");
            Matcher matcher = pattern.matcher(cookie.getValue());
            return matcher.find();
        }

        /**
         * Return a Cookie corresponding to the SessionService id (e.g. 'some-session-id')
         * @param request
         * @return
         */
        public Cookie getSessionCookie(HttpServletRequest request) {
            Cookie[] cookies = request.getCookies();
            if (cookies == null || cookies.length == 0) {
                return null;
            } else {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(sessionService.getId())) {
                        return cookie;
                    }
                }
                return null;
            }
        }
    }

}