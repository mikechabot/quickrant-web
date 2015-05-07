package com.quickrant.service;

import com.quickrant.model.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public interface SessionService extends MongoRepository<Session, String> {

}
