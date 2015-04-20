package com.quickrant.controller;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.mongodb.MongoClientException;
import com.quickrant.factory.ResponseEntityFactory;
import com.quickrant.model.Rant;
import com.quickrant.model.Comment;
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
import java.util.*;

@Controller
@RequestMapping(value = "/rants")
public class RantController {

    private static Logger log = Logger.getLogger(RantController.class);

    @Autowired
    private RantService rantService;

    @Autowired
    protected ResponseEntityFactory response;

    protected JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    /**
     * Get a page of Rants
     * @param pageNumber
     * @return Page of Rant objects
     */
    @RequestMapping(value = "/page/{pageNumber}", method = RequestMethod.GET)
         public ResponseEntity getPage(@PathVariable int pageNumber) {
        if (--pageNumber < 0) {
            return response.badRequest("Page number cannot be less than zero");
        }
        PageRequest pageRequest = getPageRequest(pageNumber, 15, SortMethod.ID_DESC);
        Page data = rantService.findAll(pageRequest);
        return response.ok(null, data);
    }

    /**
     * Find Rant by id
     * @param id
     * @return a single Rant
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity getRantById(@PathVariable String id) {
        if (id == null && id.isEmpty()) {
            return response.badRequest("Id cannot be null");
        }
        Rant rant = rantService.findOne(id);
        if (rant != null) {
            return response.ok(null, rant);
        }
        return response.badRequest("Cannot locate rant with id " + id);
    }

    /**
     * Save a Rant
     * @param rant
     * @param request
     * @return id of Rant
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveRant(@RequestBody Rant rant, HttpServletRequest request) {
        if (rant == null) throw new IllegalArgumentException("Rant cannot be null");
        try {
            rant.setCreatedDate(new Date());
            rantService.save(rant);
            ObjectNode data = nodeFactory.objectNode();
            data.set("id", nodeFactory.textNode(rant.getId()));
            return response.created("Rant saved", request.getServerName() + "/rants/" + rant.getId(), data);
        } catch (MongoClientException ex) {
            return response.badRequest("Failed to save rant");
        }
    }

    /**
     * Save a comment to a Rant
     * @param rantId
     * @param comment
     * @return
     */
    @RequestMapping(value = "/comment/{rantId}", method = RequestMethod.POST)
    public ResponseEntity saveComment(@PathVariable String rantId, @RequestBody Comment comment) {
        if (comment == null) {
            return response.badRequest("Comment cannot be null");
        }
        try {
            Rant rant = rantService.findOne(rantId);
            comment.setCreatedDate(new Date());
            rant.addComment(comment);
            rantService.save(rant);
            return response.ok("Comment added", comment);
        } catch(MongoClientException ex) {
            log.error("Failed to save comment", ex);
            return response.error("Failed to save comment", ex);
        }
    }

    /**
     * Get most active Rants
     * @return Array of Rant objects
     */
    @RequestMapping(value = "/mostactive", method = RequestMethod.POST)
    public ResponseEntity getMostActive() {

        final int TOP_10 = 10;

        /* Get rants with more than 1 comment */
        List<Rant> rants = rantService.findByCommentCountGreaterThan(1);
        Collections.sort(rants, new CommentCountComparator());

        int length = rants.size();
        if (length > TOP_10) {
            length = TOP_10;
        }

        /* Set top N most active */
        Rant[] mostActive = new Rant[length];
        for (int i = 0; i < length; i++) {
            mostActive[i] = rants.get(i);
        }

        return response.ok(null, mostActive);
    }

    /**
     * Generate a PageRequest
     * @param pageNumber page number from which to start
     * @param size number of objects to pull
     * @param sortMethod sort method
     * @return
     */
    public PageRequest getPageRequest(int pageNumber, int size, SortMethod sortMethod ) {
        return new PageRequest(pageNumber, size, MongoSort.SORT_BY.get(sortMethod));
    }

    /**
     * Sort Rants by commentCount descending
     */
    public class CommentCountComparator implements Comparator<Rant> {
        public int compare(Rant o1, Rant o2) {
            long c1 = o1.getCommentCount();
            long c2 = o2.getCommentCount();
            if (c1 > c2) {
                return -1;
            } else if (c1 < c2) {
                return 1;
            }
            return 0;
        }
    }

}
