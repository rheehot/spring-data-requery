package com.coupang.springframework.data.requery.domain;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.configs.RequeryTestConfiguration;
import io.requery.sql.EntityDataStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * com.coupang.springframework.data.requery.domain.AbstractDomainTest
 *
 * @author debop
 * @since 18. 6. 4
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RequeryTestConfiguration.class })
public abstract class AbstractDomainTest {

    protected static Random rnd = new Random(System.currentTimeMillis());

    @Inject
    protected EntityDataStore<Object> dataStore;

    @Inject
    protected RequeryOperations requeryTemplate;

    @Test
    public void context_loading() {
        assertThat(dataStore).isNotNull();
        assertThat(requeryTemplate).isNotNull();
    }
}
