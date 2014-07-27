package com.quickrant.web.cache;

import java.sql.Timestamp;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import com.quickrant.api.Configuration;
import com.quickrant.api.utils.TimeUtils;

/**
 * Thread-safe, self-cleaning cache
 *  
 */
public abstract class Cache {
	
	private static Logger log = Logger.getLogger(Cache.class);
	
	public static String name;
	
	protected ConcurrentMap<Timestamp, String> entries = new ConcurrentHashMap<Timestamp, String>();
	
	/* In minutes */
	protected long expiry;

	private Timer timer;
	private boolean initialized = false;
	

	public void initialize(Configuration conf, String cacheName) {
		if (initialized) return;
		
		/* Set cache name */
		name = cacheName;

		/* Get configuration properties */
		expiry = conf.getRequiredLong(name + "-expiry");
		
		log.info("expiry="+expiry);
		log.info("expiry="+expiry * 60 * 1000);
		
		
		/* Start the CleanCacheTask */
		timer = new Timer();
        timer.schedule(new CleanCacheTask(), 5000, expiry * 60 * 1000);
        
		initialized = true;
	}
	
	public int size() {
		return entries.size();
	}

	public Entry<Timestamp, String> newEntry(String value) {
		return new SimpleEntry<Timestamp, String>(TimeUtils.getNowTimestamp(), value);
	}

	public Entry<Timestamp, String> newEntry(Timestamp timestamp, String value) {
		return new SimpleEntry<Timestamp, String>(timestamp, value);
	}

	public void put(Timestamp timestamp, String entry) {
		entries.put(timestamp, entry);		
	}

	public void put(Entry<Timestamp, String> entry) {
		entries.put(entry.getKey(), entry.getValue());
		
	}

	public void updateByValue(String oldValue, String newValue) {
		Entry<Timestamp, String> entry = getByValue(oldValue);
		if (entry != null) {
			put(newEntry(entry.getKey(), newValue));
		}
	}

	public void updateByTimestamp(Timestamp timestamp, String newValue) {
		if (containsTimestamp(timestamp)) {
			put(newEntry(timestamp, newValue));
		}
	}

	public Entry<Timestamp, String> getByValue(String value) {
		for (Entry<Timestamp, String> each : entries.entrySet()) {
			if (each.getValue().equals(value)) {
				return each;
			}
		}
		return null;
	}

	public Entry<Timestamp, String> getByTimestamp(Timestamp value) {
		for (Entry<Timestamp, String> each : entries.entrySet()) {
			if (each.getKey().equals(value)) {
				return each;
			}
		}
		return null;
	}
	
	public boolean contains(Entry<Timestamp, String> entry) {
		if (entry == null) return false;
		if (!containsValue(entry.getValue())) return false;
		if (!containsTimestamp(entry.getKey())) return false;
		return true;
	}    
	
	public boolean containsValue(String value) {
		if (value == null || value.length() == 0) return false;
		return (entries != null && !entries.isEmpty()) ? entries.containsValue(value) : false;
	}

	public boolean containsTimestamp(Timestamp timestamp) {
		if (timestamp == null) return false;
		return (entries != null && !entries.isEmpty()) ? entries.containsKey(timestamp) : false;
	}

    /**
     * Clean the cache every N minutes     
     * 
     */
    private class CleanCacheTask extends TimerTask {
    	
    	private Logger log = Logger.getLogger(CleanCacheTask.class);
    	
    	@Override
    	public void run() {
    		log.info("Cleaned up " + cleanCache() + " cached cookies (" + entries.size() + " active)");
    		log.info("Next cleaning: " + TimeUtils.getFutureTimestamp(expiry));
        }

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
    	 * @param Timestamp
    	 * @return boolean
    	 */
    	private boolean shouldBeRemoved(Timestamp ts) {
    		return (TimeUtils.getNow() - ts.getTime()) > expiry * 60 * 1000;
    	}
    }

}
