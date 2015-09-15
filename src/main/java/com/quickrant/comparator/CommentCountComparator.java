package com.quickrant.comparator;

import com.quickrant.model.Rant;

import java.util.Comparator;

public class CommentCountComparator implements Comparator<Rant> {

    @Override
    public int compare(Rant o1, Rant o2) {
        long c1 = o1.getCommentCount();
        long c2 = o2.getCommentCount();
        if (c1 > c2) {
            return -1;
        } else if (c1 < c2) {
            return 1;
        }
        return 0;
    }

}
