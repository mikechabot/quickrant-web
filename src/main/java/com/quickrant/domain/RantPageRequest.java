package com.quickrant.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class RantPageRequest {

    private int pageNumber;
    private int numberOfRantsViewed;

    public int getNumberOfRantsViewed() {
        return numberOfRantsViewed;
    }

    public void setNumberOfRantsViewed(int numberOfRantsViewed) {
        this.numberOfRantsViewed = numberOfRantsViewed;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "pageNumber=" + pageNumber +
                ", numberOfRantsViewed=" + numberOfRantsViewed +
                '}';
    }

}
