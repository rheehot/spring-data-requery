package com.coupang.springframework.data.requery.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Lists
 *
 * @author debop@coupang.com
 * @since 18. 6. 20
 */
@UtilityClass
public class Lists {

    public static <T> List<T> toList(Iterable<T> source) {
        if (source instanceof List) {
            return (List<T>) source;
        } else {
            List<T> list = new ArrayList<>();
            for (T item : source) {
                list.add(item);
            }
            return list;
        }
    }
}
