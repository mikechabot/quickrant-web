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
 * Cache that holds unique session values, and the date of their creation; as such
 * this cache expects that not only is the key unique, but also the value.
 *
 *  > Thread-safe
 *  > Guaranteed singleton
 *  > Self-cleaning
 */
public enum SessionCache {

    INSTANCE;

    private final Logger log = Logger.getLogger(SessionCache.class);

    private ConcurrentMap<String, Session> cache = new ConcurrentHashMap<>(0);
    private Timer timer = new Timer();

    int interval = 2;   // minutes
    int expiry = 5;     // minutes
    int delay = 10;     // seconds

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
        if (session == null || session.getId() == null)
        cache.put(session.getId(), session);
    }

    public boolean contains(Session session) {
        if (session == null || session.getId() == null) return false;
        return cache.get(session.getId()) != null;
    }

    /**
     * Clean the cache every N minutes
     */
    protected class CleanTask extends TimerTask {

        private Logger log = Logger.getLogger(CleanTask.class);

        public CleanTask() {
            log.info("Session cached initialized. Expiry: " + expiry + " minute(s)");
        }

        @Override
        public void run() {
            cleanCache();
            log.info("Next run time: " + new DateTime().plusMinutes(interval));
        }

        /**
         * Clean the cache
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
            return sessionCreated.isBefore(now.minusMinutes(expiry));
        }
    }

}