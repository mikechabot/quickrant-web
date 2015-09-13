package com.quickrant.service;

import com.quickrant.domain.Session;

import java.util.List;

public interface SessionService {

    public List<Session> getPersistedSessions();
    public Session createSession(String ipAddress, String userAgent);
    public void addSessionToCache(Session session);
    public void addSessionsToCache(List<Session> sessions);
    public String getNewSessionKey();
    public void deleteSession(Session session);
    public int getNumberOfActiveSessions();
    public boolean exists(String sessionKey);
    public boolean exists(Session session);
    public boolean isExpired(Session session);
    public void clean();

}
