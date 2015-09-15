package com.quickrant.repository;

import com.quickrant.model.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SessionRepository extends MongoRepository<Session, String> {

        public List<Session> findByActiveIsTrue();

}
