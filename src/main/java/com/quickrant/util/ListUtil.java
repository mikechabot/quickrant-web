package com.quickrant.util;

import java.util.List;

public class ListUtil {

    public static <T> void pop(List<T> list) {
        if (list == null || list.isEmpty()) return;
        list.remove(list.size()-1);
    }

    public static <T> T shift(List<T> list) {
        if (list == null || list.isEmpty()) return null;
        return list.remove(0);
    }

}
