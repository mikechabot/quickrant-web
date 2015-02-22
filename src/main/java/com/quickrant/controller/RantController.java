package com.quickrant.controller;

import com.quickrant.model.Rant;
import com.quickrant.service.RantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/rants")
public class RantController extends AbstractRestController {

    @Autowired
    private RantService rantService;

    @ResponseBody
    @RequestMapping(value = "/page/{pageNumber}", method = RequestMethod.GET)
    public ResponseEntity getPage(@PathVariable int pageNumber) {
        if (--pageNumber < 0) return response.badRequest("Page number cannot be less than zero", null);
        PageRequest pageRequest = getPageRequest(pageNumber);
        Page page = rantService.findAll(pageRequest);
        return response.ok(null, page);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveRant(@RequestBody Rant rant, HttpServletRequest request) {
        rantService.save(rant);
        return rant.getId() != null ?
                response.created("Rant saved", getLocationHeader(request, rant)) :
                response.badRequest("Failed to save rant");
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

    /**
     * Generate Location header for HTTP response status 201 (CREATED)
     * @param request
     * @param rant
     * @return
     */
    private HttpHeaders getLocationHeader(HttpServletRequest request, Rant rant){
        HttpHeaders header = new HttpHeaders();
        header.set("Location", getDomainName(request) + "/rants/" + rant.getId());
        return header;
    }

}
