package org.springframework.data.requery.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * org.springframework.data.requery.utils.Iterables
 *
 * @author debop
 * @since 18. 6. 20
 */
@UtilityClass
public class Iterables {

    public static <T> List<T> toList(Iterable<T> iterable) {
        if (iterable instanceof List) {
            return (List<T>) iterable;
        } else {
            List<T> list = new ArrayList<>();
            for (T item : iterable) {
                list.add(item);
            }
            return list;
        }
    }

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
