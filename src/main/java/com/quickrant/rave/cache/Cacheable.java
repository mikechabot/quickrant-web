package com.quickrant.rave.cache;

import java.sql.Timestamp;
import java.util.Map.Entry;

import com.quickrant.rave.Configuration;

public interface Cacheable {

	public void initialize(Configuration conf, String name);
	public Entry<Timestamp, String> newEntry(String value);
	public Entry<Timestamp, String> newEntry(Timestamp timestamp, String value);
	public void put(Timestamp timestamp, String entry);
	public void put(Entry<Timestamp, String> entry);
	public void updateByValue(String oldValue, String newValue);
	public void updateByTimestamp(Timestamp timestamp, String newValue);
	public Entry<Timestamp, String> getByValue(String value);
	public Entry<Timestamp, String> getByTimestamp(Timestamp value);
	public boolean contains(Entry<Timestamp, String> entry);
	public boolean containsValue(String value);
	public boolean containsTimestamp(Timestamp timestamp);
	public int size();

}
