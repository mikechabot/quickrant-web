package com.quickrant.service;

import com.quickrant.domain.Comment;
import com.quickrant.domain.RantPageResponse;
import com.quickrant.domain.RantPageRequest;
import com.quickrant.model.Rant;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RantService {

    Page getPageByPageNumber(int pageNumber);
    RantPageResponse getRantPage(RantPageRequest pageRequest);
    List<Rant> getPopularRants();
    Rant getRantById(String id);
    void addCommentToRant(Rant rant, Comment comment);
    void saveRant(Rant rant);

}
