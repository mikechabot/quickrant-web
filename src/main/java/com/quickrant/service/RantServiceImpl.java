package com.quickrant.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.quickrant.comparator.CommentCountComparator;
import com.quickrant.model.Comment;
import com.quickrant.model.Rant;
import com.quickrant.repository.RantRepository;
import com.quickrant.sort.MongoSort;
import com.quickrant.sort.SortMethod;
import com.quickrant.util.StringUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class RantServiceImpl implements RantService {

    private static final int NUMBER_OF_POPULAR_RANTS = 15;
    private static final int NUMBER_OF_RANTS_PER_PAGE = 15;

    @Autowired
    private RantRepository rantRepository;

    @Override
    public Page getPageByPageNumber(int pageNumber) {
        if (pageNumber < 0) throw new IllegalArgumentException("Page number cannot be less than zero");
        return rantRepository.findAll(new PageRequest(pageNumber, NUMBER_OF_RANTS_PER_PAGE, MongoSort.SORT_BY.get(SortMethod.ID_DESC)));
    }

    @Override
    public List<Rant> getRantsCreatedAfter(Date date) {
        if (date == null) throw new IllegalArgumentException("Date cannot be null");
        return rantRepository.findByCreatedDateGreaterThan(date);
    }

    @Override
    public List<Rant> getPopularRants() {

        List<Rant> rants = rantRepository.findByCommentCountGreaterThan(1);
        Collections.sort(rants, new CommentCountComparator());

        while (rants.size() > NUMBER_OF_POPULAR_RANTS) {
            rants.remove(rants.size()-1);
        }

        return rants;
    }

    @Override
    public Rant getRantById(String id) {
        if (StringUtil.isEmpty(id)) return null;
        return rantRepository.findOne(id);
    }

    @Override
    public void addCommentToRant(Rant rant, Comment comment) {
        if (rant == null || comment == null) throw new IllegalArgumentException("Rant and/or comment cannot be null");
        Date createdDate = new Date();
        comment.setCreatedDate(createdDate);
        comment.setLastModifiedDate(createdDate);
        rant.addComment(comment);
        saveRant(rant);
    }

    @Override
    public void saveRant(Rant rant) {
        rantRepository.save(rant);
    }

}
