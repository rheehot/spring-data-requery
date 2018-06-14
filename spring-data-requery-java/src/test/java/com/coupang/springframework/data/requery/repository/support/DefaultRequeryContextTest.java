package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.repository.support.DefaultRequeryContext;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * com.coupang.springframework.data.requery.repository.support.DefaultRequeryContextTest
 *
 * @author debop
 * @since 18. 6. 8
 */
public class DefaultRequeryContextTest {

    @Test
    public void reject_null_EntityDataStores() {

        assertThatThrownBy(() -> {

            new DefaultRequeryContext(null);

        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void reject_emtpy_EntityDataStores() {

        assertThatThrownBy(() -> {

            new DefaultRequeryContext(Collections.emptySet());

        }).isInstanceOf(IllegalArgumentException.class);

    }
}
