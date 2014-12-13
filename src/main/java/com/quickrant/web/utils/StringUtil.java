package com.quickrant.web.utils;

import com.google.common.base.CharMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtil {

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


    /**
     * Return true of the string is empty
     * @param value
     * @return boolean
     */
    public static boolean isEmpty(String value) {
        if (value == null || value.isEmpty()) {
            return true;
        } else {
            return removeWhitespaces(value).isEmpty();
        }
    }

    /**
     * Remove whitepaces from a string
     * @param value
     * @return String
     */
    public static String removeWhitespaces(String value) {
        return CharMatcher.WHITESPACE.trimFrom(value);
    }

    /**
     * Remove non-alphabetic characters from a string
     * @param value
     * @return String
     */
    public static String sanitize(String value) {
        return value.toLowerCase().replaceAll("[^a-zA-Z]", "");
    }
    
}