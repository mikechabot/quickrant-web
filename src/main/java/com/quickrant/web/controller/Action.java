package com.quickrant.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;

import java.io.IOException;

/**
 * Actions must correspond to an HTTP method type
 */
public abstract class Action {

    private HttpMethod methodType;

    public Action(HttpMethod methodType) {
        this.methodType = methodType;
    }

    public HttpMethod getMethodType() {
        return methodType;
    }

    public abstract JsonElement execute(HttpServletRequest req, HttpServletResponse resp) throws IOException;

}