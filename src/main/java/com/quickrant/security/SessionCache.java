package com.quickrant.security;

import com.quickrant.model.Session;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Cache that holds active sessions. Concurrent HashMap using a
 * cookie value (a UUID) as the key, and a Session object as the value
 *
 *  > Thread-safe
 *  > Guaranteed singleton
 *  > Self-cleaning
 */
public enum SessionCache {

    INSTANCE;

    private ConcurrentMap<String, Session> cache = new ConcurrentHashMap<>(0);
    private Timer timer = new Timer();

    int expiry = 30;     // Expire cache entries in N days
    int interval = 5;    // Run the cleaning task every N minutes
    int delay = 10;      // Delay the initial run of the cleaning task by N seconds

    private SessionCache() throws ExceptionInInitializerError {
        timer.schedule(new CleanTask(), delay * 1000, interval * 60 * 1000);
    }

    public int size() {
        return cache.size();
    }

    public void addAll(List<Session> sessions) {
        if (sessions == null || sessions.isEmpty()) return;
        for (Session session : sessions) {
            add(session);
        }
    }

    public void add(Session session) {
        if (session == null || session.getCookieValue() == null) return;
        cache.put(session.getCookieValue(), session);
    }

    public boolean contains(String key) {
        return cache.get(key) != null;
    }

    public boolean contains(Session session) {
        if (session == null || session.getCookieValue() == null) return false;
        return cache.get(session.getCookieValue()) != null;
    }

    /**
     * TimerTask that cleans the cache on an interval
     */
    protected class CleanTask extends TimerTask {

        private Logger log = Logger.getLogger(CleanTask.class);

        @Override
        public void run() {
            cleanCache();
            log.info("Next run time: " + new DateTime().plusMinutes(interval));
        }

        /**
         * Removed expired entries from the cache
         * @return
         */
        private void cleanCache() {
            int previous = cache.size();
            for(Session session : cache.values()) {
                if (isExpired(session)) {
                    cache.remove(session);
                }
            }
            int current = cache.size();
            log.info("Cleaned up " + (previous - current) + " cached cookies (" + current + " active)");
        }

        /**
         * Check to see if the entry is older than
         * @param session
         * @return boolean
         */
        private boolean isExpired(Session session) {
            DateTime sessionCreated = new DateTime(session.getCreatedDate());
            DateTime now = new DateTime();
            return sessionCreated.isBefore(now.minusDays(expiry));
        }
    }

}