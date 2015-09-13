package com.quickrant.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;

public class MongoDocument {

    @Id
    public String id;

    @NotNull
    @CreatedDate
    public DateTime createdDate;

    @LastModifiedDate
    public DateTime lastModifiedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public DateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(DateTime lastModifiedDate) {
        lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MongoDocument that = (MongoDocument) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "MongoDocument{" +
                "id='" + id + '\'' +
                ", createdDate=" + createdDate +
                ", LastModifiedDate=" + lastModifiedDate +
                '}';
    }

}
