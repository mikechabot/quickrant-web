package com.quickrant.service;

import com.quickrant.model.Reply;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReplyService extends MongoRepository<Reply, String> {

    List<Reply> findRepliesByRantId(String rantId);

}
