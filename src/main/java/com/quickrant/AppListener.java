package com.quickrant;

import com.quickrant.domain.Session;
import com.quickrant.service.SessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;

import org.apache.log4j.Logger;

@Component
public class AppListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = Logger.getLogger(AppListener.class);

    @Autowired
    private SessionService sessionService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        populateSessionCache();
    }

    private void populateSessionCache() {
        List<Session> sessions = sessionService.getPersistedSessions();
        sessionService.addSessionsToCache(sessions);
        log.info("Populated cache with " + sessionService.getNumberOfActiveSessions() + " active sessions");
    }

}
