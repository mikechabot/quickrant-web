package com.quickrant.repository;

import com.quickrant.domain.Emotion;
import com.quickrant.model.Rant;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface RantRepository extends MongoRepository<Rant, String> {

    List<Rant> findByCommentCountGreaterThan(long count);
    List<Rant> findByCreatedDateGreaterThan(Date date);
    Long countByEmotion(Emotion emotion);

}
