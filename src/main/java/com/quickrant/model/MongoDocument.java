package com.quickrant.model;

import org.springframework.data.annotation.*;

import java.util.Date;

public class MongoDocument {

    @Id
    public String id;

    @CreatedDate
    protected Date createdDate;

    @CreatedBy
    protected String createdBy;

    @LastModifiedDate
    protected Date lastModifiedDate;

    @LastModifiedBy
    protected String lastModifiedBy;

    @Version
    protected Long version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
                ", createdBy='" + createdBy + '\'' +
                ", lastModifiedDate=" + lastModifiedDate +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", version=" + version +
                '}';
    }

}
