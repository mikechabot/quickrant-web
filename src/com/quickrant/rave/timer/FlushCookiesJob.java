package com.quickrant.rave.timer;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.quickrant.rave.Configuration;
import com.quickrant.rave.Configuration.ConfigurationException;
import com.quickrant.rave.service.CookieService;
import com.quickrant.rave.service.RanterService;

public class FlushCookiesJob {
	
	private static Logger log = Logger.getLogger(FlushCookiesJob.class);
	
	private long TIMER_DELAY;
	private long TIMER_INTERVAL;
	
    public FlushCookiesJob() throws ConfigurationException {
    	Configuration config = Configuration.getInstance();
		config.initialize();
		
		this.TIMER_DELAY = config.getOptionalLong("cookie-flush-delay", 2);
		this.TIMER_INTERVAL = config.getOptionalLong("cookie-flush-interval", 5);
    	
    	Timer timer = new Timer();
        timer.schedule(new FlushCookiesTask(), TIMER_DELAY*60*1000, TIMER_INTERVAL*60*1000);
        
        log.info("Timer delay: " + TIMER_DELAY + " minute(s)");
        log.info("Timer interval: " + TIMER_INTERVAL + " minute(s)");
        log.info("Next scheduled run time: " + getNextRunTime(true));
    }

    class FlushCookiesTask extends TimerTask {
    	public void run() {
    		CookieService.clean();
    		RanterService.clean();
    		log.info("Next scheduled run time: " + getNextRunTime());
        }
    }

    public Timestamp getNextRunTime() {
    	return getNextRunTime(false);
    }
    
    public Timestamp getNextRunTime(boolean initializing) {
    	return new Timestamp(new Date().getTime() + (initializing ? TIMER_DELAY*60*1000 : TIMER_INTERVAL*60*1000));
    }
    
}