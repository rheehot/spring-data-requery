package com.coupang.springframework.data.requery.java.configs;

import com.coupang.springframework.data.requery.core.RequeryTemplate;
import io.requery.sql.EntityDataStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RequeryConfigurationTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RequeryTestConfiguration.class })
public class RequeryConfigurationTest {

    @Inject
    EntityDataStore dataStore;

    @Inject
    RequeryTemplate requeryTemplate;

    @Test
    public void contextLoading() {
        log.debug("Context Loading...");
        assertThat(dataStore).isNotNull();
        assertThat(requeryTemplate).isNotNull();
    }
}
