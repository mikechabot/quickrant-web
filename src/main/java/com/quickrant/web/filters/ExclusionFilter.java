package com.quickrant.web.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quickrant.web.utils.StringUtil;
import org.apache.log4j.Logger;

public class ExclusionFilter implements Filter {

	private static Logger log = Logger.getLogger(ExclusionFilter.class);
	
	private static List<Pattern> patterns = new ArrayList<Pattern>(0);
	
	@Override
	public void init(FilterConfig config) {
		log.info("Initializing filter");
		
		/* Compile the exclusion patterns */
		List<String> exclusions = StringUtil.getListFromCsv(config.getInitParameter("exclusions"));
		compilePatterns(exclusions);
	}
	
	@Override
	public void destroy() {
		log.info("Destroying filter");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		/* Check URL for exclusions */
		String path = request.getServletPath();
		if (path != null) {
			if (matchesExclusions(path)) {
				/* Skip the remaining filters */
				request.getRequestDispatcher(path).forward(request,  response);
				return;
			}
		}
		
		chain.doFilter(request, response);
	}
	
	private boolean matchesExclusions(String path) {
		for (Pattern pattern : patterns) {
			Matcher matcher = pattern.matcher(path);
			if (matcher.find()) {				
				return true;
			}
		}
		return false;
	}
	
	private void compilePatterns(List<String> exclusions) {
		for (String each : exclusions) {
			log.info("Adding exclusion: " + StringUtil.despecialize(each));
			Pattern pattern = Pattern.compile(each);
			patterns.add(pattern);
		}
	}

}
