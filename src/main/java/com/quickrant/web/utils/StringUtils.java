package com.quickrant.web.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtils {

    public static String toSentenceCase(String text) {
		StringBuilder temp = new StringBuilder();
		temp.append(text.substring(0,1).toUpperCase());
		temp.append(text.substring(1, text.length()));
		return temp.toString();
    }
    
    /**
     * Get a List from a comma separated value
     * @param string
     * @return
     */
	public static List<String> getListFromCsv(String string) {
		String[] array = string.toLowerCase().split(("\\s*,\\s*"));
		List<String> list = new ArrayList<String>(Arrays.asList(array));
		return list;
	}

	public static String despecialize(String each) {
		return each.replace("\\","");
	}
    
}