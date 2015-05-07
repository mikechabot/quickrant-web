package com.quickrant;

import com.quickrant.model.Session;
import com.quickrant.security.SessionCache;
import com.quickrant.service.SessionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BootstrapApp implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = Logger.getLogger(BootstrapApp.class);

    @Autowired
    SessionService sessionService;

    SessionCache cache = SessionCache.INSTANCE;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        populateSessionCache();
    }

    private void populateSessionCache() {
        List<Session> sessions = sessionService.findAll();
        cache.addAll(sessions);
        log.info("Populating cache with " + cache.size() + " active sessions");
    }

}
