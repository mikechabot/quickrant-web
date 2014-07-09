package com.quickrant.rave.jobs;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.quickrant.rave.Configuration;
import com.quickrant.rave.service.CookieService;

public class PurgeCookiesJob {
	
	private static Logger log = Logger.getLogger(PurgeCookiesJob.class);
	
	private long delayInMin;
	private long intervalInMin;
	
    public PurgeCookiesJob(Configuration conf) {		
    	delayInMin = conf.getOptionalLong("cookie-flush-delay", 2);
    	intervalInMin = conf.getOptionalLong("cookie-flush-interval", 5);
    }
    
    public void start() {
    	Timer timer = new Timer();
        timer.schedule(new FlushCookiesTask(), delayInMin*60*1000, intervalInMin*60*1000);
        log.info("Timer delay: " + delayInMin + " minute(s)");
        log.info("Timer interval: " + intervalInMin + " minute(s)");
        log.info("Next run time: " + new Timestamp(System.currentTimeMillis() + delayInMin*60*1000));
    }

    class FlushCookiesTask extends TimerTask {
    	
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