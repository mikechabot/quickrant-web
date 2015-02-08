package com.quickrant.web.cache;

import java.sql.Timestamp;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.quickrant.web.Configuration;
import org.apache.log4j.Logger;

import com.quickrant.web.utils.SimpleEntry;
import com.quickrant.web.utils.TimeUtils;

/**
 * Thread-safe, self-cleaning cache
 *  
 */
public abstract class Cache {
	
	private static Logger log = Logger.getLogger(Cache.class);
	
	protected String id;

	private ConcurrentMap<Timestamp, String> entries = new ConcurrentHashMap<Timestamp, String>(0);

	/* In minutes */
	protected long expiry;

	private Timer timer;
	private boolean initialized = false;
    private Configuration conf = Configuration.INSTANCE;

	public void initializeCache(String id, boolean doCleanCache) {
		if (initialized) return;

		/* Set cache id */
        this.id = id;

		if (doCleanCache) {
			timer = new Timer();
            /* Delay for 5 seconds, execute every 2 minutes */
	        timer.schedule(new CleanCacheTask(), 5*1000, 2*60*1000);
		}

		/* Get configuration properties */
		expiry = conf.getRequiredInt(id + "-expiry");
		log.info("Cache '" + id + "' initialized -> expiry: " + expiry + " minute(s)");

		initialized = true;
	}

	public int size() {
		return entries.size();
	}

    public String getId() {
        return id;
    }

	protected Entry<Timestamp, String> newEntry(String value) {
		return new SimpleEntry<Timestamp, String>(TimeUtils.getNowTimestamp(), value);
	}

    protected Entry<Timestamp, String> newEntry(Timestamp timestamp, String value) {
		return new SimpleEntry<Timestamp, String>(timestamp, value);
	}

    protected void put(Timestamp timestamp, String entry) {
		entries.put(timestamp, entry);
	}

    protected void put(Entry<Timestamp, String> entry) {
		entries.put(entry.getKey(), entry.getValue());

	}

    protected void updateByValue(String oldValue, String newValue) {
		Entry<Timestamp, String> entry = getByValue(oldValue);
		if (entry != null) {
			put(newEntry(entry.getKey(), newValue));
		}
	}

    protected void updateByTimestamp(Timestamp timestamp, String newValue) {
		if (containsTimestamp(timestamp)) {
			put(newEntry(timestamp, newValue));
		}
	}

    protected Entry<Timestamp, String> getByValue(String value) {
		for (Entry<Timestamp, String> each : entries.entrySet()) {
			if (each.getValue().equals(value)) {
				return each;
			}
		}
		return null;
	}

    protected Entry<Timestamp, String> getByTimestamp(Timestamp value) {
		for (Entry<Timestamp, String> each : entries.entrySet()) {
			if (each.getKey().equals(value)) {
				return each;
			}
		}
		return null;
	}

    protected boolean contains(Entry<Timestamp, String> entry) {
		if (entry == null) return false;
		if (!containsValue(entry.getValue())) return false;
		if (!containsTimestamp(entry.getKey())) return false;
		return true;
	}

    protected boolean containsValue(String value) {
		if (value == null || value.length() == 0) return false;
		return (entries != null && !entries.isEmpty()) ? entries.containsValue(value) : false;
	}

    protected boolean containsTimestamp(Timestamp timestamp) {
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
    		log.info("Next run time: " + TimeUtils.getFutureTimestamp(2));
        }
    	
    	/**
    	 * Clean the cache
    	 * @return
    	 */
    	private int cleanCache() {
    		int cleaned = 0;
    		if(entries != null) {
    			int start = entries.size();
    			for(Timestamp temp : entries.keySet()) {
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
    	private boolean shouldBeRemoved(Timestamp timestamp) {
    		return (TimeUtils.getNow() - timestamp.getTime()) > expiry * 60 * 1000;
    	}
    }
}