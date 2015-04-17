package com.quickrant.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Reply extends MongoDocument{

    Ranter ranter;
    String rantId;
    String reply;

    public Ranter getRanter() {
        return ranter;
    }

    public void setRanter(Ranter ranter) {
        this.ranter = ranter;
    }

    public String getRantId() {
        return rantId;
    }

    public void setRantId(String rantId) {
        this.rantId = rantId;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

}
