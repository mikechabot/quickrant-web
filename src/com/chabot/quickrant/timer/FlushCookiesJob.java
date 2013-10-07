package com.chabot.quickrant.timer;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.chabot.quickrant.service.CookieService;
import com.chabot.quickrant.service.RanterService;

public class FlushCookiesJob {
	
	private static Logger log = Logger.getLogger(FlushCookiesJob.class);
	
	private final long TIMER_DELAY = 60*1000; 		// 1 seconds
	private final long TIMER_INTERVAL = 180*1000;	// 3 minutes

	private boolean initializing = true;
	
    public FlushCookiesJob() {
    	Timer timer = new Timer();
        timer.schedule(new FlushCookiesTask(), TIMER_DELAY, TIMER_INTERVAL);
        log.info("Timer delay: " + (TIMER_DELAY / 1000)/60 + " minute(s)");
        log.info("Timer interval: " + (TIMER_INTERVAL / 1000)/60 + " minute(s)");
        log.info("Next scheduled run time: " + getNextRunTime());
        initializing = false;
    }

    class FlushCookiesTask extends TimerTask {
    	public void run() {
    		CookieService.clean();
    		RanterService.clean();
    		log.info("Next scheduled run time: " + getNextRunTime());
        }
    }
    
    public Timestamp getNextRunTime() {
    	return new Timestamp(new Date().getTime() + (initializing ? TIMER_DELAY : TIMER_INTERVAL));
    }
    
}