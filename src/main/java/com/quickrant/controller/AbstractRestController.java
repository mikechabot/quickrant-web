package com.quickrant.controller;

import com.quickrant.json.ResponseEntityFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractRestController {

    @Autowired
    protected ResponseEntityFactory response;

    public String getDomainName(HttpServletRequest request) {
        if (request == null) throw new IllegalArgumentException("Request cannot be null");
        return request.getServerName();
    }

}
