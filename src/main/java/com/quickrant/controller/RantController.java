package com.quickrant.controller;

import com.quickrant.json.JsonResponse;
import com.quickrant.json.JsonResponseFactory;
import com.quickrant.model.Rant;
import com.quickrant.service.RantService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rants")
public class RantController extends DomainController {

    private static Logger log = Logger.getLogger(RantController.class);

    @Autowired
    RantService rantService;

    @ResponseBody
    @RequestMapping(value = "/page/{pageNumber}", method = RequestMethod.GET)
    public JsonResponse getPaginatedRants(@PathVariable int pageNumber) {
        if (--pageNumber < 0) {
            return JsonResponseFactory.failure("Page number cannot be less than zero", null);
        } else {
            PageRequest pageRequest = new PageRequest(pageNumber, 20, new Sort(Sort.Direction.DESC, "_id"));
            return JsonResponseFactory.success(null, rantService.findAll(pageRequest));
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity postRant(@RequestBody Rant rant) {
        rantService.save(rant);
        JsonResponse json;
        if (rant.getId() != null) {
            json = JsonResponseFactory.success("Rant saved", null);
            return new ResponseEntity(json, getLocationHeader(rant.getId()), HttpStatus.CREATED);
        } else {
            json = JsonResponseFactory.failure("Failure saving rant", null);
            return new ResponseEntity(json, null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Generate Location header for HTTP response status 201 (CREATED)
     * @param id
     * @return
     */
    private HttpHeaders getLocationHeader(String id) {
        HttpHeaders header = new HttpHeaders();
        header.set("Location", getDomainName() + "/rants/" + id);
        return header;
    }

}
