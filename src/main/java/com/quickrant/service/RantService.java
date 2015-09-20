package com.quickrant.service;

import com.quickrant.model.Comment;
import com.quickrant.model.Rant;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface RantService {

    Page getPageByPageNumber(int pageNumber);
    List<Rant> getRantsCreatedAfter(Date date);
    List<Rant> getPopularRants();
    Rant getRantById(String id);
    void addCommentToRant(Rant rant, Comment comment);
    void saveRant(Rant rant);

}
