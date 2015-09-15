package com.quickrant.service;

import javax.validation.Valid;

import com.quickrant.domain.RantPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.quickrant.comparator.CommentCountComparator;
import com.quickrant.domain.Comment;
import com.quickrant.domain.RantPageResponse;
import com.quickrant.model.Rant;
import com.quickrant.repository.RantRepository;
import com.quickrant.sort.MongoSort;
import com.quickrant.sort.SortMethod;
import com.quickrant.util.StringUtil;

import java.util.Collections;
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
    public RantPageResponse getRantPage(RantPageRequest pageRequest) {

        int pageNumber = pageRequest.getPageNumber();
        if (pageNumber < 0) {
            throw new IllegalArgumentException("Page number cannot be less than zero");
        }

        Page page = getPageByPageNumber(pageNumber);
        if (page == null) {
            return null;
        }

        RantPageResponse pageResponse = new RantPageResponse(page, pageRequest.getNumberOfRantsViewed());
        return pageResponse;
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
    public void addCommentToRant(@Valid Rant rant, @Valid Comment comment) {
        if (rant == null || comment == null) throw new IllegalArgumentException("Rant and/or comment cannot be null");
        rant.addComment(comment);
        saveRant(rant);
    }

    @Override
    public void saveRant(Rant rant) {
        rantRepository.save(rant);
    }

}
