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
	private long delayInMin;
	private long intervalInMin;
	
    public PurgeCookiesJob(Configuration conf) {		
    	delayInMin = conf.getOptionalLong("cookie-flush-delay", 2);
    	intervalInMin = conf.getOptionalLong("cookie-flush-interval", 5);
    }
    
    /**
     * Start the job
     */
    public void start() {
    	timer = new Timer();
        timer.schedule(new PurgeTask(), delayInMin*60*1000, intervalInMin*60*1000);
        log.info("Next cookie purge: " + new Timestamp(System.currentTimeMillis() + delayInMin*60*1000));
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
    class PurgeTask extends TimerTask {
    	
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