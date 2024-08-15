package com.dudko.blazinghot.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

    public static <T> List<T> compactLists(List<List<T>> lists) {
        List<T> list = new ArrayList<>();
        for (List<T> l : lists) {
            list.addAll(l);
        }
        return list;
    }

}
