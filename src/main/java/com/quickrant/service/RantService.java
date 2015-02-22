package com.quickrant.service;

import com.quickrant.model.Rant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface RantService extends MongoRepository<Rant, String> {

    Rant findById(@Param("id") String id);

}
