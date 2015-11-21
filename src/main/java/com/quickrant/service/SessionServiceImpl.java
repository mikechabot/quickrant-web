package com.quickrant.service;

import com.quickrant.beans.SessionCacheProperties;
import com.quickrant.repository.SessionRepository;
import com.quickrant.model.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.cache.concurrent.ConcurrentMapCache;

import org.joda.time.DateTime;

import javax.servlet.http.Cookie;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

@Service
public class SessionServiceImpl extends ConcurrentMapCache implements SessionService {

    private static Logger log = Logger.getLogger(SessionServiceImpl.class);

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionCacheProperties sessionCacheProperties;

    public SessionServiceImpl() {
        super("SessionServiceImpl");
    }

    @Override
    public List<Session> getActiveSessions() {
        return sessionRepository.findByActiveIsTrue();
    }

    @Override
    public Session createSession(String ipAddress, String userAgent) {
        Cookie cookie = createSessionCookie();
        Session session = new Session(cookie, ipAddress, userAgent);
        sessionRepository.save(session);
        addSessionToCache(session);
        return session;
    }

    @Override
    public void addSessionToCache(Session session) {
        if (session == null || session.getSessionKey() == null) throw new IllegalArgumentException("Session and/or cookie value cannot be null");
        put(session.getSessionKey(), session);
    }

    @Override
    public void addSessionsToCache(List<Session> sessions) {
        if (sessions == null) throw new IllegalArgumentException("Sessions cannot be null");
        for (Session each : sessions) {
            addSessionToCache(each);
        }
    }

    @Override
    public void inactivateSession(Session session) {
        evict(session.getSessionKey());
        session.setActive(false);
        sessionRepository.save(session);
    }

    @Override
    public int getNumberOfActiveSessions() {
        return getNativeCache().size();
    }

    @Override
    public boolean exists(String sessionKey) {
        return get(sessionKey) != null;
    }

    @Override
    public boolean isExpired(Session session) {
        DateTime sessionCreated = new DateTime(session.getCreatedDate());
        DateTime now = new DateTime();
        return sessionCreated.isBefore(now.minusDays(sessionCacheProperties.getCacheEntryExpiry()));
    }

    @Override
    @Scheduled(initialDelay = 5000, fixedDelay = 3600000)  // Run every hour
    public void clean() {
        int start = getNumberOfActiveSessions();
        for(Object each : getNativeCache().values()) {
            Session session = (Session) each;
            if (isExpired(session)) {
                inactivateSession(session);
            }
        }
        int finish = getNumberOfActiveSessions();
        log.info("Cleaned up " + (start - finish) + " cached cookies (" + finish + " active)");
    }

    @Override
    public String getCacheName() {
        return sessionCacheProperties.getCacheName();
    }

    @Override
    public int getCacheEntryExpiryInDays() {
        return sessionCacheProperties.getCacheEntryExpiry();
    }

    private Cookie createSessionCookie() {
        Cookie cookie = new Cookie(sessionCacheProperties.getCacheName(), String.valueOf(UUID.randomUUID()));
        cookie.setMaxAge(sessionCacheProperties.getCacheEntryExpiry()*24*60*60);
        cookie.setPath("/");
        return cookie;
    }

}
