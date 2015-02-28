package com.quickrant.controller;

import com.quickrant.model.Rant;
import com.quickrant.service.RantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@Controller
@RequestMapping(value = "/rants")
public class RantController extends AbstractRestController {

    @Autowired
    private RantService rantService;

    @RequestMapping(value = "/page/{pageNumber}", method = RequestMethod.GET)
    public ResponseEntity getPage(@PathVariable int pageNumber) {
        if (--pageNumber < 0) return response.badRequest("Page number cannot be less than zero");
        Sort sort = new Sort(Sort.Direction.DESC, "_id");
        PageRequest pageRequest = new PageRequest(pageNumber, 15, sort);
        Page page = rantService.findAll(pageRequest);
        return response.ok(null, page);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveRant(@RequestBody Rant rant, HttpServletRequest request) {
        rantService.save(rant);
        if (rant.getId() != null) {
            return response.created("Rant saved", null, URI.create(getDomainName(request) + "/rants/" + rant.getId()));
        }
        return response.badRequest("Failed to save rant");
    }

    /**
     * Generate an offset page request sorted by descending id
     * @param pageNumber
     * @return
     */
    private PageRequest getPageRequest(int pageNumber) {
        Sort sort = new Sort(Sort.Direction.DESC, "_id");
        return new PageRequest(pageNumber, 15, sort);
    }

}
