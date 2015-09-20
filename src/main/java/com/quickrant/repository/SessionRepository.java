package com.quickrant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quickrant.model.Session;

import java.util.List;

public interface SessionRepository extends MongoRepository<Session, String> {

        public List<Session> findByActiveIsTrue();

}
