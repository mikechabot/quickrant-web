package com.quickrant.controller;

import com.quickrant.http.RequestWrapper;
import com.quickrant.service.SessionServiceImpl;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.quickrant.factory.AjaxResponseFactory;
import com.quickrant.model.Rant;
import com.quickrant.domain.Comment;
import com.quickrant.domain.RantPage;
import com.quickrant.service.RantService;
import com.quickrant.util.StringUtil;

import org.joda.time.DateTime;

import org.apache.log4j.Logger;

@Controller
@RequestMapping(value = "/rants")
public class RantController {

    private static Logger log = Logger.getLogger(RantController.class);

    @Autowired
    private RantService rantService;

    @Autowired
    protected AjaxResponseFactory ajaxResponse;

    /**
     * Get a page of Rants
     * @param pageNumber
     * @return Page of Rant objects
     */
    @RequestMapping(value = "/page/{pageNumber}", method = RequestMethod.GET)
    public ResponseEntity getPage(@PathVariable int pageNumber) {
        if (pageNumber < 0) {
            return ajaxResponse.fail("Page number cannot be less than zero");
        }
        RantPage rantPage = rantService.getRantPageByPageNumber(pageNumber);
        return rantPage != null
                ? ajaxResponse.success(null, rantPage)
                : ajaxResponse.fail("Unable to locate rant page with page number '" + pageNumber + "'");
    }

    /**
     * Find Rant by id
     * @param id
     * @return a single Rant
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getRantById(@PathVariable String id) {
        if (StringUtil.isEmpty(id)) {
            return ajaxResponse.fail("Rant id cannot be null");
        }
        Rant rant = rantService.getRantById(id);
        return rant != null
                ? ajaxResponse.success(null, rant)
                : ajaxResponse.fail("Unable to locate rant with id '" + id + "'");
    }

    /**
     * Save a Rant
     * @param rant
     * @return id of Rant
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveRant(@RequestBody @Valid Rant rant, HttpServletRequest httpRequest) {
        if (rant == null) {
            ajaxResponse.fail("Rant cannot be null");
        }

        RequestWrapper request = new RequestWrapper(httpRequest);

        rant.setCookie(request.getCookie(SessionServiceImpl.SESSION_COOKIE_NAME).getValue());
        rant.setUserAgent(request.getUserAgent());
        rant.setCreatedDate(new DateTime());

        rantService.saveRant(rant);
        return rant.getId() != null
                ? ajaxResponse.success("Rant saved", rant)
                : ajaxResponse.error("Failed to save rant");
    }

    /**
     * Save a comment to a Rant
     * @param rantId
     * @param comment
     * @return
     */
    @RequestMapping(value = "/comment/{rantId}", method = RequestMethod.POST)
    public ResponseEntity saveComment(@PathVariable String rantId, @RequestBody @Valid Comment comment) {
        if (comment == null) {
            return ajaxResponse.fail("Save comment failed: Comment cannot be null");
        }
        Rant rant = rantService.getRantById(rantId);
        if (rant ==  null) {
            return ajaxResponse.fail("Save comment failed: Unable to locate rant (" + rantId + ")");
        }
        rantService.addCommentToRant(rant, comment);
        return ajaxResponse.success();
    }

    /**
     * Get most active Rants
     * @return Array of Rant objects
     */
    @RequestMapping(value = "/popular", method = RequestMethod.GET)
    public ResponseEntity getPopularRants() {
        return ajaxResponse.success(null, getPopularRants());
    }


}
