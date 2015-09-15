package com.quickrant.sort;

import org.springframework.data.domain.Sort;

import java.util.EnumMap;

public class MongoSort {

    public static final EnumMap<SortMethod, Sort> SORT_BY = new EnumMap<>(SortMethod.class);

    static {
        SORT_BY.put(SortMethod.ID_DESC, new Sort(Sort.Direction.DESC, "_id"));
        SORT_BY.put(SortMethod.ID_ASC, new Sort(Sort.Direction.ASC, "_id"));
    }

}
