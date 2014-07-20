package com.quickrant.rave.database;

import org.apache.log4j.Logger;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.Statistics;

public class DatabaseStats extends Statistics {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(DatabaseStats.class);
	
	public DatabaseStats(BoneCP pool) {
		super(pool);		
	}
	
	public String getCacheStats() {
		return "cache [hits: " + getCacheHits() + ", misses: " + getCacheMiss() + ", ratio: " + getCacheHitRatio() + "]";
	}
	
	public String getConnectionStats() {
		return "connections [free: " + getTotalFree() + ", leased: " + getTotalLeased() + "]";
	}
	
	public String getPerformanceStats() {
		return "performance [avg wait: " + getConnectionWaitTimeAvg() + ", avg execute: " + getStatementExecuteTimeAvg() + "]";
	}

	public void printStats() {
		log.info(getCacheStats());
		log.info(getConnectionStats());
		log.info(getPerformanceStats());
	}
	
}
