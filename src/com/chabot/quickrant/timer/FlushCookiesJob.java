package com.chabot.quickrant.timer;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.chabot.quickrant.service.CookieService;

public class FlushCookiesJob {
	
	private static Logger log = Logger.getLogger(FlushCookiesJob.class);
	
	private final long TIMER_DELAY = 300*1000; 		// 5 minutes
	private final long TIMER_INTERVAL = 300*1000;
	
    public FlushCookiesJob() {
    	Timer timer = new Timer();
        timer.schedule(new FlushCookiesTask(), TIMER_DELAY, TIMER_INTERVAL);
        log.info("Timer interval: " + (TIMER_INTERVAL / 1000)/60 + " minutes");
        log.info("Timer delay: " + (TIMER_DELAY / 1000)/60 + " minutes");
        log.info("Next scheduled run time: " + getNextRunTime());
    }

    class FlushCookiesTask extends TimerTask {
    	public void run() {
    		int start = CookieService.getCookiesSize();
    		CookieService.clean();
    		int finish = CookieService.getCookiesSize();
    		log.info("Cleaned up " + (start-finish) + " cookies (" + finish + " active)");
    		log.info("Next scheduled run time: " + getNextRunTime());
        }
    }
    
    public Timestamp getNextRunTime() {
    	return new Timestamp(new Date().getTime() + TIMER_INTERVAL);
    }
    
}