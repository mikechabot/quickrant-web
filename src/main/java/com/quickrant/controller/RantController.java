package com.quickrant.controller;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.mongodb.MongoClientException;
import com.quickrant.factory.ResponseEntityFactory;
import com.quickrant.model.Rant;
import com.quickrant.model.Reply;
import com.quickrant.sort.MongoSort;
import com.quickrant.sort.SortMethod;
import com.quickrant.service.RantService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/rants")
public class RantController {

    private static Logger log = Logger.getLogger(RantController.class);

    @Autowired
    private RantService rantService;

    @Autowired
    protected ResponseEntityFactory response;

    protected JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    @RequestMapping(value = "/page/{pageNumber}", method = RequestMethod.GET)
         public ResponseEntity getPage(@PathVariable int pageNumber) {
        if (--pageNumber < 0) {
            return response.badRequest("Page number cannot be less than zero");
        }
        PageRequest pageRequest = getPageRequest(pageNumber, 15, SortMethod.ID_DESC);
        Page data = rantService.findAll(pageRequest);
        return response.ok(null, data);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity getRantById(@PathVariable String id) {
        if (id == null && id.isEmpty()) {
            return response.badRequest("Id cannot be null");
        }
        Rant rant = rantService.findById(id);
        if (rant != null) {
            return response.ok(null, rant);
        }
        return response.badRequest("Cannot locate rant with id " + id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveRant(@RequestBody Rant rant, HttpServletRequest request) {
        if (rant == null) throw new IllegalArgumentException("Rant cannot be null");
        try {
            rantService.save(rant);
            ObjectNode data = nodeFactory.objectNode();
            data.set("id", nodeFactory.textNode(rant.getId()));
            return response.created("Rant saved", request.getServerName() + "/rants/" + rant.getId(), data);
        } catch (MongoClientException ex) {
            return response.badRequest("Failed to save rant");
        }
    }

    @RequestMapping(value = "/reply/{rantId}", method = RequestMethod.POST)
    public ResponseEntity saveReply(@PathVariable String rantId, @RequestBody Reply reply) {
        if (reply == null) {
            return response.badRequest("Reply cannot be null");
        }
        try {
            Rant rant = rantService.findById(rantId);
            rant.addReply(reply);
            rantService.save(rant);
            return response.ok(null, null);
        } catch(MongoClientException ex) {
            log.error("Failed to save reply", ex);
            return response.error("Failed to save reply", ex);
        }
    }

    public PageRequest getPageRequest(int pageNumber, int size, SortMethod sortMethod ) {
        return new PageRequest(pageNumber, size, MongoSort.SORT_BY.get(sortMethod));
    }

}
