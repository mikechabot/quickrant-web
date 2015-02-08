package com.quickrant.web.service;

import com.mongodb.DBObject;

import java.util.List;

public interface NoSqlService {

    public List<DBObject> findAll();
    public boolean insert(DBObject object);
    public DBObject find(DBObject object);
    public DBObject findById(String id);
    public boolean delete(DBObject object);
    public boolean deleteById(String id);
    public void dropCollection();

}
