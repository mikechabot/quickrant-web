package com.quickrant.repository;

import com.quickrant.domain.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public interface SessionRepository extends MongoRepository<Session, String> {

}
