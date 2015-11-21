package com.quickrant.controller;

import com.quickrant.ajax.AjaxResponseFactory;
import com.quickrant.http.RequestWrapper;
import com.quickrant.service.SessionService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import com.quickrant.model.Rant;
import com.quickrant.model.Comment;
import com.quickrant.service.RantService;
import com.quickrant.util.StringUtil;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/rants")
public class RantController {

    private static Logger log = Logger.getLogger(RantController.class);

    @Autowired
    private RantService rantService;

    @Autowired
    private AjaxResponseFactory ajaxResponse;

    @Autowired
    private SessionService sessionService;

    /**
     * Get a page of Rants
     * @param pageNumber
     * @return Page of Rant objects
     */
    @RequestMapping(value = "/page/{pageNumber}", method = RequestMethod.GET)
    public ResponseEntity getPage(@PathVariable int pageNumber) {
        if (pageNumber < 0) {
            return ajaxResponse.failWithMessage("Page number cannot be less than zero");
        }
        Page page = rantService.getPageByPageNumber(pageNumber);
        return page != null
                ? ajaxResponse.successWithData(page)
                : ajaxResponse.failWithMessage("Unable to locate rant page with page number '" + pageNumber + "'");
    }

    /**
     * Get list of Rants since a specified date
     * @param date
     * @return
     */
    @RequestMapping(value = "/since/{date}", method = RequestMethod.GET)
    public ResponseEntity getRantsSince(@PathVariable Long date) {
        if (date == null) {
            return ajaxResponse.failWithMessage("Date cannot be null");
        }
        List<Rant> rants = rantService.getRantsCreatedAfter(new Date(date));
        return ajaxResponse.successWithData(rants);
    }

    /**
     * Find Rant by id
     * @param id
     * @return a single Rant
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getRantById(@PathVariable String id) {
        if (StringUtil.isEmpty(id)) {
            return ajaxResponse.failWithMessage("Rant id cannot be null");
        }
        Rant rant = rantService.getRantById(id);
        return rant != null
                ? ajaxResponse.successWithData(rant)
                : ajaxResponse.failWithMessage("Unable to locate rant with id '" + id + "'");
    }

    /**
     * Save a Rant
     * @param rant
     * @return id of Rant
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveRant(@RequestBody Rant rant, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        if (rant == null) {
            ajaxResponse.failWithMessage("Rant cannot be null");
        }

        RequestWrapper request = new RequestWrapper(httpRequest, httpResponse);

        rant.setIpAddress(request.getIpAddress());
        rant.setUserAgent(request.getUserAgent());
        rant.setCookieValue(request.getCookieValue(sessionService.getCacheName()));

        rantService.saveRant(rant);
        return rant.getId() != null
                ? ajaxResponse.successWithMessageAndData("Rant saved", rant)
                : ajaxResponse.errorWithMessage("Failed to save rant");
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
            return ajaxResponse.failWithMessage("Save comment failed: Comment cannot be null");
        }
        Rant rant = rantService.getRantById(rantId);
        if (rant ==  null) {
            return ajaxResponse.failWithMessage("Save comment failed: Unable to locate rant (" + rantId + ")");
        }
        rantService.addCommentToRant(rant, comment);
        return ajaxResponse.successWithMessageAndData("Comment saved", comment);
    }

    /**
     * Get most active Rants
     * @return Array of Rant objects
     */
    @RequestMapping(value = "/popular", method = RequestMethod.GET)
    public ResponseEntity getPopularRants() {
        return ajaxResponse.successWithData(rantService.getPopularRants());
    }

}
