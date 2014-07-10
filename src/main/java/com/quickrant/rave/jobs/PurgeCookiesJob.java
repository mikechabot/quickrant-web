package com.quickrant.rave.jobs;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.quickrant.rave.Configuration;
import com.quickrant.rave.service.CookieService;

public class PurgeCookiesJob {
	
	private static Logger log = Logger.getLogger(PurgeCookiesJob.class);
	
	private Timer timer;
	private int delayInMin;
	private int intervalInMin;
	
	private boolean initialized = false;
	
	public PurgeCookiesJob(Configuration conf) { 
		delayInMin = conf.getOptionalInt("cookie-flush-delay", 2);
    	intervalInMin = conf.getOptionalInt("cookie-flush-interval", 5);
	}
    
    /**
     * Start the job
     */
    public void start() {
    	if (initialized) return;
    	timer = new Timer();
        timer.schedule(new PurgeTask(), delayInMin*60*1000, intervalInMin*60*1000);
        initialized = true;
    }
    
    /**
     * Cancel the job
     */
    public void stop() {
    	timer.cancel();
    }

    /**
     * Purge cookies every N minutes
     * 
     */
    private class PurgeTask extends TimerTask {
    	
    	@Override
    	public void run() {
    		CookieService.clean();
//    		RanterService.clean();
    		log.info("Next run time: " + getNextRunTime());
        }

        private Timestamp getNextRunTime() {
        	return new Timestamp(System.currentTimeMillis() + intervalInMin*60*1000);
        }
        
    }
    
}