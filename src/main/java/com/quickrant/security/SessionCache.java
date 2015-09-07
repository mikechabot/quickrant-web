package com.quickrant.security;

import com.quickrant.model.Session;
import com.quickrant.repository.SessionRepository;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class SessionCache extends ConcurrentMapCache {

    @Autowired
    private SessionRepository sessionService;

    private Timer timer = new Timer();

    int expiry = 30;      // Expire cache entries in N days
    int interval = 60;    // Run the cleaning task every N minutes
    int delay = 10;       // Delay the initial run of the cleaning task by N seconds

    public SessionCache() {
        super("SessionCache");
        timer.schedule(new CleanTask(), delay * 1000, interval * 60 * 1000);
    }

    public void addSession(Session session) {
        if (session == null || session.getCookieValue() == null) return;
        put(session.getCookieValue(), session);
    }

    public int size() {
        return getNativeCache().size();
    }

    public void addSessions(List<Session> sessions) {
        if (sessions == null || sessions.isEmpty()) return;
        for (Session session : sessions) {
            addSession(session);
        }
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    public boolean contains(Session session) {
        if (session == null || session.getCookieValue() == null) return false;
        return get(session.getCookieValue()) != null;
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
            int start = size();
            for(Object each : getNativeCache().values()) {
                Session session = (Session) each;
                if (isExpired(session)) {
                    evict(session.getCookieValue());
                    sessionService.delete(session);
                }
            }
            int finish = size();
            log.info("Cleaned up " + (start - finish) + " cached cookies (" + finish + " active)");
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