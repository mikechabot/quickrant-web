package com.quickrant.repository;

import com.quickrant.model.Rant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RantRepository extends MongoRepository<Rant, String> {

    List<Rant> findByCommentCountGreaterThan(long count);

    List<Rant> findByRantLike(String rant);

}
