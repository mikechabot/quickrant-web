package com.quickrant.controller;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.quickrant.factory.ResponseEntityFactory;
import com.quickrant.model.Rant;
import com.quickrant.sort.MongoSort;
import com.quickrant.sort.SortMethod;
import com.quickrant.service.RantService;

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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveRant(@RequestBody Rant rant, HttpServletRequest request) {
        if (rant == null) throw new IllegalArgumentException("Rant cannot be null");
        rantService.save(rant);
        if (rant.getId() != null) {
            ObjectNode data = nodeFactory.objectNode();
            data.set("id", nodeFactory.textNode(rant.getId()));
            return response.created("Rant saved", request.getServerName() + "/rants/" + rant.getId(), data);
        } else {
            return response.badRequest("Failed to save rant");
        }
    }

    @RequestMapping(value = "/drop")
    public ResponseEntity dropRants() {
        rantService.deleteAll();
        return response.ok();
    }

    public PageRequest getPageRequest(int pageNumber, int size, SortMethod sortMethod ) {
        return new PageRequest(pageNumber, size, MongoSort.SORT_BY.get(sortMethod));
    }

}
