package org.springframework.data.requery.domain;

import io.requery.sql.EntityDataStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.requery.config.RequeryTestConfiguration;
import org.springframework.data.requery.core.RequeryOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * org.springframework.data.requery.domain.AbstractDomainTest
 *
 * @author debop
 * @since 18. 6. 4
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RequeryTestConfiguration.class })
public abstract class AbstractDomainTest {

    protected static Random rnd = new Random(System.currentTimeMillis());

    @Autowired
    protected EntityDataStore<Object> dataStore;

    @Autowired
    protected RequeryOperations requeryTemplate;

    @Test
    public void context_loading() {
        assertThat(dataStore).isNotNull();
        assertThat(requeryTemplate).isNotNull();
    }
}
