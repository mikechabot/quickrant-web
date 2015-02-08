package com.quickrant.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.log4j.Logger;

public abstract class JsonRestService extends HttpServlet  {

    protected Gson gson = new Gson();

    private static Logger log = Logger.getLogger(JsonRestService.class);

    /**
     * Map of Action objects corresponding to a request method (GET, POST, PUT, DELETE)
     *
     *  - If a request is received with a method type whose Action has not been registered
     *    in the map, return a "501 Not Implemented".
     *  - It is the responsibility of the registered Action to handle the request directly, or
     *    route it (based on path) to an Action registered in a map corresponding to the method
     *    type (see getMappings, postMappings, etc.)
     *
     *    Example: A POST request will initially be handled by the Action defined in the
     *             requestActions map (or return a 501 if undefined). This default Action must either
     *             handle the POST request itself, or, based on path (i.e. /statistics, /person), route
     *             the request to an Action defined the postMappings map
     */
    private Map<HttpMethod, Action> requestActions = new HashMap<>();

    /**
     * Map of Action objects corresponding to a URL mapping (e.g. "/statistics")
     *
     *  - Each method type has its own Map.
     *  - Handle different URLs by registering an Action in the appropriate Map.
     *  - These actions are only invoked by their method type parent defined in
     *    Map<HttpMethod, Action> requestActions.
     *  - If a request is received with URL mapping whose Action has not been registered
     *    in the map, return a "501 Not Implemented" back to the browser.
     */
    protected Map<String, Action> getMappings = new HashMap<>();
    protected Map<String, Action> postMappings = new HashMap<>();
    protected Map<String, Action> putMappings = new HashMap<>();
    protected Map<String, Action> deleteMappings = new HashMap<>();

    public void init() {
        registerRequestActions();
        registerMappings();
    }

    /**
     * Register default Action based on request type (GET, POST, DELETE, PUT)
     */
    protected abstract void registerRequestActions();

    /**
     * Put an action in the map
     * @param action
     */
    protected void registerRequestAction(Action action) {
        requestActions.put(action.getMethodType(), action);
    }

    /**
     * Register Actions for a given URL mapping (e.g. "/statistics")
     */
    protected abstract void registerMappings();

    /**
     * Register an action for a URL
     * @param mapping
     * @param action
     */
    protected void registerGetMapping(String mapping, Action action) {
        getMappings.put(mapping, action);
    }

    protected void registerPostMapping(String mapping, Action action) {
        postMappings.put(mapping, action);
    }

    protected void registerPutMapping(String mapping, Action action) {
        putMappings.put(mapping, action);
    }

    protected void registerDeleteMapping(String mapping, Action action) {
        deleteMappings.put(mapping, action);
    }

    /**
     * Process the request and respond with JSON
     * @param method
     * @param request
     * @param response
     * @throws IOException
     */
    public void doExecute(HttpMethod method, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isRequestActionImplemented(method)) {
            JsonElement json = requestActions.get(method).execute(request, response);
            respond(response, json);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }

    /**
     * Attach JSON to the response body
     * @param response
     * @param json
     * @throws IOException
     */
    public void respond(HttpServletResponse response, JsonElement json) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter printout = response.getWriter();
        printout.print(json);
        printout.flush();
    }

    /**
     * Handle GET requests
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doExecute(HttpMethod.GET, request, response);
    }

    /**
     * Handle POST requests
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doExecute(HttpMethod.POST, request, response);
    }

    /**
     * Handle PUT requests
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doExecute(HttpMethod.PUT, request, response);
    }

    /**
     * Handle DELETE requests
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doExecute(HttpMethod.DELETE, request, response);
    }

    /**
     * Check whether the action has been implemented
     * @param method
     * @return
     */
    public boolean isRequestActionImplemented(HttpMethod method) {
        if (requestActions.get(method) == null) {
            log.info(method + " not implemented");
            return false;
        }
        return true;
    }

    /**
     * Factory for generating AjaxResponses
     */
    protected static class AjaxResponseFactory {

        public static AjaxResponse getSuccess(String message, JsonElement data) {
            return new AjaxResponse(true, message, data);
        }

        public static AjaxResponse getFailure(String message, JsonElement data) {
            return new AjaxResponse(false, message, data);
        }
    }

    /**
     * Represents a typical AJAX response
     */
    protected static class AjaxResponse {

        private boolean success;
        private String message;
        private JsonElement response;

        public AjaxResponse(boolean success, String message, JsonElement data) {
            this.success = success;
            this.message = message;
            this.response = getJson(success, message, data);
        }

        private JsonElement getJson(boolean success, String message, JsonElement data) {
            JsonObject json = success ? getSuccess() : getFailure();
            json.addProperty("message", message);
            json.add("data", data);
            return json;
        }

        private JsonObject getSuccess() {
            JsonObject results = new JsonObject();
            results.addProperty("success", true);
            return results;
        }

        private JsonObject getFailure() {
            JsonObject results = new JsonObject();
            results.addProperty("success", false);
            return results;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public JsonElement getResponse() {
            return response;
        }

        public void setResponse(JsonElement response) {
            this.response = response;
        }

    }

}