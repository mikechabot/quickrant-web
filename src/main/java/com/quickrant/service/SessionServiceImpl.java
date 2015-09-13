package com.quickrant.service;

import com.quickrant.repository.SessionRepository;
import com.quickrant.domain.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.cache.concurrent.ConcurrentMapCache;

import org.joda.time.DateTime;

import javax.servlet.http.Cookie;

import java.util.List;

import org.apache.log4j.Logger;

@Service
public class SessionServiceImpl extends ConcurrentMapCache implements SessionService {

    private static Logger log = Logger.getLogger(SessionServiceImpl.class);

    public static final String SESSION_COOKIE_NAME = "quickrant-uuid";      // Cookie name
    public static final int CACHE_ENTRY_EXPIRY_IN_DAYS = 30;                                    // Expire cache entries in N days

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionCookieService cookieService;

    public SessionServiceImpl() {
        super("SessionServiceImpl");
    }

    @Override
    public List<Session> getPersistedSessions() {
        return sessionRepository.findAll();
    }

    @Override
    public Session createSession(String ipAddress, String userAgent) {
        Cookie cookie = cookieService.createCookieWithRandomValue(SESSION_COOKIE_NAME);
        Session session = new Session(cookie, ipAddress, userAgent);
        addSessionToCache(session);
        return session;
    }

    @Override
    public void addSessionToCache(Session session) {
        if (session == null || session.getSessionKey() == null) throw new IllegalArgumentException("Session and/or cookie value cannot be null");
        sessionRepository.save(session);
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
    public String getNewSessionKey() {
        return null;
    }

    @Override
    public void deleteSession(Session session) {
        evict(session.getSessionKey());
        sessionRepository.delete(session);
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
    public boolean exists(Session session) {
        if (session == null || session.getSessionKey() == null) return false;
        return exists(session.getSessionKey());
    }

    @Override
    public boolean isExpired(Session session) {
        DateTime sessionCreated = new DateTime(session.getCreatedDate());
        DateTime now = new DateTime();
        return sessionCreated.isBefore(now.minusDays(CACHE_ENTRY_EXPIRY_IN_DAYS));
    }

    @Override
    @Scheduled(initialDelay = 5000, fixedDelay = 3600000)  // Run every hour
    public void clean() {
        int start = getNumberOfActiveSessions();
        for(Object each : getNativeCache().values()) {
            Session session = (Session) each;
            if (isExpired(session)) {
                deleteSession(session);
            }
        }
        int finish = getNumberOfActiveSessions();
        log.info("Cleaned up " + (start - finish) + " cached cookies (" + finish + " active)");
    }

}
