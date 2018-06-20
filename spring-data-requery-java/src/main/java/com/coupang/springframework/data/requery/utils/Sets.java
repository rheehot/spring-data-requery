package com.coupang.springframework.data.requery.utils;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

/**
 * Sets
 *
 * @author debop@coupang.com
 * @since 18. 6. 20
 */
@UtilityClass
public class Sets {

    public static <T> Set<T> toSet(Iterable<T> source) {
        if (source instanceof Set) {
            return (Set<T>) source;
        } else {
            Set<T> set = new HashSet<>();

            for (T item : source) {
                set.add(item);
            }
            return set;
        }
    }
}
