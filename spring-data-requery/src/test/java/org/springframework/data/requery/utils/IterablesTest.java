package org.springframework.data.requery.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * com.coupang.springframework.data.requery.utils.IterablesTest
 *
 * @author debop
 * @since 18. 6. 20
 */
@Slf4j
public class IterablesTest {

    @Test
    public void iterableToList() {
        Iterable<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        List<Integer> list = Iterables.toList(numbers);

        assertThat(list).hasSize(5).containsAll(numbers);
    }

    @Test
    public void iterableToSet() {
        Iterable<Integer> numbers = Arrays.asList(1, 2, 3, 3, 4, 5);

        Set<Integer> set = Iterables.toSet(numbers);
        assertThat(set).hasSize(5).containsAll(numbers);
    }
}
