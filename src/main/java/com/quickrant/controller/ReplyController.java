package com.quickrant.controller;

import com.quickrant.factory.ResponseEntityFactory;
import com.quickrant.model.Reply;
import com.quickrant.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class ReplyController {

    @Autowired
    ReplyService replyService;

    @Autowired
    protected ResponseEntityFactory response;

    @RequestMapping(value = "/replies/{rantId}", method = RequestMethod.GET)
    public ResponseEntity getPage(@PathVariable String rantId) {
        if (rantId == null || rantId.isEmpty()) {
            return response.badRequest("Rant id cannot be null");
        }
        List<Reply> replies = replyService.findRepliesByRantId(rantId);
        return response.ok(null, replies);
    }

    @RequestMapping(value = "/reply", method = RequestMethod.POST)
    public ResponseEntity saveReply(@RequestBody Reply reply) {
        if (reply == null) {
            return response.badRequest("Reply cannot be null");
        }
        replyService.save(reply);
        return reply.getId() != null ?
                response.ok(null, null) :
                response.error("Failed to save reply", null);
    }

}
