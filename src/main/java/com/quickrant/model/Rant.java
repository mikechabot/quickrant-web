package com.quickrant.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Rant extends MongoDocument {

    private Ranter ranter;
    private String rant;
    private Selection selection;
    private List<String> hashtags;

    public Ranter getRanter() {
        return ranter;
    }

    public void setRanter(Ranter ranter) {
        this.ranter = ranter;
    }

    public String getRant() {
        return rant;
    }

    public void setRant(String rant) {
        this.rant = rant;
    }

    public Selection getSelection() {
        return selection;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

}
