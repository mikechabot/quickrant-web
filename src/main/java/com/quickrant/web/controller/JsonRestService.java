package com.quickrant.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.quickrant.web.utils.StringUtil;
import org.apache.log4j.Logger;

public abstract class JsonRestService extends HttpServlet  {

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
    public void registerRequestAction(Action action) {
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
    public void registerGetMapping(String mapping, Action action) {
        getMappings.put(mapping, action);
    }

    public void registerPostMapping(String mapping, Action action) {
        postMappings.put(mapping, action);
    }

    public void registerPutMapping(String mapping, Action action) {
        putMappings.put(mapping, action);
    }

    public void registerDeleteMapping(String mapping, Action action) {
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
     * Parse String from request body
     * @param request
     * @return
     * @throws IOException
     */
    protected String getRequestBodyAsString(HttpServletRequest request) throws IllegalArgumentException, IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            if (inputStream != null) {
                reader =  new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead;
                while ((bytesRead = reader.read(charBuffer)) > 0) {
                    sb.append(charBuffer, 0, bytesRead);
                }
            } else {
                sb.append("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        if (StringUtil.isEmpty(sb.toString())) throw new IllegalArgumentException("Request body cannot be empty");
        return sb.toString();
    }

    /**
     * Parse JsonObject from request body
     * @param request
     * @return
     * @throws IOException
     */
    protected JsonObject getRequestBodyAsJson(HttpServletRequest request) throws IOException {
        return new JsonParser().parse(getRequestBodyAsString(request)).getAsJsonObject();
    }

}