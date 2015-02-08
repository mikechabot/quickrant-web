package com.quickrant.web.service;

import com.mongodb.DBObject;
import com.quickrant.web.models.Rant;
import com.quickrant.web.utils.JsonUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RantService extends MongoService {

    private static Logger log = Logger.getLogger(RantService.class);

    public RantService() {
        super("rants");
    }

    public boolean postRant(Rant rant) {
        if (!rant.isValid()) {
            log.error("Failed to post invalid rant");
            return false;
        }
        return insert(JsonUtil.getDbObject(rant));
    }

    public List<Rant> getRants() {
        List<Rant> rants = new ArrayList<>();
        for (DBObject object : findAll()) {
            rants.add(gson.fromJson(object.toString(), Rant.class));
        }
        return rants;
    }
}
