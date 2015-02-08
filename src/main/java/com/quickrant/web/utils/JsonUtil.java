package com.quickrant.web.utils;

import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class JsonUtil {

    private static Gson gson = new Gson();

    public static DBObject getDbObject(Object object) {
        return (DBObject) JSON.parse(gson.toJson(object));
    }

}
