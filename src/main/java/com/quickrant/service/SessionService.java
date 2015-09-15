package com.quickrant.service;

import com.quickrant.model.Session;

import java.util.List;

public interface SessionService {

    public List<Session> getActiveSessions();
    public Session createSession(String ipAddress, String userAgent);
    public void addSessionToCache(Session session);
    public void addSessionsToCache(List<Session> sessions);
    public void inactivateSession(Session session);
    public int getNumberOfActiveSessions();
    public boolean exists(String sessionKey);
    public boolean isExpired(Session session);
    public void clean();
    public String getCacheName();
    public int getCacheEntryExpiryInDays();

}
