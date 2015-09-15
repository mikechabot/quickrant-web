package com.quickrant.util;

import com.google.common.base.CharMatcher;

public class StringUtil {

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

}