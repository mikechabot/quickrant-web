package com.quickrant.beans;

public class SessionCacheProperties {

    private String cacheName;
    private int cacheEntryExpiry;

    public SessionCacheProperties(String cacheName, int cacheEntryExpiry) {
        this.cacheName = cacheName;
        this.cacheEntryExpiry = cacheEntryExpiry;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public int getCacheEntryExpiry() {
        return cacheEntryExpiry;
    }

    public void setCacheEntryExpiry(int cacheEntryExpiry) {
        this.cacheEntryExpiry = cacheEntryExpiry;
    }
}
