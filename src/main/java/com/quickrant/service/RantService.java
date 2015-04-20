package com.quickrant.service;

import com.quickrant.model.Rant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RantService extends MongoRepository<Rant, String> {

    List<Rant> findByReplyCountGreaterThan(int count);

}
