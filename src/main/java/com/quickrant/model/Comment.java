package com.quickrant.model;

import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

public class Comment {

    private Ranter ranter;
    private String comment;
    private Date createdDate;

    public Ranter getRanter() {
        return ranter;
    }

    public void setRanter(Ranter ranter) {
        this.ranter = ranter;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    @CreatedDate
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
