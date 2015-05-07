package com.quickrant.security;

import java.util.Date;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.quickrant.util.MapEntry;
import org.apache.log4j.Logger;

/**
 * Thread-safe, self-cleaning cache
 *
 */
public abstract class CookieCache {

    private static Logger log = Logger.getLogger(CookieCache.class);

    protected String id;

    private ConcurrentMap<Date, String> entries = new ConcurrentHashMap<Date, String>(0);

    /* In minutes */
    protected long expiry;

    private Timer timer;

    public void initializeCache() {
        timer = new Timer();
        timer.schedule(new CleanCacheTask(), 5000, 120000);

        expiry = 10;
        log.info("Cache '" + id + "' initialized -> expiry: " + expiry + " minute(s)");
    }

    public int size() {
        return entries.size();
    }

    public String getId() {
        return id;
    }

    protected Entry<Date, String> newEntry(String value) {
        return new MapEntry<Date, String>(new Date(System.currentTimeMillis()), value);
    }

    protected Entry<Date, String> newEntry(Date timestamp, String value) {
        return new MapEntry<Date, String>(timestamp, value);
    }

    protected void put(Date timestamp, String entry) {
        entries.put(timestamp, entry);
    }

    protected void put(Entry<Date, String> entry) {
        entries.put(entry.getKey(), entry.getValue());
    }

    protected void updateByValue(String oldValue, String newValue) {
        Entry<Date, String> entry = getByValue(oldValue);
        if (entry != null) {
            put(newEntry(entry.getKey(), newValue));
        }
    }

    protected void updateByTimestamp(Date timestamp, String newValue) {
        if (containsTimestamp(timestamp)) {
            put(newEntry(timestamp, newValue));
        }
    }

    protected Entry<Date, String> getByValue(String value) {
        for (Entry<Date, String> each : entries.entrySet()) {
            if (each.getValue().equals(value)) {
                return each;
            }
        }
        return null;
    }

    protected Entry<Date, String> getByTimestamp(Date value) {
        for (Entry<Date, String> each : entries.entrySet()) {
            if (each.getKey().equals(value)) {
                return each;
            }
        }
        return null;
    }

    protected boolean contains(Entry<Date, String> entry) {
        if (entry == null) return false;
        if (!containsValue(entry.getValue())) return false;
        if (!containsTimestamp(entry.getKey())) return false;
        return true;
    }

    protected boolean containsValue(String value) {
        if (value == null || value.length() == 0) return false;
        return (entries != null && !entries.isEmpty()) ? entries.containsValue(value) : false;
    }

    protected boolean containsTimestamp(Date timestamp) {
        if (timestamp == null) return false;
        return (entries != null && !entries.isEmpty()) ? entries.containsKey(timestamp) : false;
    }

    /**
     * Clean the cache every N minutes
     */
    protected class CleanCacheTask extends TimerTask {

        private Logger log = Logger.getLogger(CleanCacheTask.class);

        @Override
        public void run() {
            int cleaned = cleanCache();
            log.info("Cleaned up " +  cleaned + " cached cookies (" + entries.size() + " active)");
            log.info("Next run time: whenevever");
        }

        /**
         * Clean the cache
         * @return
         */
        private int cleanCache() {
            int cleaned = 0;
            if(entries != null) {
                int start = entries.size();
                for(Date temp : entries.keySet()) {
                    if (shouldBeRemoved(temp)) {
                        entries.remove(temp);
                    }
                }
                cleaned = start - entries.size();
            }
            return cleaned;
        }

        /**
         * Check to see if the entry is older than maxEntryAgeInMin
         * @param timestamp
         * @return boolean
         */
        private boolean shouldBeRemoved(Date timestamp) {
            return (System.currentTimeMillis() - timestamp.getTime()) > expiry * 60 * 1000;
        }
    }

}