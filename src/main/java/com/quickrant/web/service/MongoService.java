package com.quickrant.web.service;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.quickrant.web.database.Mongo;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class MongoService implements NoSqlService {

    private static Logger log = Logger.getLogger(MongoService.class);

    private DB mongo = Mongo.INSTANCE.getDb();
    private DBCollection collection;

    protected Gson gson = new Gson();

    public MongoService(String name) {
        collection = mongo.getCollection(name);
    }

    @Override
    public List<DBObject> findAll() {
        List<DBObject> objects = new ArrayList<>();
        DBCursor cursor = collection.find();
        try {
            while(cursor.hasNext()) {
                DBObject obj = cursor.next();
                System.out.println(obj.toString());
                objects.add(obj);
            }
        } finally {
            cursor.close();
        }
        return objects;
    }

    @Override
    public boolean insert(DBObject object) {
        if (object == null) throw new IllegalArgumentException("DBObject cannot be null");
        try {
            collection.insert(object, WriteConcern.ACKNOWLEDGED);
        } catch (Exception ex) {
            log.error("Error inserting into collection '" + collection.getName() + "' - " + (object != null  ? object.toString() : "null"));
            return false;
        }
       return true;
    }

    @Override
    public DBObject find(DBObject object) {
        return null;
    }

    @Override
    public DBObject findById(String id) {
        return null;
    }

    @Override
    public boolean delete(DBObject object) {
        return false;
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

    @Override
    public void dropCollection() {
        collection.drop();
    }

}
